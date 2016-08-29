package com.qdong.hcp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qdong.communal.library.module.BaseRefreshableListFragment.BaseRefreshableListFragment;
import com.qdong.communal.library.widget.StarEvaluateView;
import com.qdong.hcp.R;

import com.qdong.hcp.entity.CommunityBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * MyAdapter
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/22  14:41
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class ProjectAdapter extends com.qdong.communal.library.module.BaseRefreshableListFragment.MyBaseRecyclerAdapter<ProjectAdapter.MyViewHolder,CommunityBean> {


    public ProjectAdapter(List<CommunityBean> mData, BaseRefreshableListFragment fragment) {
        super(mData, fragment);
    }


    @Override
    public MyViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mFragment.getActivity()).
                inflate(R.layout.listview_item_jobs, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void getView(MyViewHolder holder, final int position) {

        final CommunityBean bean =mData.get(position);

        if(bean!=null){

            float[] floats={0.5f,1.0f,5.0f,4.0f,3.5f};
            int index=position%5;
            float f =floats[index];
            TextView tv= (TextView) holder.itemView.findViewById(R.id.tv_star);

            //BigDecimal b  =   new BigDecimal(f);
            //float   f1   =  b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();//保留一位小数
            tv.setText(f+"分");
            StarEvaluateView view= (StarEvaluateView) holder.itemView.findViewById(R.id.stars_evaluate);
            view.setDefaultChosen(f,"");

        }

    }


     class MyViewHolder extends RecyclerView.ViewHolder {


            MyViewHolder(View itemView) {
             super(itemView);
            }
    }

}