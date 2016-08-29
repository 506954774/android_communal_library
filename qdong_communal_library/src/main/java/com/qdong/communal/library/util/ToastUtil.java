/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：gumengfu
 * 创 建 日 期：2014-7-21 下午3:57:08
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 ***********************************************************/
package com.qdong.communal.library.util;

import android.content.Context;


import com.qdong.communal.library.R;
import com.qdong.communal.library.widget.DrawerToast;



/**
 * <p>
 * 	快速打印Toast工具类
 * 
 * 
 * 
 * </p>
 * @author gumengfu
 * @date 2014-7-21
 * @version 
 * @since 
 */
public class ToastUtil {

	private static DrawerToast mToast;

	
	/**
	 * 显示自定义的Toast消息  
	 * @param context
	 * @param textResId  消息id
	 */
	public static void showCustomMessage(Context context, int textResId){
		String msg=context.getResources().getString(textResId);
		mToast=DrawerToast.getInstance(context);
		mToast.setDefaultBackgroundResource(R.drawable.common_toast_bg);
		mToast.show(msg);
	}
	

	public static void showCustomMessage(Context context, String message){
		mToast=DrawerToast.getInstance(context);
		mToast.setDefaultBackgroundResource(R.drawable.common_toast_bg);
		mToast.show(message);
	}


}
