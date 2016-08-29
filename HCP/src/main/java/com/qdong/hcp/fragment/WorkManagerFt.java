/**
 *
 */
package com.qdong.hcp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.qdong.hcp.R;
import com.qdong.hcp.activity.EmployeManagerActivity;
import com.qdong.hcp.activity.MainActivity;
import com.qdong.hcp.activity.TeamManagerActivity;
import com.qdong.hcp.adapter.WorkManagerAdapter;
import com.qdong.hcp.databinding.FragmentWorkmanagerBinding;

import java.util.ArrayList;


/**
 * Title:WorkManagerFt Description:
 *
 * @author YYH
 * @date 2016年8月20日 上午10:15:30
 */
public class WorkManagerFt extends BaseFragment<FragmentWorkmanagerBinding> {

    private MainActivity activity;
    private ListView ListView;
    private WorkManagerAdapter managerAdapter;
    private ArrayList<String> dateList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_workmanager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        loadData();
        initListener();
    }

    /**
     * 增加点击事件
     */
    private void initListener() {
        mViewBind.wrokmanagerTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TeamManagerActivity.class));
            }
        });
        mViewBind.wrokmanagerEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeManagerActivity.class));
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        dateList = new ArrayList<String>();
        dateList.add("");
        dateList.add("");
        dateList.add("");
        dateList.add("");
        dateList.add("");
        dateList.add("");
        managerAdapter = new WorkManagerAdapter(activity, dateList);
        mViewBind.workManagerList.setAdapter(managerAdapter);
    }

}
