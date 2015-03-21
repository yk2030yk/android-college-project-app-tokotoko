package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectGuideHotelActivity extends Activity {
	private Context context;
	private LayoutInflater layoutInflater;
	
	private ListView listView;
	private LinearLayout searchLayout;
	private Button mode1;
	private Button mode2;
	private View searchView;
	private EditText searchText;
	private Button searchButton;
	
	private ListAdapter adapter;
	private Loading loading;
	private Loading loadingSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_guide_hotel);
		context = this;
		layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		listView = (ListView)findViewById(R.id.listView_hotel);
		searchLayout = (LinearLayout)findViewById(R.id.layout_search);
		mode1 = (Button)findViewById(R.id.button_mode1);
		mode2 = (Button)findViewById(R.id.button_mode2);
		mode1.setEnabled(false);
		mode2.setEnabled(true);
		adapter = new ListAdapter(context, new ArrayList<HashMap<String, String>>());
		
		mode1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mode1.setEnabled(false);
				mode2.setEnabled(true);
				searchLayout.removeAllViews();
				initFavorite();
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		
		mode2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mode1.setEnabled(true);
				mode2.setEnabled(false);
				searchLayout.addView(searchView);
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		
		searchView = (View)layoutInflater.inflate(R.layout.item_menu_search, null);
		searchText = (EditText)searchView.findViewById(R.id.editText_search);
		searchButton = (Button)searchView.findViewById(R.id.button_search);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String k = searchText.getText().toString();
				initSearch(k);
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		
		loading = new Loading(this, "読み込み中...");
		loadingSearch = new Loading(this, "検索中...");
		
		initFavorite();
	}
	
	private void initFavorite() {
		loading.show();
		adapter.clear();
		
		PostServerTask hotelTask = new PostServerTask(URLManager.GET_FAVORITE_HOTEL_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<HashMap<String, String>> list = xr.getFavoriteHotelId();
				adapter = new ListAdapter(context, list);
				listView.setAdapter(adapter);
				loading.hide();
			}
			
		};
		hotelTask.execute();
	}
	
	void initSearch(String keyword) {
    	adapter.clear();
    	loadingSearch.show();
    	
    	JalanApiURLCreator c = new JalanApiURLCreator();
    	c.setHotelName(keyword);
    	String url = c.createHotelURL();
    	PostServerTask task = new PostServerTask(url) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Hotel> list = xr.getHotelData();
				ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
				adapter = new ListAdapter(context, maps);
				listView.setAdapter(adapter);
				
				if (list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("FavoriteHotelId", "");
						map.put("HotelId", String.valueOf(list.get(i).hotelID));
						map.put("Name", list.get(i).name);
						map.put("Lat", String.valueOf(list.get(i).lat));
						map.put("Lng", String.valueOf(list.get(i).lng));
						adapter.add(map);
					}
				} else {
					
				}
				
				adapter.notifyDataSetChanged();
				loadingSearch.hide();
			}
    		
    	};
    	task.execute();
    }
	
	private class ListAdapter extends ArrayAdapter<HashMap<String, String>> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<HashMap<String, String>> arrayList;
	    private int viewResId = R.layout.item_select_guide_hotel;
	    
	    public ListAdapter (Context context, ArrayList<HashMap<String, String>> list) {
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
	        
	        final TextView nameText = (TextView)view.findViewById(R.id.textView1);
	        final HashMap<String, String> map = arrayList.get(position);
	        
	        nameText.setText(map.get("Name"));
	        nameText.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!map.get("HotelId").equals("-1")) {
						Intent i = new Intent();
						i.putExtra(IntentKey.HOTEL_ID, map.get("HotelId"));
						i.putExtra(IntentKey.HOTEL_NAME, map.get("Name"));
						setResult(RESULT_OK, i);
					    finish();
				    }
				}
			});
	        
	        return view;
	    }
	}

	
}
