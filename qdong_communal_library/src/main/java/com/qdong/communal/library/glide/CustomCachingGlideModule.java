package com.qdong.communal.library.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.qdong.communal.library.util.Constants;

/**
 * CustomCachingGlideModule
 * 这个类在manifest里面配置,使glide按我们定义的模式缓存
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/6  22:26
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class CustomCachingGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // set size & external vs. internal
        int cacheSize100MegaBytes = 1024*1024*100;//100M

        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
        );


        /***
         * SD卡路径  Chuck 2016/07/06
         */
        String downloadDirectoryPath = Constants.getGlideImageRootDir();
        builder.setDiskCache(
                new DiskLruCacheFactory( downloadDirectoryPath, cacheSize100MegaBytes )
        );

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // nothing to do here
        try {
            glide.clearDiskCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
