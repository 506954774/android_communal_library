package com.qdong.hcp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qdong.communal.library.module.network.QDongApi;
import com.qdong.communal.library.module.network.QDongNetInfo;
import com.qdong.communal.library.module.network.RetrofitAPIManager;
import com.qdong.communal.library.module.network.RxHelper;
import com.qdong.communal.library.util.ToastUtil;
import com.qdong.communal.library.widget.CustomMaskLayerView.CustomMaskLayerView;
import com.qdong.communal.library.widget.TabViews.OnSelectedIndexChangedListener;
import com.qdong.communal.library.widget.TabViews.PagerTab;
import com.qdong.communal.library.widget.TabViews.TabWithoutViewPager;
import com.qdong.greendao.User;
import com.qdong.hcp.R;
import com.qdong.hcp.cache.UserCacheDao;
import com.qdong.hcp.databinding.ActivityLoginBinding;
import com.qdong.hcp.entity.UserBean;
import com.qdong.hcp.green_dao.DBHelper;
import com.qdong.hcp.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * LoginActivity
 * 登录的业务逻辑:
 *
 * 链式逻辑:
 * 1,主线程开启loading动画;
 * 2,子线程同步调用登录接口,如果登录成功了,把sessionId存起来;
 * 3,子线程里把返回的user对象保存起来
 * 4,主线程里反馈登录结果
 *
 *
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/11  20:43
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements OnSelectedIndexChangedListener {

    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.et_user_name) EditText etUserName;
    @Bind(R.id.et_password) EditText etPassword;

    @Bind(R.id.tabs)
    PagerTab mTab;
    @Bind(R.id.tabs_without_viewpager_)
    TabWithoutViewPager mTabWithoutViewPager;



    /***
     * 观察者(回调)
     */
    final Observer<QDongNetInfo> loginObserver = new Observer<QDongNetInfo>() {
        @Override
        public void onCompleted() {
            LogUtil.e("RxJava", "onCompleted(),线程id:" + Thread.currentThread().getId());
            //ToastUtil.showCustomMessage(LoginActivity.this,"onCompleted(),线程id:" + Thread.currentThread().getId());
            mLoadingView.dismiss();
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e("RxJava", "onError(),线程id:" + Thread.currentThread().getId() + ",异常:" + e.toString());
            mLoadingView.dismiss();
            tv.setText("onError(),线程id:" + Thread.currentThread().getId() + ",异常:" + e.toString());
        }

        @Override
        public void onNext(QDongNetInfo Info) {
            LogUtil.e("RxJava", "onNext(),线程id:" + Thread.currentThread().getId() + ",结果:" + Info.toString());

            if(Info!=null&&Info.isSuccess()==true) {//因为要根据不同的错误码来做不同处理,故此处还是以info作为数据传递对象

                ToastUtil.showCustomMessage(LoginActivity.this, "登录成功!");
                mLoadingView.dismiss();
                tv.setText("onNext(),线程id:" + Thread.currentThread().getId() + ",结果:" + Info.toString());

                Observable
                        .timer(4, TimeUnit.SECONDS, AndroidSchedulers.mainThread())//延时2秒跳转,这个操作符产生一个Observer<Long>
                        .map(new Func1<Long, Object>() {
                            @Override
                            public Object call(Long aLong) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_right_out);
                                finish();
                                return null;
                            }
                        })
                        .subscribe();

            }
            else {
                // TODO: 2016/7/11 异常处理
            }

        }
    };
    private Subscription subscription;


    @OnClick(R.id.button) void login(){
        final HashMap<String,String> map =new HashMap<>();
        map.put("password", "FC9DDCCA42C8FC33");
        map.put("account", "15262592514");
        map.put("deviceMac", "c4072f244658");
        map.put("gpsLong", "0");
        map.put("deviceName", "HUAWEI");
        map.put("deviceSystem", "3");
        map.put("gpsLat", "0");

        final QDongApi service = RetrofitAPIManager.provideClientApi(LoginActivity.this);//定义出来,因为在使用自动登录获取最新token时要用到


        subscription
                =Observable.timer(1, TimeUnit.MILLISECONDS, Schedulers.io())//用这个纯粹是为了获取一个被观察者,它返回的是一个Observable<Long>,后面通过floatMap转为Observable<QDongNetInfo>

                .flatMap(RxHelper.getInstance(LoginActivity.this).login(service,map))//登录的同时存储sessinId

                .subscribeOn(Schedulers.io())//指定被观察者的执行线程

                .doOnSubscribe(new Action0() {//它默认在subscribe的线程执行,但是如果后面有设置.subscribeOn线程的话,他会在离得最近的那个设置的进程里执行
                    @Override
                    public void call() {
                        mLoadingView.showLoading("登录中...");
                    }
                })

                .subscribeOn(AndroidSchedulers.mainThread()) // 指定doOnSubscribe执行的线程

                .observeOn(Schedulers.io())//指定观察者的执行线程
                .flatMap(new Func1<QDongNetInfo, Observable<QDongNetInfo>>() {
                    @Override
                    public Observable<QDongNetInfo> call(QDongNetInfo qDongNetInfo) {//子线程里缓存登录的user信息

                        LogUtil.e("RxJava", "flatMap 缓存登录user的信息,线程id:" + Thread.currentThread().getId() + ",结果:" + qDongNetInfo.toString());
                        if(qDongNetInfo!=null&&qDongNetInfo.getResult()!=null&&qDongNetInfo.isSuccess()==true){//登录成功
                            Gson gson = new Gson();
                            UserBean bean=gson.fromJson(qDongNetInfo.getResult(),UserBean.class);
                            if(bean!=null){
                                UserCacheDao.getInstance(LoginActivity.this).saveLoginUserInfo(LoginActivity.this,bean);
                                return Observable.just(qDongNetInfo);
                            }
                        }
                        return Observable.just(qDongNetInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定观察者的执行线程
                .subscribe(loginObserver);//订阅*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsTitleBar(true);//启用公共的Title
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLoadingView.setTransparentMode(CustomMaskLayerView.STYLE_TRANSPARENT_ON);//设置为半透明
        mLoadingView.showLoading("test...");

        Observable
                .timer(4, TimeUnit.SECONDS, AndroidSchedulers.mainThread())//延时4秒
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        mLoadingView.dismiss();

                        /***数据库插入动作**/
                        User user=new User();
                        user.setUser_id(10086);
                        user.setAccount("15262592514");
                        user.setPassword("lp19900908");
                        DBHelper.getInstance(LoginActivity.this).saveUser(user);

                        return null;
                    }
                })
                .subscribe();


        mTabWithoutViewPager.setOnSelectedChangeListener(this);
        mTabWithoutViewPager.setUpView("no1","no2","no3","no4");


        final String[] titles={"tab1","tab2","tab3"};

         final List<View> list=new ArrayList<>();
        TextView textView=new TextView(this);
        textView.setText("tab1");
        TextView textView1=new TextView(this);
        textView1.setText("tab2");
        TextView textView2=new TextView(this);
        textView2.setText("tab3");
        list.add(textView);
        list.add(textView1);
        list.add(textView2);

        ViewPager viewPager= (ViewPager) findViewById(R.id.vp);
        PagerAdapter adpter=new PagerAdapter() {




            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }



            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
        viewPager.setAdapter(adpter);
        viewPager.setCurrentItem(0);
        mTab.setSmoothScroll(false);
        mTab.setViewPager(viewPager);

    }

    @Override
    public void onSelectedIndexChanged(int index) {
        ToastUtil.showCustomMessage(LoginActivity.this,index+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxHelper.getInstance(LoginActivity.this).unsubscribe(subscription);

    }
}
