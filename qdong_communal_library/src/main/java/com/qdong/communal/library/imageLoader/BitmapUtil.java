/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-18 下午4:25:47
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.qdong.communal.library.imageLoader;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.qdong.communal.library.BuildConfig;
import com.qdong.communal.library.R;
import com.qdong.communal.library.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

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


public class BitmapUtil {

    /**是否压缩BitMap*/
    public static boolean isCompressBitmap = false;

    /**初始化是否压缩Bitmap,如果应用的最大可分配内存大于64兆不压缩bitmap,小于等于64兆则压缩bitmap
     * */
    public static void initIsCompressBitmap(Context context) {
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        isCompressBitmap = !(memClass > BuildConfig.Compress_Bitmap);
    }

	/**
	 * 获取icon,此方法获取图标更快
	 */
	public static void loadImageBitmap(Context context, String url, ImageView imageView, int resourceId, ImageCache bitmapCache) {
		Drawable defaultDrawable = context.getResources().getDrawable(resourceId);
		if (TextUtils.isEmpty(url)) {
			imageView.setImageDrawable(defaultDrawable);
			return;
		}
		
		Bitmap bitmap = bitmapCache.getBitmapFromCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		if (cancelPotentialBitmapDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, bitmapCache);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(defaultDrawable, task);
			//尺寸适配
//			int width = ((BitmapDrawable)defaultDrawable).getBitmap().getWidth();
//			int height = ((BitmapDrawable)defaultDrawable).getBitmap().getHeight();
//			Bitmap aaBitmap = ((BitmapDrawable)downloadedDrawable).getBitmap();
//			Bitmap bbbiBitmap = aaBitmap.createScaledBitmap(aaBitmap, width, height, true);
//			imageView.setImageBitmap(bbbiBitmap);
			
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(context, url);
		}
	}
	
	public static void loadImageBitmap(Context context, String url, ImageView imageView, int resourceId, ImageCache bitmapCache, URLImageCallback imageCallback) {
		Drawable defaultDrawable = context.getResources().getDrawable(resourceId);
		if (TextUtils.isEmpty(url)) {
			imageView.setImageDrawable(defaultDrawable);
			return;
		}
		
		Bitmap bitmap = bitmapCache.getBitmapFromCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			if(null != imageCallback){
				imageCallback.imageLoadComplete(bitmap, imageView);
			}
			return;
		}
		if (cancelPotentialBitmapDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, bitmapCache);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(defaultDrawable, task);
			//尺寸适配
