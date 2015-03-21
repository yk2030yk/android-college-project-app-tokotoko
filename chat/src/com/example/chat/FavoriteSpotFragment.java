package com.example.chat;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteSpotFragment extends Fragment {
	private Context context;
	private View frgLayout;
	private static MyCustomAdapter adapter;
    private ArrayList<HashMap<String, String>> spots = new ArrayList<HashMap<String, String>>();
	private ListView listView;
	private final String SAVE_KEY = "SPOTS";
	private Vibrator vibrator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		frgLayout = inflater.inflate(R.layout.fragment_favorite, container, false);
		return frgLayout;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity().getApplicationContext();
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		
		if (savedInstanceState != null) {
			spots = (ArrayList<HashMap<String, String>>)savedInstanceState.getSerializable(SAVE_KEY);
		}
	}
	
	public void onStart(){
		super.onStart();
		listView = (ListView)frgLayout.findViewById(R.id.listView1);
		adapter = new MyCustomAdapter(context, spots);
		listView.setAdapter(adapter);
		
		if (spots.isEmpty()) {
			initList();
		}
		
		FavoriteActivity act = (FavoriteActivity)getActivity();
		if (act.isDataChangedS) {
			initList();
			act.isDataChangedS = false;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVE_KEY, spots);
		Log.d("saveInstavceState", "ÉZÅ[ÉuÇ≥ÇÍÇΩÇÊ");
	}
	
	public void renew() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
	
	public void reload() {
		if (adapter != null && spots != null) {
			initList();
			FavoriteActivity act = (FavoriteActivity)getActivity();
			if (act != null) {
				act.isDataChangedH = false;
			}
		}
	}
	
	private void initList() {
		adapter.clear();
		
		PostServerTask psTask = new PostServerTask(URLManager.GET_FAVORITE_SPOT_URL) {
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				spots = xr.getFavoriteSpotId();
				
				adapter = new MyCustomAdapter(context, spots);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		};
		psTask.setPostData("group_id", 1);
		psTask.execute();
	}
	
	private class MyCustomAdapter extends ArrayAdapter<HashMap<String, String>> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<HashMap<String, String>> arrayList;
	    private int viewResId = R.layout.item_favorite;
	    
	    public MyCustomAdapter (Context context, ArrayList<HashMap<String, String>> list) {
	        super(context, 0, list);
	        mycontext = context;
	        layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        arrayList = list;
	    }
	  
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View v = convertView;
	        if (v == null) {
	        	v = layoutInflater.inflate(viewResId, null);
	        }
	        final View view = v;
	        
	        final HashMap<String, String> itemData = arrayList.get(position);
	        final TextView myText = (TextView)view.findViewById(R.id.item_favorite_content);
	        final TextView delText = (TextView)view.findViewById(R.id.item_favorite_del);
	        final ImageView myImage = (ImageView)view.findViewById(R.id.item_favorite_image);
	        myText.setText(itemData.get("Name"));
	        
	        Resources r = getResources();
	        Bitmap b = BitmapFactory.decodeResource(r, R.drawable.ic_noimage);
	        myImage.setImageBitmap(b);
	        
	        delText.setVisibility(View.GONE);
	        final FavoriteActivity act = (FavoriteActivity)getActivity();
	        view.setOnClickListener(new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
	        		if (!act.isEditMode) {
	        			String key = IntentKey.SPOT_ID; 
						Intent i = new Intent(context, SpotDialogActivity.class);
						int id = Integer.parseInt(itemData.get("SpotId"));
						i.putExtra(key, id);
						startActivity(i);	
	        		} else {
						vibrator.vibrate(20);
						if (act.spots.contains(itemData)) {
							view.setBackgroundColor(Color.parseColor("#00ffffff"));
							act.spots.remove(itemData);
						} else {
							view.setBackgroundColor(Color.parseColor("#88ff7f50"));
							act.spots.add(itemData);
						}
					}
				}
			});
	       
	        if (act.isEditMode) {
	        	view.setBackgroundColor(Color.parseColor("#00ffffff"));
	        }
	        
	        if (act.spots.contains(itemData)) {
	        	view.setBackgroundColor(Color.parseColor("#88ff7f50"));
			} else {
				view.setBackgroundColor(Color.parseColor("#00ffffff"));
			}
	        
	        return view;
	    }
	}
	
}