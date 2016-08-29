package com.qdong.communal.library.imageLoader;

import android.graphics.Bitmap;

public abstract class ImageCache {
	
	public abstract void putBitmapToCache(Bitmap bitmap, String imageUrl);
	
	public abstract Bitmap getBitmapFromCache(String imageUrl);
	
	public abstract void putBitmapToLocal(Bitmap bitmap, String imageUrl);
	
	public abstract Bitmap getBitmapFromLocal(String imageUrl);
}
