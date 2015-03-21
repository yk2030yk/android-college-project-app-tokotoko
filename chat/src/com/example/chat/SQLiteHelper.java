package com.example.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		String sql=
				"CREATE TABLE CHAT_TABLE(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"REGI_ID TEXT, " +
				"TALK TEXT, " +
				"TIME TEXT, " +
				"KIND INTEGER," +
				"SPOT_ID INTEGER)";
		db.execSQL(sql);
		
		String sql2=
				"CREATE TABLE MEMBER_TABLE(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"REGI_ID TEXT UNIQUE, " +
				"MY_ID TEXT UNIQUE, " +
				"NAME TEXT, " +
				"IMAGE TEXT)";
		db.execSQL(sql2);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	
}
