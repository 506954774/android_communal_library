package com.qdong.communal.library.imageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface URLImageCallback extends ImageCallback {
	void imageLoadBefore(Bitmap imageDrawable);
	void imageLoadComplete(Bitmap imageDrawable, ImageView imageView);
}
