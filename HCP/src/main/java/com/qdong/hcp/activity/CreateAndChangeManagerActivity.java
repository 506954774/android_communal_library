package com.qdong.hcp.activity;

import android.os.Bundle;
import android.view.View;

import com.qdong.communal.library.util.ToastUtil;
import com.qdong.hcp.R;
import com.qdong.hcp.databinding.ActivityCreateNewManagerBinding;

/**
 * CreateAndChangeManager
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  19:29
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class CreateAndChangeManagerActivity extends BaseActivity<ActivityCreateNewManagerBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(false);
        setContentView(R.layout.activity_create_new_manager);
        if (getIntent().getExtras().getInt("type") == 1) {//创建
            mViewBind.createChangeNamagerTitle.commenBarTitle.setText(getResources().getString(R.string.createNewManager));
        } else {//修改
            mViewBind.createChangeNamagerTitle.commenBarTitle.setText("修改经理信息");
            mViewBind.createChangeNamagerAccount.setText(getIntent().getExtras().getString("111"));//账号
            mViewBind.createChangeNamagerPassword.setText(getIntent().getExtras().getString("111"));//密码
            mViewBind.createChangeNamagerName.setText(getIntent().getExtras().getString("111"));//姓名
            mViewBind.createChangeNamagerPhone.setText(getIntent().getExtras().getString("111"));//电话号码
        }
        mViewBind.createChangeNamagerTitle.commenBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewBind.createChangeNamagerCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewBind.createChangeNamagerAccount.getText().toString().trim().equals("")) {
                    ToastUtil.showCustomMessage(CreateAndChangeManagerActivity.this, "请输入账号");
                } else if (mViewBind.createChangeNamagerPassword.getText().toString().trim().equals("")) {
                    ToastUtil.showCustomMessage(CreateAndChangeManagerActivity.this, "请输入密码");
                } else if (mViewBind.createChangeNamagerName.getText().toString().trim().equals("")) {
                    ToastUtil.showCustomMessage(CreateAndChangeManagerActivity.this, "请输入姓名");
                } else if (mViewBind.createChangeNamagerPhone.getText().toString().trim().equals("")) {
                    ToastUtil.showCustomMessage(CreateAndChangeManagerActivity.this, "请输入电话");
                }
            }
        });
    }
}