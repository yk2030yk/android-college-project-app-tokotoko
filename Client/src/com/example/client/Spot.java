package com.example.client;

import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//Serializable : Intent ‚Ìó‚¯“n‚µ‚Ì‚É•K—v
//transient : Serializable‚·‚é‚Æ‚«‚É‚Å‚«‚È‚¢‚à‚Ì‚ğœŠO‚·‚é
public class Spot implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SPOT_IMAGE_FILE_NAME = "spot0";

	public int favoriteID = -1;
	public int spotID = -1;

	public String name = "";
	public String exp = "";
	public String address = "";
	public String tel = "";
	public String fee = "";
	public String access = "";
	public String openingHours = "";
	public String categoryName = "";
	public String areaName = "";
	public double lat = 35.39291572;
	public double lng = 139.44288869;
	public String imageUrl = "";
	public boolean hasImage = false;
	public transient Bitmap bitmap = null;

	public Spot() {

	}

	public Spot(String name) {
		this.name = name;
	}

	public Spot(int id, String name, String exp) {
		this.spotID = id;
		this.name = name;
		this.exp = exp;
	}

	public void setLatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public void failure(Context context) {
		this.name = "“Ç‚İ‚İ‚É¸”s‚µ‚Ü‚µ‚½";
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loadfailure);
	}

}
