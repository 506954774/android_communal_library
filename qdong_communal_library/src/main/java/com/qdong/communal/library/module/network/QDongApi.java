package com.qdong.communal.library.module.network;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * QDongApi
 * 趣动api接口,Header没有用注解写,header添加的逻辑在RetrofitAPIManager里
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/28  11:19
 * Copyright : 2014-2015 深圳掌通宝科技有限公司-版权所有
 */
public interface QDongApi {

    /********登录********/
    //登陆,用Call是为了获取响应里的cookie,拿到sessionId
    @POST("AppServer/app/user/login.do")
    Call<QDongNetInfo> login(@Body Map<String, String> map);//为了获取头里面的sessionId
    @POST("AppServer/app/user/login.do")
    Observable<QDongNetInfo> userLogin(@Body Map<String, String> map);

    /********TFS上传********/
    @Multipart
    @POST("AppServer/app/file/multipleUpload.do")
    Observable<QDongNetInfo> uploadMultipleFile(@PartMap Map<String, RequestBody> params);


    //修改用户头像    //http://1501q8n685.51mypc.cn:10005/AppServer/app/user/updateHeadPhoto/T1ZNCTByxT1RCvBVdK.do
    @GET("AppServer/app/user/updateHeadPhoto/{tfs_path}.do")
    Observable<QDongNetInfo> updateHeadPhoto(@Path("tfs_path") String tfs_path);


    //获取设备列表
    @GET("AppServer/app/dev/mgr/list.do")
    Observable<QDongNetInfo> getDevicesList();

    //获取动态列表
    @GET("AppServer/app/social/findLatestDynamic/{page_num}/{page_size}.do")
    Observable<QDongNetInfo> findLatestDynamic(@Path("page_num") int pageNum, @Path("page_size") int pageSize);


}
