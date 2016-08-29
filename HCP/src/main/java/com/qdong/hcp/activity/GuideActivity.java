package com.qdong.hcp.activity;

import com.qdong.communal.library.module.GuiderActivity.BaseGuideActivity;
import com.qdong.communal.library.util.SharedPreferencesUtil;
import com.qdong.hcp.R;

/**
 * GuideActivity
 * 引导页,实现三个方法
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/27  19:26
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class GuideActivity extends BaseGuideActivity {
    @Override
    public Class getDestinationActivity() {
        return MainActivity.class;
    }

    @Override
    public int[] getGuideImageResourseIds() {
        return new int[]{R.mipmap.guide_bg1,R.mipmap.guide_bg3,R.mipmap.guide_bg1,R.mipmap.guide_bg3,R.mipmap.guide_bg1,R.mipmap.guide_bg3};
    }

    @Override
    public void onShowGuideFinish() {
        /**存值,已经展示过了引导界面*/
        SharedPreferencesUtil.getInstance(this).setGuideActivityShowed();
    }
}
