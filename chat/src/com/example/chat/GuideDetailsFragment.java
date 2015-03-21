package com.example.chat;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuideDetailsFragment extends Fragment {
	private String guideDayId = "1";
	private String seasonId;
	
	private LayoutInflater layoutInflater;
	private Resources res;
	
	private View frgLayout;
	private LinearLayout mainLayout;
	
	private ArrayList<Route> myRoutes = new ArrayList<Route>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		frgLayout = inflater.inflate(R.layout.fragment_guide_details, container, false);	
        return frgLayout;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		res = getActivity().getResources();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Bundle bundle = getArguments();
	    guideDayId = bundle.getString(IntentKey.GUIDE_DAY_ID);
	    seasonId = bundle.getString(IntentKey.GUIDE_SEASON);
	    
	    mainLayout = (LinearLayout)frgLayout.findViewById(R.id.layout_main);
	    
	    PostServerTask task = new PostServerTask(URLManager.GET_ROUTE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader x = new XmlReader(this.httpData);
				ArrayList<Route> list = x.getRoute();
				if (list.size() > 0) {
					myRoutes = list;
					getAPIData(myRoutes);
					createRouteView();
				} else {
					createNoRouteView(seasonId);
				}
			}
			
		};
		task.setPostData("GuideDayId", guideDayId);
		task.execute();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	//ルート内のホテルデータを取得
	private void getAPIData(ArrayList<Route> list) {
		
		for (int i = 0; i < list.size(); i++) {
			final Route r = list.get(i);
			//kind = 0 : hotel , kind = 1 : spot, kind = 2 : gourmet.
			if (r.destPoint.kind == 0) {
				JalanApiURLCreator c = new JalanApiURLCreator();
				c.setHotelID(String.valueOf(r.destPoint.id));
				PostServerTask task = new PostServerTask(c.createHotelURL()){

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Hotel> list = xr.getHotelData();
						if (!list.isEmpty()) {
							Hotel h = list.get(0);
							r.destPoint = new Point(h);
						}
						createRouteView();
					}
					
				};
				task.execute();
			} else if (r.destPoint.kind == 2) {
				LocaTouchApiURLCreator c = new LocaTouchApiURLCreator();
				c.setId(String.valueOf(r.destPoint.id));
				PostServerTask task = new PostServerTask(c.createUrl()){

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Gourmet> list = xr.getGourmetLocaTouch();
						if (!list.isEmpty()) {
							Gourmet g = list.get(0);
							r.destPoint = new Point(g);
						}
						createRouteView();
					}
					
				};
				task.execute();
			}
			
		}
	}
	
	@SuppressLint("CutPasteId")
	private void createRouteView() {
		mainLayout.removeAllViews();
		
    	for (int i = 0; i < myRoutes.size(); i++) {
    		final Route route = myRoutes.get(i);
    		
    		View view = layoutInflater.inflate(R.layout.item_route2, null);
    		View kindLine = view.findViewById(R.id.view_line);
    		
    		TextView durText = (TextView)view.findViewById(R.id.text_duration);
    		durText.setText("距離:" + route.distance + "\n所要時間:約" + route.duration);
    		
    		TextView stayText = (TextView)view.findViewById(R.id.textView_time);
    		stayText.setText(route.startTime + "\n~\n" + route.endTime);
    		
    		TextView nameText = (TextView)view.findViewById(R.id.textView_name);
    		if (route.destPoint.name.equals("") || route.destPoint.name == null) { 
    			nameText.setText("読み込み中...");
    		} else {
    			nameText.setText(route.destPoint.name);
    		}
    		
    		if (i == 0) {
    			LinearLayout layout = (LinearLayout)view.findViewById(R.id.main_layout);
        		LinearLayout dlayout = (LinearLayout)view.findViewById(R.id.layout_duration);
        		stayText.setText(route.endTime + "\n\n出発");
        		layout.removeView(dlayout);
        		
        		FrameLayout layout2 = (FrameLayout)view.findViewById(R.id.layout_frame);
        		LinearLayout line = (LinearLayout)view.findViewById(R.id.layout_line);
        		layout2.removeView(line);
    		}
    		
    		if (i == myRoutes.size() - 1) {
    			stayText.setText(route.startTime + "\n\n到着");
    			LinearLayout layout2 = (LinearLayout)view.findViewById(R.id.layout_line);
        		View line = (View)view.findViewById(R.id.lineview);
        		layout2.removeView(line);
    		}
    		
    		if (route.destPoint.kind == 0) {
    			kindLine.setBackgroundColor(res.getColor(R.color.route_hotel));
    		} else if(route.destPoint.kind == 1) {
    			kindLine.setBackgroundColor(res.getColor(R.color.route_spot));
    		} else if(route.destPoint.kind == 2) {
    			kindLine.setBackgroundColor(res.getColor(R.color.route_gourmet));
    		}
    		
    		if (route.destPoint.kind == 0) {
    			view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(), HotelDialogActivity.class);
						int id = Integer.parseInt(route.destPoint.id);
						i.putExtra(IntentKey.HOTEL_ID, id);
						startActivity(i);
					}
				});
    		} else if(route.destPoint.kind == 1) {
    			view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(), SpotDialogActivity.class);
						int id = Integer.parseInt(route.destPoint.id);
						i.putExtra(IntentKey.SPOT_ID, id);
						startActivity(i);
					}
				});
    		} else if(route.destPoint.kind == 2) {
    			view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(), GourmetDialogActivity.class);
						i.putExtra(IntentKey.GOURMET_ID, route.destPoint.id);
						startActivity(i);
					}
				});
    		}
    		
    		mainLayout.addView(view);
    	}
    }
    
	private void createNoRouteView(String s) {
    	mainLayout.removeAllViews();
    	View view = layoutInflater.inflate(R.layout.item_no_route, null);
    	ImageView image = (ImageView)view.findViewById(R.id.imageView1);
    	if (s.equals("100")) {
        	image.setImageDrawable(res.getDrawable(R.drawable.spring_route));
        } else if (s.equals("200")) {
        	image.setImageDrawable(res.getDrawable(R.drawable.noroute));
        } else if (s.equals("300")) {
        	image.setImageDrawable(res.getDrawable(R.drawable.autumn2_route));
        } else if (s.equals("400")) {
        	image.setImageDrawable(res.getDrawable(R.drawable.winter_route));
        }
		mainLayout.addView(view);	
    }
}

