package com.qdong.hcp.cache;/**
 * Created by AA on 2016/7/11.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.qdong.communal.library.cache.BaseCacheTool;
import com.qdong.communal.library.util.SerialRwDataUtil;
import com.qdong.hcp.entity.UserBean;

import java.util.ArrayList;

/**
 * UserCacheDao
 * 储存一个UserBean集合,所有登录过的user均存入.缓存机制通过对象流实现.
 * 注意:
 * 1,serialVersionUID要显示申明,否则后续加字段,改字段会抛异常;
 * 2,获取单例的getInstance方法里的FILE_NAME,和KEY要和实现的抽象方法里返回的一致,否则会存取失败;
 *
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/11  11:40
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class    UserCacheDao  extends BaseCacheTool<UserBean> {

    private static final long serialVersionUID = 19840902L;


    private final static String POSTFIX=".data";
    private final static String FILE_NAME=UserCacheDao.class.getName().hashCode()+POSTFIX;
    private final static String KEY=UserCacheDao.class.getName().hashCode()+"";
    private final static String NULL="null";

    private String mItemDataKey="login_users";//默认的数据key,此处以"login_users"为key,存储所有登录过的user

    public String getmItemDataKey() {
        return mItemDataKey;
    }

    public void setmItemDataKey(String mItemDataKey) {
        this.mItemDataKey = mItemDataKey;
    }

    private static UserCacheDao ourInstance;

    public static UserCacheDao getInstance(Context context) {

        if (ourInstance == null) {//如果内存里没有
            SharedPreferences mySharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            if (!mySharedPreferences.getString(KEY, NULL).equals(NULL)){//如果xml里有,则取出并赋值给instance
                //数据存在
                SerialRwDataUtil tool = new SerialRwDataUtil();
                ourInstance = (UserCacheDao) tool.GetMessageData(FILE_NAME, context, KEY);
            }
            else {//xml里也没有,则new
                //数据不存在
                ourInstance= new UserCacheDao(context);
                SerialRwDataUtil tool = new SerialRwDataUtil();
                tool.SaveMessageData(FILE_NAME, ourInstance, context, KEY);
            }
        }
        return ourInstance;


    }

    private UserCacheDao(Context context) {
    }


    @Override
    protected String getSharedPreferencesFileName() {
        return FILE_NAME;
    }

    @Override
    protected String getSharedPreferencesKey() {
        return KEY;
    }

    /*****************************************************************************************************************
     * 以上是完成基本逻辑所必须实现的方法
     */


    /**
     * @method name:logout
     * @des:缓存登出
     * @param :[]
     * @return type:boolean
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    public  boolean logout(){

        ArrayList<UserBean> users= (ArrayList<UserBean>) getCache(mItemDataKey);
        if(users==null||users.size()==0){//没有集合
            return  false;
        }
        else {//之前就有
            for (int i = 0; i < users.size(); i++){//遍历
                if (users.get(i)!=null){
                    users.get(i).setmIsLogin(false);
                }
            }
            return  true;
        }
    }

    /**
     * @method name:getLoginUserInfo
     * @des:获取当前登录的用户信息
     * @param :[context, userId]
     * @return type:com.qdong.onemile.entity.UserBean
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    public UserBean getLoginUserInfo(Context context){

        ArrayList<UserBean> users= (ArrayList<UserBean>) getCache(mItemDataKey);
        if(users==null||users.size()==0){//没有集合
            return  null;
        }
        else {//之前就有
            int index = -1;
            for (int i = 0; i < users.size(); i++){//遍历
                if (users.get(i)!=null){
                    if(users.get(i).ismIsLogin()==true){
                        return users.get(i);
                    }
                }
            }
            return  null;
        }
    }


    /**
     * @method name:saveLoginUserInfo
     * @des:保存登录的用户信息
     * @param :[context, info]
     * @return type:boolean
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    public boolean saveLoginUserInfo(Context context,UserBean info){

        if(info==null){
            return  false;
        }
        else {
            int id = info.getUserId();
            info.setmIsLogin(true);//只保持当前这一个是登录状态

            ArrayList<UserBean> users= (ArrayList<UserBean>) getCache(mItemDataKey);
            if(users==null||users.size()==0){//没有集合
                users=new ArrayList<>();
                users.add(info);
                insertDatas(context,users,mItemDataKey);
                return  true;
            }
            else {//之前就有,则覆盖
                int index = -1;
                for (int i = 0; i < users.size(); i++){//遍历,看之前有无此数据,遍历的时候,把是否登录设置为false
                    if (users.get(i)!=null){
                        users.get(i).setmIsLogin(false);
                        if(users.get(i).getUserId()==id){
                            index = i;
                        }
                    }
                }
                if(index!=-1){//之前就存在,移除旧的
                    users.remove(index);
                }
                users.add(info);
                insertDatas(context,users,mItemDataKey);
                return  true;
            }
        }
    }


}
