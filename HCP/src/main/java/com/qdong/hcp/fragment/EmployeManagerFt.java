package com.qdong.hcp.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.JsonElement;
import com.qdong.communal.library.module.BaseRefreshableListFragment.MyBaseRecyclerAdapter;
import com.qdong.communal.library.module.network.QDongApi;
import com.qdong.communal.library.module.network.QDongNetInfo;
import com.qdong.hcp.activity.EmployeDetailActivity;
import com.qdong.hcp.adapter.EmployeManagerAdapter;
import com.qdong.hcp.entity.CommunityBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * EmployeManagerFt
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  17:27
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class EmployeManagerFt extends BaseRefreshableFragment<String> {

    private EmployeManagerAdapter myAdapter;

    @Override
    public Observable<QDongNetInfo> callApi(QDongApi api, int currentPage, int maxPage) {
        return api.findLatestDynamic(currentPage, maxPage);
    }

    @Override
    public String getBaseUrl() {
        return "http://1501q8n685.51mypc.cn:10005/";
    }

    @Override
    public List<String> resolveData(JsonElement jsonStr) {
        ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        return mDatas;
    }

    @Override
    public MyBaseRecyclerAdapter initAdapter() {
        ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        myAdapter = new EmployeManagerAdapter(mDatas, this);
        return myAdapter;
    }

    @Override
    public void onInitDataResult(boolean isSuccessfuly) {

    }

    @Override
    public void onRefreshDataResult(boolean isSuccessfuly) {

    }

    @Override
    public void onLoadMoreDataResult(boolean isSuccessfuly) {

    }
}
