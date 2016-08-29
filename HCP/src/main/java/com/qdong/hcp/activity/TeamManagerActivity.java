package com.qdong.hcp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qdong.hcp.R;
import com.qdong.hcp.adapter.TeamManagerAdapter;
import com.qdong.hcp.databinding.ActivityTeammanagerBinding;

import java.util.ArrayList;

/**
 * TeamManagerFt
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  10:06
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class TeamManagerActivity extends BaseActivity<ActivityTeammanagerBinding> {

    private TeamManagerAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(false);
        setContentView(R.layout.activity_teammanager);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mViewBind.commenBar.commenBarTitle.setText("团队管理");
        mViewBind.commenBar.commenBarTvRight.setVisibility(View.VISIBLE);
        mViewBind.commenBar.commenBarTvRight.setText("创建项目经理");
        mViewBind.commenBar.commenBarTvRight.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_add), null, null, null);
        mViewBind.commenBar.commenBarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TeamManagerActivity.this, CreateAndChangeManagerActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        mViewBind.commenBar.commenBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<String> dateList = new ArrayList<String>();
        dateList.add("");
        dateList.add("");
        dateList.add("");
        teamAdapter = new TeamManagerAdapter(this, dateList);
        teamAdapter.setOnClickEidt(new TeamManagerAdapter.interOnClickListener() {
            @Override
            public void editOnClick(int position) {
                Intent intent = new Intent();
                intent.setClass(TeamManagerActivity.this, CreateAndChangeManagerActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        mViewBind.teammanagerDirectorList.setAdapter(teamAdapter);
    }
}
