package com.qdong.communal.library.widget.RefreshRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qdong.communal.library.widget.RefreshRecyclerView.listener.LoadMoreRecyclerListener;
import com.qdong.communal.library.widget.RefreshRecyclerView.listener.OnBothRefreshListener;
import com.qdong.communal.library.widget.RefreshRecyclerView.listener.OnLoadMoreListener;
import com.qdong.communal.library.widget.RefreshRecyclerView.listener.OnPullDownListener;
import com.qdong.communal.library.widget.RefreshRecyclerView.manager.RecyclerMode;
import com.qdong.communal.library.widget.RefreshRecyclerView.widget.RefreshHeader;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by Syehunter on 2015/11/22.
 */
public class RefreshRecyclerView extends PtrFrameLayout {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private LayoutParams params;
    private LoadMoreRecyclerListener mOnScrollListener;
    private RecyclerMode mode;
    private RefreshHeader mHeaderView;
    private float mDownY;

    public RefreshRecyclerView(Context context) {
        super(context);
        this.mContext = context;
        init();

    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mRecyclerView = new RecyclerView(mContext);
        params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(params);
        addView(mRecyclerView);

        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        setKeepHeaderWhenRefresh(true);
        setPullToRefresh(false);
        //ViewPager滑动冲突
        disableWhenHorizontalMove(true);

        mHeaderView = new RefreshHeader(mContext);
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        if (null == adapter){
            throw new NullPointerException("adapter cannot be null");
        }
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator){
        if (null == itemAnimator){
            return;
        }
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    public void setMode(RecyclerMode mode){
        this.mode = mode;
        if (RecyclerMode.NONE == mode || RecyclerMode.BOTTOM == mode){

            setEnabled(false);
        } else {
            setEnabled(true);
        }

        if(null != mOnScrollListener){
            mOnScrollListener.setMode(mode);
        }
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        if (null == listener){
            return;
        }
        if (listener instanceof LoadMoreRecyclerListener){
            mOnScrollListener = (LoadMoreRecyclerListener) listener;
            mRecyclerView.addOnScrollListener(mOnScrollListener);
        } else {
            mRecyclerView.addOnScrollListener(listener);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(){
        return mRecyclerView.getLayoutManager();
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor){
        if (null == decor){
            return;
        }
        mRecyclerView.addItemDecoration(decor);
    }

    public void setOnBothRefreshListener(final OnBothRefreshListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            //当前允许下拉刷新

            setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if (null != mOnScrollListener){
                mOnScrollListener.setOnBothRefreshListener(listener);
            }
        }
    }

    public void setOnPullDownListener(final OnPullDownListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            //当前允许下拉刷新
            setPtrHandler(new PtrHandler() {

                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    listener.onPullDown();
                }
            });
        }
    }

    public void setOnLoadMoreListener(final OnLoadMoreListener listener){
        if (RecyclerMode.NONE == mode || null == listener){
            return;
        }

        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if (null != mOnScrollListener){
                mOnScrollListener.setOnLoadMoreListener(listener);
            }
        }
    }

    public RecyclerView real(){
        return mRecyclerView;
    }

    public void onRefreshCompleted(){
        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            refreshComplete();
        }
        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if (null != mOnScrollListener){
                mOnScrollListener.onRefreshComplete();
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (!isEnabled()) {
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mDownY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float currentY = e.getY();
                    if ((currentY - mDownY) > 0) {
                        //手指向下
                        mOnScrollListener.isLoadingMoreEnabled = false;
                    }
                    else {
                        //手指向上
                        mOnScrollListener.isLoadingMoreEnabled = true;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(e);
    }


    /*********************************************************************************************
     * 加上这部分代码,实现"无更多数据"的footview展示
     * 2016/06/21 by:Chuck
     */
    private Handler mUiHanler =new Handler(Looper.getMainLooper());
    public void onNoMoreDataDisappearDelay(long delay){
        onRefreshCompleted();
        if (null != mOnScrollListener){
            mOnScrollListener.onNoMoreData(mRecyclerView);
        }
        mUiHanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshCompleted();
            }
        },delay);

    }
    public void onNoMoreData(){
        onRefreshCompleted();
        if (null != mOnScrollListener){
            mOnScrollListener.onNoMoreData(mRecyclerView);
        }

    }
    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
    /****
     * 加上这部分代码,实现"无更多数据"的footview展示
     * 2016/06/21 by:Chuck  end
     ***********************************************************************************************************/


}
