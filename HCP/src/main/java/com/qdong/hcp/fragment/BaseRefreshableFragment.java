package com.qdong.hcp.fragment;

import com.qdong.communal.library.module.BaseRefreshableListFragment.BaseRefreshableListFragment;
import com.qdong.hcp.activity.AppLoader;

import java.util.HashMap;

/**
 * BaseRefreshableFragment
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/22  16:03
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public abstract class BaseRefreshableFragment<T> extends BaseRefreshableListFragment<T> {


    /**
     * @param :[]
     * @return type:java.util.HashMap<java.lang.String,java.lang.String>
     *
     * @method name:getAutoLoginParameterMap
     * @des:统一提供自动登录时需要的参数map
     * @date 创建时间:2016/8/22
     * @author Chuck
     **/
    @Override
    public HashMap<String, String> getAutoLoginParameterMap() {
        return AppLoader.getInstance().getAutoLoginParameterMap();
    }
}
