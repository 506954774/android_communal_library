package com.qdong.communal.library.cache;/**
 * Created by AA on 2016/6/18.
 */

import android.content.Context;
import android.text.TextUtils;

import com.qdong.communal.library.util.SerialRwDataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * BaseCacheTool
 * 数据缓存基类
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/18  10:04
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public abstract class BaseCacheTool<T extends Serializable> implements Serializable {


    private static final long serialVersionUID = 19840902L;


    private List<BaseCacheData> mDatas = new ArrayList<BaseCacheData>();


    protected List<BaseCacheData> getDatas() {
        return mDatas;
    }


    protected void setDatas(List<BaseCacheData> datas) {
        this.mDatas = datas==null?new ArrayList<BaseCacheData>():datas;
    }

    /**
     * @method name:insertDatas
     * @des:插入数据
     * @param :[context:上下文, subDatas:集合, key:键]
     * @return type:void
     * @date 创建时间:2016/6/18
     * @author Chuck
     **/
    public void insertDatas(Context context,List<T> subDatas,String key){

        int index = -1;
        for (int i = 0; i < mDatas.size(); i++){//遍历,看之前有无此数据
            String k=mDatas.get(i).getKey();
            if (!TextUtils.isEmpty(k)&&k.equals(key)){
                index = i;
                break;
            }
        }
        if(index != -1){//存在,则更新
            if (subDatas != null){
                mDatas.get(index).setmSubDatas(subDatas);
            }
        }
        else{//不存在,则加入
            BaseCacheData item = new BaseCacheData();
            item.setKey(key);
            if (subDatas != null){
                item.setmSubDatas(subDatas);
            }
            mDatas.add(item);
        }
        saveCache(context);
    }

    /**
     * @method name:removeDatasByKey
     * @des:按键删除数据集合
     * @param :[context, key]
     * @return type:void
     * @date 创建时间:2016/6/18
     * @author Chuck
     **/
    public void removeDatasByKey(Context context,String key){

        int index = -1;
        for (int i = 0; i < mDatas.size(); i++){//遍历,看之前有无此数据
            String k=mDatas.get(i).getKey();
            if (!TextUtils.isEmpty(k)&&k.equals(key)){
                index = i;
                break;
            }
        }
        if(index != -1){//存在,则删除
            mDatas.remove(index);
        }

        saveCache(context);
    }


    /**
     * 获取集合
     * @param key
     * @return
     */
    public List<T> getCache(String key){
        int index = -1;
        for (int i = 0; i < mDatas.size(); i++){//遍历,看之前有无此数据
            String k=mDatas.get(i).getKey();
            if (!TextUtils.isEmpty(k)&&k.equals(key)){
                index = i;
                break;
            }
        }
        if(index != -1){//存在,则返回
            {
                return mDatas.get(index).getmSubDatas();
            }
        }
        return null;
    }


    /**
     * BaseCacheData
     * 数据缓存实体基类
     * 责任人:  Chuck
     * 修改人： Chuck
     * 创建/修改时间: 2016/6/17  22:11
     * Copyright : 趣动智能科技有限公司-版权所有
     **/
    private class BaseCacheData implements Serializable {

        private static final long serialVersionUID = 19840902L;

        private List<T> mSubDatas;

        private String key;


        public List<T> getmSubDatas() {
            return mSubDatas;
        }

        public void setmSubDatas(List<T> mSubDatas) {
            this.mSubDatas = mSubDatas;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private void saveCache(Context context){
        SerialRwDataUtil tool = new SerialRwDataUtil();
        tool.SaveMessageData(getSharedPreferencesFileName(), this, context, getSharedPreferencesKey());
    }


    /**子类实现此方法,设置在偏好设置的文件名**/
    protected abstract String getSharedPreferencesFileName();
    /**子类实现此方法,设置key**/
    protected abstract String getSharedPreferencesKey();
}
