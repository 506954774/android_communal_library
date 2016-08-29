package com.qdong.hcp.fragment;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.qdong.hcp.R;
import com.qdong.hcp.activity.AppLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * BaseFragment
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/8  9:44
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    protected T mViewBind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       /* if (mViewBind == null) {
            mViewBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, true);
        } else {
            // 不为null时，需要把自身从父布局中移除，因为ViewPager会再次添加
            ViewParent parent = mViewBind.getRoot().getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView( mViewBind.getRoot());
            }
        }*/

        mViewBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewBind.getRoot();
    }

    public abstract int getLayoutId();



    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onPageStart(getClass().getName()); //统计页面，页面名可自定义
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPageEnd(getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
