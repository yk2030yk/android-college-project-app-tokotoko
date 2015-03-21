package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

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

public class FavoriteSelectHotelFragment extends Fragment {
	private Context context;
	private ArrayList<Point> hotels = new ArrayList<Point>();
	private static MyCustomAdapter adapter;
	private View frgLayout;
	private ListView listView;
	private final String SAVE_KEY = "HOTELS";
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
			hotels = (ArrayList<Point>)savedInstanceState.getSerializable(SAVE_KEY);
		}
	}
	
	public void onStart(){
		super.onStart();
		listView = (ListView)frgLayout.findViewById(R.id.listView1);
		adapter = new MyCustomAdapter(context, hotels);
		listView.setAdapter(adapter);
		
		if (hotels.isEmpty()) {
			PostServerTask task = new PostServerTask(URLManager.GET_FAVORITE_HOTEL_URL) {
	
				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					XmlReader xr = new XmlReader(this.httpData);
					ArrayList<HashMap<String, String>> list = xr.getFavoriteHotelId();
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, String> h = list.get(i);
						Hotel hotel =  new Hotel();
						hotel.hotelID = Integer.parseInt(h.get("HotelId"));
						hotel.name = h.get("Name");
						hotel.lat = Double.parseDouble(h.get("Lat"));
						hotel.lng = Double.parseDouble(h.get("Lng"));
						adapter.add(new Point(hotel));
					}
				}
				
			};
			task.execute();	
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVE_KEY, hotels);
		Log.d("saveInstavceState", "ÉZÅ[ÉuÇ≥ÇÍÇΩÇÊ");
	}
		
	private class MyCustomAdapter extends ArrayAdapter<Point> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<Point> arrayList;
	    private int viewResId = R.layout.item_select_favorite;
	    private ImageFileHelper imageFileHelper;
	    private int iconSize;
	    
	    public MyCustomAdapter (Context context, ArrayList<Point> list) {
	        super(context, 0, list);
	        mycontext = context;
	        layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        arrayList = list;
	        imageFileHelper = new ImageFileHelper(context);
	        iconSize = ImageLoadTask.getPxFromDp(context, 48);
	    }
	  
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View view = convertView;
	        if (view == null) {
	        	view = layoutInflater.inflate(viewResId, null);
	        }
	        
	        final Point itemData = arrayList.get(position);
	        final TextView nameText = (TextView)view.findViewById(R.id.item_favorite_content);
	        final TextView addButton = (TextView)view.findViewById(R.id.item_favorite_del);
	        final ImageView image = (ImageView)view.findViewById(R.id.item_favorite_image);
	        nameText.setText(itemData.name);
	        
	        String fileName = Hotel.HOTEL_IMAGE_FILE_NAME + itemData.id;
        	Bitmap bitmap = imageFileHelper.loadBitmap(fileName, iconSize, iconSize);
	        if (bitmap != null) {
	        	image.setImageBitmap(bitmap);
	        } else {
	        	loadImage(itemData.id, fileName); 
	        }
	        
	        final FavoriteSelectActivity activity = (FavoriteSelectActivity) getActivity();
	        if (activity.routePoints.contains(itemData)) {
	        	int rank = activity.routePoints.indexOf(itemData) + 1;
	        	addButton.setText("" + rank);
	        } else {
	        	addButton.setText("");
	        }
	        
	        addButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					vibrator.vibrate(10);
					if (activity.routePoints.contains(itemData)) {
						activity.routePoints.remove(itemData);
			        } else {
			        	activity.routePoints.add(itemData);
			        }
					adapter.notifyDataSetChanged();
				}
			});
	        
	        nameText.setOnClickListener(new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
	        		Intent i = new Intent(context, HotelDialogActivity.class);
					i.putExtra(IntentKey.HOTEL_ID, Integer.parseInt(itemData.id));
					startActivity(i);
				}
			});
	        
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
