/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-18 下午4:26:30
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.qdong.communal.library.imageLoader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.qdong.communal.library.util.FileUtils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * <p>
 * lrucache和softreference lru作为一级缓存，softrefenrence作为二
 * 级缓存，当lur满的时候，使用softreference
 * </p>
 * 
 * @author
 * @date 2014-7-18
 * @version
 * @since
 */
public class ListImageCache extends ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;
	private LinkedHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache;
	private Context mContext;
	private static ListImageCache INSTANCE;

	private ListImageCache(Context mContext) {
		this.mContext=mContext;
		init(mContext);
	}

	public static ListImageCache getInstance(Context mContext) {
		if (INSTANCE == null) {
			INSTANCE = new ListImageCache(mContext);
		} else if (null == INSTANCE.mContext) {
			throw new IllegalAccessError("must init image cache before use");
		}
		return INSTANCE;
	}

	public void init(Context context) {
		this.mContext = context;
		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		Log.d("memory is ", (memClass / 6) + "");
		// 用1/6的可用内存作为内缓存
		final int cacheSize = 1024 * 1024 * memClass / 6;

		if (mMemoryCache == null) {
			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}

				@Override
				protected void entryRemoved(boolean evicted, String key,
											Bitmap oldValue, Bitmap newValue) {
					// 硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区

					mSoftBitmapCache.put(key, new SoftReference<Bitmap>(
							oldValue));
				}
			};
		}

		// 如果大于100张图，自动清理
		final int SOFT_CACHE_CAPACITY = 100;
		if (mSoftBitmapCache == null) {
			mSoftBitmapCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
					SOFT_CACHE_CAPACITY, 0.75f, true) {

				private static final long serialVersionUID = 1L;

				@Override
				public SoftReference<Bitmap> put(String key,
												 SoftReference<Bitmap> value) {
					return super.put(key, value);
				}

				@Override
				protected boolean removeEldestEntry(
						LinkedHashMap.Entry<String, SoftReference<Bitmap>> eldest) {
					if (size() > SOFT_CACHE_CAPACITY) {
						Log.v("tag", "Soft Reference limit , purge one");
						return true;
					}
					return false;
				}

			};
		}
	}

	/**
	 * 根据key获得对应的一张图片 硬引用缓存区间中读取失败，从软引用缓存区间读取
	 * 
	 * @param key 是图片的url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String key) {
		if (key==null || key.length()==0) {
			return null;
		}
		
		//key = key.substring(key.lastIndexOf("/") + 1, key.length());

		key = FileUtils.createFileNameByUrl(key);
		synchronized (mMemoryCache) {
			final Bitmap bitmap = mMemoryCache.get(key);
			if (bitmap != null) {
				return bitmap;
			}

			synchronized (mSoftBitmapCache) {
				SoftReference<Bitmap> bitmapReference = mSoftBitmapCache
						.get(key);
				if (bitmapReference != null) {
					final Bitmap bitmap2 = bitmapReference.get();
					if (bitmap2 != null) {
						return bitmap2;
					} else {
						Log.v("tag", "soft reference have recycle");
						mSoftBitmapCache.remove(key);
					}
				}
			}
			return null;
		}
	}

//	/**
//	 * 从本地SD卡获取图片
//	 * 
//	 * @param mContext
//	 * @param imageUrl
//	 */
//	public Bitmap getBitmapFromSD(Context mContext, String imageUrl) {
//		String path = FileUtils.getImageDownloadDir(mContext);
//		File file = new File(path);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
//				imageUrl.length());
//		String filePath = file.getAbsolutePath() + "/" + imageName;
//		File fileName = new File(filePath);
//		if (fileName.exists()) {
//			Bitmap bitmap = BitmapUtil.getSDImg(mContext, filePath);
//			return bitmap;
//		}
//		return null;
//	}

//	/**
//	 * 把bitmap加入缓存
//	 * 
//	 * @param uri
//	 *            key，下次取bitmap的时候用这个put进去的key
//	 * @param bitmap
//	 */
//	public void addBitmapToCache(String imageUrl, Bitmap bitmap) {
//		if (null == imageUrl || null == bitmap) {
//			return;
//		}
//		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
//				imageUrl.length());
//		BitmapWorkerTask mTask = new BitmapWorkerTask();
//		Object[] params = new Object[2];
//		params[0] = bitmap;
//		params[1] = imageName;
//		mTask.execute(params);
//	}

	/**
	 * 用一个key标志，添加Bitmap到缓存中
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromCache(key) == null) {
			synchronized (mMemoryCache) {
				mMemoryCache.put(key, bitmap);
			}
		}
	}

	/**
	 * anroid AsyncTask异步执行
	 * 
	 * @author jacky
	 * 
	 */
	class BitmapWorkerTask extends AsyncTask<Object, Integer, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Object... params) {
			addBitmapToMemoryCache((String) params[1], (Bitmap) params[0]);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progresses) {
		}

		@Override
		protected void onPostExecute(String result) {
		}

		@Override
		protected void onCancelled() {
		}
	}

	/**
	 * 把bitmap加入缓存
	 */
	@Override
	public void putBitmapToCache(Bitmap bitmap, String imageUrl) {
		if (bitmap==null || imageUrl==null || imageUrl.length()==0) {
			return;
		}



		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/

		String imageName = FileUtils.createFileNameByUrl(imageUrl);

		BitmapWorkerTask mTask = new BitmapWorkerTask();
		Object[] params = new Object[2];
		params[0] = bitmap;
		params[1] = imageName;
		mTask.execute(params);
	}

	@Override
	public void putBitmapToLocal(Bitmap bitmap, String imageUrl) {
		if (bitmap==null || imageUrl==null || imageUrl.length()==0) {
			return;
		}
		
		FileUtils.saveToSDCard(bitmap, imageUrl);
	}

	/**
	 * 从本地SD卡获取图片
	 */
	@Override
	public Bitmap getBitmapFromLocal(String imageUrl) {
		if (imageUrl==null || imageUrl.length()==0) {
			return null;
		}
		
		String path = FileUtils.getImageDownloadDir(mContext);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/
		String imageName = FileUtils.createFileNameByUrl(imageUrl);
		String filePath = file.getAbsolutePath() + "/" + imageName;
		File fileName = new File(filePath);
		if (fileName.exists()) {
			Bitmap bitmap = BitmapUtil.getSDImg(mContext, filePath);
			return bitmap;
		}
		return null;
	}


}
