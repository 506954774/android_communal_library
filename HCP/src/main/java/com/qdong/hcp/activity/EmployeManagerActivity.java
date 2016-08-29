package com.qdong.hcp.activity;

import android.os.Bundle;
import android.view.View;

import com.qdong.hcp.R;
import com.qdong.hcp.databinding.ActivityEmployeManagerBinding;
import com.qdong.hcp.fragment.EmployeManagerFt;

/**
 * EmployeManagerActivity
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  17:21
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class EmployeManagerActivity extends BaseActivity<ActivityEmployeManagerBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(false);
        setContentView(R.layout.activity_employe_manager);
        mViewBind.titleEmploe.commenBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewBind.titleEmploe.commenBarTitle.setText("人员结构");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_employe_manager, new EmployeManagerFt()).commit();
    }
}
