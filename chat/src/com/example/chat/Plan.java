package com.example.chat;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String PlanName = "";
	public String PlanCD = "";
	public ArrayList<String> RoomType = new ArrayList<String>();
	public String RoomName = "";
	public String RoomCD = "";
	public String PlanCheckIn = "";
	public String PlanCheckOut = "";
	public String PlanDetailURL = "";
	public String PlanPictureURL = "";
	public String PlanPictureCaption = "";
	public String Meal = "";
	public String PlanSampleRateFrom = "";
	
	public Plan() {
		
	}
}
