package com.qdong.communal.library.imageLoader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.qdong.communal.library.R;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * AlbumImageCache
 * 只在内存里,不写入卡
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/20  16:01
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class AlbumImageCache extends ImageCache{

    public final String TAG = getClass().getSimpleName();

	public Handler handler = new Handler();
	private LruCache<String, Bitmap> mMemoryCache;
	private LinkedHashMap<String, SoftReference<Bitmap>> imageCache;
	private Bitmap placeholdBimap;
	private Context mContext;

    private static AlbumImageCache INSTANCE;

    private AlbumImageCache(Context mContext) {
        this.mContext=mContext;
        init(mContext);
    }

    public static AlbumImageCache getInstance(Context mContext) {
        if (INSTANCE == null) {
            INSTANCE = new AlbumImageCache(mContext);
        } else if (null == INSTANCE.mContext) {
            throw new IllegalAccessError("must init image cache before use");
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
		placeholdBimap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.album_placehold_image);
		Log.d(TAG, "--->BitmapCache: placeholdBimap=" + placeholdBimap);

		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		Log.i("memory is ", (memClass / 10) + "");
		// 用1/10的可用内存作为内缓存
		final int cacheSize = 1024 * 1024 * memClass / 10;

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
					Log.i("AlbumImageCache", "put bitmap to sSoftBitmapCache is "+key);
					imageCache.put(key, new SoftReference<Bitmap>(oldValue));
				}
			};
		}

        // 如果大于100张图，自动清理
        final int SOFT_CACHE_CAPACITY = 100;
        if (imageCache == null) {
            imageCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
                    SOFT_CACHE_CAPACITY, 0.75f, true) {

                private static final long serialVersionUID = 1L;

                @Override
                public SoftReference<Bitmap> put(String key, SoftReference<Bitmap> value) {
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

    public void displayBmp(final ImageView iv, final String thumbPath,
                           final String sourcePath, final ImageCallback callback) {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
            Log.e(TAG, "no paths pass in");
            return;
        }

        final String path;
        final boolean isThumbPath;
        if (!TextUtils.isEmpty(thumbPath) && !thumbPath.equals(sourcePath)) {
            path = thumbPath;
            isThumbPath = true;
        } else if (!TextUtils.isEmpty(sourcePath)) {
            path = sourcePath;
            isThumbPath = false;
        } else {
            return;
        }

        Bitmap bmp =getBitmapFromCache(path);
        if (bmp != null) {
            if (callback != null) {
                callback.imageLoad(iv, bmp, path);
            }
            iv.setImageBitmap(bmp);
            Log.d(TAG, "--->displayBmp: hit cache, path="+path);
            return;
        }

        iv.setImageBitmap(placeholdBimap);

        new Thread() {
            Bitmap thumb;

            public void run() {

                try {
                    if (isThumbPath) {
                        thumb = BitmapUtil.revitionImageSize(thumbPath, 200, 200);
                        if (thumb == null) {
                            thumb = BitmapUtil.revitionImageSize(sourcePath, 200, 200);
                        }
                    } else {
                        thumb = BitmapUtil.revitionImageSize(sourcePath, 200, 200);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "--->displayBmp: path=" + path+", exception="+e.toString());
                }
                if (thumb == null) {
                    thumb = BitmapFactory.decodeResource(
                            mContext.getResources(),
                            R.mipmap.icon_addpic_unfocused);

                    Log.d(TAG, "--->displayBmp: path=" + path+", res=icon_addpic_unfocused");
                }

                putBitmapToCache(thumb, path);

                if (callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.imageLoad(iv, thumb, path);
                        }
                    });
                }
            }
        }.start();
    }

	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params);
	}

	@Override
	public void putBitmapToCache(Bitmap bitmap, String path) {
        if (bitmap==null || path==null || path.length()==0) {
            return;
        }

        synchronized (mMemoryCache) {
            mMemoryCache.put(path, bitmap);
        }
	}
	
	/**
	 * 根据key获得对应的一张图片 硬引用缓存区间中读取失败，从软引用缓存区间读取
	 * 
	 * @param path  图片的存储路径
	 * @return
	 */
	@Override
	public Bitmap getBitmapFromCache(String path) {
        if (path==null || path.length()==0) {
            return null;
        }

		synchronized (mMemoryCache) {
			final Bitmap bitmap = mMemoryCache.get(path);
			if (bitmap != null) {
				return bitmap;
			}

			synchronized (imageCache) {
				SoftReference<Bitmap> bitmapReference = imageCache.get(path);
				if (bitmapReference != null) {
					final Bitmap bitmap2 = bitmapReference.get();
					if (bitmap2 != null) {
						return bitmap2;
					} else {
						Log.v("tag", "soft reference have recycle");
						imageCache.remove(path);
					}
				}
			}

			return null;
		}
	}

	@Override
	public void putBitmapToLocal(Bitmap bitmap, String imageUrl) {
		// TODO Auto-generated method stub
	}

	@Override
	public Bitmap getBitmapFromLocal(String imageUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
