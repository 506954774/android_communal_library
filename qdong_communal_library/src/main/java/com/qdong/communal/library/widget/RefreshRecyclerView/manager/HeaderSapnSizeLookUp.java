package com.qdong.communal.library.widget.RefreshRecyclerView.manager;

import android.support.v7.widget.GridLayoutManager;

import com.qdong.communal.library.widget.RefreshRecyclerView.adapter.RefreshRecyclerViewAdapter;


/**
 * Created by Syehunter on 2015/11/22.
 */
public class HeaderSapnSizeLookUp extends GridLayoutManager.SpanSizeLookup {

    private RefreshRecyclerViewAdapter mAdapter;
    private int mSpanSize;

    public HeaderSapnSizeLookUp(RefreshRecyclerViewAdapter adapter, int spanSize){
        this.mAdapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = mAdapter.isHeader(position) || mAdapter.isFooter(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}
