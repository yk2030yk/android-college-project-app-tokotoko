package com.example.client;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_MEMBER_VERSION = "memberVersion";
	public static final String PROPERTY_SHOW_MODE = "show_mode";
	public static final String PROPERTY_NOTIFICATION_MODE = "notification_mode";
	public static final String USER_NAME = "user_name";
	public static final String USER_ID = "user_id";
	public static final String COUNT = "count";

	private SharedPreferences prefs;

	public SharedPreferencesHelper(Context context) {
		prefs = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
	}

	public String getRegistrationId() {
		String id = prefs.getString(PROPERTY_REG_ID, "");
		return id;
	}

	public int getAppVersion() {
		int ver = prefs.getInt(PROPERTY_APP_VERSION, 0);
		return ver;
	}

	public int getMemberVersion() {
		int ver = prefs.getInt(PROPERTY_MEMBER_VERSION, 0);
		return ver;
	}

	public String getUserName() {
		String name = prefs.getString(USER_NAME, "");
		return name;
	}

	public String getUserId() {
		String name = prefs.getString(USER_ID, "");
		return name;
	}

	public int getShowMode() {
		int showMode = prefs.getInt(PROPERTY_SHOW_MODE, 0);
		return showMode;
	}

	public boolean getNotificationMode() {
		boolean showMode = prefs.getBoolean(PROPERTY_NOTIFICATION_MODE, true);
		return showMode;
	}

	public void editRegistrationId(String id) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, id);
		editor.commit();
	}

	public void editAppVersion(int ver) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PROPERTY_APP_VERSION, ver);
		editor.commit();
	}

	public void editMemberVersion(int ver) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PROPERTY_MEMBER_VERSION, ver);
		editor.commit();
	}

	public void editUserName(String name) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(USER_NAME, name);
		editor.commit();
	}

	public void editUserId(String id) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(USER_ID, id);
		editor.commit();
	}

	public void editShowMode(int mode) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PROPERTY_SHOW_MODE, mode);
		editor.commit();
	}

	public void editNotificationMode(boolean mode) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(PROPERTY_NOTIFICATION_MODE, mode);
		editor.commit();
	}

	public int getCount() {
		int cnt = prefs.getInt(COUNT, 0);
		return cnt;
	}

	public void editCount(int cnt) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(COUNT, cnt);
		editor.commit();
	}
}
