package com.example.chat;

import java.io.Serializable;
import java.util.ArrayList;
//implements Serializable‚Íintent‚Å‚ÌŽó‚¯“n‚µ‚É•K—v
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;
	public String prefName = null;
	public String prefId = null;
	public ArrayList<LargeArea> largeAreas = new ArrayList<LargeArea>();
	
	public Area(String name, String id) {
		this.prefName = name;
		this.prefId = id;
	}
		
	public void addLarge(String name, String id) {
		largeAreas.add(new LargeArea(name, id));
	}
	
}
