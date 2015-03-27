package com.example.client;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Gourmet implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String GOURMET_IMAGE_FILE_NAME = "gourmet0";

	public String gourmetId = "-1";
	public String name = "";
	public double lat;
	public double lng;
	public String property = "";
	public String rate = "";
	public String category = "";
	public String url = "";
	public String postCode = "";
	public String address = "";
	public String tel = "";
	public String fax = "";
	public String lunch = "";
	public String dinner = "";
	public String close = "";
	public String openTime = "";
	public String weekday = "";
	public String saturday = "";
	public String holiday = "";
	public String access = "";
	public String equipment = "";
	public String sampleRate = "";
	public String imageURL = "";
	public boolean hasImage = false;
	public transient Bitmap bitmap = null;

	public Gourmet() {

	}

	public Gourmet(String name) {
		this.name = name;
	}

}
