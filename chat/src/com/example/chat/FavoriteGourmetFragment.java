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

public class FavoriteGourmetFragment extends Fragment {
	private Context context;
	private ArrayList<HashMap<String, String>> gourmets = new ArrayList<HashMap<String, String>>();
	private static MyCustomAdapter adapter;
	private View frgLayout;
	private ListView listView;
	private final String SAVE_KEY = "GROUMETS";
	private Vibrator vibrator;
	private  ImageFileHelper imageFileHelper;
	
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
		imageFileHelper = new ImageFileHelper(context);
		
		if (savedInstanceState != null) {
			gourmets = (ArrayList<HashMap<String, String>>)savedInstanceState.getSerializable(SAVE_KEY);
		}
	}
	
	public void onStart(){
		super.onStart();
		listView = (ListView)frgLayout.findViewById(R.id.listView1);
		adapter = new MyCustomAdapter(context, gourmets);
		listView.setAdapter(adapter);
		
		if (gourmets.isEmpty()) {
			initList();
		}
		
		final FavoriteActivity act = (FavoriteActivity)getActivity();
		if (act.isDataChangedG) {
			initList();
			act.isDataChangedG = false;
		}
	}
	
	void initList() {
		PostServerTask task = new PostServerTask(URLManager.GET_FAVORITE_GOURMET_URL) {
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				gourmets = xr.getFavoriteGourmetId();
				adapter = new MyCustomAdapter(context, gourmets);
				listView.setAdapter(adapter);
			}
		};
		task.execute();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVE_KEY, gourmets);
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
				act.isDataChangedG = false;
			}
		}
	}
	
	private class MyCustomAdapter extends ArrayAdapter<HashMap<String, String>> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<HashMap<String, String>> arrayList;
	    private int viewResId = R.layout.item_favorite;
	    private int iconSize;
	    
	    public MyCustomAdapter(Context context, ArrayList<HashMap<String, String>> list) {
	        super(context, 0, list);
	        mycontext = context;
	        layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        arrayList = list; 
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
	        final TextView nameText = (TextView)view.findViewById(R.id.item_favorite_content);
	        final TextView delText = (TextView)view.findViewById(R.id.item_favorite_del);
	        
	        final ImageView image = (ImageView)view.findViewById(R.id.item_favorite_image);
	        nameText.setText(itemData.get("Name"));
	        
	        String fileName = Gourmet.GOURMET_IMAGE_FILE_NAME + itemData.get("GourmetId");
        	Bitmap bitmap = imageFileHelper.loadBitmap(fileName, iconSize, iconSize);
	        if (bitmap != null) {
	        	image.setImageBitmap(bitmap);
	        } else {
	        	loadImage(itemData.get("GourmetId"), fileName); 
	        }
	        
	        delText.setVisibility(View.GONE);
	        final FavoriteActivity act = (FavoriteActivity)getActivity();
	        view.setOnClickListener(new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
	        		if (!act.isEditMode) {
						Intent i = new Intent(context, GourmetDialogActivity.class);
						i.putExtra(IntentKey.GOURMET_ID, itemData.get("GourmetId"));
						startActivity(i);
	        		} else {
						vibrator.vibrate(20);
						if (act.gourmets.contains(itemData)) {
							view.setBackgroundColor(Color.parseColor("#00ffffff"));
							act.gourmets.remove(itemData);
						} else {
							view.setBackgroundColor(Color.parseColor("#88ff7f50"));
							act.gourmets.add(itemData);
						}
					}
				}
			});
	       
	        if (act.isEditMode) {
	        	view.setBackgroundColor(Color.parseColor("#00ffffff"));
	        }
	        
	        if (act.gourmets.contains(itemData)) {
	        	view.setBackgroundColor(Color.parseColor("#88ff7f50"));
			} else {
				view.setBackgroundColor(Color.parseColor("#00ffffff"));
			}
	        
	        return view;
	    }
	    
	    private void loadImage(String id, String f) {
			final String fileName = f;
			
			LocaTouchApiURLCreator c = new LocaTouchApiURLCreator();
			c.setId(id);
			String url = c.createUrl();
			
			PostServerTask task = new PostServerTask(url){

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					XmlReader xr = new XmlReader(this.httpData);
					ArrayList<Gourmet> list = xr.getGourmetLocaTouch();
					if (!list.isEmpty()) {
						Gourmet g = list.get(0);
						ImageLoadTask load = new ImageLoadTask(g.imageURL, context) {
							@Override
							protected void onPostExecute(Boolean result) {
								super.onPostExecute(result);
								saveLocalFile(fileName);
								adapter.notifyDataSetChanged();
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
