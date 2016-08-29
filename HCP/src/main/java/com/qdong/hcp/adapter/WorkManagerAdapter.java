/**
 *
 */
package com.qdong.hcp.adapter;

import java.util.ArrayList;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qdong.hcp.R;
import com.qdong.hcp.activity.MainActivity;

/**
 * Title:WorkManagerAdapter Description:
 *
 * @author YYH
 * @date 2016年8月20日 上午10:39:50
 */
public class WorkManagerAdapter extends BaseAdapter {

    private MainActivity activity;
    private ArrayList<String> dateList;

    public WorkManagerAdapter(MainActivity activity, ArrayList<String> dateList) {
        this.activity = activity;
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_workmanager, parent, false);
            hodler.WorkManagerWorkName = (TextView) convertView.findViewById(R.id.item_work_manager_workname);
            hodler.WorkManagerCompanyName = (TextView) convertView.findViewById(R.id.item_work_manager_companyname);
            hodler.WorkManagerDirectorName = (TextView) convertView.findViewById(R.id.item_work_manager_directorname);
            hodler.WorkManagerTime = (TextView) convertView.findViewById(R.id.item_work_manager_time);
            hodler.WorkManagerSpendTime = (TextView) convertView.findViewById(R.id.item_work_manager_spendtime);
            hodler.WorkManagerStatus = (TextView) convertView.findViewById(R.id.item_work_manager_status);
            hodler.WorkManagerTimeHaveSpend = (TextView) convertView.findViewById(R.id.item_work_manager_time_havespend);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHodler {
        private TextView WorkManagerWorkName;
        private TextView WorkManagerCompanyName;
        private TextView WorkManagerDirectorName;
        private TextView WorkManagerTime;
        private TextView WorkManagerSpendTime;
        private TextView WorkManagerTimeHaveSpend;
        private TextView WorkManagerStatus;
    }

}
