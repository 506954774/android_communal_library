/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-18 下午4:30:04
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.qdong.communal.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 * 
 * @author yangbo
 * @date 2014-7-18
 * @version
 * @since
 */
public class FileUtils {
	private static String JPG=".jpg";
	public static final String FileSeparator = "/";

	/**
	 * 获取图片路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getImageDownloadDir(Context context) {
		String filePath = "";
		if (FileUtils.hasSDCard()) {
			// 有SD卡
			filePath = Constants.STORE_IMG_PATH;
		} else {
			// 没有SD卡
			String path = context.getFilesDir().getPath();
			filePath = path + File.separator + "ztb_charger/image"+ File.separator;
		}
		return filePath;
	}

	public static long readSDCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			Log.v("readSDCard", "block大小:" + blockSize + ",block数目:"
					+ blockCount + ",总大小:" + blockSize * blockCount / 1024
					+ "KB");
			Log.v("readSDCard", "可用的block数目：:" + availCount + ",剩余空间:"
					+ availCount * blockSize / 1024 + "KB");
			return availCount * blockSize / 1024;
		}
		return 0;
	}

	static public boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	static public String createDirInSDCard(String dir) {
		File dirFile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + dir + File.separator);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				return null;
			}
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + dir + File.separator;
	}

	/**
	 * 获取apk下载的路径
	 *
	 * @param
	 * @return
	 */
	public static String getApkDownloadDir(Context context) {
		String downFilePath = null;
		if (hasSDCard()) {
			downFilePath = Constants.STORE_APK_PATH;
		} else {
			downFilePath = getSaveApkPath(context);
		}
		return downFilePath;
	}

	/**
	 * 获取file下载的路径
	 *
	 * @param context
	 * @return
	 */
	public static String getFileDownloadDir(Context context) {
		String downFilePath = null;
		if (hasSDCard()) {
			downFilePath = Constants.STORE_RECOVERY_PATH;
		} else {
			downFilePath = getSaveFilePath(context);
		}
		return downFilePath;
	}

	/**
	 * 没有sd卡的状况下，获取存储文件的路径
	 *
	 * @return
	 */
	private static String getSaveFilePath(Context context) {
		String path = context.getFilesDir().getPath();
		String retPath = path + File.separator;
		return retPath;
	}

	/*******************************************************
	 * 说明：
	 *
	 * @author yangbo
	 * @param
	 * @return String
	 *******************************************************/
	public static String getBackupPath(Context context) {
		String path = getBackupDir(context);
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return path;
	}

	private static String getBackupDir(Context context) {
		String backupPath = "";
		if (hasSDCard()) {
			backupPath = Constants.STORE_BACKUP_PATH;
		} else {
			String path = context.getFilesDir().getPath();
			backupPath = path + File.separator + "ztb_charger/backup"
					+ File.separator;
		}
		return backupPath;
	}

	/*******************************************************
	 * 说明：
	 *
	 * @author yangbo
	 * @param context
	 * @return String
	 *******************************************************/
	public static String getRecoveryPath(Context context) {
		String path = getRecoveryDir(context);
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return path;
	}

	private static String getRecoveryDir(Context context) {
		String recoveryPath = "";
		if (hasSDCard()) {
			recoveryPath = Constants.STORE_RECOVERY_PATH;
		} else {
			String path = context.getFilesDir().getPath();
			recoveryPath = path + File.separator + "qudong/recovery"
					+ File.separator;
		}
		return recoveryPath;
	}

	/**
	 * 没有sd卡的状况下，获取存储文件的路径
	 * */
	private static String getSaveApkPath(Context context) {
		String path = context.getFilesDir().getPath();
		String retPath = path + File.separator;
		return retPath;
	}

	/**
	 * 是否有apk安装文件
	 *
	 * @param
	 * @return
	 */
	public static boolean hasApkFile(Context context,String name) {
		String path = getApkDownloadDir(context);
		boolean has = isFileExist(path + name + ".apk");
		return has;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 *
	 * @param fileName
	 * @return boolean
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 是否有sd卡
	 *
	 * @param
	 * @return
	 */
	public static boolean hasSDCard() {
		String SDState = android.os.Environment.getExternalStorageState();
		if (SDState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Constants.STORE_APK_PATH);
            return file.exists() || file.mkdirs();

        }
		return false;
	}

	/**
	 * 并存入本地SD卡
	 *
	 * @param url
	 *            bitmap
	 * @return
	 */
	public static void saveToSDCard(Bitmap bm, String url) {
		String downFilePath = Constants.STORE_IMG_PATH;
		saveBitamp(bm, url, downFilePath);
	}


