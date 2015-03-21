package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class FavoriteHotelFragment extends Fragment {
	private Context context;
	private ArrayList<HashMap<String, String>> hotels = new ArrayList<HashMap<String, String>>();
	private static MyCustomAdapter adapter;
	private View frgLayout;
	private ListView listView;
	final String SAVE_KEY = "HOTELS";
	Vibrator vibrator;
	
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
			hotels = (ArrayList<HashMap<String, String>>)savedInstanceState.getSerializable(SAVE_KEY);
		}
	}
	
	public void onStart(){
		super.onStart();
		listView = (ListView)frgLayout.findViewById(R.id.listView1);
		adapter = new MyCustomAdapter(context, hotels);
		listView.setAdapter(adapter);
		adapter.clear();
		
		if (hotels.isEmpty()) {
			initList();
		}
		
		FavoriteActivity act = (FavoriteActivity)getActivity();
		if (act.isDataChangedG) {
			initList();
			act.isDataChangedH = false;
		}
	}
	
	void initList() {
		PostServerTask task = new PostServerTask(URLManager.GET_FAVORITE_HOTEL_URL) {
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				hotels = xr.getFavoriteHotelId();
				adapter = new MyCustomAdapter(context, hotels);
				listView.setAdapter(adapter);
			}
			
		};
		task.execute();	
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVE_KEY, hotels);
		Log.d("saveInstavceState", "ÉZÅ[ÉuÇ≥ÇÍÇΩÇÊ");
	}
	
	public void renew() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
	
	public void reload() {
		if (adapter != null) {
			initList();
			FavoriteActivity act = (FavoriteActivity)getActivity();
			if (act != null) {
				act.isDataChangedH = false;
			}
		}
	}
		
	private class MyCustomAdapter extends ArrayAdapter<HashMap<String, String>> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<HashMap<String, String>> arrayList;
	    int viewResId = R.layout.item_favorite;
	    MyCustomAdapter adapter;
	    ImageFileHelper imageFileHelper;
	    int iconSize;
	    
	    public MyCustomAdapter (Context context, ArrayList<HashMap<String, String>> list) {
	        super(context, 0, list);
	        mycontext = context;
	        layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        arrayList = list;
	        adapter = this;
	        imageFileHelper = new ImageFileHelper(context);
	        iconSize = ImageLoadTask.getPxFromDp(context, 48);
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
	        final ImageView image = (ImageView)view.findViewById(R.id.item_favorite_image);
	        myText.setText(itemData.get("Name"));
	        
	        String fileName = Hotel.HOTEL_IMAGE_FILE_NAME + itemData.get("HotelId");
        	Bitmap bitmap = imageFileHelper.loadBitmap(fileName, iconSize, iconSize);
	        if (bitmap != null) {
	        	image.setImageBitmap(bitmap);
	        } else {
	        	loadImage(itemData.get("HotelId"), fileName); 
	        }
	        
	        delText.setVisibility(View.GONE);
	        final FavoriteActivity act = (FavoriteActivity)getActivity();
	        view.setOnClickListener(new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
	        		if (!act.isEditMode) {
						Intent i = new Intent(context, HotelDialogActivity.class);
						i.putExtra(IntentKey.HOTEL_ID, Integer.parseInt(itemData.get("HotelId")));
						startActivity(i);
	        		} else {
						vibrator.vibrate(20);
						if (act.hotels.contains(itemData)) {
							view.setBackgroundColor(Color.parseColor("#00ffffff"));
							act.hotels.remove(itemData);
						} else {
							view.setBackgroundColor(Color.parseColor("#88ff7f50"));
							act.hotels.add(itemData);
						}
					}
				}
			});
	       
	        if (act.isEditMode) {
	        	view.setBackgroundColor(Color.parseColor("#00ffffff"));
	        }
	        
	        if (act.hotels.contains(itemData)) {
	        	view.setBackgroundColor(Color.parseColor("#88ff7f50"));
			} else {
				view.setBackgroundColor(Color.parseColor("#00ffffff"));
			}
	        
	        //final FavoriteActivity act = (FavoriteActivity)getActivity();
	        //delText.setOnClickListener(new View.OnClickListener() {
				
				//@Override
				//public void onClick(View v) {
					//vibrator.vibrate(20);
					//if (act.hotels.contains(itemData)) {
						//delText.setText("");
						//act.hotels.remove(itemData);
					//} else {
						//delText.setText("Åù");
						//act.hotels.add(itemData);
					//}
				//}
			//});
	        
	        //if (act.hotels.contains(itemData)) {
				//delText.setText("Åù");
			//} else {
				//delText.setText("");
			//}
	        
	        //if (act.isEditMode) {
	        	//delText.setVisibility(View.VISIBLE);
	        //} else {
	        	//delText.setVisibility(View.GONE);
	        //}
	        
	        return view;
	    }  
	    
	    private void loadImage(String id, String f) {
			final String fileName = f;
			
			JalanApiURLCreator c = new JalanApiURLCreator();
			c.setHotelID(id);
			String url = c.createHotelURL();
			
			PostServerTask task = new PostServerTask(url){

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					XmlReader xr = new XmlReader(this.httpData);
					ArrayList<Hotel> list = xr.getHotelData();
					if (!list.isEmpty()) {
						Hotel h = list.get(0);
						ImageLoadTask load = new ImageLoadTask(h.imageUrl, context) {
							@Override
							protected void onPostExecute(Boolean result) {
								super.onPostExecute(result);
								if (this.bitmap != null) {
									saveLocalFile(fileName);
									adapter.notifyDataSetChanged();
								}
							}
						};
						load.execute();
					}
				}
			};
			task.execute();
		}
	}
	
}
