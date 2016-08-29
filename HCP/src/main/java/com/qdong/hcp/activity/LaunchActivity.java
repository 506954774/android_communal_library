package com.qdong.hcp.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import com.qdong.communal.library.util.DensityUtil;
import com.qdong.communal.library.util.SharedPreferencesUtil;
import com.qdong.hcp.R;
import com.qdong.hcp.utils.Constants;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * LaunchActivity
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/7  15:38
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class LaunchActivity extends BaseActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setIsTitleBar(false);
               setContentView(R.layout.launch_activity);


        subscription=Observable
                .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())//延时2秒跳转,这个操作符产生一个Observer<Long>
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        startActivity(new Intent(LaunchActivity.this,
                                /**首次进来,则先展示引导界面.isGuideActvityShowed()==true表示已经展示过了引导界面**/
                                SharedPreferencesUtil.getInstance(LaunchActivity.this).isGuideActvityShowed()? MainActivity.class:GuideActivity.class));

                        finish();
                        return null;
                    }
                })
                .subscribe();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //activity获取焦点时调用
        if(hasFocus){
            //初始化应用状态栏的高度
            Rect outRect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            if(outRect.top != 0 && AppLoader.STATUS_BAR_HEIGHT == 0) {
                AppLoader.STATUS_BAR_HEIGHT = outRect.top;
                SharedPreferencesUtil.getInstance(this).putInt(SharedPreferencesUtil.STATEBARHEIGHT, outRect.top);
            }

             int  screenHeight= DensityUtil.getDisplayHeight(this);
             int  screenWidth= DensityUtil.getDisplayWidth(this);
             if(screenHeight>0){
                 SharedPreferencesUtil.getInstance(this).putInt(Constants.SCREEN_HEIGHT,screenHeight);
             }
             if(screenWidth>0){
                 SharedPreferencesUtil.getInstance(this).putInt(Constants.SCREEN_WIDTH,screenWidth);
             }
        }
    }

}
