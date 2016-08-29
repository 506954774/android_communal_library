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
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;


import com.qdong.communal.library.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * <p>
 * lrucache和softreference lru作为一级缓存，softrefenrence作为二
 * 级缓存，当lur满的时候，使用softreference
 * </p>
 * 
 * @author 。。。
 * @date 2014-7-18
 * @version
 * @since
 */
public class PersistantImageCache extends ImageCache {
	private final String TAG="PersistantImageCache";

	private LruCache<String, Bitmap> mMemoryCache;
	private LinkedHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache;
	private Context mContext;
	private static PersistantImageCache INSTANCE;

	private PersistantImageCache(Context mContext) {
		this.mContext=mContext;
		init(mContext);
	}

	public static PersistantImageCache getInstance(Context mContext) {
		if (INSTANCE == null) {
			INSTANCE = new PersistantImageCache( mContext);
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
					Log.i("PersistantImageCache", "put bitmap to sSoftBitmapCache is "+key);
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
		key =  FileUtils.createFileNameByUrl(key);
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

	/**
	 * 把bitmap加入缓存
	 * 
	 * @param imageUrl，下次取bitmap的时候用这个put进去的key
	 *
	 * @param bitmap
	 */
	@Override
	public void putBitmapToCache(Bitmap bitmap, String imageUrl) {
		if (bitmap==null || imageUrl==null || imageUrl.length()==0) {
			return;
		}
		
		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/

		String imageName =  FileUtils.createFileNameByUrl(imageUrl);

		Log.d("PersitantImageCache", "--->putBitmapToCache: imageName="+imageName+", bitmap="+bitmap);
		
		synchronized (mMemoryCache) {
			mMemoryCache.put(imageName, bitmap);
		}
	}

	/**
	 * 将图片保存到/data/data目录下
	 */
	@Override
	public void putBitmapToLocal(Bitmap bitmap, String imageUrl) {
		if (bitmap==null || imageUrl==null || imageUrl.length()==0) {
			return;
		}
		
		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/

		String imageName = FileUtils.createFileNameByUrl(imageUrl);



		FileOutputStream out;
		try {
			out = mContext.openFileOutput(imageName, Context.MODE_PRIVATE);
			Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.JPEG;
			
			Log.d("PersitantImageCache", "--->putBitmapToLocal: imageName="+imageName+", out="+out+", bitmap="+bitmap);
			  
			bitmap.compress(localCompressFormat, 90, out);
			  
			out.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 从/data/data目录下获取图片
	 */
	@Override
	public Bitmap getBitmapFromLocal(String imageUrl) {
		if (imageUrl==null || imageUrl.length()==0) {
			return null;
		}
		
		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/
		String imageName = FileUtils.createFileNameByUrl(imageUrl);
		Bitmap bitmap = null;
		try {
			// 判断手机内存状态
			if (BitmapUtil.isCompressBitmap){
				String filePath = mContext.getFilesDir() + File.separator+imageName;
                bitmap =BitmapUtil.revitionImageSize(filePath, 2);

			}else{
                FileInputStream localStream = mContext.openFileInput(imageName);
				bitmap = BitmapFactory.decodeStream(localStream);
				localStream.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	public void deleteBitmapFromLocal(String imageUrl){
		if (imageUrl==null || imageUrl.length()==0) {
			return;
		}
		
		/*String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.length());*/

		String imageName = FileUtils.createFileNameByUrl(imageUrl);

		FileUtils.deleteDataDataFile( mContext,imageName);
	}
}
