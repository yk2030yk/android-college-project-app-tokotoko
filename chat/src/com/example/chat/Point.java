package com.example.chat;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String id = "";
	public String name = "";
	public int kind; // 0 : hotel, 1 : spot, 2 : gourmet.
	public double lat;
	public double lng;
	
	public Point() {
		
	}
	
	public Point(Spot spot) {
		this.id = String.valueOf(spot.spotID);
		this.name = spot.name;
		this.kind = 1;
		this.lat = spot.lat;
		this.lng = spot.lng;
	}
	
	public Point(Hotel hotel) {
		this.id = String.valueOf(hotel.hotelID);
		this.name = hotel.name;
		this.kind = 0;
		this.lat = hotel.lat;
		this.lng = hotel.lng;
	}
	
	public Point(Gourmet gourmet) {
		this.id = gourmet.gourmetId;
		this.name = gourmet.name;
		this.kind = 2;
		this.lat = gourmet.lat;
		this.lng = gourmet.lng;
	}
}
