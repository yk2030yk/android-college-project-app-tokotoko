package com.example.chat;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {
	private LruCache<String, Bitmap> memoryCache;
	 
    public BitmapCache() {
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
 
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }
    
    public Bitmap getBitmap(String url) {
        return memoryCache.get(url);
    }
 
    public void putBitmap(String key, Bitmap bitmap) {
    	if (memoryCache.get(key) == null) {
    		if (key != null && bitmap != null) { 
		        Bitmap old = memoryCache.put(key, bitmap);
		        if (old != null){
		            if(!old.isRecycled()){
		                old.recycle();
		            }
		            old = null;
		        }
	        }
        }
    }
}
