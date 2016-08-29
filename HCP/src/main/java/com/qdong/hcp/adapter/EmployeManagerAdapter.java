package com.qdong.hcp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qdong.communal.library.module.BaseRefreshableListFragment.BaseRefreshableListFragment;
import com.qdong.hcp.R;
import com.qdong.hcp.activity.EmployeDetailActivity;
import com.qdong.hcp.databinding.ItemEmployeManagerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * EmployeManagerAdapter
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  17:33
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class EmployeManagerAdapter extends com.qdong.communal.library.module.BaseRefreshableListFragment.MyBaseRecyclerAdapter<EmployeManagerAdapter.MyViewHolder, String> {

    private Activity activity;
    private ArrayList<String> dateList;
    private ItemEmployeManagerBinding employeBinding;

    public EmployeManagerAdapter(List<String> mData, BaseRefreshableListFragment fragment) {
        super(mData, fragment);
        activity = fragment.getActivity();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public MyViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mFragment.getActivity()).
                inflate(R.layout.item_employe_manager, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void getView(MyViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, EmployeDetailActivity.class));
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