	/**
	 * 保存Bitamp图片
	 *
	 * @param url
	 *            bitmap
	 * @return
	 */
	private static void saveBitamp(Bitmap bm, String url, String downFilePath) {
		try {
			File dirFile = new File(downFilePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			//File file = new File(downFilePath + url.substring(url.lastIndexOf("/") + 1));
			File file = new File(downFilePath + createFileNameByUrl(url));
			if (file.exists()) {
				return;
			}
			FileOutputStream fos = new FileOutputStream(file);
			if (url.endsWith(".png")) {
				bm.compress(CompressFormat.PNG, 100, fos);
			} else {
				bm.compress(CompressFormat.JPEG, 100, fos);
			}
			// fos.write(out.toByteArray(), 0, out.toByteArray().length);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缓存至SD卡
	 */
	public static void saveCache(File file, String result) {
		try {
			OutputStream os = new FileOutputStream(file);
			os.write(result.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从SD卡获取缓存
	 */
	public static String restoreCache(File file) {
		String result = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] data = new byte[1024];
			while ((len = fileInputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			result = new String(byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    // 删除所有文件
    public static void deleteall_ext2(File file) {
        if (file.isFile()) {

            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                //file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].getAbsolutePath().contains("cache"))
                    deleteall_ext3(childFiles[i]);
            }
            //file.delete();
        }
    }
    public static void deleteall_ext3(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                //file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                childFiles[i].delete();
            }
            //file.delete();
        }
    }
    // 删除所有文件
    public static void deleteall_ext(File file) {
        if (file.isFile()) {

            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                //file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].getAbsolutePath().contains("webview"))
                deleteall_ext(childFiles[i]);
            }
            file.delete();
        }
    }
	// 删除所有文件
	public static void deleteall(File file) {
		if (file.isFile()) {

			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteall(childFiles[i]);
			}
			file.delete();
		}
	}

	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File[] flist = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File[] flist = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	public static List<Uri> getListUriFromFile(String path) {
		List<Uri> uris = new ArrayList<Uri>();
		File file = new File(path);
		File[] files = file.listFiles();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				// 如果是目录 递归目录
				getListUriFromFile(files[i].getAbsolutePath());
			} else {
				uris.add(Uri.fromFile(files[i]));
			}
		}
		return uris;
	}

	/**
	 * 将dir和name拼接成完整的路径
	 *
	 * @param dir
	 * @param name
	 * @return
	 */
	public static String catFullPath(String dir, String name) {
		if (dir.endsWith(FileSeparator)) {
			return dir + name;
		} else {
			return dir + FileSeparator + name;
		}
	}

	public static String getParentPath(String path) {
		if (TextUtils.isEmpty(path) || path.equals(FileSeparator)) {
			return "";
		}
		if (path.endsWith(FileSeparator)) {
			path = path.substring(0, path.lastIndexOf(FileSeparator));
		}
		return path.substring(0, path.lastIndexOf(FileSeparator));
	}

	public static String getName(String absolutePath) {
		if (TextUtils.isEmpty(absolutePath)
				|| absolutePath.equals(FileSeparator)) {
			return "";
		}
		if (absolutePath.endsWith(FileSeparator)) {
			absolutePath = absolutePath.substring(0,
					absolutePath.lastIndexOf(FileSeparator));
		}
		if (TextUtils.isEmpty(absolutePath)) {
			return "";
		}
		return absolutePath
				.substring(absolutePath.lastIndexOf(FileSeparator) + 1);
	}

	/**
	 * 文件重命名
	 *
	 * @param oldname
	 * @param newname
	 */
	public static void renameFile(String oldname, String newname) {
		renameFile(new File(oldname), new File(newname));
	}

	/**
	 * 文件重命名
	 * @param oldFile
	 * @param newFile
	 */
	public static void renameFile(File oldFile, File newFile) {
		try {
			fileChannelCopy(oldFile, newFile);
			// 删除原文件
			oldFile.delete();
		} catch (Exception e) {
			Log.e("[error]","文件重命名失败!"+ e);
		}
	}
    /**
     * @method name: copyFileByAssert
     * @des: 从assert 中读取文件
     * @param :[sourcePath, t, ctx]
     * @return type:void
     * @date 创建时间：2015/5/23
     * @author zsw
     */
    public static void copyFileByAssert(String sourcePath, File t, Context ctx)
    {
        try {
            InputStream ss = ctx.getAssets().open(sourcePath);
            copyFileByStream(ss, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @method name: copyFileByStream
     * @des: 从文件流里面读到文件中
     * @param :[s, t]
     * @return type:void
     * @date 创建时间：2015/5/23
     * @author zsw
     */
	public static void copyFileByStream(InputStream s, File t)
    {
        InputStream fis = null;
        OutputStream fos = null;

        try {
            fis = s;
            fos = new BufferedOutputStream(new FileOutputStream(t));
            byte[] buf = new byte[1024];
            int i ;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf,0,i);
            }
        } catch (Exception e) {

        }finally{
            try{
                fis.close();
                fos.close();
            }catch(Exception e){

            }
        }
    }
	/**
	 * 使用缓冲流的方式复制文件
	 * @param s
	 * @param t
	 */
	public static void copyFile(File s, File t){
		InputStream fis = null;
		OutputStream fos = null;

		try {
			fis = new BufferedInputStream(new FileInputStream(s));
			fos = new BufferedOutputStream(new FileOutputStream(t));
			byte[] buf = new byte[1024];
			int i ;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf,0,i);
			}
		} catch (Exception e) {

		}finally{
			try{
				fis.close();
				fos.close();
			}catch(Exception e){

			}
		}


	}

	/**
	 * 使用文件通道的方式复制文件
	 * @param s  源文件
	 * @param t  复制到的新文件
	 */
	public static void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			Log.e("[error]", "文件复制发生异常!"+e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				Log.e("[error]", "文件复制,资源释放时发生异常"+e);
			}
		}
	}


	/**
	 * 创建nomedia隐藏文件防止系统扫描缓存图片文件
	 */
	public static void nomedia(){
		File file = new File(Constants.STORE_APP_PATH+".nomedia");
		if(!file.exists()){
			file.mkdirs();
		}
	}

