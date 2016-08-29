/**
 *
 */
package com.qdong.hcp.fragment;

import java.util.ArrayList;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.qdong.communal.library.widget.TabViews.OnSelectedIndexChangedListener;
import com.qdong.hcp.R;
import com.qdong.hcp.activity.MainActivity;
import com.qdong.hcp.adapter.ContactsExpanableListAdapter;
import com.qdong.hcp.adapter.ContactsListViewAdapter;
import com.qdong.hcp.databinding.FragmentContactsBinding;

/**
 * Title:ContactsFt Description:
 *
 * @author YYH
 * @date 2016年8月20日 下午2:23:09
 */
public class ContactsFt extends BaseFragment<FragmentContactsBinding> implements OnSelectedIndexChangedListener {

    private MainActivity activity;
    private ContactsListViewAdapter contactsListAdapter;
    private ContactsExpanableListAdapter contactsExpanaAdatper;
    private ArrayList<String> dateList, dateList2;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        dateList = new ArrayList<String>();
        dateList.add("");
        dateList.add("");
        dateList2 = new ArrayList<String>();
        dateList2.add("");
        dateList2.add("");
        contactsListAdapter = new ContactsListViewAdapter(activity, dateList);
        contactsExpanaAdatper = new ContactsExpanableListAdapter(activity, dateList, dateList2);
       /* mViewBind.contantsTvMessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewBind.contactsExpandeList.setVisibility(View.GONE);
                mViewBind.contactsMessageList.setVisibility(View.VISIBLE);
                mViewBind.contactsMessageList.setAdapter(contactsListAdapter);
            }
        });
        mViewBind.contantsTvContants.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewBind.contactsExpandeList.setVisibility(View.VISIBLE);
                mViewBind.contactsMessageList.setVisibility(View.GONE);
                mViewBind.contactsExpandeList.setAdapter(contactsExpanaAdatper);
            }
        });*/
        mViewBind.contactsExpandeList.setVisibility(View.GONE);
        mViewBind.contactsMessageList.setVisibility(View.VISIBLE);
        mViewBind.contactsMessageList.setAdapter(contactsListAdapter);

        /**自定义的tabbar加标题,加监听 by:chuck 2016/08/24**/
        mViewBind.tabsWithoutViewpager.setOnSelectedChangeListener(this);
        mViewBind.tabsWithoutViewpager.setUpView(getString(R.string.message),getString(R.string.contacts));
    }

    @Override
    public void onSelectedIndexChanged(int index) {
        switch (index){
            case 0:
                mViewBind.contactsExpandeList.setVisibility(View.GONE);
                mViewBind.contactsMessageList.setVisibility(View.VISIBLE);
                mViewBind.contactsMessageList.setAdapter(contactsListAdapter);
                break;
            case 1:
                mViewBind.contactsExpandeList.setVisibility(View.VISIBLE);
                mViewBind.contactsMessageList.setVisibility(View.GONE);
                mViewBind.contactsExpandeList.setAdapter(contactsExpanaAdatper);
                break;
        }
    }
}
