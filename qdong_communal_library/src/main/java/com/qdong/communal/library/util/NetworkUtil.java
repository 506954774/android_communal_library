/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-22 下午1:55:19
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.qdong.communal.library.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.qdong.communal.library.R;


/**
 * <p>
 * 
 * </p>
 * 
 * @author yangbo
 * @date 2014-7-22
 * @version
 * @since
 */
public class NetworkUtil {
	final static String TAG = "NetworkUtil";
	

	 private static boolean isChinese(char c) {
	        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	            return true;  
	        }  
	        return false;  
	    }  
	 public static boolean isChinese(String strName) {
	        char[] ch = strName.toCharArray();  
	        for (int i = 0; i < ch.length; i++) {  
	            char c = ch[i];  
	            if (isChinese(c)) {  
	                return true;  
	            }  
	        }  
	        return false;  
	    }  
	/**
	 * 获取网络状态
	 * @return 1为wifi连接，2为2g网络，3为3g网络，-1为无网络连接
	 */
	public static int getNetworkerStatus(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conMan.getActiveNetworkInfo();
		if (null != info && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {
				case 1:
				case 2:
				case 4:
					// 2G网络
					return 2;
				default:
					// 3G及其以上网络
					return 3;
				}
			} else {
				// wifi网络
				return 1;
			}
		} else {
			// 无网络
			return -1;
		}
	}

	public static boolean hasNetWork(Context context) {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	/**
	 * 检查网络状况,如果没网,土司提示
	 * @return
	 */
	public static boolean checkNetWorkWithToast(Context context) {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		if(!netSataus){
			ToastUtil.showCustomMessage(context.getApplicationContext(), context.getApplicationContext().getString(R.string.order_list_network_error));
		}
		return netSataus;
	}

	/** 判断当前网络是否是wifi网络 */
	public static boolean isWifi(Context context) {
		return getNetworkerStatus(context) == 1;
	}
	public static boolean isWifiConnected(Context context){
		try{
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			return mWifi.isConnected();
		}catch(Exception e){
		}
		return false;
	}

	private static WifiInfo getWifiInfo(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo;
	}

	public static String getSSID(Context context){
		String ssid = "";
		try{
			WifiInfo wifiInfo = NetworkUtil.getWifiInfo(context);
			ssid = wifiInfo.getSSID();
		}catch(Exception e){

		}
		return ssid;
	}
	public static String getMac(Context context){
		String mac = "";
		try{
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	        WifiInfo info = wifi.getConnectionInfo();
	        mac = info.getMacAddress();
		}catch(Exception e){

		}
		return mac;
	}
	public static String getIpAddr(Context context){
		String ip = "";
		try{
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	        WifiInfo info = wifi.getConnectionInfo();
	        int ipnum = info.getIpAddress();
	     ip =   ( ipnum & 0xFF) + "." +
	       ((ipnum >> 8 ) & 0xFF) + "." + 
	       ((ipnum >> 16 ) & 0xFF) + "." +
	       ((ipnum >> 24 ) & 0xFF );
		}catch(Exception e){

		}
		return ip;
	}
	public static String getImei(Context context)
	{
		String imei = "";
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
		} catch (Exception e) {
			// TODO: handle exception

		}
		return imei;
	}


	/**
	 * 判断网络连接状态
	 * 
	 * @return 返回true为连接
	 */
	public static boolean isNetworkerConnect(Context context) {
		return getNetworkerStatus(context) != -1;
	}

	/** 进入网络设置 */
	public static void netWorkSetting(Context context) {
		String sdkVersion = android.os.Build.VERSION.SDK;
		Intent intent = null;
		if (Integer.valueOf(sdkVersion) > 10) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
			intent.setComponent(comp);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}


}