//	public static String SDPATH = Environment.getExternalStorageDirectory()
//			+ "/formats/";

	public static String SDPATH = Constants.STORE_IMG_PATH;

	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist(SDPATH, "")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".jpeg");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String path, String fileName) {
		File file = new File(path + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
    /**
     * @method name: WriteFile
     * @des:
     * @param :[fileName, fileContent, ctx, mask]
     * @return type:void
     * @date 创建时间：2015/5/23
     * @author zsw
     */
    public static void WriteFile(String fileName, String fileContent, Context ctx, int mask) throws Exception
    {
        // Activity的父类的父类就是context，context与其他框架中的context相同为我们以供了一些核心操作工具。
        FileOutputStream fileOutputStream = ctx.openFileOutput(
                fileName, mask);
        fileOutputStream.write(fileContent.getBytes());
    }
    /**
     * @method name: ReadFile
     * @des:
     * @param :[fileName, ctx]
     * @return type:java.lang.String
     * @date 创建时间：2015/5/23
     * @author zsw
     */
    public static String ReadFile(String fileName, Context ctx) throws Exception
    {
        FileInputStream fileInputStream = ctx.openFileInput(fileName);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(buffer)) > 0) {
            byteArray.write(buffer, 0, len);
        };
        return byteArray.toString();
    }
    
    /**
     * 删除/data/data目录下的文件
     * @param fileName
     */
    public static void deleteDataDataFile(Context ctx,String fileName)
    {
    	 String filePath = ctx.getFilesDir() + File.separator+fileName;
         File file = new File(filePath);
         
         if (file.exists()) {
			boolean result=file.delete();
			Log.d("FileUtils", "---->deleteDataDataFile: fileName="+fileName+" delete result="+result);
         }
    }

	public static String createFileNameByUrl(String url){
		if(TextUtils.isEmpty(url)){
			return  null;
		}
		else {
			return  url.hashCode()+JPG;
		}
	}


	/**
	 * @method name:getAssetsCacheFile
	 * @des:把assets里面的文件缓存起来
	 * @param :[context, fileName]
	 * @return type:java.lang.String
	 * @date 创建时间:2016/6/4
	 * @author Chuck
	 **/
	public static String getAssetsCacheFile(Context context, String fileName)   {

		File cacheFile = new File(context.getCacheDir(), fileName);
		try {
			InputStream inputStream = context.getAssets().open(fileName);
			try {
				FileOutputStream outputStream = new FileOutputStream(cacheFile);
				try {
					byte[] buf = new byte[1024];
					int len;
					while ((len = inputStream.read(buf)) > 0) {
						outputStream.write(buf, 0, len);
					}
				} finally {
					outputStream.close();
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cacheFile.getAbsolutePath();
	}

	/**
	 * @method name:getSDPath
	 * @des:获取sd卡路径
	 * @param :[]
	 * @return type:java.lang.String
	 * @date 创建时间:2016/6/4
	 * @author Chuck
	 **/
	public static String getSDPath(){

		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
		if(sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		return sdDir.toString();
	}
}
