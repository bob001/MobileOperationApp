package com.amap.cloud.scheme.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class AMApCloudImageCache implements ImageCache {
	int mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	int mCacheSize = mMaxMemory / 8;
	final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
			mCacheSize);

	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		if (url == null || url.equals("")) {
			return null;
		}
		return mImageCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		if (url == null || bitmap == null) {
			return;
		}
		if (getBitmap(url) == null) {
			mImageCache.put(url, bitmap);
		}
	}
}
