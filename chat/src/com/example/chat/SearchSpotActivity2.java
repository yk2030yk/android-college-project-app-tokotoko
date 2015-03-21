package com.example.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SearchSpotActivity2 extends Activity {
	private Context context;
	private ListView listView;
	private ArrayList<Spot> list = new ArrayList<Spot>();
	int scnt = 1;
	Loading load;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search2);
		getActionBar().hide();
		
		context = this;
		listView = (ListView)findViewById(R.id.listView1);
		Button pre = (Button)findViewById(R.id.button1);
		Button next = (Button)findViewById(R.id.button2);
		load = new Loading(this, "éÊìæíÜ...");
		load.show();
		
		pre.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (scnt != 1) {
					scnt--;
				}
				initList(String.valueOf(scnt));
			}
		});
		
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scnt++;
				initList(String.valueOf(scnt));
			}
		});
		
		initList(String.valueOf(scnt));
	}
    
    void initList(String keyword) {
    	list.clear();
    	list.add(new Spot("" + keyword + "page"));
    	load.show();
    	
    	PostServerTask task = new PostServerTask(URLManager.GET_SEARCH_SPOT_ALL_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Spot> list2 = xr.getSpotData();
				list.addAll(list2);
				if (list.size() == 1) {
					list.add(new Spot("åüçıåãâ Ç™0åèÇ≈Ç∑"));
				}
				CustomAdapter adapter = new CustomAdapter(context, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				load.hide();
			}
    		
    	};
    	task.setPostData("SpotName", keyword);
    	task.execute();
    }
    
    
    private class CustomAdapter extends ArrayAdapter<Spot> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<Spot> arrayList;
	    int viewResId = R.layout.item_drawer_favorite;
	    
	    public CustomAdapter (Context context, ArrayList<Spot> list) {
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
	        
	        final Spot itemData = arrayList.get(position);
	        TextView myText = (TextView)view.findViewById(R.id.textView_name);
	        myText.setText(itemData.name);
	        
	        myText.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (itemData.spotID != -1) {
						Intent i = new Intent(SearchSpotActivity2.this, SpotDialogActivity.class);
						i.putExtra(IntentKey.SPOT_ID, itemData.spotID);
						startActivity(i);
					}
				}
			});
	       
	        return view;
	    }
	} 
}
