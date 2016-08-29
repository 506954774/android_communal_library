/**
 * StarEvaluateView.java
 * Created by doudou on 2015年7月28日
 */
package com.qdong.communal.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;


import com.qdong.communal.library.R;

import java.util.HashMap;

/**
 * 星级评分控件,是垂直的线性布局.实现多个项目打分.每行一排星星
 * 通过xml  findViewById实例化,
 * 通过{@link StarEvaluateView#setItemNames(String...)}设置每个条目名,同时开始展示;
 * 通过{@link StarEvaluateView#setDefaultChosen(float ,String...)}设置每个条目默认选中几颗星,同时开始展示;
 * 通过{@link StarEvaluateView#getResult()}获取结果
 * 
 * 
 * StarEvaluateView.java
 * 责任人:  Chuck
 * 修改人：   Chuck
 * 创建/修改时间: 2016年7月28日
 * Copyright : 2014-2016 深圳趣动智能-版权所有
 */
public class StarEvaluateView extends LinearLayout implements OnRatingBarChangeListener {
	
	/**总行数*/
	private int  linesCount;
	/**每行的星星个数*/
	private int  starsCount;
	/**每行星星初始化时候默认有几颗被选择*/
	private float defaultChosen;
	/**默认的选取间隔,0.5则是半颗星*/
	private float stepSize;
	/**储存用户最后选取结果的map,key是从上到下的行index,value是几颗星是亮的*/
	private HashMap<Integer, Float> resultMap=new HashMap<Integer, Float>();
	/**视图构造器*/
    private LayoutInflater inflater;
    /**item名数组**/
	private String[]  itemNames;
	/**item字体大小,是dimen*/
	private int      textSizeDimen;
	/**可否选择*/
	private boolean checkable;
	/**可否为大号星星*/
	private boolean isBig;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 */
	@SuppressLint("NewApi")
	public StarEvaluateView(Context context, AttributeSet attrs,
							int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public StarEvaluateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public StarEvaluateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.StarEvaluateView);
		 linesCount=types.getInteger(R.styleable.StarEvaluateView_linesCount, 1);//默认只有一行
		 starsCount=types.getInteger(R.styleable.StarEvaluateView_starsCount, 5);//默认一行有五颗星
		 defaultChosen=types.getFloat(R.styleable.StarEvaluateView_defaultChosen, 1.0f);//默认只有一颗星星被选中
		 stepSize=types.getFloat(R.styleable.StarEvaluateView_stepSize, 1.0f);//默认每次增长一可选,没有半颗星可选
		 checkable=types.getBoolean(R.styleable.StarEvaluateView_checkable, false);//默认不可选择
		 isBig=types.getBoolean(R.styleable.StarEvaluateView_isBig, false);//默认是小号的星星
		 inflater = LayoutInflater.from(context);
		 this.setOrientation(LinearLayout.VERTICAL);
	     initView();
	}

	
	
	/**
	 * @return the itemNames
	 */
	public String[] getItemNames() {
		return itemNames;
	}

	/**
	 * @param itemNames the itemNames to set
	 */
	public void setItemNames(String... itemNames) {
		if(resultMap!=null){
			resultMap.clear();
		}
		if(itemNames!=null){
			this.itemNames = itemNames;
			this.linesCount=itemNames.length;
			show();
		}
	}

	public float getDefaultChosen() {
		return defaultChosen;
	}

	/**
	 * @method name:setDefaultChosen
	 * @des:按需要来展示
	 * @param :[defaultChosen:默认的选中数, itemNames:每行的标题]
	 * @return type:void
	 * @date 创建时间:2016/8/23
	 * @author Chuck
	 **/
	public void setDefaultChosen(float defaultChosen,String... itemNames) {

		this.defaultChosen = defaultChosen;
		if(resultMap!=null){
			resultMap.clear();
		}
		if(itemNames!=null){
			this.itemNames = itemNames;
			this.linesCount=itemNames.length;
		}
		else{
			this.itemNames = itemNames;
			this.linesCount=1;//默认只有一行
		}
		show();
	}

	/**
	 * @return the textSizeDimen
	 */
	public int getTextSizeDimen() {
		return textSizeDimen;
	}

	/**
	 * @param textSizeDimen the textSizeDimen to set
	 */
	public void setTextSizeDimen(int textSizeDimen) {
		this.textSizeDimen = textSizeDimen;
		
	}

	/**
	  * @method name: initView
	  * @des: 绘制view
	  * @param : 
	  * @return type:void
	  * @date 创建时间：2015年7月28日
	  * @author Chuck
	  */
	private void initView() {
		
		for(int i=0;i<linesCount;i++){
			View view =null;
			view=inflater.inflate(isBig?R.layout.include_star_evaluate_item_big:R.layout.include_star_evaluate_item_small, null);
			RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_rating_average);
			ratingBar.setNumStars(starsCount);
			ratingBar.setClickable(true);
			ratingBar.setEnabled(true);
			ratingBar.setIsIndicator(!checkable);
			ratingBar.setRating(defaultChosen);
			ratingBar.setStepSize(stepSize);
			resultMap.put(i,defaultChosen );//把默认值给上
			
			ratingBar.setOnRatingBarChangeListener(this);
			ratingBar.setTag(i);	//绑定
			
			TextView title =(TextView) view.findViewById(R.id.tv_item_name);
			title.setVisibility(View.GONE);
			
				if( itemNames!=null){
					if(i<itemNames.length&& !TextUtils.isEmpty(itemNames[i])){//防止数组越界
						title.setVisibility(View.VISIBLE);
							if(textSizeDimen>0){
								title.setTextSize(textSizeDimen);
							}
							else{
								title.setTextSize(isBig?17:11);
							}
						title.setText(itemNames[i]+":");
					}
				}
				
				this.addView(view);
			
		}
		
	}
	/* (non-Javadoc)
	 * @see android.widget.RatingBar.OnRatingBarChangeListener#onRatingChanged(android.widget.RatingBar, float, boolean)
	 */
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
								boolean fromUser) {
		Integer index  = (Integer) ratingBar.getTag();
		Log.i("STAR", "index:"+index+",rating:"+rating);
		resultMap.put(index, rating);
	}
	
	/**
	 * 
	  * @method name: getResult
	  * @des:  返回结果map
	  * @param : @return
	  * @return type:HashMap<Integer,Float>
	  * @date 创建时间：2015年7月28日
	  * @author Chuck
	 */
	public HashMap<Integer, Float> getResult(){
		return  resultMap;
	}

	/**
	 * 对外公开的方法
	  * @method name: show
	  * @des: 
	  * @param : 
	  * @return type:void
	  * @date 创建时间：2015年7月28日
	  * @author Chuck
	 */
	private void show(){
		this.removeAllViews();
		initView();
	}
	
	
	/**
	 * @param context
	 */
	public StarEvaluateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


}
