package com.qdong.hcp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qdong.hcp.R;
import com.qdong.hcp.databinding.FtMyBinding;

/**
 * 个人中心
 * Created by LHD on 2016/8/20.
 */
public class MyFt extends BaseFragment<FtMyBinding> implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.ft_my;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mViewBind.setClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userMessage:
                break;
            case R.id.transactionDetailTv:
                break;
            case R.id.profileTv:
                break;
            case R.id.evaluateTv:
                break;
            case R.id.settingTv:
                break;
            case R.id.feedbackTv:
                break;
            case R.id.callTv:
                break;
        }
    }
}
