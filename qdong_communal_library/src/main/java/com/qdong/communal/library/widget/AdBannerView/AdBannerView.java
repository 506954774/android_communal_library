package com.qdong.communal.library.widget.AdBannerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdong.communal.library.R;
import com.qdong.communal.library.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class AdBannerView extends FrameLayout{

	

	private final static int TIME_INTERVAL = 6;

	private static boolean isAutoPlay = true;


	private List<ProductBean> adList;

	private List<ImageView> imageViewsList;

	private List<View> dotViewsList;

	private ViewPager viewPager;

	private int currentItem = 0;

	private ScheduledExecutorService scheduledExecutorService;

	private Context context;

	private TextView mAdDescTv;
	
	private TextView mAdDescPriceTv;
	
	private TextView mAdDescDurationTv;

    private boolean isInitFlag = false;
	private boolean isTouch = false;
	
	
	public boolean isInit(){
		return isInitFlag;
	}
	
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if(adList.size()>0){
				viewPager.setCurrentItem(currentItem, true);
			}
		}

	};

	public AdBannerView(Context context)
	{
		this(context, null);
		this.context = context;
	}

	public AdBannerView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		this.context = context;
	}

	public AdBannerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;

	}

	private Handler mHandler;
	private int clickFlag = 0;

	public int getClickFlag()
	{
		return clickFlag;
	}

	public void setClickFlag(int clickFlag)
	{
		this.clickFlag = clickFlag;
	}

	
	public void init(Handler mHandler, List<ProductBean> adList)
	{
		this.mHandler = mHandler;
		this.adList = adList;
		initData();
		if (isAutoPlay && null == scheduledExecutorService)
		{
			startPlay();
		}
	}


	private void startPlay()
	{
		if(isAutoPlay){
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 10,TIME_INTERVAL, TimeUnit.SECONDS);
		}
	}


	private void stopPlay()
	{
		if(null != scheduledExecutorService){
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}
	

	private void restartPlay() {
		if (isAutoPlay) {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					synchronized (viewPager) {
						if (null != imageViewsList && imageViewsList.size() > 1) {
							currentItem = (currentItem + 1)
									% imageViewsList.size();


							if (viewPager.getCurrentItem() == viewPager
									.getAdapter().getCount() - 1 && !isAutoPlay) {
								currentItem = 0;
							}

							else if (viewPager.getCurrentItem() == 0
									&& !isAutoPlay) {
								currentItem = viewPager.getAdapter().getCount() - 1;
							}

							handler.obtainMessage().sendToTarget();
						}
					}
				}
			};
			
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), TIME_INTERVAL, TIME_INTERVAL, TimeUnit.SECONDS);
		}
	}


	private void initData()
	{
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		initUI(context);
	}


	private void initUI(Context context){
		if (adList == null || adList.size() == 0) return;
		
		this.removeAllViews();
		
		isInitFlag = true;
		View rootView = LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

		mAdDescTv = (TextView) rootView.findViewById(R.id.tv_bottomNavAdvDesc);
		mAdDescPriceTv = (TextView) rootView.findViewById(R.id.tv_bottomNavDescPrice);
		mAdDescDurationTv = (TextView) rootView.findViewById(R.id.tv_bottomNavDescDuration);
		LinearLayout dotLayout = (LinearLayout) rootView.findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();


		for (int i = 0; i < adList.size(); i++){
			ImageView view = new ImageView(context);
			//view.setTag(adList.get(i));
			if (i == 0)
			view.setBackgroundResource(R.mipmap.ease_default_image);
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView = new ImageView(context);
			int dotWidth = getResources().getDimensionPixelSize(R.dimen.commen_With_Height7_5);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotWidth,dotWidth);
			params.leftMargin = 10;
			params.rightMargin = 10;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);
        AdvPagerAdapter mAdapter = new AdvPagerAdapter();
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(new AdvPageChangeListener());
		viewPager.setOffscreenPageLimit(2);
		
		viewPager.setOnTouchListener(new TouchListener());
		

		ViewPagerScroller mViewPagerScroller = new ViewPagerScroller(context);
		mViewPagerScroller.initViewPagerScroll(viewPager);
		
		
		//mAdDescTv.setText(adList.get(0).getGoods_name());
		//mAdDescPriceTv.setText(String.format(getResources().getString(R.string.item_subtext_price), adList.get(0).getGoods_price()));
		//mAdDescDurationTv.setText(String.format(getResources().getString(R.string.item_advtext_time), adList.get(0).getDuration()));
		
		for (int i = 0; i < dotViewsList.size(); i++){
			if (i == 0){
				((View) dotViewsList.get(0)).setBackgroundResource(R.mipmap.icon_circle_focus_on);
			}
			else{
				((View) dotViewsList.get(i)).setBackgroundResource(R.mipmap.icon_circle_focus_off);
			}
		}
		stopPlay();
	}


	private class AdvPagerAdapter extends PagerAdapter{

		@Override
		public void destroyItem(View container, int position, Object object){
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position){

			//Log.e("AdBannerView", "instantiateItem:"+context.toString());

			final int p = position;
			ImageView imageView = imageViewsList.get(position);

//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showImageForEmptyUri(R.mipmap.icon_product_adv_default)
//			.showImageOnFail(R.mipmap.icon_product_adv_default)
//			.showImageOnLoading(R.mipmap.icon_product_adv_default)
//			.build();
//			ImageLoader loader = ImageLoader.getInstance();
//			loader.displayImage(adList.get(position).getRecommend_image(), imageView, options, new AnimateFirstDisplayListener()); 



			BitmapUtil.loadPhoto(context, adList.get(position).getImage_url(), imageView);


			/*BitmapUtil.loadImageBitmap(
					context,
					adList.get(position).getImage_url(),
					imageView,
					R.mipmap.ease_default_image,
					ListImageCache.getInstance(context));//存内存,存sd
					//AlbumImageCache.getInstance(context));//不存sd
					//PersistantImageCache.getInstance(context));//写进dada/dada下,用户无法删除,除非卸载*/


			//BitmapUtil.loadPhoto(context, adList.get(position).getImage_url(), imageView);
			

			((ViewPager) container).addView(imageViewsList.get(position));


			imageView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Message msg = new Message();
					msg.what = clickFlag;
					msg.obj = p;
					mHandler.sendMessage(msg);
				}
			});

			return imageViewsList.get(position);
		}

		@Override
		public int getCount(){
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1){
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1){

		}

		@Override
		public Parcelable saveState(){
			return null;
		}

		@Override
		public void startUpdate(View view){

		}

		@Override
		public void finishUpdate(View view){

		}

	}
	


	private class AdvPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			switch (arg0)
			{
				case 1:
					isAutoPlay = false;
					break;
				case 2:
					isAutoPlay = true;
					break;
				case 0:
					isAutoPlay = true;
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageSelected(int pos){
			//mAdDescTv.setText(adList.get(pos).getGoods_name());
			//mAdDescPriceTv.setText(String.format(getResources().getString(R.string.item_subtext_price), adList.get(pos).getGoods_price()));
			//mAdDescDurationTv.setText(String.format(getResources().getString(R.string.item_advtext_time), adList.get(pos).getDuration()));
			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++){
				if (i == pos){
					((View) dotViewsList.get(pos)).setBackgroundResource(R.mipmap.icon_circle_focus_on);
				}
				else{
					((View) dotViewsList.get(i)).setBackgroundResource(R.mipmap.icon_circle_focus_off);
				}
			}
		}

	}
	
	
	private class TouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stopPlay();
				break;
			case MotionEvent.ACTION_MOVE:
				stopPlay();
				break;
			case MotionEvent.ACTION_UP:
				isAutoPlay=true;
				restartPlay();
				break;
			default:
				break;
			}
			
			return false;
		}
	}
	

	private class SlideShowTask implements Runnable
	{

		@Override
		public void run()
		{
			synchronized (viewPager)
			{
				if (null != imageViewsList && imageViewsList.size() > 1)
				{
					currentItem = (currentItem + 1) % imageViewsList.size();

					if (viewPager.getCurrentItem() == viewPager
							.getAdapter().getCount() - 1 && !isAutoPlay) {
						currentItem = 0;
					}

					else if (viewPager.getCurrentItem() == 0
							&& !isAutoPlay) {
						currentItem = viewPager.getAdapter().getCount() - 1;
					}
					
					handler.obtainMessage().sendToTarget();
				}
			}
		}

	}


	public void recycle(){

		if(null != scheduledExecutorService){
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
		
		if(null != imageViewsList){
			for (int i = 0; i < imageViewsList.size(); i++){
				ImageView imageView = imageViewsList.get(i);
				Drawable drawable = imageView.getDrawable();
				if (drawable != null){

					drawable.setCallback(null);
				}
			}
		}
	}
}
