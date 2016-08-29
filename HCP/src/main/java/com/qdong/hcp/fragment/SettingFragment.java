package com.qdong.hcp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.qdong.hcp.R;

/**
 * SettingFragment
 * 设置
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/8  9:54
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class SettingFragment extends BaseFragment {

    public static SettingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }
}
