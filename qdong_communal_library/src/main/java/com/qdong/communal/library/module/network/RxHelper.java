package com.qdong.communal.library.module.network;/**
 * Created by AA on 2016/7/11.
 */

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.qdong.communal.library.BuildConfig;
import com.qdong.communal.library.util.Constants;
import com.qdong.communal.library.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * RxHelper
 * RxJava辅助类
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/11  9:20
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class RxHelper {

    private static RxHelper ourInstance;
    private Context context;

    public static RxHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new RxHelper(context);
        }
        return ourInstance;
    }

    private RxHelper(Context context) {
        this.context=context;
    }

    private static final String SESSION_ERROR= BuildConfig.SESSION_ERROR_CODE;
    private static final String SET_COOKIE="Set-Cookie";
    private static final String SESSION_ID="JSESSIONID=";
    private static final String SPLIT_SIGN=";";
    private static final String EQUAL_SIGN="=";
    private static final String EXCEPTION_SESSION_IS_ERROR="session is error!";
    private static final String THROWABLE_MESSAGE_AUTO_LOGIN_FAILED="auto login failed";
    private static final String THROWABLE_MESSAGE_LOGIN_FAILED="login failed";


    /**
     * @method name:unsubscribe
     * @des:消除订阅.最后释放资源时,一定要调用,不然会有内存溢出的风险
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/6/28
     * @author Chuck
     **/
    public void unsubscribe(Subscription subscription) {

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    /**
     * @method name:judgeSessionExpired
     * 重载这个是为了给相同请求(比如同一个接口,可以有刷新,加载的不同动作)的加一个类型
     * @des:判断session有无过期,判断的逻辑根据服务器定义的错误码来判断
     * 返回的值作为flatMap的参数,也就是把一个源数据通过一个Func函数变为一个被观察者对象(可以是异常对象)
     * @param :[observer :观察者;actionType:请求类型,这个是专门为下拉刷新的界面做的]
     * @return type:rx.functions.Func1<com.rengwuxian.rxjavasamples.qdong_test.QDongNetInfo,rx.Observable<com.rengwuxian.rxjavasamples.qdong_test.QDongNetInfo>>
     * @date 创建时间:2016/6/29
     * @author Chuck
     **/
    public Func1<QDongNetInfo, Observable<QDongNetInfo>> judgeSessionExpired(final Observer<QDongNetInfo> observer, final int actionType){

        return
                new Func1<QDongNetInfo, Observable<QDongNetInfo>>(){
                    @Override
                    public Observable<QDongNetInfo> call(QDongNetInfo netInfo) {

                        Log.e("RxJava", "flatMap判断session是否失效,线程id:" + Thread.currentThread().getId() + ",netInfo:" + netInfo.toString());

                        if(netInfo==null){//网络错误
                            Log.e("RxJava", "flatMap,====>session失效,线程id:" + Thread.currentThread().getId() + ",第一次请求就没有正常返回,框架将处理这个");
                            return  Observable.<QDongNetInfo>error(new NetworkErrorException());
                        }
                        else {

                            netInfo.setActionType(actionType);//赋值给它,这样,观察者可以通过他的actionType来判断请求类型

                            if (SESSION_ERROR.equals(netInfo.getErrorCode())) {//服务器返回"010035",表示session已经过期,则再获取一次tocken
                                Log.e("RxJava", "flatMap,====>session失效,线程id:" + Thread.currentThread().getId() + ",准备抛出异常,触发retry");

                                /**抛出这个异常**/
                                return Observable.<QDongNetInfo>error(new QDongException(EXCEPTION_SESSION_IS_ERROR));//返回一个thrawable
                            }
                            else {
                                Log.e("RxJava", "flatMap,====>session没有失效,线程id:" + Thread.currentThread().getId() + ",准备触发观察者的回调");
                                if(observer!=null){
                                    observer.onNext(netInfo);//手动调用观察者的回调
                                }

                                /**返回这个后,事件结束**/
                                return  Observable.<QDongNetInfo>empty();
                            }
                        }
                    }
                };
    }



    /**
     * @method name:judgeSessionExpired
     * @des:判断session有无过期,判断的逻辑根据服务器定义的错误码来判断
     * 返回的值作为flatMap的参数,也就是把一个源数据通过一个Func函数变为一个被观察者对象(可以是异常对象)
     * @param :[observer :观察者]
     * @return type:rx.functions.Func1<com.rengwuxian.rxjavasamples.qdong_test.QDongNetInfo,rx.Observable<com.rengwuxian.rxjavasamples.qdong_test.QDongNetInfo>>
     * @date 创建时间:2016/6/29
     * @author Chuck
     **/
    public Func1<QDongNetInfo, Observable<QDongNetInfo>> judgeSessionExpired(final Observer<QDongNetInfo> observer){

        return
                new Func1<QDongNetInfo, Observable<QDongNetInfo>>(){
                    @Override
                    public Observable<QDongNetInfo> call(QDongNetInfo netInfo) {

                        Log.e("RxJava", "flatMap判断session是否失效,线程id:" + Thread.currentThread().getId() + ",netInfo:" + netInfo.toString());

                        if(netInfo==null){//网络错误
                            Log.e("RxJava", "flatMap,====>session失效,线程id:" + Thread.currentThread().getId() + ",第一次请求就没有正常返回,框架将处理这个");
                            return  Observable.<QDongNetInfo>error(new NetworkErrorException());
                        }
                        else {
                            if (SESSION_ERROR.equals(netInfo.getErrorCode())) {//服务器返回"010035",表示session已经过期,则再获取一次tocken
                                Log.e("RxJava", "flatMap,====>session失效,线程id:" + Thread.currentThread().getId() + ",准备抛出异常,触发retry");

                                /**抛出这个异常**/
                                return Observable.<QDongNetInfo>error(new QDongException(EXCEPTION_SESSION_IS_ERROR));//返回一个thrawable
                            }
                            else {
                                Log.e("RxJava", "flatMap,====>session没有失效,线程id:" + Thread.currentThread().getId() + ",准备触发观察者的回调");
                                if(observer!=null){
                                    observer.onNext(netInfo);//手动调用观察者的回调
                                }

                                /**返回这个后,事件结束**/
                                return  Observable.<QDongNetInfo>empty();
                            }
                        }
                    }
                };
    }



    /**
     * @method name:judgeRetry
     * @des:判断要不要执行retry
     * @param :[service :api类, action:请求动作,HashMap<String, String> map:登录参数map]
     * @return type:rx.functions.Func1<rx.Observable<? extends java.lang.Throwable>,rx.Observable<?>>
     * @date 创建时间:2016/6/29
     * @author Chuck
     **/
    public  Func1<Observable<? extends Throwable>, Observable<?>> judgeRetry(final QDongApi service, final Observable<QDongNetInfo> action,final HashMap<String, String> map){

        return
                new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?>      call(Observable<? extends Throwable> observable) {

                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {

                                Log.e("RxJava", "retryWhen执行,线程id:" + Thread.currentThread().getId());

                                /**如果是我们指定的异常,则执行retry**/
                                if (throwable !=null && throwable instanceof QDongException && EXCEPTION_SESSION_IS_ERROR.equals(throwable.getMessage())) {

                                    Log.e("RxJava", "retryWhen执行,线程id:" + Thread.currentThread().getId() + "====>是session过期的问题!");

                                    /**调用自动登录获取最新sessonId,并根据登录结果返回Observable对象**/
                                    return autoLogin(service,action,map);

                                }
                                else {

                                    Log.e("RxJava", "retryWhen执行,线程id:" + Thread.currentThread().getId() + "====>不是session过期的问题,将返回Observable.error(throwable)" );

                                    /**其他的异常直接抛回给框架**/
                                    return Observable.error(throwable);
                                }

                            }
                        });
                    }
                };
    }


    /**
     * @method name:autoLogin
     * @des:登录接口,在获取用户信息的同时,保存最新sessionId
     * @param :[service, map]
     * @return type:rx.Observable<com.qdong.communal.library.module.network.QDongNetInfo>
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    public Observable<QDongNetInfo> loginAndSaveSession (QDongApi service,HashMap<String, String> map){

        Log.e("RxJava", "login,线程id:" + Thread.currentThread().getId() );

        boolean success=false;//是否获取到了最新的token(即是是否登录成功)
        Response<QDongNetInfo> response = null;//定义响应体

        try {
            Call<QDongNetInfo> call = service.login(map);//同步调用,为何要同步?因为我要抓取cookie里面的sessionId
            response = call.execute();//执行

            Log.e("RxJava", "flatMap,线程id:" + Thread.currentThread().getId() + ",responseResponse:" + response.raw().headers().toString());

            Headers headers = response.raw().headers();//获取头

            String cookiesString = headers.get(SET_COOKIE);

            if(!TextUtils.isEmpty(cookiesString)){
                String[] cookies = cookiesString.split(SPLIT_SIGN);//拆分
                for (String s : cookies) {
                    if (!TextUtils.isEmpty(s)) {
                        if (s.startsWith(SESSION_ID)) {
                            String sessionId = s.substring((s.indexOf(EQUAL_SIGN) + 1));
                            Log.e("RxJava", "==============>获取sessionId成功!--->" + sessionId);
                            RetrofitAPIManager.JSESSIONID = sessionId;//赋值

                            /***
                             * 存起来
                             */
                            SharedPreferencesUtil.getInstance(context).putString(Constants.SESSION_ID,sessionId);

                            success=true;
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RxJava", "flatMap catch,==============>线程id:" + Thread.currentThread().getId() + ",error:" + e.toString());
        }
        finally {
            if(success){
                /**获取成功*/
                return  Observable.just(response.body());
            }
            else {
                /**获取失败**/
                return Observable.error(new Throwable(THROWABLE_MESSAGE_LOGIN_FAILED));
            }
        }


    }

    /**
     * @method name:autoLogin
     * @des:登录接口,(在获取用户信息的同时,保存最新sessionId)
     * @param :[service:请求实例, map:登录参数map]
     * @return type:rx.Observable<com.qdong.communal.library.module.network.QDongNetInfo>
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    public Func1<Long, Observable<QDongNetInfo>> login (final QDongApi service, final HashMap<String, String> map){

        return new Func1<Long, Observable<QDongNetInfo>>() {
            @Override
            public Observable<QDongNetInfo> call(Long aLong) {
                return loginAndSaveSession(service,map);
            }
        };


    }




    /**
     * @method name:autoLogin
     * @des:自动登录
     * @param :[service,
     * action,
     * map:登录需要的参数map
     * ]
     * @return type:rx.Observable<?>
     * @date 创建时间:2016/8/22
     * @author Chuck
     **/
    private Observable<?> autoLogin(QDongApi service,Observable<QDongNetInfo> action,HashMap<String, String> map) {

        boolean success=false;//是否获取到了最新的token

        Response<QDongNetInfo> response = null;//定义响应体

        try {
            Call<QDongNetInfo> call = service.login(map);//同步调用,为何要同步?因为我只需要抓取cookie里面的sessionId
            response = call.execute();//执行

            Log.e("RxJava", "flatMap,线程id:" + Thread.currentThread().getId() + ",responseResponse:" + response.raw().headers().toString());

            Headers headers = response.raw().headers();//获取头

            String cookiesString = headers.get(SET_COOKIE);

            if(!TextUtils.isEmpty(cookiesString)){
                String[] cookies = cookiesString.split(SPLIT_SIGN);//拆分
                for (String s : cookies) {
                    if (!TextUtils.isEmpty(s)) {
                        if (s.startsWith(SESSION_ID)) {
                            String sessionId = s.substring((s.indexOf(EQUAL_SIGN) + 1));
                            Log.e("RxJava", "==============>获取sessionId成功!--->" + sessionId);
                            RetrofitAPIManager.JSESSIONID = sessionId;//赋值

                            /***
                             * 存起来
                             */
                            SharedPreferencesUtil.getInstance(context).putString(Constants.SESSION_ID,sessionId);

                            success=true;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RxJava", "flatMap catch,==============>线程id:" + Thread.currentThread().getId() + ",error:" + e.toString());
        }
        finally {
            if(success){
                /**获取成功,返回一个Observable类型,否则不会执行retry*/
                return  action;
            }
            else {
                /**获取失败,抛出指定的异常:重新登陆失败,交给观察者处理**/
                return Observable.error(new Throwable(THROWABLE_MESSAGE_AUTO_LOGIN_FAILED));
            }
        }
    }

}
