package com.qdong.communal.library.module.GuiderActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qdong.communal.library.R;

import java.util.ArrayList;
import java.util.List;


/**
 * BaseGuideActivity
 * 引导界面的抽象父类,子类必须实现三个方法:
 * {@link #getGuideImageResourseIds()}:提供看完引导页,点击按钮要跳转的类
 * {@link #getDestinationActivity()}:提供引导界面资源图片数组
 * {@link #onShowGuideFinish()}:引导页看完的回调函数
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/27  19:45
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public  abstract class BaseGuideActivity extends Activity implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private List<ImageView> imageViewList;
    private int basicWidth = -1; // 点与点之间的距离, 默认为-1
    private LinearLayout llPointGroup; // 点的组
    private View mSelectedPoint;
    private Button btnStartExperience;

    //引导页数量以及显示的图片资源id
    int[] imageIDs = getGuideImageResourseIds();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        initView();
    }


    /**
     * @method name:initView
     * @des:初始化控件
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/8/27
     * @author Chuck
     **/
    private void initView() {

        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        btnStartExperience = (Button) findViewById(R.id.btn_guide_start_experience);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_guide_point_group);
        mSelectedPoint = findViewById(R.id.v_guide_selected_point);

        imageViewList = new ArrayList<ImageView>();

        ImageView iv;
        View view;
        for (int i = 0; i < imageIDs.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageIDs[i]);
            imageViewList.add(iv);

            // 向LinearLayout中添加一个点的控件
            view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelOffset(R.dimen.guide_point_bulk),
                    getResources().getDimensionPixelOffset(R.dimen.guide_point_bulk));
            if(i != 0) {
                params.leftMargin = 20;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.mipmap.guide_point_gray_bg);
            llPointGroup.addView(view);
        }

        // 把Adapter适配器和ViewPager关联起来
        GuideAdapter mAdapter = new GuideAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        btnStartExperience.setOnClickListener(this);
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 把对应position位置的Imageview在ViewPager中移除掉
            container.removeView(imageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 向ViewPager中添加一个ImageView对象, 并且在此方法中把那个被添加的imageView对象返回
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }
    }

    /**
     * 当滚动时
     * position 目前被选中的页面的索引
     * positionOffset 移动到下一个页面的比例, 范围是: 0.0 ~ 1.0
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if(basicWidth <= 0) {
            // 只有在第一次时, 当滚动时取出基本移动的宽度, 计算方式: 第1个点的左边 - 第0个点的左边 = 距离
            basicWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
        }

        int leftMargin = (int) ((position + positionOffset) * basicWidth);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.guide_point_bulk),
                getResources().getDimensionPixelOffset(R.dimen.guide_point_bulk));
        params.leftMargin = leftMargin;
        mSelectedPoint.setLayoutParams(params);
    }

    /**
     * 当新的页面被选中时
     */
    @Override
    public void onPageSelected(int position) {
        if(position == imageViewList.size() - 1) {
            btnStartExperience.setVisibility(View.VISIBLE);
        } else {
            btnStartExperience.setVisibility(View.GONE);
        }
    }

    /**
     * 当页面滚动的状态改变时
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        if(getDestinationActivity()!=null){
            //跳转到xxx Activity
            startActivity(new Intent(this, getDestinationActivity()));
            // 在此函数里修改布尔值
            onShowGuideFinish();
            finish();
        }

    }


    /**子类提供点击后跳转的类**/
    public abstract Class getDestinationActivity();
    /***子类提供滑动的几张图片**/
    public abstract int[] getGuideImageResourseIds();
    /***引导页结束,回调,通常在里面改变一个布尔值,表示引导页已经展示过了***/
    public abstract void onShowGuideFinish();
}
