/*****************************************************
 * Copyright % 2014-1201 ��������ͨ������Ƽ����޹�˾
 * �����ˣ� zsw
 * �������ڣ�2014-12-3
 * �޸��ˣ�
 * ������
 * <p>
 *
 * </p>
 *****************************************************/
package com.qdong.communal.library.util;

import android.os.Environment;

import com.qdong.communal.library.BuildConfig;

import java.io.File;


/**
 * <p>
 *  存放全局静态常量，
 * </p>
 *
 * @author chuck
 * @date:2014-12-3
 * @version
 * @since
 */
public class Constants {

	/*******************************************************
	 *文件相关
	 *************************************************/

	/**glide文件root目录**/
	public static final String GLIDE="/glide";

	/**可以在gradle里配置**/
	public static final String FILE_ROOT_NAME= BuildConfig.FILE_ROOT_NAME;

	/** 配置获取Glide图片在sd卡的根目录 */
	public static String getGlideImageRootDir() {
		return Environment.getExternalStorageDirectory() + "/"+FILE_ROOT_NAME+GLIDE;
	}
	/** 获取sd卡项目目录 */
	public static String getQDongDir() {
		return Environment.getExternalStorageDirectory() + "/"+FILE_ROOT_NAME+"/";
	}

	/** 获取sd卡图片目录 */
	public static final String QD_IMAGE_DIR = getQDongDir() + "images";
	/** 获取sd卡personal目录 */
	public static final String QD_PERSONAL_DIR = getQDongDir() + "saves";

	/** 应用路径 */
	public static final String STORE_APP_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ FILE_ROOT_NAME+ File.separator;

	/**图片路径**/
	public static final String STORE_IMG_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ FILE_ROOT_NAME
			+ File.separator
			+"image" + File.separator;

	
	/** 下载APK路径 */
	public static final String STORE_APK_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ FILE_ROOT_NAME
			+ File.separator
			+ "apk" + File.separator;

	public static final String STORE_RECOVERY_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ FILE_ROOT_NAME
			+ File.separator
			+ "recovery" + File.separator;
	
	public static final String STORE_BACKUP_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ FILE_ROOT_NAME
			+ File.separator
			+ "backup" + File.separator;




	/*******************************************************
	 * **********************************************************
	 * 偏好设置 key
	 ********************************************************************/
	public static final String SESSION_ID="SESSION_ID";
	public static final String HAS_SHOWED_GUID_ACTIVITY="HAS_SHOWED_GUID_ACTIVITY";//是否展示过引导界面,默认false


	/*******************************************************
	 * **********************************************************
	 * url
	 ********************************************************************/
	public static final String SERVER_URL = "http://1501q8n685.51mypc.cn:10005/";//baseUrl,默认的
}
