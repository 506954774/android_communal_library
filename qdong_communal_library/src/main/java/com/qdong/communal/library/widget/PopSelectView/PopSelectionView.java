package com.qdong.communal.library.widget.PopSelectView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qdong.communal.library.R;
import com.qdong.communal.library.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * PopSelectionView
 * 非联动下拉选择控件,xml findviewbyID实例化
 * 通过{@link #setPop_style(int)}设置风格
 * 通过 {@link #initView(int,String...)} 设置默认的title
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/24  11:44
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class PopSelectionView extends RelativeLayout implements OnClickListener {

     private IPopSelectionListener callback;
     private Context mContext;
     public LinearLayout mPopTopLayout;
     private RelativeLayout mCanversLayout;// 阴影遮挡图层
     private PopupWindow mPopupWindow;// 弹窗对象
     private ListView mListZone;
     private ZoneAdapater mZoneAdapater;
     // private int lastPosition = -1;

     private TextView mTopButtonText1;
     private TextView mTopButtonText2;
     private TextView mTopButtonText3;

     private RelativeLayout mTopButton2;
     private RelativeLayout mTopButton3;
	 private ImageView checkTopBox1;

     private ImageView mTopImageView2;
     private ImageView mTopImageView3;

     private LinearLayout mDivderLayout;

     public static final int POP_STYLE_ALL = 3;
     public static final int POP_STYLE_ONLY_LEFT = 1;
     public static final int POP_STYLE_LEFT_MID = 2;
     public static final int POP_STYLE_DEFUALT = POP_STYLE_ALL;

     public static final int POP_FLAG_LEFT_CLICK = 1;
     public static final int POP_FLAG_MIDD_CLICK = 2;
     public static final int POP_FLAG_RIGHT_CLICK = 3;
	private RelativeLayout mTopButton1;
	private String mHint;//文本前缀xxx,比如:    反馈类型: 安全隐患

	public String getmHint() {
		return mHint;
	}

	public void setmHint(String mHint) {
		this.mHint = mHint;
	}

	public int getPop_style() {
		return pop_style;
	}

	/**
	 * @method name:setPop_style
	 * @des:0表示有多个列表,布局风格见:   {@link R.layout.popwin_selection_layout}
	 *       1表示只有一个列表,布局风格见:   {@link R.layout.popwin_selection_layout2}
	 * @param :[pop_style]
	 * @return type:void
	 * @date 创建时间:2016/8/24
	 * @author Chuck
	 **/
	public void setPop_style(int pop_style) {

		this.pop_style = pop_style;
	}

	public int pop_style = 0;

     private int buttonClickFlag = -1;

     private List<ZoneFilterBean> mLeftDatas;
     private List<ZoneFilterBean> mMiddDatas;
     private List<ZoneFilterBean> mRightDatas;

     public void setLeftDatas(List<ZoneFilterBean> datas) {
	    if (null == datas) {
		   datas = new ArrayList<ZoneFilterBean>();
	    }
	    this.mLeftDatas = datas;
     }

     public void setMiddDatas(List<ZoneFilterBean> datas) {
	    if (null == datas) {
		   datas = new ArrayList<ZoneFilterBean>();
	    }
	    this.mMiddDatas = datas;
     }

     public void setRightDatas(List<ZoneFilterBean> datas) {
	    if (null == datas) {
		   datas = new ArrayList<ZoneFilterBean>();
	    }
	    this.mRightDatas = datas;
     }

     public PopSelectionView(Context context) {
	    super(context, null);
     }

     public PopSelectionView(Context context, AttributeSet attrs) {
	    this(context, attrs, 0);
     }

     public PopSelectionView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    this.mContext = context;
     }

	/**
	 * @method name:initView
	 * @des:初始化控件
	 * @param :[style:布局,几个列表供选择, titles:列表标题数组]
	 * @return type:void
	 * @date 创建时间:2016/8/24
	 * @author Chuck
	 **/
     public void initView(int style, String... titles) {

	    if (null == titles || titles.length == 0) {
		   titles = new String[] { StringUtil.EMPTY, StringUtil.EMPTY,
			     StringUtil.EMPTY };
	    }
	    initView();
	    switch (style) {
		   case POP_STYLE_ONLY_LEFT:
			  mTopButtonText1.setText(titles[0]);
			  mTopButtonText2.setText(StringUtil.EMPTY);
			  mTopButtonText3.setText(StringUtil.EMPTY);

			  mTopImageView2.setVisibility(View.GONE);
			  mTopImageView3.setVisibility(View.GONE);
			  mDivderLayout.setVisibility(View.GONE);

			  mTopButton2.setClickable(false);
			  mTopButton3.setClickable(false);

			  //把第二个和第三个隐藏
			  mTopButton2.setVisibility(View.GONE);
			  mTopButton3.setVisibility(View.GONE);

			  break;
		   case POP_STYLE_LEFT_MID:
			  if (titles.length >= 2) {
				 mTopButtonText2.setText(titles[1]);
			  }
			  mTopButtonText1.setText(titles[0]);
			  mTopButtonText3.setText(StringUtil.EMPTY);
			  mTopImageView3.setVisibility(View.GONE);
			  mTopButton3.setClickable(false);

			  //把第三个隐藏
			  mTopButton3.setVisibility(View.GONE);

			  break;
		   case POP_STYLE_ALL:
			  if (titles.length >= 3) {
				 mTopButtonText2.setText(titles[1]);
				 mTopButtonText3.setText(titles[2]);
			  }
			  mTopButtonText1.setText(titles[0]);
		   default:
			  break;
	    }
     }
     private void initView() {
		 if (getPop_style() == 0)
		 {
			 LayoutInflater.from(mContext).inflate(R.layout.popwin_selection_layout,
					 this, true);
		 }
		 else
		 {
			 LayoutInflater.from(mContext).inflate(R.layout.popwin_selection_layout2,
					 this, true);
			 checkTopBox1 = (ImageView) findViewById(R.id.check_down_id1);
			 checkTopBox1.setOnClickListener(this);
		 }


	    mCanversLayout = (RelativeLayout) findViewById(R.id.rl_canvers);
	    final View mPopView = LayoutInflater.from(mContext).inflate(
			R.layout.fragment_selection_pop, null);
	    mListZone = (ListView) mPopView.findViewById(R.id.lv_zone);

		 /**初始化popwindow*/
	    mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT, true);


		 /**下面被点击时,弹窗消失*/
		 // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		 mPopView.setOnTouchListener(new OnTouchListener() {
			 public boolean onTouch(View v, MotionEvent event) {
				 int height = mPopView.findViewById(R.id.rl_devide).getTop();
				 int y = (int) event.getY();
				 if (event.getAction() == MotionEvent.ACTION_UP) {
					 if (y >height) {//触碰到了下面,则dismiss
                         if(mPopupWindow!=null){
							 mPopupWindow.dismiss();
						 }
					 }
				 }
				 return true;
			 }
		 });

	    mTopButton1 = (RelativeLayout) findViewById(R.id.popBtn);
	    mTopButton2 = (RelativeLayout) findViewById(R.id.popBtn2);
	    mTopButton3 = (RelativeLayout) findViewById(R.id.popBtn3);

	    mTopButton1.setOnClickListener(this);

	    mTopButton2.setOnClickListener(this);
	    mTopButton3.setOnClickListener(this);

	    mTopImageView2 = (ImageView) findViewById(R.id.pop_top_img2);
	    mTopImageView3 = (ImageView) findViewById(R.id.pop_top_img3);

	    mTopButtonText1 = (TextView) findViewById(R.id.pop_top_text1);
	    mTopButtonText2 = (TextView) findViewById(R.id.pop_top_text2);
	    mTopButtonText3 = (TextView) findViewById(R.id.pop_top_text3);

	    mDivderLayout = (LinearLayout) findViewById(R.id.pop_top_dvider);

	    // 初始化adapter
	    mZoneAdapater = new ZoneAdapater(mContext, mLeftDatas);
		 if (getPop_style() == 1)
		 {
			 mZoneAdapater.setPop_style(1);
		 }
	    mListZone.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> parent, View view,
								   int position, long id) {

			  // lastPosition = position;

			  ZoneFilterBean selectBean = null;
			  int selectType = -1;

			  String btnText ="";
			  switch (buttonClickFlag) {
				 case POP_FLAG_LEFT_CLICK:
					btnText=mLeftDatas.get(position).getName();
					 if (getPop_style() == 1)
					 {
						 mTopButtonText1.setText((TextUtils.isEmpty(mHint)?"":mHint) + btnText);
					 }
					 else
					 {
						 mTopButtonText1.setText(btnText);
					 }

					for (int i = 0; i < mLeftDatas.size(); i++) {
					     if (i == position)
						    mLeftDatas.get(position).setChecked(true);
					     else
						    mLeftDatas.get(i).setChecked(false);
					}
					selectBean = mLeftDatas.get(position);

					selectType = 0;
					break;
				 case POP_FLAG_MIDD_CLICK:
					btnText=mMiddDatas.get(position).getName();
					mTopButtonText2.setText(btnText);
					for (int i = 0; i < mMiddDatas.size(); i++) {
					     if (i == position)
						    mMiddDatas.get(position).setChecked(true);
					     else
						    mMiddDatas.get(i).setChecked(false);
					}
					selectBean = mMiddDatas.get(position);
					selectType = 1;
					break;
				 case POP_FLAG_RIGHT_CLICK:
					btnText=mRightDatas.get(position).getName();
					mTopButtonText3.setText(btnText);
					for (int i = 0; i < mRightDatas.size(); i++) {
					     if (i == position)
						    mRightDatas.get(position).setChecked(true);
					     else
						    mRightDatas.get(i).setChecked(false);
					}
					selectBean = mRightDatas.get(position);
					selectType = 2;
					break;
				 default:


					break;
			  }



			  // 出发自定义事件并将选中的数据传递过去
			  if (callback != null) {
				 callback.onSelectItem(selectBean, selectType);
			  }
			  // mZoneAdapater.setDatas(_datas);
			  // mZoneAdapater.notifyDataSetChanged();
			  mPopupWindow.dismiss();

		   }
	    });
	    mListZone.setAdapter(mZoneAdapater);

	    mPopTopLayout = (LinearLayout) findViewById(R.id.ll_top);

	    mPopupWindow.setOnDismissListener(new OnDismissListener() {
		   @Override
		   public void onDismiss() {// 点击消失
			  mCanversLayout.setVisibility(View.GONE);
			   if (checkTopBox1 != null)
			   {
				  // checkTopBox1.setChecked(checkTopBox1.isChecked()?false:true);
			   }
		   }
	    });
     }

     @Override
     public void onClick(View view) {

	    boolean heightFlag = false;

		 if(view.getId()==R.id.check_down_id1){
			 buttonClickFlag = POP_FLAG_LEFT_CLICK;
				mZoneAdapater.setDatas(mLeftDatas);
				if (getPop_style() == 0)
				{
					if (mLeftDatas.size() < 5)
						heightFlag = true;
					else
						heightFlag = false;
				}
				else
				{
					heightFlag = true;
				}
		 }

		 else if(view.getId()==R.id.popBtn){
			 buttonClickFlag = POP_FLAG_LEFT_CLICK;
			   if (checkTopBox1 != null)
			   {
				   //checkTopBox1.setChecked(checkTopBox1.isChecked()?false:true);
			   }
			  mZoneAdapater.setDatas(mLeftDatas);
			   if (getPop_style() == 0)
			   {
				   if (mLeftDatas.size() < 5)
					   heightFlag = true;
				   else
					   heightFlag = false;
		   }
			   else
			   {
				   heightFlag = true;
			   }

		 }

		 else if(view.getId()==R.id.popBtn2){
			 buttonClickFlag = POP_FLAG_MIDD_CLICK;
			   mZoneAdapater.setDatas(mMiddDatas);
			  if (mMiddDatas.size() < 5)
				 heightFlag = true;
			  else
				 heightFlag = false;
		 }

		 else if(view.getId()==R.id.popBtn3){
			 buttonClickFlag = POP_FLAG_RIGHT_CLICK;
			   mZoneAdapater.setDatas(mRightDatas);
			  if (mRightDatas.size() < 5)
				 heightFlag = true;
			  else
				 heightFlag = false;
		 }



		 /**只让listview最高为5条item的高度***/
	    android.widget.LinearLayout.LayoutParams params;

	    if (heightFlag
			&& mListZone.getLayoutParams().height != LayoutParams.WRAP_CONTENT) {
		   // 数据小于5条高度为包裹内容
		   params = new android.widget.LinearLayout.LayoutParams(
			     LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		   mListZone.setLayoutParams(params);
		   mListZone.requestLayout();
		   mListZone.invalidate();
	    } else if (!heightFlag
			&& mListZone.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
		   // 数据大于等于5条 高度为固定5条宽度
		   params = new android.widget.LinearLayout.LayoutParams(
			     LayoutParams.MATCH_PARENT, getResources()
			     .getDimensionPixelSize(
					 R.dimen.pop_top_listview_item_height));
		   mListZone.setLayoutParams(params);
		   mListZone.requestLayout();
		   mListZone.invalidate();
	    }

	    mZoneAdapater.notifyDataSetChanged();


		 //实例化一个ColorDrawable颜色为半透明
		 ColorDrawable dw = new ColorDrawable(0xb0000000);
		 //设置SelectPicPopupWindow弹出窗体的背景
		 mPopupWindow.setBackgroundDrawable(dw);


	    mPopupWindow.showAsDropDown(mPopTopLayout, 0, 1);
	    mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
	    mPopupWindow.setFocusable(true);
	    mPopupWindow.setOutsideTouchable(false);
	    mPopupWindow.update();

	    mCanversLayout.setVisibility(View.VISIBLE);
     }



     /**
	* 回收popwindow以及界面view
	*/
     public void recycle() {
	    try {
		   if (mPopupWindow.isShowing()) {
			  mPopupWindow.dismiss();
			  mPopupWindow = null;
			  this.removeAllViewsInLayout();
		   }
		   if (null != mLeftDatas) {
			  mLeftDatas.clear();
			  mLeftDatas = null;
		   }
		   if (null != mMiddDatas) {
			  mMiddDatas.clear();
			  mMiddDatas = null;
		   }
		   if (null != mRightDatas) {
			  mRightDatas.clear();
			  mRightDatas = null;
		   }
		   if (null != callback) {
			  callback = null;
		   }
	    } catch (Exception e) {
		   e.printStackTrace();
	    }

     }

     /**
	* 设置选中某一个item时的监听
	*
	* @param listener
	*/
     public void setOnPopSelectItemListener(IPopSelectionListener listener) {
	    this.callback = listener;
     }

     /**
	* @method name:setTextByIndex
	* @des:此方法供外部修改btn文本值
	* @param :[index, text]
	* @return type:void
	* @date 创建时间:2015/11/4
	* @author Chuck
	**/
     public void setTextByIndex(int index, String text){

	    if(index<0||index>2){//防止越界
		   return;
	    }
	    switch (index){
		   case  0:
			  if(!TextUtils.isEmpty(text)){
				 mTopButtonText1.setText(text );
			  }
			  break;
		   case  1:
			  if(!TextUtils.isEmpty(text)){
				 mTopButtonText2.setText(text );
			  }
			  break;
		   case  2:
			  if(!TextUtils.isEmpty(text)){
				 mTopButtonText3.setText(text );
			  }
			  break;
	    }
     }


}
