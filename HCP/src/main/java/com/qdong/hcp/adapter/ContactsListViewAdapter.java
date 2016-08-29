/**
 * 
 */
package com.qdong.hcp.adapter;

import java.util.ArrayList;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdong.hcp.R;
import com.qdong.hcp.activity.MainActivity;

/**
 * Title:ContactsListViewAdapter Description:
 *
 * @author YYH
 * @date 2016年8月20日 下午3:13:30
 */
public class ContactsListViewAdapter extends BaseAdapter {

	private MainActivity activity;
	private ArrayList<String> dataList;

	public ContactsListViewAdapter(MainActivity activity, ArrayList<String> dateList) {
		this.activity = activity;
		this.dataList = dateList;
	}

	@Override
	public int getCount() {
		return dataList.size();
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_contacts_chat, parent, false);
			holder.item_chat_usericon = (ImageView) convertView.findViewById(R.id.item_chat_usericon);
			holder.item_chat_username = (TextView) convertView.findViewById(R.id.item_chat_username);
			holder.item_chat_content = (TextView) convertView.findViewById(R.id.item_chat_content);
			holder.item_chat_time = (TextView) convertView.findViewById(R.id.item_chat_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private class ViewHolder {
		private ImageView item_chat_usericon;
		private TextView item_chat_username;
		private TextView item_chat_content;
		private TextView item_chat_time;
	}

}
