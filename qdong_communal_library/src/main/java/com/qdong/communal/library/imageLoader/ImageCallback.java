/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-22 上午10:11:13
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.qdong.communal.library.imageLoader;

import android.graphics.Bitmap;

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
public interface ImageCallback {
	public void imageLoaded(Bitmap imageDrawable, String imageUrl);
}
