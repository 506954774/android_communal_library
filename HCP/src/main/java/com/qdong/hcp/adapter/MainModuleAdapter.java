package com.qdong.hcp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdong.hcp.R;
import com.qdong.hcp.entity.MainModuleBean;

import java.util.ArrayList;

/**
 * MainDoduleAdapter
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/20  18:23
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class MainModuleAdapter extends BaseAdapter {

    private ArrayList<MainModuleBean> mData;
    private Context mContext;

    public MainModuleAdapter(ArrayList<MainModuleBean> mData, Context mContext) {
        this.mData = mData==null?new ArrayList<MainModuleBean>():mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MainModuleBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MainModuleBean bean =mData.get(position);
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.gridview_item_home_modle,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        if(bean!=null){
            try {
                holder.ivModuleIcon.setImageResource(bean.getImageResouseId());
                holder.tvModuleName.setText(bean.getStringResouseId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return convertView;
    }

    protected class ViewHolder {
        private ImageView ivModuleIcon;
        private TextView tvModuleName;

        public ViewHolder(View view) {
            ivModuleIcon = (ImageView) view.findViewById(R.id.iv_module_icon);
            tvModuleName = (TextView) view.findViewById(R.id.tv_module_name);
        }
    }
}
