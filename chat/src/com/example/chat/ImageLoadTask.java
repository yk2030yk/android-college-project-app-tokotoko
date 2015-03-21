package com.example.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoadTask extends AsyncTask<Integer, Integer, Boolean> {
	private String url;
	private InputStream inputStream;
	public ImageView image = null;
	public String tag = null;
	public Bitmap bitmap = null;
	public Context context;
		
	public ImageLoadTask(String url, ImageView image, Context context) {
		this.url = url;
		this.image = image;
		this.context = context;
		if (image.getTag() != null) {
			this.tag = image.getTag().toString();
		}
	}
		
	public ImageLoadTask(String url, Context context) {
		this.url = url;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Integer... arg0) {
		try {
	    	URL u = new URL(url);
	    	inputStream = u.openStream();
	        bitmap = BitmapFactory.decodeStream(inputStream);
	    } catch (IOException e) {
			
	    } finally {
	    	try {
	    		if (inputStream != null) {
	    			inputStream.close();
				}
			} catch (IOException e) {
				Log.e("ERROR", e.toString());
			}
	    	
	    	if (bitmap == null) {
	    		Resources res = context.getResources();
				bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_noimage);
	    	}
	    }
		return null;
	}
	
	protected void saveLocalFile(String fileName) {
		ImageFileHelper ifh = new ImageFileHelper(context);
		ifh.save(bitmap, fileName);
	}
	
	public static int getPxFromDp(Context context, int dp){
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int px = (int)(metrics.density * dp); 
		return px;
	}
}
