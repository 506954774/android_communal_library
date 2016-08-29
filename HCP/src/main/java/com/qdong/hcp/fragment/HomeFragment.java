package com.qdong.hcp.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.qdong.communal.library.module.BaseRefreshableListFragment.MyBaseRecyclerAdapter;
import com.qdong.communal.library.module.TFSHelper.TFSUploader;
import com.qdong.communal.library.module.network.QDongApi;
import com.qdong.communal.library.module.network.QDongNetInfo;
import com.qdong.communal.library.module.network.RxHelper;
import com.qdong.communal.library.util.BitmapUtil;
import com.qdong.communal.library.util.DensityUtil;
import com.qdong.communal.library.util.ToastUtil;
import com.qdong.communal.library.widget.AdBannerView.AdBannerView;
import com.qdong.communal.library.widget.AdBannerView.ProductBean;
import com.qdong.communal.library.widget.PopSelectView.IPopSelectionListener;
import com.qdong.communal.library.widget.PopSelectView.PopSelectionView;
import com.qdong.communal.library.widget.PopSelectView.ZoneFilterBean;
import com.qdong.hcp.R;
import com.qdong.hcp.activity.AppLoader;
import com.qdong.hcp.activity.LoginActivity;
import com.qdong.hcp.adapter.MainModuleAdapter;
import com.qdong.hcp.adapter.ProjectAdapter;
import com.qdong.hcp.customWidget.MyGridView;
import com.qdong.hcp.entity.CommunityBean;
import com.qdong.hcp.entity.MainModuleBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * HomeFragment
 * 首页
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/8  9:53
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class HomeFragment extends BaseRefreshableFragment<CommunityBean> implements AdapterView.OnItemClickListener, IPopSelectionListener {

    private static  int LIMIT_LOCATION_Y =140;/**触发选择器控件悬浮的滑动阀值,默认是140像素,在{@link #initAdapter() 里赋值}*/
    private ProjectAdapter myAdapter;//适配器
    private LinearLayout mLlHeadviewContainer;//头部的筛选框父容器
    private PopSelectionView mPopViewHeadView;//过滤控件

    @Override
    public String getBaseUrl() {
        return "http://1501q8n685.51mypc.cn:10005/";
    }

    @Override
    public Observable<QDongNetInfo> callApi(QDongApi api, int currentPage, int maxPage) {
        return api.findLatestDynamic(currentPage,maxPage);
    }

    @Override
    public List<CommunityBean> resolveData(JsonElement jsonStr)  {

        ArrayList<CommunityBean> entitys = new ArrayList<CommunityBean>();
        if(jsonStr==null){
            return entitys;
        }
        else {
            Log.e("RxJava", "子类解析json串:"+jsonStr);
            JsonArray array=jsonStr.getAsJsonArray();
            Gson gson = new Gson();
            for (int i = 0; i < array.size(); i++) {
                CommunityBean t1 = (CommunityBean) gson.fromJson(array.get(i), CommunityBean.class);
                entitys.add(t1);
            }
            return entitys;
        }
    }

    @Override
    public MyBaseRecyclerAdapter initAdapter() {

        /**向上滑动的阀值,等于状态栏的高度加上此fragment的title高度,在xml里是写死为45dp的 by:Chuck 2016/08/25 **/
        LIMIT_LOCATION_Y=AppLoader.getStateBarHeight()+ DensityUtil.dp2px(getActivity(),45);

        loadingView.dismiss();
        ArrayList<CommunityBean> mDatas = new ArrayList<>();
        mDatas.add(new CommunityBean());
        myAdapter=new ProjectAdapter(mDatas,this);


        /**recyclerView加滑动监听,每当上滑一定距离时,将筛选控件悬浮**/
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int[] location = new int[2];
                mLlHeadviewContainer.getLocationOnScreen(location);
                //LogUtil.e("recycleView滑","mLlHeadviewContainer 在屏幕位置>location[0]:"+location[0]+",location[1]:"+location[1]);

                if(location[1]<=LIMIT_LOCATION_Y){//根据父容器在屏幕的位置来设置是否悬浮展示
                    mLlFloatContainer.removeAllViews();
                    mLlHeadviewContainer.removeAllViews();
                    mLlFloatContainer.addView(mPopViewHeadView);
                }
                else {
                    mLlFloatContainer.removeAllViews();
                    mLlHeadviewContainer.removeAllViews();
                    mLlHeadviewContainer.addView(mPopViewHeadView);
                }

            }
        });


        return myAdapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getRecyclerViewItemDecoration() {
        return null;
    }

    @Override
    /**
     * @method name:getRecyclerViewHeadView
     * @des:重写,加headview
     * @param :[]
     * @return type:android.view.View
     * @date 创建时间:2016/6/22
     * @author Chuck
     **/
    protected View getRecyclerViewHeadView() {

        /**头部root**/
        View head = LayoutInflater.from(getActivity()).inflate(R.layout.listview_headview_home_ft,null);
        /**选择控件的父容器**/
        mLlHeadviewContainer= (LinearLayout) head.findViewById(R.id.ll_head);
        /**广告栏**/
        AdBannerView ad = (AdBannerView) head.findViewById(R.id.ad_list);

        List<ProductBean> urls=new ArrayList<>();
        String [] s = {
                "http://www.photo0086.com/member/3385/pic/2010092419203020302.JPG",
                "http://images.csdn.net/20160618/jibo.jpg",
                "http://dl2.iteye.com/upload/attachment/0092/4368/aaaae379-f112-3bc2-952e-2dec729038da.png",
                "http://images.csdn.net/20160620/up4.jpg"
        };

        for(int i=0;i<4;i++){
            ProductBean bean =new ProductBean();
            bean.setImage_url(s[i]);
            urls.add(bean);
        }
        ad.init(new Handler(Looper.getMainLooper()),urls);

        /**模块**/
        MyGridView gridView= (MyGridView) head.findViewById(R.id.gv_moudle);

        // public MainModuleBean(int id, int stringResouseId, int imageResouseId) {
        ArrayList<MainModuleBean> data=new ArrayList<>();
        data.add(new MainModuleBean(0,R.string.recruiting_management,R.mipmap.icon_recruiting_management));
        data.add(new MainModuleBean(1,R.string.bid_management,R.mipmap.icon_bid_management));
        data.add(new MainModuleBean(2,R.string.my_collection,R.mipmap.icon_my_collection));
        data.add(new MainModuleBean(3,R.string.find_build_resourse,R.mipmap.icon_find_build_resourse));
        data.add(new MainModuleBean(4,R.string.assist_disability,R.mipmap.icon_assist_disability));
        data.add(new MainModuleBean(5,R.string.industry_infomation,R.mipmap.icon_industry_infomation));
        data.add(new MainModuleBean(6,R.string.education,R.mipmap.icon_education));
        data.add(new MainModuleBean(7,R.string.law_service,R.mipmap.icon_law_service));
        data.add(new MainModuleBean(8,R.string.hometown,R.mipmap.icon_hometown));
        data.add(new MainModuleBean(9,R.string.all,R.mipmap.icon_all));
        MainModuleAdapter adapter =new MainModuleAdapter(data,getActivity());
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(adapter);

        /**筛选控件**/
        mPopViewHeadView= (PopSelectionView) head.findViewById(R.id.pop_id);
        mPopViewHeadView.initView(PopSelectionView.POP_STYLE_LEFT_MID,"智能排序","筛选");
        mPopViewHeadView.setOnPopSelectItemListener(HomeFragment.this);
        mPopViewHeadView.setLeftDatas(getLeftSortData());
        mPopViewHeadView.setMiddDatas(getMiddleSortData());

        return head;
    }

    /**
     * @method name:getMiddleSortData
     * @des:第二列的下拉数据源
     * @param :[]
     * @return type:java.util.List<com.qdong.communal.library.widget.PopSelectView.ZoneFilterBean>
     * @date 创建时间:2016/8/23
     * @author Chuck
     **/
    private List<ZoneFilterBean> getMiddleSortData() {
        String [] titles={"距离","规模","工价","时间","伙食"};
        ArrayList<ZoneFilterBean> zoneData = new ArrayList<ZoneFilterBean>();
        for(int i=0;i<titles.length;i++){
            ZoneFilterBean bean=new ZoneFilterBean();
            bean.setId(i);
            bean.setName(titles[i]);
            zoneData.add(bean);
        }
        return zoneData;
    }

    /**
     * @method name:getLeftSortData
     * @des:左边下拉数据源
     * @param :[]
     * @return type:java.util.List<com.qdong.communal.library.widget.PopSelectView.ZoneFilterBean>
     * @date 创建时间:2016/8/23
     * @author Chuck
     **/
    private List<ZoneFilterBean> getLeftSortData() {
        String [] titles={"公司规模","薪酬","福利","工地交通状况"};
        ArrayList<ZoneFilterBean> zoneData = new ArrayList<ZoneFilterBean>();
        for(int i=0;i<titles.length;i++){
            ZoneFilterBean bean=new ZoneFilterBean();
            bean.setId(i);
            bean.setName(titles[i]);
            zoneData.add(bean);
        }
        return zoneData;
    }

    @Override
    protected View getFragmentTitleView() {//页面加标
        View head = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_title,null);
        return head;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainModuleAdapter adapter= (MainModuleAdapter) parent.getAdapter();
        ToastUtil.showCustomMessage(getActivity(), getString(adapter.getItem(position).getStringResouseId()));

        /***以下为测试代码,测试TSF链式调用
         *
         *  先上传至tfs,拿到返回的url后调用更换头像
         *
         * **/
        TFSUploader uploader= TFSUploader.getInstance(AppLoader.getInstance());
        final QDongApi api=uploader.getmApiService();

        String s1=BitmapUtil.saveImageFromViewToSdcard(getActivity(),view);//保存这个veiw,截图,存起来,返回图片的绝对路径
        ArrayList<String> list=new ArrayList<>();
        list.add(s1);

        /**最终的观察者,当执行完了tsf上传,并把头像的网络url路径更换成功后,才触发onNext的回调 by:chuck 2016/08/26**/
        final Observer<QDongNetInfo> observer= new Observer<QDongNetInfo>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(QDongNetInfo qDongNetInfo) {
                ToastUtil.showCustomMessage(getActivity(),"更换头像成功!");//成功后打印返回的路径
            }
        };

        Subscription subscription=
                uploader.executeUploadMultipleFiles(list)//tfs图片上传
                .flatMap(new Func1<QDongNetInfo, Observable<QDongNetInfo>>() {
                    @Override
                    public Observable<QDongNetInfo> call(QDongNetInfo qDongNetInfo) {
                        return api.updateHeadPhoto(qDongNetInfo.getResult().getAsJsonArray().get(0).getAsString());
                    }
                })//获取返回的路径,调用更换头像的接口
                .subscribeOn(Schedulers.io())//指定被观察者的执行线程
                .observeOn(AndroidSchedulers.mainThread())//切换到主线程,因为如果正常反回了,我要将事件直接交给观察者,观察者通常在主线程
                .flatMap(RxHelper.getInstance(getActivity()).judgeSessionExpired(observer))//判断session是否过期,如果过期了,会抛出异常到事件流里
                .observeOn(Schedulers.io())//切换到子线程,执行retryWhen,里面有网络请求
                .retryWhen(RxHelper.getInstance(getActivity()).judgeRetry(api,uploader.executeUploadMultipleFiles(list) ,AppLoader.getInstance().getAutoLoginParameterMap() ))//触发retry
                .observeOn(AndroidSchedulers.mainThread())//指定观察者的执行线程
                .subscribe(observer);//订阅


        /***封装后的调用,主要提供一个文件路径集合和一个二次操作的flatMap **/
       /*  uploader.uploadMultipleFilesThenUpdateUrl(
                list,
                getAutoLoginParameterMap(),
                new Func1<QDongNetInfo, Observable<QDongNetInfo>>() {
                 @Override
                  public Observable<QDongNetInfo> call(QDongNetInfo qDongNetInfo) {
                   return api.updateHeadPhoto(qDongNetInfo.getResult().getAsJsonArray().get(0).getAsString());
                  }
                },
                observer);*/

    }

    @Override
    public void onSelectItem(Object item, int selecionType) {
        ZoneFilterBean selectBean = (ZoneFilterBean) item;
        ToastUtil.showCustomMessage(getActivity(), selectBean.getName());

        if(selecionType==1){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        else {

            /***以下为测试TFS 封装,下面的是异步调用,成功后打印出返回的网络url路径 by:chuck 2016/08/26**/
            String s1=BitmapUtil.saveImageFromViewToSdcard(getActivity(),mPopViewHeadView);//保存这个veiw,截图,存起来,返回图片的绝对路径
            String s2=BitmapUtil.saveImageFromViewToSdcard(getActivity(),mPopViewHeadView);

            ArrayList<String> list=new ArrayList<>();
            list.add(s1);
            list.add(s2);

            TFSUploader  uploader=TFSUploader.getInstance(AppLoader.getInstance());//获取单例

            Observer<QDongNetInfo> observer= new Observer<QDongNetInfo>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(QDongNetInfo qDongNetInfo) {
                    ToastUtil.showCustomMessage(getActivity(),qDongNetInfo.getResult().toString());//成功后打印返回的路径
                }
            };
            uploader.uploadMultipleFiles(list,AppLoader.getInstance().getAutoLoginParameterMap(),observer);//上传
        }



    }


    @Override
    public void onInitDataResult(boolean isSuccessfuly) {}

    @Override
    public void onRefreshDataResult(boolean isSuccessfuly) {}

    @Override
    public void onLoadMoreDataResult(boolean isSuccessfuly) {}

    @Override
    protected boolean isShowLoadingFirstTime() {
        return true;
    }


    @Override
    public HashMap<String, String> getAutoLoginParameterMap() {
        return AppLoader.getInstance().getAutoLoginParameterMap();
    }
}

