package com.qdong.hcp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qdong.communal.library.util.SharedPreferencesUtil;
import com.qdong.communal.library.util.ToastUtil;
import com.qdong.hcp.R;
import com.qdong.hcp.customWidget.CustomBottomBar;
import com.qdong.hcp.databinding.ActivityMainBinding;
import com.qdong.hcp.enums.MainMoudleType;
import com.qdong.hcp.interfaces.OnSoftKeyBoardVisibleListener;

import java.util.HashMap;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements CustomBottomBar.BottomBarOnClickListener {


    /**
     * 记录所有的fragment，防止重复创建
     */
    private HashMap<MainMoudleType, Fragment> mFragmentMap = new HashMap<MainMoudleType, Fragment>();

    private MainMoudleType[] mMoudles = {
            MainMoudleType.FRAGMENT_HOME,
            MainMoudleType.FRAGMENT_JOB_MANAGE,
            MainMoudleType.FRAGMENT_CONTACTS,
            MainMoudleType.FRAGMENT_MINE

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(false);
        setContentView(R.layout.activity_main);
        setUpView();
        setListener();
        replaceFragment(createFragment(mMoudles[0]));
    }


    /**
     * @param : [fragment: 需要替换的fragment]
     * @return type: void
     *
     * @method name: replaceFragment
     * @des: 替换Fragment方法的封装
     */
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_main_fragment, fragment).commitAllowingStateLoss();
    }

    /**
     * @param :[flag]
     * @return type:android.support.v4.app.Fragment
     *
     * @method name:createFragment
     * @des:创建碎片
     * @date 创建时间:2016/7/8
     * @author Chuck
     **/
    public Fragment createFragment(MainMoudleType flag) {

        Fragment fragment = mFragmentMap.get(flag);

        if (fragment == null) {

            if (flag != null) {
                try {
                    fragment = (Fragment) flag.getmClass().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            mFragmentMap.put(flag, fragment);
        }
        return fragment;
    }

    @Override
    public void onRadioBtnClick(int index) {
        //ToastUtil.showCustomMessage(MainActivity.this, "按钮索引:" + index);
        replaceFragment(createFragment(mMoudles[index]));

        /**测试隐藏某个红点**/
        if(index==1){
            mViewBind.customBottomBar.clearPoint(1);
        }
        if(index==2){
            mViewBind.customBottomBar.clearPoint(2);
        }

    }

    @Override
    public void onCreatBtnClick() {
        startActivity(new Intent(MainActivity.this, CreateNewProjectActivity.class));
        //ToastUtil.showCustomMessage(MainActivity.this, "点击创建按钮");
    }

    private void setUpView() {
        /***测试底部红点展示 **/
        mViewBind.customBottomBar.setUnReadPoint(1);
        mViewBind.customBottomBar.setUnReadPointQuantitiy(2,"new");
        //BitmapUtil.loadHead(this,"https://www.bugclose.com/oss/16/40/95/176af9545b6c.jpg",new GlideRoundTransform(this,40),mIvUserHead);
    }

    /**
     * @param :[]
     * @return type:void
     *
     * @method name:setListener
     * @des:加监听器
     * @date 创建时间:2016/7/8
     * @author Chuck
     **/
    private void setListener() {
        mViewBind.customBottomBar.setmListener(this);
    }


    @Override
    protected OnSoftKeyBoardVisibleListener getOnSoftKeyBoardVisibleListener() {
        return new OnSoftKeyBoardVisibleListener() {
            @Override
            public void onSoftKeyBoardVisible(boolean visible) {
                ToastUtil.showCustomMessage(MainActivity.this, visible + "");
            }
        };
    }


}
