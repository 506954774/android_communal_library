package com.qdong.communal.library.module.TFSHelper;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.qdong.communal.library.module.network.QDongApi;
import com.qdong.communal.library.module.network.QDongNetInfo;
import com.qdong.communal.library.module.network.RetrofitAPIManager;
import com.qdong.communal.library.module.network.RxHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * TFSUploader
 * TFS文件上传的辅助类,
 * 1,通过{@link #getInstance(Application)}获取单例
 * 2,通过{@link #executeUploadMultipleFiles(ArrayList)}获取一个被观察者
 * 3,通过{@link #uploadMultipleFiles(ArrayList, HashMap, Observer)}实现异步调用
 * 4,通过{@link #uploadMultipleFilesThenUpdateUrl(ArrayList, HashMap, Func1, Observer)}实现链式调用
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/25  19:44
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class TFSUploader {

    private static TFSUploader instance;//单例
    private QDongApi mApiService;//api
    private Application mContext;//保存上下文,不用activity,避免内存溢出

    /**
     * @method name:TFSUploader
     * @des:构造
     * @param :[context]
     * @return type:
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    private TFSUploader(Application context){
        /**第二个参数表示此任务为文件上传,此时用的头不一样,但是sessionId还是有的 by:chuck 2016/08/25**/
        mApiService = RetrofitAPIManager.provideClientApi(context,true);
        this.mContext=context;
    }

    /**
     * @method name:getInstance
     * @des:获取单例
     * @param :[context]
     * @return type:com.qdong.communal.library.module.TFSHelper.TFSUploader
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public static  TFSUploader getInstance(Application context){
        if(instance==null){
            instance=new TFSUploader(context);
        }
        return instance;
    }

    /**
     * @method name:getmApiService
     * @des:获取接口类
     * @param :[]
     * @return type:com.qdong.communal.library.module.network.QDongApi
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public QDongApi getmApiService() {
        return mApiService;
    }


    /**
     * @method name:executeUploadMultipleFiles
     * @des:调用图片上传,返回一个被观察者
     * @param :[filePaths]
     * @return type:rx.Observable<com.qdong.communal.library.module.network.QDongNetInfo>
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public Observable<QDongNetInfo>  executeUploadMultipleFiles(ArrayList<String> filePaths){
        if(filePaths==null||filePaths.size()==0){
            return null;
        }
        else {
            /***拼接map**/
            final Map<String, RequestBody> params = getStringRequestBodyMap(filePaths);
            return mApiService.uploadMultipleFile(params);
        }
    }


    /**
     * @method name:uploadMultipleFilesThenUpdateUrl
     * @des:链式调用,在调用了tfs后再链式调用另外的接口(在服务器修改url)
     * @param :[
     * filePaths:文件路径集合,
     * loginMap:自动登录的参数map,
     * flatMap:这个是个flatMap,获取tsf上传的结果,然后在里面调用别的接口,
     * observer:最终观察者]
     * @return type:rx.Subscription
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public Subscription  uploadMultipleFilesThenUpdateUrl(
                                             ArrayList<String> filePaths,
                                             HashMap<String, String> loginMap,
                                             Func1<QDongNetInfo, Observable<QDongNetInfo>> flatMap,
                                             Observer<QDongNetInfo> observer){



        if(filePaths==null||filePaths.size()==0||flatMap==null||observer==null){
            return null;
        }
        else {
            Subscription subscription=
                            executeUploadMultipleFiles(filePaths)//tfs图片上传
                            .flatMap(flatMap)//获取返回的路径,调用某个接口
                            .subscribeOn(Schedulers.io())//指定被观察者的执行线程
                            .observeOn(AndroidSchedulers.mainThread())//切换到主线程,因为如果正常反回了,我要将事件直接交给观察者,观察者通常在主线程
                            .flatMap(RxHelper.getInstance(mContext).judgeSessionExpired(observer))//判断session是否过期,如果过期了,会抛出异常到事件流里
                            .observeOn(Schedulers.io())//切换到子线程,执行retryWhen,里面有网络请求
                            .retryWhen(RxHelper.getInstance(mContext).judgeRetry(mApiService,executeUploadMultipleFiles(filePaths) ,loginMap ))//触发retry
                            .observeOn(AndroidSchedulers.mainThread())//指定观察者的执行线程
                            .subscribe(observer);//订阅
            return subscription;
        }

    }

    /**
     * @method name:uploadMultipleFiles
     * @des:异步调用,实现TFS上传
     * @param :[filePaths:路径集合,map:session过期时提供登录参数,实现自动重试, observer:观察者]
     * @return type:void
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public Subscription uploadMultipleFiles(ArrayList<String> filePaths,HashMap<String, String> map,Observer<QDongNetInfo> observer) {

        if(filePaths==null||filePaths.size()==0||observer==null){
            return null;
        }
        else {

            /***拼接map**/
            final Map<String, RequestBody> params = getStringRequestBodyMap(filePaths);

            Subscription subscription= mApiService.uploadMultipleFile(params)
                    .subscribeOn(Schedulers.io())//指定被观察者的执行线程
                    .observeOn(AndroidSchedulers.mainThread())//切换到主线程,因为如果正常反回了,我要将事件直接交给观察者,观察者通常在主线程
                    .flatMap(RxHelper.getInstance(mContext).judgeSessionExpired(observer))//判断session是否过期,如果过期了,会抛出异常到事件流里
                    .observeOn(Schedulers.io())//切换到子线程,执行retryWhen,里面有网络请求
                    .retryWhen(RxHelper.getInstance(mContext).judgeRetry(mApiService,mApiService.uploadMultipleFile(params) ,map ))//触发retry
                    .observeOn(AndroidSchedulers.mainThread())//指定观察者的执行线程
                    .subscribe(observer);//订阅

            return  subscription;

        }


    }

    /**
     * @method name:cancelUpload
     * @des:取消当前上传任务
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    public void cancelUpload(Subscription subscription){
        if(subscription!=null){
            subscription.unsubscribe();
        }
    }

    @NonNull
    /**
     * @method name:getStringRequestBodyMap
     * @des:拼接RequestBodyMap
     * @param :[filePaths]
     * @return type:java.util.Map<java.lang.String,okhttp3.RequestBody>
     * @date 创建时间:2016/8/26
     * @author Chuck
     **/
    private Map<String, RequestBody> getStringRequestBodyMap(ArrayList<String> filePaths) {

        if(filePaths==null||filePaths.size()==0){
            return null;
        }

        final Map<String, RequestBody> params = new HashMap<>();
        for(String path:filePaths){
            if(!TextUtils.isEmpty(path)){
                File file = new File(path);
                if(file.exists()){
                    String mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(
                                    MimeTypeMap.getFileExtensionFromUrl(file.getPath()));
                    RequestBody fileBody = RequestBody.create(MediaType.parse(mimeType),file);

                    params.put("files\"; filename=\"" + file.getName() + "", fileBody);//拼接
                }
            }
        }

        return params;
    }


}
