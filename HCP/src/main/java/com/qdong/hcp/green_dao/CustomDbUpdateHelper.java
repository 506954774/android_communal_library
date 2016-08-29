package com.qdong.hcp.green_dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qdong.greendao.DaoMaster;

import com.qdong.greendao.UserDao;


/**
 * CustomDbUpdateHelper
 * 数据库升级辅助类
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/19  14:51
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class CustomDbUpdateHelper extends DaoMaster.OpenHelper {

    public CustomDbUpdateHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("getgreenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");

        /***这里根据实际的来,比如数据库从10到11时要升级User表,可以写if语句来迁移数据**/
        try {
            MigrationHelper.getInstance().migrate(db,UserDao.class);//数据迁移
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getgreenDAO", e.toString());
        }
    }
}