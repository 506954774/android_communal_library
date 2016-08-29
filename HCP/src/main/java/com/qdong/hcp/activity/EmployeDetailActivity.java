package com.qdong.hcp.activity;

import android.os.Bundle;
import android.view.View;

import com.qdong.hcp.R;
import com.qdong.hcp.adapter.ContactsExpanableListAdapter;
import com.qdong.hcp.databinding.ActivityEmployeDetailBinding;

import java.util.ArrayList;

/**
 * EmployeDetailActivity
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-23  12:10
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class EmployeDetailActivity extends BaseActivity<ActivityEmployeDetailBinding> {

    private ContactsExpanableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(false);
        setContentView(R.layout.activity_employe_detail);
        mViewBind.titleEmploe.commenBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewBind.titleEmploe.commenBarTitle.setText("人员结构");
        ArrayList<String> gruopList = new ArrayList<String>();
        ArrayList<String> childList = new ArrayList<String>();
        gruopList.add("");
        gruopList.add("");
        gruopList.add("");
        childList.add("");
        childList.add("");
        childList.add("");
        listAdapter = new ContactsExpanableListAdapter(EmployeDetailActivity.this, gruopList, childList);
        mViewBind.employeExpandeList.setAdapter(listAdapter);
    }
}
