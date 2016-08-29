package com.qdong.hcp.activity;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qdong.communal.library.widget.CustomMaskLayerView.CustomMaskLayerView;
import com.qdong.hcp.R;
import com.qdong.hcp.interfaces.OnSoftKeyBoardVisibleListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * BaseActivity
 * activity父类,统一处理以下:
 * <p/>
 * 1,保存软键盘的当前的状态,并给出一个方法给子类,让其开启/关闭软键盘
 * 2,沉浸式状态栏的颜色设置
 * 3,baseActivity提供了一个公用的title,包含一个标题文本和一个左上角的返回按钮,可以设置为不可见(在setContentView之前调用setIsTitleBar(false)即可 )
 * 4,onTrimMemory里释放Glide资源
 * 5,提供一个loadingView,{@link #mLoadingView}盖在布局最外层,在titile下面.默认为隐藏
 * 6,子类可以重写getOnSoftKeyBoardVisibleListener,提供接口来处理软键盘弹出,收起事件
 * 7,通过ViewDataBinding绑定布局 见:{@link BaseActivity#setContentView(int)},{@link BaseActivity#setContentView(View)}
 * 8,提供自动登录的HashMap 见:{@link BaseActivity#getAutoLoginParameterMap()}
 * 9,友盟,页面停留时间统计 {@link #onResume()},{@link #onPause()}
 * <p/>
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/7  11:53
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity {


    //控制是否需要 沉浸式状态栏
    private boolean isTranslucentStatus = true;

    //沉浸式状态栏底色布局,api>=19时才会初始化
    private View stateBar;

    //最外层的线性布局容器
    private LinearLayout ll_container;

    //是否使用公共的标题栏布局
    private boolean isTitleBar = true;

    //title栏的布局容器
    private View titleBarView;

    //返回按钮
    private ImageView iv_my_left;

    //标题
    private TextView tv_my_title;

    protected T mViewBind;

    protected CustomMaskLayerView mLoadingView;//loadingView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll_container = new LinearLayout(this);
        ll_container.setOrientation(LinearLayout.VERTICAL);
        addOnSoftKeyBoardVisibleListener(this, getOnSoftKeyBoardVisibleListener());
    }

    @Override
    public void setTitle(CharSequence title) {
        setTitleText(title.toString());
    }

    /**
     * 设置沉浸式状态栏的背景
     */
    public void setStateBarBackgroundColor(int color) {
        if (stateBar != null) {
            stateBar.setBackgroundColor(color);
        }
    }

    /**
     * 设置标题栏的背景颜色
     */
    public void setTitleBarBackgroundColor(int color) {
        if (titleBarView != null) {
            titleBarView.setBackgroundColor(color);
        }
    }

    /**
     * 设置标题文字
     */
    public void setTitleText(String title) {
        if (tv_my_title != null) {
            tv_my_title.setText(title);
        }
    }

    /**
     * 设置沉浸式状态栏布局是否显示
     */
    public void setStateBarVisibility(int visibility) {
        if (stateBar != null) {
            stateBar.setVisibility(visibility);
        }
    }

    /**
     * 设置沉浸式状态栏的高度
     */
    public void setStateBarHeight(int height) {
        if (stateBar != null) {
            ViewGroup.LayoutParams layoutParams = stateBar.getLayoutParams();
            layoutParams.height = height;
            stateBar.setLayoutParams(layoutParams);
        }
    }


    /**
     * 获取沉浸式状态栏的启用状态
     */
    public boolean isTranslucentStatus() {
        return isTranslucentStatus;
    }

    /**
     * 获取title栏的启用状态
     */
    public boolean isTitleBar() {
        return isTitleBar;
    }

    /**
     * 设置是否使用公用的title栏,默认为true
     */
    public void setIsTitleBar(boolean isTitleBar) {
        this.isTitleBar = isTitleBar;
    }

    /**
     * 设置是否启用沉浸式状态栏
     */
    public void setIsTranslucentStatus(boolean isTranslucentStatus) {
        this.isTranslucentStatus = isTranslucentStatus;
    }

    /**
     * 获取title栏中间的textview
     */
    public TextView getTv_title() {
        return tv_my_title;
    }


    /**
     * 获取title栏右边的textview
     */
    public ImageView getLeftImageView() {
        return iv_my_left;
    }


    @Override
    public void setContentView(int layoutResID) {
        initSaataeBar();
        initTitleBar();
        View view = LayoutInflater.from(this).inflate(layoutResID, null);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_base_root, null);
        LinearLayout activityParent= (LinearLayout) root.findViewById(R.id.ll_activity_root);
        mLoadingView= (CustomMaskLayerView) root.findViewById(R.id.loading_view);
        activityParent.addView(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //ll_container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ll_container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewBind = DataBindingUtil.bind(view);
        super.setContentView(ll_container);
    }

    @Override
    public void setContentView(View view) {
        initSaataeBar();
        initTitleBar();

        View root = LayoutInflater.from(this).inflate(R.layout.activity_base_root, null);
        LinearLayout activityParent= (LinearLayout) root.findViewById(R.id.ll_activity_root);
        mLoadingView= (CustomMaskLayerView) root.findViewById(R.id.loading_view);
        activityParent.addView(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //ll_container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll_container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewBind = DataBindingUtil.bind(view);
        super.setContentView(ll_container);
    }

    private void initTitleBar() {
        if (isTitleBar) {
            titleBarView = View.inflate(this, R.layout.common_title_bar, null);
            iv_my_left = (ImageView) titleBarView.findViewById(R.id.iv_my_back);
            tv_my_title = (TextView) titleBarView.findViewById(R.id.tv_my_title);
            iv_my_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelOffset(R.dimen.title_bar_height));
            ll_container.addView(titleBarView, params);
        }
    }

    private void initSaataeBar() {
        if (isTranslucentStatus && Build.VERSION.SDK_INT >= 19) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //初始化stateBar
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppLoader.getStateBarHeight());
            stateBar = new View(this);
            stateBar.setBackgroundColor(getStatusBarColor());
            ll_container.addView(stateBar, params);
        }
    }

    /**
     * @param :[]
     * @return type:int
     *
     * @method name:getStatusBarColor
     * @des:子类可以重写这个,修改状态栏的颜色
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    protected int getStatusBarColor() {
        return Color.parseColor("#ff378DDA");
    }


    /////////////////////////////////////////


    private boolean sLastVisiable;//标示此时键盘是否已经弹出  true为已经弹出

    /**
     * @param :[activity, listener]
     * @return type:void
     *
     * @method name:addOnSoftKeyBoardVisibleListener
     * @des:添加软键盘是否弹出的监听器
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    private void addOnSoftKeyBoardVisibleListener(Activity activity, final OnSoftKeyBoardVisibleListener listener) {

        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHight = rect.bottom - rect.top;
                int hight = decorView.getHeight();
                boolean visible = (double) displayHight / hight < 0.8;

                if (visible != sLastVisiable && listener != null) {
                    listener.onSoftKeyBoardVisible(visible);
                }
                sLastVisiable = visible;
            }
        });
    }

    /**
     * @param :[]
     * @return type:com.qdong.onemile.interfaces.OnSoftKeyBoardVisibleListener
     *
     * @method name:getOnSoftKeyBoardVisibleListener
     * @des:子类可以重写这个来获取软键盘的弹出,收起事件
     * @date 创建时间:2016/7/11
     * @author Chuck
     **/
    protected OnSoftKeyBoardVisibleListener getOnSoftKeyBoardVisibleListener() {
        return null;
    }


    /**
     * @param :[]
     * @return type:boolean
     *
     * @method name:isSoftInputShowwing
     * @des:软键盘此时是否弹出了 true为已弹出
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    protected boolean isSoftInputShowwing() {
        return sLastVisiable;
    }

    /**
     * @param :[show, view]
     * @return type:void
     *
     * @method name:showOrHideInput
     * @des:展示或者隐藏软件盘 切换 如果此时软键盘弹了就隐藏,如果此时隐藏了就弹出
     * @date 创建时间:2016/7/7
     * @author Chuck
     **/
    protected void showOrHideInput(EditText view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        // 参数1：MainActivity进场动画，参数2：SecondActivity出场动画
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_left_out);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        try {
            // 必须在UI线程中调用
            if (!AppLoader.getInstance().isAppFront())
                Glide.get(this).clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected HashMap<String, String> getAutoLoginParameterMap() {
        return AppLoader.getInstance().getAutoLoginParameterMap();
    }


    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onResume(this);       //友盟统计时长
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPause(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
