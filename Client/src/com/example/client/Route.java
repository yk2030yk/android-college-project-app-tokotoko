package com.example.client;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class Route {
	public String routeId = "";
	public int priority = 0;

	public Point originPoint = null;
	public Point destPoint = null;

	public String duration = "";
	public String distance = "";
	public String startTime = "";
	public String endTime = "";

	public int durationSec = 0;
	public int startTimeSec = 0;
	public int endTimeSec = 0;
	public int stayTimeSec = 60 * 60;

	public ArrayList<LatLng> routeLatLng = new ArrayList<LatLng>();

	public Route() {

	}
}
