package com.example.chat;

import java.io.Serializable;
import java.util.HashMap;

import android.util.Log;

public class JalanApiURLCreator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final String areaSearchUrl = "http://jws.jalan.net/APICommon/AreaSearch/V1/?key=tau144d3935adc";
	public final String hotelSearchUrl = "http://jws.jalan.net/APIAdvance/HotelSearch/V1/?key=tau144d3935adc";
	private String option = "";
	private String areaSearchOption = "";
	public HashMap<String, String> optionValueMap;
	public HashMap<String, String> optionSelectedValueMap;
	
	//area option 
	private String prefStr = "&pref=";
	private String largeAreaStr = "&l_area=";
	private String smallAreaStr = "&s_area=";
	
	//hotel option
	private String hotelNameStr = "&h_name=";
	private String hotelIDStr = "&h_id=";
	
	//
	//private String o_area_idStr = "&o_area_id=";
	//private String o_idStr = "&o_id=";
	
	//position option
	//private String xStr = "&x=";
	//private String yStr = "&y=";
	//private String rangeStr = "&range=";

	private String startStr = "&start=";
	private String countStr = "&count=";
	private int hotelSearchStart = 1;
	private int hotelSearchCount = 10;
	private int areaSearchStart = 1;
	private int areaSearchCount = 10;
	//private String pictSizaStr = "&pict_size="; 
	//private String pictNumStr = "&picts=";
	private String order = "";
	private String xml_ptn = "";
	
	//ÇªÇÃëºåüçıèåè
	public static final String [] optionKey = {
			"&stay_date=", "&stay_count=", "&room_count=", "&adult_num=", "&sc_num=",
			"&lc_num_bed_meal=", "&lc_num_meal_only=", "&lc_num_bed_only=",
			"&lc_num_no_bed_meal=", "&min_rate=", "&max_rate=", "&h_type="};
	
	public static final String[] optionSelectedKey = {
			"&room_b=", "&room_d=", "&prv_b=", "&prv_d=", "&early_in=", "&late_out=", 
			"&pub_bath=", "&prv_bath=", "&v_bath=", "&onsen=", "&o_bath=", "&cloudy=", "&pour=",
			"&sauna=", "&esthe=", "&mssg=", "&jacz=",
			"&o_pool=", "&i_pool=", "&fitness=", "&gym=", "&p_field=", "&r_ski=", "&r_brd=", "&p_pong=",
			"&hall=", "&bbq=",
			"&parking=", "&pet=", "&limo=", "&no_smk=", "&net=",
			"&r_room=", "&high=", "&sp_room=", "&bath_to=", "&5_station=",
			"&5_beach=", "&5_slope=", "&cvs=", "&no_meal=", "&b_only=",
			"&d_only=", "&2_meals=", "&sng_room=", "&twn_room=", "&dbl_room=",
			"&tri_room=", "&4bed_room=", "&jpn_room=", "&j_w_room=", "&child_price=",
			"&c_sc=", "&c_bed_meal=","&c_no_bed_meal=", "&c_meal_only=", "&c_bed_only="};
	
	public static final String[] optionCard = {
			"&c_card=", "&c_jcb=", "&c_visa=",
			"&c_master=", "&c_amex=", "&c_uc=",
			"&c_dc=", "&c_nicos=", "&c_diners=",
			"&c_saison=", "&c_ufj="};
		
	public JalanApiURLCreator(){
		optionValueMap = new HashMap<String, String>();
		for (int i = 0; i < optionKey.length; i++){
			optionValueMap.put(optionKey[i], "");
		}
		
		optionSelectedValueMap = new HashMap<String, String>();
		for (int i = 0; i < optionSelectedKey.length; i++){
			optionSelectedValueMap.put(optionSelectedKey[i], "0");
		}
	}
	
	public void setPref(String s) { 
		if (s != null) {
			option += (prefStr + s);
			areaSearchOption += (prefStr + s);
		}
	}
	
	public void setLarge(String s) { 
		if (s != null) {
			option += (largeAreaStr + s);
			areaSearchOption += (largeAreaStr + s);
		}
	}
	
	public void setSmall(String s) { 
		if (s != null) {
			option += (smallAreaStr + s);
		}
	}
	
	public void setHotelName(String s){ 
		if (s != null) {
			option += (hotelNameStr + s);
		}
	}
	
	public void setHotelID(String s) { 
		if (s != null) {
			option += (hotelIDStr + s);
		}
	}
	
	public void setXml_ptn(int n) {
		if (0 <= n && n <= 2) {
			xml_ptn = "&xml_ptn=" + n;
		}
	}
	
	public void setOrder(int n) {
		if (0 <= 0 && n <= 4) {
			order = "&order=" + n;
		} else {
			order = "";
		}
	}
	
	public void changeSelectedOptionValue(HashMap<String, String> map) {
		optionSelectedValueMap = map;
	}
	
	public void changeOptionValue(HashMap<String, String> map) {
		optionValueMap = map;
	}
	
	public void setOptionValue() {
		String tempOption = "";
		for (int i = 0; i < optionSelectedValueMap.size(); i++) {
			String value = optionSelectedValueMap.get(optionSelectedKey[i]);
			if (value != null) {
				if (value.equals("1")) {
					tempOption += optionSelectedKey[i] + value;
				}
			}
		}
		for (int i = 0; i < optionValueMap.size(); i++) {
			String value = optionValueMap.get(optionSelectedKey[i]);
			if (value != null) {
				if (!value.equals("")) {
					tempOption += optionSelectedKey[i] + value;
				}
			}
		}
		option += tempOption;
	}
	
	public void resetHotelCount() {
		hotelSearchStart = 1;
		hotelSearchCount = 10;
	}
	
	public void resetAreaCount() {
		areaSearchStart = 1;
		areaSearchCount = 10;
	}
	
	public String createHotelURL() {
		String url = hotelSearchUrl + option + startStr + hotelSearchStart + countStr + hotelSearchCount + order + xml_ptn;
		areaSearchStart += hotelSearchCount;
		Log.d("JALAN URL", url);
		return url;
	}
	
	public String createAreaURL() {
		String url = areaSearchUrl + areaSearchOption + startStr + areaSearchStart + countStr + areaSearchCount + order + xml_ptn;
		areaSearchStart += areaSearchCount;
		return url;
	}
}
