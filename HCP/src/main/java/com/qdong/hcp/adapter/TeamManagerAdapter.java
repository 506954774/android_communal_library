package com.qdong.hcp.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qdong.hcp.R;
import com.qdong.hcp.databinding.ItemTeammanagerBinding;

import java.util.ArrayList;

/**
 * TeamManagerAdapter
 * 责任人:  杨毅晖
 * 修改人： 杨毅晖
 * 创建/修改时间: 2016-08-22  12:39
 * Copyright : 2015-2016 深圳趣动智能科技有限公司-版权所有
 **/
public class TeamManagerAdapter extends BaseAdapter {

    private ArrayList<String> dataList;
    private ItemTeammanagerBinding itemBinding;
    private Activity activity;
    private interOnClickListener onClickEidt;

    public TeamManagerAdapter(Activity activity, ArrayList<String> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            itemBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_teammanager, parent, false);
            convertView = itemBinding.getRoot();
            convertView.setTag(itemBinding);
        } else {
            itemBinding = (ItemTeammanagerBinding) convertView.getTag();
        }
        itemBinding.itemTeamManagerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEidt.editOnClick(position);
            }
        });
        return convertView;
    }

    public interface interOnClickListener {
        public void editOnClick(int position);
    }

    public void setOnClickEidt(interOnClickListener onClickEidt) {
        this.onClickEidt = onClickEidt;
    }
}
