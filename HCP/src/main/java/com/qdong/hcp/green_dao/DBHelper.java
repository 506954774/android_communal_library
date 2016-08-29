package com.qdong.hcp.green_dao;

import android.content.Context;
import android.util.Log;

import com.qdong.greendao.DaoSession;
import com.qdong.greendao.MqttChatEntity;
import com.qdong.greendao.MqttChatEntityDao;
import com.qdong.greendao.User;
import com.qdong.greendao.UserDao;
import com.qdong.hcp.activity.AppLoader;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * DBHelper
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/19  12:28
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private UserDao mUserDao;//数据表的操作类
    private MqttChatEntityDao mMqttChatDao;//数据表的操作类


    private DBHelper() {
    }
    //单例模式，DBHelper只初始化一次
    public static  DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = AppLoader.getDaoSession(context);
            instance.mUserDao = instance.mDaoSession.getUserDao();
        }
        return instance;
    }



    //删除User表
    public  void dropUserTable()
    {
        UserDao.dropTable(mDaoSession.getDatabase(), true);
    }


    //删除所有表
    public void dropAllTable()
    {
        UserDao.dropTable(mDaoSession.getDatabase(), true);
     }

    //创建所有表
    public void createAllTable()
    {
        UserDao.createTable(mDaoSession.getDatabase(), true);

    }

    //插入或者替换user项
    public long saveUser(User bean){
        return mUserDao.insertOrReplace(bean);
    }

    //根据id找到某一项
    public User loadNote(long id) {
        return mUserDao.load(id);
    }

    //获得所有的User列表
    public List<User> loadAllNote(){
        return mUserDao.loadAll();
    }

    //查询满足params条件的列表
    public List<User> queryNote(String where, String... params){
        return mUserDao.queryRaw(where, params);
    }


    //比较难的查询可以使用QueryBuilder来查询
    public List<MqttChatEntity> loadLastMsgBySessionid(String sessionid){
        QueryBuilder<MqttChatEntity> mqBuilder = mMqttChatDao.queryBuilder();
        mqBuilder.where(MqttChatEntityDao.Properties.Sessionid.eq(sessionid))//where语句
                .orderDesc(MqttChatEntityDao.Properties.Id)//排序
                .limit(1);//limit
        return mqBuilder.list();
    }


    public List<MqttChatEntity> loadMoreMsgById(String sessionid, Long id){
        QueryBuilder<MqttChatEntity> mqBuilder = mMqttChatDao.queryBuilder();
        mqBuilder.where(MqttChatEntityDao.Properties.Id.lt(id))
                .where(MqttChatEntityDao.Properties.Sessionid.eq(sessionid))
                .orderDesc(MqttChatEntityDao.Properties.Id)
                .limit(20);
        return mqBuilder.list();
    }




    public void deleteUserById(long id){
        mUserDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }


    public void deleteNote(User note){
        mUserDao.delete(note);
    }
}
