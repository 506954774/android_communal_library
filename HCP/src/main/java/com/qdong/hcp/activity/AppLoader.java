package com.qdong.hcp.activity;/**
 * Created by AA on 2016/7/7.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.qdong.communal.library.BuildConfig;
import com.qdong.communal.library.util.SharedPreferencesUtil;
import com.qdong.greendao.DaoMaster;
import com.qdong.greendao.DaoSession;
import com.qdong.hcp.enums.ActivityLifecycleStatus;
import com.qdong.hcp.green_dao.CustomDbUpdateHelper;
import com.qdong.hcp.utils.Constants;
import com.qdong.hcp.utils.LogUtil;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * AppLoader
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/7  14:13
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class AppLoader extends Application {

    private static final String TAG = AppLoader.class.getSimpleName();

    private static AppLoader ourInstance;

    /**activity生命周期监测**/
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacksImpl;

    /**手机状态栏的高度**/
    public static int STATUS_BAR_HEIGHT;//在LuanchActivity里赋值

    /**手机屏幕高度**/
    public static int SCREEN_HEIGHT;//在LuanchActivity里赋值

    /**手机屏幕宽度**/
    public static int SCREEN_WIDTH;//在LuanchActivity里赋值


    public static AppLoader getInstance() {
        return ourInstance;
    }

    /**key是activity类名,value是他此时的状态**/
    private HashMap<String,ActivityLifecycleStatus> mActivityStack=new HashMap<String,ActivityLifecycleStatus>();


    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance=this;
        mActivityLifecycleCallbacksImpl=new ActivityLifecycleCallbacksImpl();
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }



    /**
     * @method name:getScreenHeight
     * @des:获取屏幕高度
     * @param :[]
     * @return type:int
     * @date 创建时间:2016/7/12
     * @author Chuck
     **/
    public  int getScreenHeight(){

        if(SCREEN_HEIGHT<=0){
            SCREEN_HEIGHT=SharedPreferencesUtil.getInstance(ourInstance).getInt(Constants.SCREEN_HEIGHT,1280);
        }
        return  SCREEN_HEIGHT;
    }

    /**
     * @method name:getScreenWidth
     * @des:获取屏幕宽度
     * @param :[]
     * @return type:int
     * @date 创建时间:2016/7/12
     * @author Chuck
     **/
    public  int getScreenWidth(){

        if(SCREEN_WIDTH<=0){
            SCREEN_WIDTH=SharedPreferencesUtil.getInstance(ourInstance).getInt(Constants.SCREEN_WIDTH,720);
        }
        return  SCREEN_WIDTH;
    }

    /**
     * @method name:getStateBarHeight
     * @des:获取顶部状态栏的高度
     * @param :[]
     * @return type:int
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    public static int getStateBarHeight() {

        if(STATUS_BAR_HEIGHT== 0) {
            STATUS_BAR_HEIGHT = SharedPreferencesUtil.getInstance(ourInstance).getInt(SharedPreferencesUtil.STATEBARHEIGHT, 0);
        }
        return STATUS_BAR_HEIGHT;
    }
    /**
     * @method name:isAppFront
     * @des:app此时是否有界面处在前台
     * @param :[]
     * @return type:boolean
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    public  boolean isAppFront(){
        Set set = mActivityStack.entrySet();
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();
            ActivityLifecycleStatus value = (ActivityLifecycleStatus)entry.getValue();
            if(value!=null){
                if(value==ActivityLifecycleStatus.RESUMED){//有界面处于RESUMED
                    return  true;
                }
            }
        }
        return  false;
    }

    /**
     * @method name:getFrontActivityClassName
     * @des:获取最前台那个activity的类名(如果在前台)
     * @param :[]
     * @return type:java.lang.String
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    public String getFrontActivityClassName(){

        Set set = mActivityStack.entrySet();
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();
            ActivityLifecycleStatus value = (ActivityLifecycleStatus)entry.getValue();
            if(value!=null){
                if(value==ActivityLifecycleStatus.RESUMED){//有界面处于Resumed
                    return  (String)entry.getKey();
                }
            }
        }
        return  null;
    }




    private  class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtil.e("AppLoader", "onActivityCreated:" + activity.getClass().getName());
            mActivityStack.put(activity.getClass().getName(), ActivityLifecycleStatus.CREATED);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtil.e("AppLoader", "onActivityStarted:" + activity.getClass().getName());
            mActivityStack.put(activity.getClass().getName(), ActivityLifecycleStatus.STARTED);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtil.e("AppLoader", "onActivityResumed:" + activity.getClass().getName());
            mActivityStack.put(activity.getClass().getName(), ActivityLifecycleStatus.RESUMED);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtil.e("AppLoader", "onActivityPaused:" + activity.getClass().getName());
            mActivityStack.put(activity.getClass().getName(), ActivityLifecycleStatus.PAUSED);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtil.e("AppLoader", "onActivityStopped:" + activity.getClass().getName());
            mActivityStack.put(activity.getClass().getName(), ActivityLifecycleStatus.STOPPED);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtil.e("AppLoader", "onActivityDestroyed:" + activity.getClass().getName());
            try {
                mActivityStack.remove(activity.getClass().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /***
     * GreenDao相关
     */
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static SQLiteDatabase db;
    public static final String DB_NAME = com.qdong.hcp.BuildConfig.DB_NAME;//gradle里面配置数据库名,编译时动态生成



    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            CustomDbUpdateHelper helper = new CustomDbUpdateHelper(context,DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }


    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public static SQLiteDatabase getSQLDatebase(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            db = daoMaster.getDatabase();
        }
        return db;
    }


    /**
     * @method name:getAutoLoginParameterMap
     * @des:为自动登录提供登录参数
     * @param :[]
     * @return type:java.util.HashMap<java.lang.String,java.lang.String>
     * @date 创建时间:2016/8/22
     * @author Chuck
     **/
    public   HashMap<String, String> getAutoLoginParameterMap() {

        HashMap<String, String> map = new HashMap<>();
        /**测试代码,实际从缓存里获取**/
        map.put("account", "15262592514");
        map.put("password", "FC9DDCCA42C8FC33");
        map.put("deviceMac", "c4072f244658");
        map.put("gpsLong", "100");
        map.put("deviceName", "HUAWEI");
        map.put("deviceSystem", "3");
        map.put("gpsLat", "60");

        return map;
    }
}
