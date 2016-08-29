/**
 *
 */
package com.qdong.hcp.adapter;

import java.util.ArrayList;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdong.hcp.R;
import com.qdong.hcp.activity.MainActivity;

/**
 * Title:ContactsExpanableListAdapter Description:
 *
 * @author YYH
 * @date 2016年8月20日 下午3:44:56
 */
public class ContactsExpanableListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<String> groupList;
    private ArrayList<String> childList;

    public ContactsExpanableListAdapter(Activity activity, ArrayList<String> groupList,
                                        ArrayList<String> childList) {
        this.activity = activity;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_gorup_contacts, parent, false);
            holder.contactc_group_role_name = (TextView) convertView.findViewById(R.id.contactc_group_role_name);
            holder.contactc_group_count = (TextView) convertView.findViewById(R.id.contactc_group_count);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_child_contacts, parent, false);
            holder.item_child_usericon = (ImageView) convertView.findViewById(R.id.item_child_usericon);
            holder.item_child_username = (TextView) convertView.findViewById(R.id.item_child_username);
            holder.item_child_content = (TextView) convertView.findViewById(R.id.item_child_content);
            holder.item_child_time = (TextView) convertView.findViewById(R.id.item_child_time);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupViewHolder {
        private TextView contactc_group_role_name;
        private TextView contactc_group_count;
    }

    private class ChildViewHolder {
        private ImageView item_child_usericon;
        private TextView item_child_username;
        private TextView item_child_content;
        private TextView item_child_time;
    }

}
