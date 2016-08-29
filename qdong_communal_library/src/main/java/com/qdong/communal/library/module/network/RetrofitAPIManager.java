package com.qdong.communal.library.module.network;

import android.content.Context;
import android.util.Log;

import com.qdong.communal.library.util.Constants;
import com.qdong.communal.library.util.LogUtil;
import com.qdong.communal.library.util.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * RetrofitAPIManager
 * http的管理类,为了避免通过注解去写header,使用此类统一管理
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/28  11:46
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class RetrofitAPIManager {

    public static  String JSESSIONID="1";

    public static QDongApi provideClientApi(Context context) {//初始化时,把sessionId取出来
        return provideClientApi(context,false);
    }

    public static QDongApi provideClientApi(Context context,String url) {
        return provideClientApi(context, url,false);
    }

    /**
     * @method name:provideClientApi
     * @des:获取client
     * @param :[context, isFileUpload:是不是文件上传的任务]
     * @return type:com.qdong.communal.library.module.network.QDongApi
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    public static QDongApi provideClientApi(Context context,boolean isFileUpload) {//初始化时,把sessionId取出来

        JSESSIONID= SharedPreferencesUtil.getInstance(context).getString(Constants.SESSION_ID,"-1");
        Retrofit retrofit = new Retrofit.Builder()
                .client(isFileUpload?genericFileUploadClient():genericClient())
                .addConverterFactory(GsonConverterFactory.create())//json解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//调用适配器
                .baseUrl(Constants.SERVER_URL)
                .build();
        return retrofit.create(QDongApi.class);
    }

    /**
     * @method name:provideClientApi
     * @des:获取client
     * @param :[context, url:baseUrl, isFileUpload:是否是文件上传]
     * @return type:com.qdong.communal.library.module.network.QDongApi
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    public static QDongApi provideClientApi(Context context,String url,boolean isFileUpload) {

        JSESSIONID= SharedPreferencesUtil.getInstance(context).getString(Constants.SESSION_ID,"-1");

        Retrofit retrofit = new Retrofit.Builder()
                .client(isFileUpload?genericFileUploadClient():genericClient())
                .addConverterFactory(GsonConverterFactory.create())//json解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//调用适配器
                .baseUrl(url)
                .build();
        return retrofit.create(QDongApi.class);
    }


    /**
     * @method name:genericClient
     * @des:添加拦截器,这里做的操作是给请求加头(普通的头),每次发送网络请求的时候都会执行这里
     * @param :[]
     * @return type:okhttp3.OkHttpClient
     * @date 创建时间:2016/6/28
     * @author Chuck
     **/
    public static OkHttpClient genericClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request()
                                .newBuilder()
                                //.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)//加sessionId
                                .build();

                       LogUtil.e("RxJava", "Interceptor执行!线程id:" + Thread.currentThread().getId() + ",===========>request:"+chain.request().toString());

                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }




    /**
     * @method name:genericClient
     * @des:添加拦截器,这里做的操作是给请求加头(针对文件上传),每次发送网络请求的时候都会执行这里
     * @param :[]
     * @return type:okhttp3.OkHttpClient
     * @date 创建时间:2016/6/28
     * @author Chuck
     **/
    public static OkHttpClient genericFileUploadClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request()
                                .newBuilder()
                                //.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Content-Type", "application/octet-stream; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip")
                                .addHeader("Connection", "Keep-Alive")
                                .addHeader("Cookie", "JSESSIONID=" + JSESSIONID)//加sessionId
                                .build();

                       LogUtil.e("RxJava", "Interceptor执行!线程id:" + Thread.currentThread().getId() + ",===========>request:"+chain.request().toString());

                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

}
