package com.example.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TopActivity extends Activity {
	private SharedPreferencesHelper sph;
	public SQLiteDatabase db;
	private Context context;
	private Button signUpButton;
	private Button settingButton;
	private ListView listView;
	private CustomAdapter adapter;
	private ArrayList<String> nameList;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		getActionBar().setTitle("ホーム");
		context = this;
		
		SQLiteOpenHelper helper = new SQLiteHelper(getApplicationContext(), "my.db", null, 1);
		db = helper.getWritableDatabase();
		sph = new SharedPreferencesHelper(this);
		
		if (sph.getRegistrationId().equals("")) {
			Intent i = new Intent(TopActivity.this, SignUpActivity.class);
			startActivity(i);
		} else {
			PostServerTask psTask = new PostServerTask(URLManager.CHECK_SERVER_URL){
	        	
	    		@Override
	        	protected void onPostExecute(Boolean result) {
	        		if (taskResult) {
	        			Loading load = new Loading(context, "確認中...");
	        			load.show();
	        			UpdateMemberTask task = new UpdateMemberTask(context);
	        			task.setLoad(load);
	        			task.execute();
	        		}
	        	}	
	        };
	        psTask.execute();
		}
		
		signUpButton = (Button)findViewById(R.id.button_sign_up);
		settingButton = (Button)findViewById(R.id.button_setting);
		
		listView = (ListView)findViewById(R.id.listView_group);
		nameList = new ArrayList<String>();
		adapter = new CustomAdapter(this, nameList);
		listView.setAdapter(adapter);
		
		settingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TopActivity.this, SettingActivity.class);
				startActivity(i);
			}
			
		});
		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TopActivity.this, SignUpActivity.class);
				startActivity(i);
			}
			
		});
		
	}
    
    @Override
   	protected void onStart() {
   		super.onStart();
   		adapter.clear();
   		adapter.add("テストグループ0");
   	}
     
    private class CustomAdapter extends ArrayAdapter<String> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<String> arrayList;
	    private int viewResId = R.layout.item_group_list;
	    
	    public CustomAdapter (Context context, ArrayList<String> list) {
	        super(context, 0, list);
	        mycontext = context;
	        layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        arrayList = list;
	    }
	  
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View view = convertView;
	        if (view == null) {
	        	view = layoutInflater.inflate(viewResId, null);
	        }
	        
	        String name = arrayList.get(position);
	        final TextView myText = (TextView)view.findViewById(R.id.item_group_name);
	        
	        myText.setText(name);
	       
	        view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) { 
					Intent i = new Intent(TopActivity.this, ChatActivity.class);
					startActivity(i);
				}
				
			});
	        
	        return view;
	    }
	}
}
