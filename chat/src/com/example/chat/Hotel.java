package com.example.chat;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

public class Hotel implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String HOTEL_IMAGE_FILE_NAME = "hotel0";
	
	public int hotelID = -1;
	public String name = "";
	public String postCode = "";
	public String address = "";
	public String hotelType = "";
	public String hotelURL = "";
	public String catchCopy = "";
	public String hotelCaption = "";
	public String x = "";
	public String y = "";
	public double lat;
	public double lng;
	public String check_in = "";
	public String check_out = "";
	public String imageUrl = "";
	public String imageCaption = "";
	public ArrayList<String> accessInformationTitle = new ArrayList<String>();
	public ArrayList<String> accessInformation = new ArrayList<String>();
	public String sampleRate = "";
	public String region = "";
	public String prefecture = "";
	public String largeArea = ""; 
	public String smallArea = "";
	public boolean hasImage = false;
	public transient Bitmap bitmap = null;
	
	public Hotel() {
	}
	
	public Hotel(String name) {
		this.name = name;
	}
	
	public void changeXY() {
		int lt = Integer.parseInt(x);
		int lg = Integer.parseInt(y);
		this.lng = (lt - lt * 0.00010695 + lg * 0.000017464 + 0.0046017) / 3600000;
		this.lat = (lg - lt * 0.000046038 - lg * 0.000083043 + 0.010040) / 3600000;
	}
	
	
}
