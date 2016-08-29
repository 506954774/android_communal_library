package com.qdong.hcp.customWidget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.qdong.hcp.R;

import cn.bingoogolapple.badgeview.BGABadgeRadioButton;
import cn.bingoogolapple.badgeview.BGABadgeView;

/**
 * CustomBottomBar
 * 底部radioGroup
 * 通过{@link #setUnReadPoint(int)}设置某个radioBtn的红点
 * 通过{@link #setUnReadPointQuantitiy(int,String)}设置某个radioBtn的红色提示信息
 * 通过{@link #clearPoint(int)}清除某个radioBtn的红色提示信息
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/20  11:20
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class CustomBottomBar extends RelativeLayout implements RadioGroup.OnCheckedChangeListener{


    private Context mContext;
    private RadioGroup mRadioGroup;
    private  BottomBarOnClickListener mListener;
    private Animation animation;//动画
    private BGABadgeRadioButton[] mRadioBtns=new BGABadgeRadioButton[4];


    public CustomBottomBar(Context context) {
        super(context);
        this.mContext=context;
        initView();
    }

    public CustomBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initView();
    }

    public CustomBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    public BottomBarOnClickListener getmListener() {
        return mListener;
    }

    public void setmListener(BottomBarOnClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * @method name:initView
     * @des:初始化布局
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.custom_bottom_bar, this, true);
        this.animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_ainimation);
        mRadioGroup= (RadioGroup) findViewById(R.id.downbar);
        mRadioGroup.setOnCheckedChangeListener(this);
        findViewById(R.id.create_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onCreatBtnClick();
                }
            }
        });
        mRadioGroup.check(R.id.rb_fragment_home);

        mRadioBtns[0]=(BGABadgeRadioButton) findViewById(R.id.rb_fragment_home);
        mRadioBtns[1]=(BGABadgeRadioButton) findViewById(R.id.rb_fragment_job_manager);
        mRadioBtns[2]=(BGABadgeRadioButton) findViewById(R.id.rb_fragment_contacts);
        mRadioBtns[3]=(BGABadgeRadioButton) findViewById(R.id.rb_fragment_mine);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_fragment_home:
                onCheckChanged(0);
                break;
            case R.id.rb_fragment_job_manager:
                onCheckChanged(1);
                break;
            case R.id.rb_fragment_contacts:
                onCheckChanged(2);
                break;
            case R.id.rb_fragment_mine:
                onCheckChanged(3);
                break;
        }
    }

    private void onCheckChanged(int index){
        if(mListener!=null){
            mListener.onRadioBtnClick(index);
        }
    }

    /**
     * @method name:setUnReadPoint
     * @des:为radioBtn设置红点
     * @param :[index:索引,第几个radioBtn,从0开始]
     * @return type:void
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    public void setUnReadPoint(int index){
        if(index>=0&&index<mRadioBtns.length){
            mRadioBtns[index].getBadgeViewHelper().setBadgeHorizontalMarginDp(10);//红点的marginRight
            mRadioBtns[index].getBadgeViewHelper().setBadgePaddingDp(4);//padding决定了红点的大小
            mRadioBtns[index].getBadgeViewHelper().setBadgeBgColorInt(Color.RED);//给颜色
            mRadioBtns[index].showCirclePointBadge();
        }
    }

    /**
     * @method name:clearPoint
     * @des:清除某个radioBtn的红点
     * @param :[index:索引]
     * @return type:void
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    public void clearPoint(int index){
        if(index>=0&&index<mRadioBtns.length){
            mRadioBtns[index].hiddenBadge();
        }
    }

    /**
     * @method name:setUnReadPointQuantitiy
     * @des:给radioBtn设置文本红字提示
     * @param :[index:索引, hint:提示]
     * @return type:void
     * @date 创建时间:2016/8/25
     * @author Chuck
     **/
    public void setUnReadPointQuantitiy(int index,String hint){
        if(index>=0&&index<mRadioBtns.length&& !TextUtils.isEmpty(hint)){
             mRadioBtns[index].getBadgeViewHelper().setBadgeTextSizeSp(10);//设置字号
             mRadioBtns[index].showTextBadge(hint);//设置文本
        }
    }


    public interface  BottomBarOnClickListener{
       void onRadioBtnClick(int index);//参数是索引
       void onCreatBtnClick();//中间那个按钮
   }
}
