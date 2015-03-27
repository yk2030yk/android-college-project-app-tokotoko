package com.example.client;

import java.io.Serializable;

public class Member implements Serializable {
	private static final long serialVersionUID = 1L;
	public String regiId = "";
	public String myId = "";
	public String name = "";
	public String imageFileName = "";
	public int resId;

	public Member() {

	}

	public Member(String regiId, String myId, String name) {
		this.regiId = regiId;
		this.myId = myId;
		this.name = name;
	}

	public static Member getSystemMember() {
		Member m = new Member();
		m.name = "íäèoÇøÇ·ÇÒ";
		m.regiId = "-1";
		m.myId = "system";
		m.resId = R.drawable.icon_system3;
		return m;
	}

	public static Member getUnknownMember() {
		Member m = new Member();
		m.name = "ïsñæ";
		m.regiId = "-2";
		m.myId = "unknown";
		m.resId = R.drawable.icon_unknown;
		return m;
	}
}