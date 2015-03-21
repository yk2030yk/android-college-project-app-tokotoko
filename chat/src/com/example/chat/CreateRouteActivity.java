package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateRouteActivity extends Activity {
	private String guideDayId = "-1";
    private String guideId = "-1";
    
	public Context context;
    private LayoutInflater layoutInflater;
    private Resources res;
    
    private LinearLayout mainLayout;
    
    public Loading loading;
    
    private int searchCount = 0;
    private int travelTime = 60 * 60 * 9;
    private ArrayList<Point> selectPoints = new ArrayList<Point>();
    private ArrayList<Point> routePoints = new ArrayList<Point>();
    private ArrayList<Route> results = new ArrayList<Route>();
    private ArrayList<Route> myRoutes = new ArrayList<Route>();
    
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);
        getActionBar().hide();
        
        context = this;
        layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        res = getResources();
        
        selectPoints = (ArrayList<Point>)getIntent().getSerializableExtra(IntentKey.LIST_DATA);
        guideDayId = getIntent().getExtras().getString(IntentKey.GUIDE_DAY_ID);
        guideId = getIntent().getExtras().getString(IntentKey.GUIDE_ID);
        
        mainLayout = (LinearLayout)findViewById(R.id.layout_main);
        
        loading = new Loading(this, "ÉãÅ[ÉgÇçÏê¨ÇµÇƒÇ‹Ç∑...");
        
        Button registerButton = (Button)findViewById(R.id.button_create);
        registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (results.size() > 0) {
					
					PostServerTask task = new PostServerTask(URLManager.REGISTER_ROUTE_URL) {
						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							if (this.taskResult) {
								Toast.makeText(context, "ìoò^Ç…ê¨å˜ÇµÇ‹ÇµÇΩyo", Toast.LENGTH_SHORT).show();
								setResult(RESULT_OK);
							    finish();
							} else {
								Toast.makeText(context, "ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
							}
						}
					};
					task.setPostData("guideDayId", guideDayId);
					for (int i = 0; i < myRoutes.size(); i++) {
							Route r = myRoutes.get(i);
							task.setPostData("priority[]", i);
							task.setPostData("spotId[]", r.destPoint.id);
							task.setPostData("Name[]", r.destPoint.name);
							task.setPostData("Lat[]", r.destPoint.lat);
							task.setPostData("Lng[]", r.destPoint.lng);
							task.setPostData("startTime[]", r.startTime);
							task.setPostData("endTime[]", r.endTime);
							task.setPostData("startTimeSec[]", r.startTimeSec);
							task.setPostData("endTimeSec[]", r.endTimeSec);
							task.setPostData("stayTimeSec[]", r.stayTimeSec);
							task.setPostData("distance[]", r.distance);
							task.setPostData("duration[]", r.duration);
							task.setPostData("durationSec[]", r.durationSec);
							task.setPostData("kind[]", r.destPoint.kind);
						}
					
					task.execute();
				}
			}
		});
           
        PostServerTask task = new PostServerTask(URLManager.GET_GUIDE_VALUE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				HashMap<String, String> map = xr.getGuideId().get(0);
				String hotelId = map.get("HotelId");
				
				JalanApiURLCreator urlcreator = new JalanApiURLCreator();
				urlcreator.setHotelID(hotelId);
				
				PostServerTask task = new PostServerTask(urlcreator.createHotelURL()) {

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Hotel> list = xr.getHotelData();
						if (list.size() != 0) {
							Hotel h = list.get(0);
							routePoints.add(new Point(h));
							routePoints.addAll(selectPoints);
							routePoints.add(new Point(h));
							
							if (routePoints.size() >= 3) {
					        	routeSearch(routePoints.get(0), routePoints.get(1));
					        } else {
					        	createNoRouteView();
					        }
						}
					}	
				};
				task.execute();
			}
			
		};
		task.setPostData("GuideId", guideId);
		task.execute();
    }
    
    private void routeSearch(Point origin, Point dest) {
    	loading.show();
    	
        final Point oPoint = origin;
        final Point dPoint = dest;
        LatLng oLocation = new LatLng(oPoint.lat, oPoint.lng);
        LatLng dLocation = new LatLng(dPoint.lat, dPoint.lng);
        String url = getGoogleMapApiUrl(oLocation, dLocation);
        PostServerTask task = new PostServerTask(url) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (httpData != null) {
					XmlReader r = new XmlReader(httpData);
					Route route = r.getGoogleMapRoute();
					route.originPoint = oPoint;
					route.destPoint = dPoint;
					results.add(route);
					searchCount++;
					
					if (searchCount < (routePoints.size() - 1)) {
						routeSearch(routePoints.get(searchCount), routePoints.get(searchCount + 1));
					} else {
						createRouteList();
						createRouteView();
						loading.hide();
					}
				}
			}
        	
        };
        task.execute();
    }

    private String getGoogleMapApiUrl(LatLng origin, LatLng dest){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String travelMode = "driving";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=ja" + "&mode=" + travelMode; 
        String url = "https://maps.googleapis.com/maps/api/directions/xml?" + parameters;
        Log.d("URL XML", url);
        return url;
    }
    
    private void createRouteList() {
    	travelTime = 9 * 60 * 60;
    	myRoutes.clear();
    	
    	for (int i = 0; i < results.size(); i++) {
    		Route route = results.get(i);
    		if (i == 0) {
    			Route sRoute = new Route();
    			sRoute.originPoint = null;
    			sRoute.destPoint = route.originPoint;
    			sRoute.duration = "0";
    			sRoute.distance = "0";
    			sRoute.durationSec = 0;
    			sRoute.startTime = "";
    			sRoute.startTimeSec = 0;
    			sRoute.stayTimeSec = 0;
    			sRoute.endTime = getTimeStr(sRoute.stayTimeSec);
        		sRoute.endTimeSec = travelTime;
    			myRoutes.add(sRoute);
    		}
    		route.startTime = getTimeStr(route.durationSec);
    		route.startTimeSec = travelTime;
    		route.endTime = getTimeStr(route.stayTimeSec);
    		route.endTimeSec = travelTime;
    		myRoutes.add(route);
    	}
    }
    
    @SuppressLint("CutPasteId")
	private void createRouteView() {
    	for (int i = 0; i < myRoutes.size(); i++) {
    		Route route = myRoutes.get(i);
    		View view = layoutInflater.inflate(R.layout.item_route, null);
    		
    		View kindLine = (View)view.findViewById(R.id.view_line);
    		
    		TextView durText = (TextView)view.findViewById(R.id.text_duration);
    		durText.setText("ãóó£:" + route.distance + "\nèäóvéûä‘:ñÒ" + route.duration);
    		
    		TextView stayText = (TextView)view.findViewById(R.id.textView_time);
    		stayText.setText(route.startTime + "\n~\n" + route.endTime);
    		
    		TextView nameText = (TextView)view.findViewById(R.id.textView_name);
    		nameText.setText(route.destPoint.name);
    		
    		if (i == 0) {
    			LinearLayout layout = (LinearLayout)view.findViewById(R.id.main_layout);
        		LinearLayout dlayout = (LinearLayout)view.findViewById(R.id.layout_duration);
        		stayText.setText(route.endTime + "\n\nèoî≠");
        		layout.removeView(dlayout);
        		
        		FrameLayout layout2 = (FrameLayout)view.findViewById(R.id.layout_frame);
        		LinearLayout line = (LinearLayout)view.findViewById(R.id.layout_line);
        		layout2.removeView(line);
    		}
    		
    		if (i == myRoutes.size() - 1) {
    			stayText.setText(route.startTime + "\n\nìûíÖ");
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
    		
    		mainLayout.addView(view);
    	}
    }
    
    private void createNoRouteView() {
    	View view = layoutInflater.inflate(R.layout.item_no_route, null);
		mainLayout.addView(view);
    }
    
    private String getTimeStr(int dur) {
    	String timeStr = "";
    	travelTime += dur;
    	int h = (travelTime / 60) / 60;
    	int m = (travelTime / 60) % 60;
    	h = h % 24;
    	if (h < 10) {
    		timeStr += "0";
    	}
    	timeStr +=  String.valueOf(h);
    	timeStr += " : ";
    	if (m < 10) {
    		timeStr += "0";
    	}
    	timeStr += String.valueOf(m);
    	
    	return timeStr;
    }
    
}
