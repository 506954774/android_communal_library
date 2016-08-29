package com.qdong.communal.library.widget.CustomMaskLayerView;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qdong.communal.library.R;


/**
 * CustomMaskLayerView
 * loading框,包含三个布局:loading布局,无内容布局,错误展示布局.分别通过:
 * showLoading(),showNoContent(),showError()切换展示
 * 错误布局里含有一个重新发请求的按钮,调用者提供接口mReloadCallback来处理点击事件
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/20  20:19
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class CustomMaskLayerView extends RelativeLayout {

    private ReloadCallback mReloadCallback;//点击了"重新获取"的回调接口

    protected Context mContext;
    //是否显示
    protected boolean isShowing = false;
    //默认时长
    public static final long DEFAULT_DURATION = 400;
    //弹出动画时长
    protected long duration = DEFAULT_DURATION;

    //透明模式
    protected int transparent = STYLE_TRANSPARENT_ON;
    //透明,灰色
    public static final int STYLE_TRANSPARENT_ON = 1;
    //不透明,白色
    public static final int STYLE_TRANSPARENT_OFF = 2;
    //提示标题
    protected TextView mTitle;

    private LinearLayout mLinearLayoutLoading;//loading 布局

    private LinearLayout mLinearLayoutNoContent;//无内容的布局
    private ImageView mImageViewNoContent;//无内容icon
    private TextView mTextViewNoContent;//无内容提示文本

    private LinearLayout mLinearLayoutError;//错误提示布局
    private ImageView mImageViewError;//错误icon
    private TextView mTextViewError;//错误提示文本
    private TextView mTextViewRetry;//再试一次提示


    public CustomMaskLayerView(Context context) {
        this(context,null);
    }

    public CustomMaskLayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMaskLayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化界面布局
     */
    protected void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.custom_mask_layout,
                this, true);
        mTitle = ((TextView)findViewById(R.id.loadingTv));

        mLinearLayoutLoading= (LinearLayout) findViewById(R.id.ll_loading);

        mLinearLayoutNoContent= (LinearLayout) findViewById(R.id.ll_nocontent);
        mImageViewNoContent= (ImageView) findViewById(R.id.img_smile);
        mTextViewNoContent= (TextView) findViewById(R.id.tv_prompt);

        mLinearLayoutError= (LinearLayout) findViewById(R.id.ll_error);
        mImageViewError= (ImageView) findViewById(R.id.iv_error);
        mTextViewError= (TextView) findViewById(R.id.tv_error_title);
        mTextViewRetry= (TextView) findViewById(R.id.tv_reload);
        mTextViewRetry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mTextViewRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReloadCallback != null) {
                    mReloadCallback.reload();
                }
            }
        });
    }

    /**
     * 显示加载框
     */
    private void show() {
        if (!isShowing) {
            //设置透明模式

            if(transparent == STYLE_TRANSPARENT_OFF){
                setBackgroundColor(getResources().getColor(R.color.custom_loading_bg_on));
                mTitle.setTextColor(getResources().getColor(R.color.custom_loading_list_explain_color));
            }else{
                setBackgroundColor(getResources().getColor(R.color.custom_loading_bg_off));
                mTitle.setTextColor(getResources().getColor(R.color.custom_loading_font_color));
                AlphaAnimation alp = new AlphaAnimation(0.5f, 1f);
                alp.setDuration(duration);
                this.startAnimation(alp);
            }
            isShowing = true;
            this.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 显示加载框
     */
    private void show(String msg) {
        if(!msg.trim().isEmpty()){
            mTitle.setText(msg);
        }else{
            mTitle.setText(R.string.loading_title);
        }
        show();
    }

    /**
     * 设置loading界面的提示标题
     * @param text
     */
    public void setLoadingTitle(String text){
        if (null != mTitle) {
            mTitle.setText(text);
        }
    }



    /**
     * 获取动画显示时间
     *
     * @return
     */
    public long getDuration() {
        return duration;
    }

    /**
     * 设置遮罩层显示的动画时间
     *
     * @param duration
     */
    public void setDuration(long duration) {
        if (duration > 100) {
            this.duration = duration;
        } else {
            this.duration = DEFAULT_DURATION;
        }
    }

    /**
     * 加载框是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }


    /**
     * 延迟隐藏对话框
     * @param delayMillis
     */
    public void dismissDelay(long delayMillis){
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delayMillis);
    }

    /**
     * 隐藏加载框
     */
    public void dismiss() {
        isShowing = false;
//		AlphaAnimation alp = new AlphaAnimation(1f, 0f);
//		alp.setDuration(duration);
//		this.startAnimation(alp);
        this.setVisibility(View.GONE);
    }

    /**
     *  设置透明模式
     * @param val
     */
    public void setTransparentMode(int val){
        if (val>2 || val <1) {
            transparent = STYLE_TRANSPARENT_OFF;
        }else{
            transparent = val;
        }
    }

    /**
     * 获取当前透模式
     * @return
     */
    public int getTransparentMode(){
        return transparent;
    }



    /**
     * @method name:showLoading
     * @des:  展示loading 画面
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showLoading(){
        mLinearLayoutLoading.setVisibility(VISIBLE);
        mLinearLayoutNoContent.setVisibility(GONE);
        mLinearLayoutError.setVisibility(GONE);
        show();
    }

    /**
     * @method name:showLoading
     * @des:展示loading 画面
     * @param :[loadingMsg] 提示信息
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showLoading(String loadingMsg){
        mLinearLayoutLoading.setVisibility(VISIBLE);
        mLinearLayoutNoContent.setVisibility(GONE);
        mLinearLayoutError.setVisibility(GONE);
        show(loadingMsg);
    }

    /**
     * @method name:showNoContent
     * @des:  展示没有内容的布局
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showNoContent(){
        isShowing = true;
        this.setVisibility(View.VISIBLE);
        mLinearLayoutLoading.setVisibility(GONE);
        mLinearLayoutNoContent.setVisibility(VISIBLE);
        mLinearLayoutError.setVisibility(GONE);
    }

    /**
     * @method name:showNoContent
     * @des:展示没有内容的布局
     * @param :[noContentMsg]:提示文本
     * @param :[resouseId]:提示icon
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showNoContent(String noContentMsg, int resouseId){

        showNoContent();
        if(!TextUtils.isEmpty(noContentMsg)){
            mTextViewNoContent.setText(noContentMsg);
        }
        try {
            if(resouseId>0){
                mImageViewNoContent.setImageResource(resouseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @method name:showError
     * @des:展示错误布局
     * @param :[]
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showError(){
        isShowing = true;
        this.setVisibility(View.VISIBLE);
        mLinearLayoutLoading.setVisibility(GONE);
        mLinearLayoutNoContent.setVisibility(GONE);
        mLinearLayoutError.setVisibility(VISIBLE);
    }

    /**
     * @method name:showError
     * @des:展示错误布局
     * @param :[errorMsg]:错误提示文本
     * @param :[retryMsg]:重新加载按钮文本
     * @param :[resouseId]:提示icon资源id
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author Chuck
     **/
    public void showError(String errorMsg, String retryMsg, int resouseId){
        showError();
        if(!TextUtils.isEmpty(errorMsg)){
            mTextViewError.setText(errorMsg);
        }
        if(!TextUtils.isEmpty(retryMsg)){
            mTextViewRetry.setText(retryMsg);
        }
        try {
            mImageViewError.setImageResource(resouseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @method name:showErrorDelay
     * @des:延迟指定时间后展示错误布局
     * @param :[delayMillis]:指定的延迟时间
     * @return type:void
     * @date 创建时间:2016/3/17
     * @author hushicheng
     **/
    public void showErrorDelay(long delayMillis){
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                showError();
            }
        }, delayMillis);
    }

    /**
     * @method name:showErrorDelay
     * @des:延迟指定时间后展示错误布局
     * @param :[errorMsg]:错误提示文本
     * @param :[retryMsg]:重新加载按钮文本
     * @param :[resouseId]:提示icon资源id
     * @param :[delayMillis]:指定的延迟时间
     * @return type:void
     * @date 创建时间:2016/1/26
     * @author hushicheng
     **/
    public void showErrorDelay(String errorMsg, String retryMsg, int resouseId, long delayMillis){
        showErrorDelay(delayMillis);
        if(!TextUtils.isEmpty(errorMsg)){
            mTextViewError.setText(errorMsg);
        }
        if(!TextUtils.isEmpty(retryMsg)){
            mTextViewRetry.setText(retryMsg);
        }
        try {
            mImageViewError.setImageResource(resouseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReloadCallback getmReloadCallback() {
        return mReloadCallback;
    }

    public void setmReloadCallback(ReloadCallback mReloadCallback) {
        this.mReloadCallback = mReloadCallback;
    }
}
