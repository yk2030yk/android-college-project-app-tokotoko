package com.example.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageFileHelper {
	private FileOutputStream out;
	private Context context;

	public ImageFileHelper(Context context) {
		this.context = context;
	}

	public boolean save(Bitmap bitmap, String fname) {
		boolean result = true;
		String fileName = fname + ".jpg";
		try {
			out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public Bitmap loadBitmap(String fname, int sizeWidth, int sizeHeight) {
		String fileName = fname + ".jpg";
		Bitmap bitmap = null;
		InputStream input = null;

		try {
			input = context.openFileInput(fileName);
		} catch (FileNotFoundException e) {

		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, options);

		float scaleX = options.outWidth / sizeWidth;
		float scaleY = options.outHeight / sizeHeight;
		options.inSampleSize = (int) Math.floor(Float.valueOf(Math.max(scaleX, scaleY)).doubleValue());

		try {
			input = context.openFileInput(fileName);
		} catch (FileNotFoundException e) {

		}

		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(input, null, options);

		return bitmap;
	}

	public void delete(String fname) {
		String fileName = fname + ".jpg";
		context.deleteFile(fileName);
	}
}
