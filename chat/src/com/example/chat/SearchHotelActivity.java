package com.example.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchHotelActivity extends Activity {
	ListView listView;
	EditText et;
	Context context;
	ArrayList<Hotel> list = new ArrayList<Hotel>();
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getActionBar().setTitle("èhåüçı");
		context = this;
		listView = (ListView)findViewById(R.id.listView1);
		et = (EditText)findViewById(R.id.editText1);
		Button search = (Button)findViewById(R.id.button1);
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String key = et.getText().toString();
				if (!key.equals("")) {
					initList(key);
					et.setText("");
				}
				
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		
		initList("ãûìs");
	}
    
    void initList(String keyword) {
    	list.clear();
    	list.add(new Hotel("Åu" + keyword + "ÅvÇÃåüçıåãâ "));
    	
    	JalanApiURLCreator c = new JalanApiURLCreator();
    	c.setHotelName(keyword);
    	String url = c.createHotelURL();
    	PostServerTask task = new PostServerTask(url) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Hotel> list2 = xr.getHotelData();
				list.addAll(list2);
				if (list.size() == 1) {
					Hotel h = new Hotel("åüçıåãâ Ç™0åèÇ≈Ç∑");
					list.add(h);
				}
				CustomAdapter adapter = new CustomAdapter(context, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
    		
    	};
    	task.execute();
    }
    
    void addFavorite(int id) {
    	PostServerTask task = new PostServerTask(URLManager.ADD_FAVORITE_URL_HOTEL) {
    		
    		@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (this.taskResult) {
					Toast.makeText(context, "ìoò^ÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "ìoò^ÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
				}
			}
    	};
    	task.setPostData("HotelId", id);
    	task.execute();
    }
    
    void showMyDialog(int id) {
    	final int hId = id;
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Ç®ãCÇ…ì¸ÇË");
        alertDialogBuilder.setPositiveButton("ìoò^Ç∑ÇÈ", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		addFavorite(hId);
        	}
        });
        alertDialogBuilder.setNegativeButton("ÉLÉÉÉìÉZÉã", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        	}
        });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    
    private class CustomAdapter extends ArrayAdapter<Hotel> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<Hotel> arrayList;
	    int viewResId = R.layout.item_drawer_favorite;
	    
	    public CustomAdapter (Context context, ArrayList<Hotel> list) {
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
	        
	        final Hotel itemData = arrayList.get(position);
	        TextView myText = (TextView)view.findViewById(R.id.textView_name);
	        myText.setText(itemData.name);
	        
	        myText.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(SearchHotelActivity.this, HotelDialogActivity.class);
					i.putExtra(IntentKey.HOTEL_ID, itemData.hotelID);
					startActivity(i);
				}
			});
	       
	        return view;
	    }
	} 
    
}
