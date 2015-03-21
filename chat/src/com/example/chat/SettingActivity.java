package com.example.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private Button clearChatButton;
	private Button clearSpotButton;
	private Button OnOffButton;
	private Button testButton;
	private Context context;
	private SharedPreferencesHelper spHelper;
	private final String OnOffLabel = "�ʒm��ON/OFF ";
	private final String ON = "[ ON ]";
	private final String OFF = "[ OFF ]";
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		context = this;
		
		spHelper = new SharedPreferencesHelper(this);
			
		clearChatButton = (Button)findViewById(R.id.button_clear_chat);
		clearSpotButton = (Button)findViewById(R.id.button_clear_spot);
		OnOffButton = (Button)findViewById(R.id.button_onoff);
		testButton = (Button)findViewById(R.id.button_test);
		
		if (spHelper.getNotificationMode()) {
			OnOffButton.setText(OnOffLabel + ON);
		} else {
			OnOffButton.setText(OnOffLabel + OFF);
		}
		
		clearChatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SQLiteOpenHelper helper = new SQLiteHelper(getApplicationContext(), "my.db", null, 1);
				SQLiteDatabase db = helper.getWritableDatabase();
				int res = db.delete("CHAT_TABLE", null, null);
				if (res != -1) {
					Toast.makeText(context, "�`���b�g�̏��������������܂���", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "�`���b�g�̏����������s���܂���", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		clearSpotButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostServerTask psTask = new PostServerTask(URLManager.DELETE_LOG_URL) {
					
					@Override
		        	protected void onPostExecute(Boolean result) {
						if (taskResult) {
							Toast.makeText(context, "�ό��n���̏��������������܂���", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "�ό��n���̏����������s���܂���", Toast.LENGTH_SHORT).show();
						}
		        	}
					
				};
				psTask.execute();
			}
			
		});
		
		OnOffButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String resStr = "";
				if (spHelper.getNotificationMode()) {
					spHelper.editNotificationMode(false);
					resStr = OFF;
				} else {
					spHelper.editNotificationMode(true);
					resStr = ON;
				}
				Toast.makeText(context, "�ʒm��" + resStr + "�ɂ��܂���", Toast.LENGTH_SHORT).show();
				OnOffButton.setText(OnOffLabel + resStr);
			}
			
		});
		
		testButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SettingActivity.this, FavoriteSelectActivity.class);
				startActivity(i);
			}
			
		});
	}
    
}
