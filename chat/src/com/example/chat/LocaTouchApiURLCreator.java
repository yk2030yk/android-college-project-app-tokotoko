package com.example.chat;

import android.util.Log;

public class LocaTouchApiURLCreator {
	public static final String PREF_KYOTO = "26";
	public static final String AREA_DEFAULT = "421";
	
	public static final String SORT_TYPE_TOTAL = "total";
	public static final String SORT_TYPE_FOOD = "food";
	public static final String SORT_TYPE_SERVICE = "service";
	public static final String SORT_TYPE_ATMOSPHERE = "atmosphere";
	public static final String SORT_TYPE_COST = "cost_performance";
	public static final String SORT_TYPE_COMMENT = "cost_performance";
	public static final String SORT_TYPE_NEW = "date";
	
	private String option = "";
	private final String MAIN_URL = "http://api.gourmet.livedoor.com/v1.0/restaurant/?api_key=319f31a67f9947c93cace3e4c081d647a34ccd7c";
	private final String ID = "&item_id=";
	private final String PREF = "&pref_id=";
	private final String NAME = "&name=";
	private final String CATEGORY = "&category_id=";
	private final String AREA = "&area_id=";
	private final String ADDRESS = "&address=";
	private final String PAGE = "&page=";
	private final String SORT = "&sort=";			
	//private final String ORDER = "&order=";
	private final String FORMAT = "&type=xml";
	
	public void setId(String id) {
		option += this.ID + id;
	}
	
	public void setPref(String id) {
		option += this.PREF + id;
	}
	
	public void setName(String name) {
		option += this.NAME + name;
	}
	
	public void setCategory(String cate) {
		if (!cate.equals("")) {
			option += this.CATEGORY + cate;
		}
	}
	
	public void setArea(String area) {
		if (!area.equals("")) { 
			option += this.AREA + area;
		}
	}
	
	public void setAddress(String address) {
		option += this.ADDRESS + address;
	}
	
	public void setSortType(String type) {
		option += this.SORT + type;
	}
	
	public void setPage(String cnt) {
		option += this.PAGE + cnt;
	}
	
	public String createUrl() {
		String url = "";
		url = MAIN_URL + option + this.FORMAT;
		Log.d("LOCA URL", url);
		return url;
	}
	
}