//			int width = ((BitmapDrawable)defaultDrawable).getBitmap().getWidth();
//			int height = ((BitmapDrawable)defaultDrawable).getBitmap().getHeight();
//			Bitmap aaBitmap = ((BitmapDrawable)downloadedDrawable).getBitmap();
//			Bitmap bbbiBitmap = aaBitmap.createScaledBitmap(aaBitmap, width, height, true);
//			imageView.setImageBitmap(bbbiBitmap);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(context,imageCallback,url);
		}
	}

	/**
	 * 背景图（从网络加载图片，对加载后的图片进行高斯处理）这样的情况，
	 * 使用imageCallback的imageLoadBefore回调方法对图片进行处理
	 * @param context
	 * @param url
	 * @param imageView
	 * @param resourceId
	 * @param bitmapCache
	 * @param imageCallback
	 */
	public static void loadImageBitmap2(Context context, String url, ImageView imageView, int resourceId, ImageCache bitmapCache, URLImageCallback imageCallback) {
		Drawable defaultDrawable = context.getResources().getDrawable(resourceId);
		if (TextUtils.isEmpty(url)) {
			if(null != imageCallback){
				Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), resourceId);
				imageCallback.imageLoadBefore(bitmap);
			}
			return;
		}
		
		Bitmap bitmap = bitmapCache.getBitmapFromCache(url);
		if (bitmap != null) {
			if(null != imageCallback){
				imageCallback.imageLoadBefore(bitmap);
			}
			return;
		}
		
		if (cancelPotentialBitmapDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, bitmapCache);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(defaultDrawable, task);
			
			task.execute(context,imageCallback,url);
		}
	}
	
	/**
	 *  从内存、SDCard、网络加载显示图片，带指定默认图片的，
	 *  如果该ImageView上已经显示得有图片了，则在加载新图片过成功，使用已用图片作为默认图片
	 * @param context
	 * @param url
	 * @param imageView
	 * @param resourceId
	 * @param bitmapCache
	 */
	public static void loadImageBitmapWithDefaultImage(Context context, String url, ImageView imageView, int resourceId, ImageCache bitmapCache) {
		Drawable defaultDrawable = context.getResources().getDrawable(resourceId);
		if (TextUtils.isEmpty(url)) {
			imageView.setImageDrawable(defaultDrawable);
			return;
		}
		
		Bitmap bitmap = bitmapCache.getBitmapFromCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		if (cancelPotentialBitmapDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, bitmapCache);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(defaultDrawable, task);
			//尺寸适配
//			int width = ((BitmapDrawable)defaultDrawable).getBitmap().getWidth();
//			int height = ((BitmapDrawable)defaultDrawable).getBitmap().getHeight();
//			Bitmap aaBitmap = ((BitmapDrawable)downloadedDrawable).getBitmap();
//			Bitmap bbbiBitmap = aaBitmap.createScaledBitmap(aaBitmap, width, height, true);
//			imageView.setImageBitmap(bbbiBitmap);
			
			// 如果ImageView已经显示某一图片了，就不再使用默认图片，这张图片就当做是默认图片，待获取网络图片回来再切换显示网络图片
			Drawable tempDrawable=imageView.getDrawable();
			if (tempDrawable == null) {
				imageView.setImageDrawable(downloadedDrawable);
			}
			
			task.execute(context, url);
		}
	}

	public static boolean cancelPotentialBitmapDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable == null || !(drawable instanceof DownloadedDrawable)) {
				return null;
			}
			DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
			return downloadedDrawable.getBitmapDownloaderTask();
		}
		return null;
	}

	/**
	 * 直接从SD卡加载图片
	 * 
	 * @param context
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getSDImg(Context context, String imagePath) {
		if (context==null || imagePath==null) {
			return null;
		}
		
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			File file = new File(imagePath);
			if (!file.exists()) {
				return null;
			}
			if (file.isDirectory()) {
				return null;
			}
			imagePath = "file://" + imagePath;
			Log.i("info", imagePath);
			is = context.getContentResolver().openInputStream(Uri.parse(imagePath));
			bitmap = BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {

		} catch (OutOfMemoryError e) {

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {

				}
			}
		}
		
		return bitmap;
	}
	//从url获取图片
	public static Bitmap loadImageFormUrlOfBlock(Context context,String url)
	{
		if (url == null) {
			return null;
		}
		
		Bitmap bitmap=null;
		if (url.equals(""))	//如果传入的为"",显示默认图片
		{
			bitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.ease_default_image);
		}
		else
		{
			final ListImageCache mImgCache = ListImageCache.getInstance(context);
			bitmap = mImgCache.getBitmapFromCache(url);
			if (bitmap != null) {
				
			}
			else {
				bitmap = loadImageFromUrl(url);
				mImgCache.putBitmapToCache(bitmap, url);
				
			}
		}
		return bitmap;
	}
	/**
	 * 根据url获取图片
	 * 
	 * @param imageUrl
	 * @param imageCallback
	 * @return
	 */
	public static Bitmap loadBitmap(final Activity mActivity, final String imageUrl, final ImageCache bitmapCache, final ImageCallback imageCallback) {
		if (TextUtils.isEmpty(imageUrl)) {
			return null;
		}
		
		Bitmap bitmap = bitmapCache.getBitmapFromCache(imageUrl);
		if (bitmap != null) {
			return bitmap;
		}
		ThreadPoolManager.executeHttpTask(new Runnable() {

			Bitmap bitmap;

			@Override
			public void run() {
				bitmap = bitmapCache.getBitmapFromLocal(imageUrl);
				if (bitmap == null) {
					bitmap = loadImageFromUrl(imageUrl);
				}
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (null == bitmap) {
							imageCallback.imageLoaded(null, imageUrl);
						} else {
							if (imageCallback != null && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)
								imageCallback.imageLoaded(bitmap, imageUrl);
						}
						// 加入缓存
						if (null != bitmap && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)// 服务器默认为像素3*3的图片
							bitmapCache.putBitmapToCache(bitmap, imageUrl);
					}
				});
				// 存入SD卡
				if (null != bitmap && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)
					bitmapCache.putBitmapToLocal(bitmap, imageUrl);
			}
		});
		return null;
	}
	public static boolean isCacheExist(Context context ,String url)
	{
		boolean bRet = false;
		Bitmap bitmap = ListImageCache.getInstance(context).getBitmapFromCache(url);
		if (bitmap != null) {
			bRet = true;
		}else {
			bitmap = ListImageCache.getInstance(context).getBitmapFromLocal(url);
			if (bitmap != null)
			{
				bRet = true;
			}
			else
			{
				bRet = false;
			}

		}
		return bRet;
	}
	/**
	 * 
	  * @method name: loadBitmap
	  * @des:  根据url获取图片，返回值为Bitmap
	  * @param : @param acivity
	  * @param : @param imageUrl
	  * @param : @param resourceId
	  * @param : @param bitmapCache
	  * @param : @param imageCallback
	  * @param : @return
	  * @return type:Bitmap
	  * @date 创建时间：2015-9-16
	  * @author hushicheng
	 */
	public static Bitmap loadBitmap(final Activity acivity, final String imageUrl, final int resourceId, final ImageCache bitmapCache, final ImageCallback imageCallback) {
		Bitmap defaultBitmap= BitmapFactory.decodeResource(acivity.getResources(), resourceId);
		
		if (TextUtils.isEmpty(imageUrl)) {
			return defaultBitmap;
			
		}else{
			Bitmap bitmap = bitmapCache.getBitmapFromCache(imageUrl);
			if (bitmap != null) {
				return bitmap;
			}else{
				bitmap = bitmapCache.getBitmapFromLocal(imageUrl);
				if (bitmap != null) {
					return bitmap;
				}
			}
		}

		ThreadPoolManager.executeHttpTask(new Runnable() {
			
			@Override
			public void run() {
				final Bitmap bitmap = loadImageFromUrl(imageUrl);
				
				acivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// 服务器默认为像素3*3的图片
						if (bitmap != null && imageCallback != null && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3){
							imageCallback.imageLoaded(bitmap, imageUrl);
						}
					}
				});
				
				// 加入缓存
				if (bitmap != null && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)// 服务器默认为像素3*3的图片
					bitmapCache.putBitmapToCache(bitmap, imageUrl);
				
				// 存入SD卡
				if (bitmap != null && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)
					bitmapCache.putBitmapToLocal(bitmap, imageUrl);
			}
		});
		
		return defaultBitmap;
	}	
	


	/**
	 * 下载图片
	 *
	 * @param url
	 * @return Bitmap
	 * */
	public static Bitmap loadImageFromUrl(String url) {
		ByteArrayOutputStream out = null;
		Bitmap bitmap = null;
		int BUFFER_SIZE = 1024 * 8;
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream(), BUFFER_SIZE);
			out = new ByteArrayOutputStream(BUFFER_SIZE);
			int length = 0;
			byte[] tem = new byte[BUFFER_SIZE];
			length = in.read(tem);
			while (length != -1) {
				out.write(tem, 0, length);
				out.flush();
				length = in.read(tem);
			}
			in.close();
			if (out.toByteArray().length != 0) {
				bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
			} else {
				out.close();
				return null;
			}
			out.close();
		} catch (OutOfMemoryError e) {

			out.reset();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size(), opts);
			return bitmap;
		} catch (Exception e) {

			return bitmap;
		}
		return bitmap;
	}

	/** 并存入本地SD卡
	 * 
	 * @param url
	 *            bitmap
	 * @return */
	public static void saveToSDCard(Bitmap bm, String url) {
	    if(FileUtils.hasSDCard()){
	        FileUtils.saveToSDCard(bm, url);
	    }
	}

	static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(Drawable defaultBitmap, BitmapDownloaderTask bitmapDownloaderTask) {
			super(((BitmapDrawable) defaultBitmap).getBitmap());
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	static class BitmapDownloaderTask extends AsyncTask<Object, Void, Bitmap> {
		private WeakReference<ImageView> imageViewReference;
		private ImageCache mBitmapCache;
		private Context context;
		private String url;
		private URLImageCallback callback;

		public BitmapDownloaderTask(ImageView imageView, ImageCache bitmapCache) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			mBitmapCache = bitmapCache;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			context = (Context) params[0];
			
			if(params[1] instanceof ImageCallback){
				callback = (URLImageCallback)params[1]; 
				url = (String) params[2];
			}else{
				url = (String) params[1];
			}
			
//			Bitmap bitmap = mImgCache.getBitmapFromSD(context, url);
			Bitmap bitmap = mBitmapCache.getBitmapFromLocal(url);
			if (bitmap == null) {
				bitmap =loadImageFromUrl(url);
				if (null != bitmap && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3 && FileUtils.hasSDCard()) {
//					FileUtils.saveToSDCard(bitmap, url);
					mBitmapCache.putBitmapToLocal(bitmap, url);
				}
			}
			if (null != bitmap && bitmap.getHeight() > 3 && bitmap.getRowBytes() > 3)// 服务器默认为像素3*3的图片
//				mImgCache.addBitmapToCache(url, bitmap);
				mBitmapCache.putBitmapToCache(bitmap, url);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if(null != callback){
				callback.imageLoadBefore(bitmap);
			}
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = null;
				bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				if (this == bitmapDownloaderTask && bitmap != null) {
					
//					Bitmap aaBitmap  = bitmap.createScaledBitmap(bitmap, imageView.getLayoutParams().width, imageView.getLayoutParams().height, true);
					imageView.setImageBitmap(bitmap);
					if(null != callback){
						callback.imageLoadComplete(bitmap,imageView);
					}
				}
			}
		}
	}
	
	/**
	 * 将源Bitmap裁剪为制定尺寸的目标Bitmap——
	 * @param sourceBitmap
	 * @param destWidth
	 * @param destHeight
	 * @return
	 */
	public static Bitmap cropImage(Bitmap sourceBitmap, float destWidth, float destHeight){
		int sourceWidth=sourceBitmap.getWidth();
		int sourceHeight=sourceBitmap.getHeight();
		
		// 如果原图尺寸比目标尺寸小，直接返回原图(不需要把小图放大了，再按目标尺寸裁剪)
		if (sourceWidth<=destWidth || sourceHeight<=destHeight) {
			return sourceBitmap;
		}
		
		float scaleWidth=destWidth/sourceWidth;
		float scaleHeight=destHeight/sourceWidth;
		// 选取scaleFactor，scaleWidth和scaleHeight之中较大者
		float scaleFactor= Math.max(scaleWidth, scaleHeight);
		
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);    
        Bitmap tempBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
        
        // 计算要裁剪部分的起始x,y坐标（相对于源Bitmap的坐标）
        int x=(int)(Math.max(sourceWidth*scaleFactor, destWidth)-destWidth)/2;
        int y=(int)(Math.max(sourceHeight*scaleFactor, destHeight)-destHeight)/2;;
        
        Bitmap destBitmap= Bitmap.createBitmap(tempBitmap, x, y, (int)destWidth, (int)destHeight);
        tempBitmap.recycle();
        
        return destBitmap;
	}
	
	/**
	 * 图片压缩（maxSize KB以内），用于上传到服务器前先对要上传的Bitmap压缩
	 * 以减少网络流量
	 * @param bitmap   //原图
	 * @param maxSize  //压缩后能够达到的最大尺寸
	 * @return
	 */
	public static byte[] compressBmpToBytes(Bitmap bitmap, int maxSize){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 >= maxSize) { 
			baos.reset();
			options -= 5;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		
		return baos.toByteArray();
	}
	
	public static byte[] compressBmpToBytes(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		
		return baos.toByteArray();
	}
	
	/**
	 * 
	  * @method name: compressImageFromFile
	  * @des: 200K以内的图片返回原图；200K以上的图片，根据图片宽高设置采样率，返回压缩的图片
	  * @param : @param srcPath    图片路径
	  * @param : @param maxWidth   指定宽度
	  * @param : @param maxHeight  指定高度
	  * @param : @return
	  * @return type:Bitmap
	  * @date 创建时间：2015-9-2
	  * @author hushicheng
	 */
	public static Bitmap compressImageFromFile(String srcPath, int maxWidth, int maxHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		Bitmap bitmap = null;

		int be = 1;
		File file=new File(srcPath);
		if (file!=null && file.exists()) {
			long length=file.length();
            // 判断图片是否大于200K或手机内存小，需要压缩
			if (length > 200*1024 || BitmapUtil.isCompressBitmap) {
				newOpts.inJustDecodeBounds = true;//只读边,不读内容
				bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
				
				int w = newOpts.outWidth;
				int h = newOpts.outHeight;
				float hh = maxWidth;//
				float ww = maxHeight;//
				
				if (w > h && w > ww) {
					be = (int) (newOpts.outWidth / ww);
				} else if (w < h && h > hh) {
					be = (int) (newOpts.outHeight / hh);
				}
				if (be <= 0)
					be = 1;
			}
		}
		
		Log.d("BitmapUtil", "--->compressImageFromFile：inSampleSize="+be+", w="+newOpts.outWidth+", h="+newOpts.outHeight);
		
		newOpts.inSampleSize = be;//设置采样率
		newOpts.inJustDecodeBounds = false;
		newOpts.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
		newOpts.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
		newOpts.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
		newOpts.inPreferredConfig = Config.RGB_565;
		
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}
	
	/**
	 * 
	  * @method name: compressImageFromFileExt
	  * @des: 根据图片路径读取Bitmap，图片会根据指定的宽高设置采样率, 尽可能获取到接近指定宽高尺寸的Bitmap
	  * @param : @param srcPath
	  * @param : @param maxWidth
	  * @param : @param maxHeight
	  * @param : @return
	  * @return type:Bitmap
	  * @date 创建时间：2015-9-2
	  * @author hushicheng
	 */
	public static Bitmap compressImageFromFileExt(String srcPath, int maxWidth, int maxHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = maxWidth;//
		float ww = maxHeight;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		
		Log.d("BitmapUtil", "--->compressImageFromFileExt：inSampleSize="+be+", w="+newOpts.outWidth+", h="+newOpts.outHeight);
		
		newOpts.inSampleSize = be;//设置采样率
		newOpts.inJustDecodeBounds = false;
		newOpts.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
		newOpts.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
		newOpts.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
		newOpts.inPreferredConfig = Config.RGB_565;
		
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}
	
	/**
	 * 根据图片路径读取Bitmap，图片会根据指定的宽高设置采样率
	 * 尽可能获取到接近指定宽高尺寸的Bitmap
	 * @param path		图片路径
	 * @param width		指定宽度
	 * @param height		指定高度
	 * @return
	 */
	public static Bitmap revitionImageSize(String path, int width, int height) {
		Bitmap bitmap = null;
		try{
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;

			while (true) {
				if ((options.outWidth >> i <= width)
						&& (options.outHeight >> i <= height)) {
					in = new BufferedInputStream(new FileInputStream(new File(
							path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					options.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
					options.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
					options.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
					options.inPreferredConfig = Config.RGB_565;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					in.close();
					break;
				}
				i += 1;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static Bitmap revitionImageSize(String path, int inSampleSize) {
		Bitmap bitmap = null;
		try{
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			options.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
			options.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
			options.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
			options.inPreferredConfig = Config.RGB_565;
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
				
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
    
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(Bitmap bitmap, int angle) {
       //旋转图片 动作   
       Matrix matrix = new Matrix();;
       matrix.postRotate(angle);  
       System.out.println("angle2=" + angle);
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
   
   /**
    * 
     * @method name: isPictureFormat
     * @des: 判断所给图片路径是否是指向图片的：识别的图片格式，png、jpg、jpeg、bmp 
     * @param : @param imagePath
     * @param : @return
     * @return type:boolean
     * @date 创建时间：2015-8-20
     * @author hushicheng
    */
   public static boolean isPictureFormat(String imagePath){
	   boolean result=false;
	   
	   if (imagePath==null || "".equals(imagePath)) {
		   result=false;
		   
	   }else if (imagePath.endsWith(".png") || imagePath.endsWith(".jpg")||imagePath.endsWith(".jpeg")||imagePath.endsWith(".bmp")){
		   result=true;
	   }
	   
	   return result;
   }
}
