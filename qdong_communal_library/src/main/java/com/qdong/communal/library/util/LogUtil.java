package com.qdong.communal.library.util;

import android.util.Log;

import com.qdong.communal.library.BuildConfig;


/**
 * LogUtil
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/7  15:28
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class LogUtil {

    private  static  final boolean LOG =BuildConfig.LOG_SWITCH;//gradle里面配置

    public static void i(String tag,Object object){
        if(LOG){
                Log.i(tag,object==null?"null":object.toString());
        }
    }
    public static void e(String tag,Object object){
        if(LOG){
                Log.e(tag,object==null?"null":object.toString());
        }
    }

    public static void d(String tag,Object object){
        if(LOG){
                Log.d(tag,object==null?"null":object.toString());
        }
    }
    public static void w(String tag,Object object){
        if(LOG){
                Log.w(tag,object==null?"null":object.toString());
        }
    }

}
