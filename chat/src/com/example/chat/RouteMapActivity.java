package com.example.chat;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;

public class RouteMapActivity  extends FragmentActivity {
    private GoogleMap map;
    private ListView list;
    private int searchCount = 0;
    private ArrayList<Route> myRoutes = new ArrayList<Route>();
    private ArrayList<GoogleMapPoint> points = new ArrayList<GoogleMapPoint>();
    private ArrayList<ArrayList<LatLng>> routeLatLng = new ArrayList<ArrayList<LatLng>>();
    private ArrayList<Polyline> lines = new ArrayList<Polyline>();
    private CustomAdapter adapter;
    private String guideDayId = "";
    private GoogleMapPoint startPoint;
    private GoogleMapPoint endPoint;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        getActionBar().hide();
        
        SupportMapFragment mapfragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapfragment.getMap();
        list = (ListView)findViewById(R.id.listView1);
        adapter = new CustomAdapter(this, points);
        list.setAdapter(adapter);
        guideDayId = getIntent().getExtras().getString(IntentKey.GUIDE_DAY_ID);
        
        PostServerTask task = new PostServerTask(URLManager.GET_ROUTE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader x = new XmlReader(this.httpData);
				ArrayList<Route> list = x.getRoute();
				if (list.size() > 0) {
					myRoutes = list;
					routeSearch(myRoutes.get(0).destPoint, myRoutes.get(1).destPoint);
				} else {
					
				}
			}
			
		};
		task.setPostData("GuideDayId", guideDayId);
		task.execute();
    }
    
    private void routeSearch(Point origin, Point dest) {
    	final GoogleMapPoint gpStart = new GoogleMapPoint(map, origin);
    	adapter.add(gpStart);
    	
    	if (startPoint == null) {
    		startPoint = gpStart;
    		startPoint.moveCamera();
    	}
    	
    	final GoogleMapPoint gpEnd = new GoogleMapPoint(map, dest);
    	endPoint = gpEnd;
    	
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
					XmlReader xr = new XmlReader(httpData);
					Route route = xr.getGoogleMapRoute();
					
					routeLatLng.add(route.routeLatLng);
					drawRoute(0);
					
					searchCount++;
					
					if (searchCount < (myRoutes.size() - 1)) {
						routeSearch(myRoutes.get(searchCount).destPoint, myRoutes.get(searchCount + 1).destPoint);
					} else {
						adapter.add(endPoint);
						
						for (int i = 0 ; i < adapter.getCount(); i++) {
							GoogleMapPoint p = adapter.getItem(i);
							p.setMarker(BitmapDescriptorFactory.HUE_RED);		
						}
					}
				}
			}
        	
        };
        task.execute();
        
    }
    
    private final String lineColorDefault = "#4682b4";
    private final String lineColorSelect = "#ff0000";
    
    private void drawRoute(int pos) {
    	for (int i = 0 ; i < lines.size(); i++) {
    		lines.get(i).remove();
    	}
    	
    	lines.clear();
    	
    	PolylineOptions selectLine = null;
    	for (int i = 0; i < routeLatLng.size(); i++) {
    		int color = Color.parseColor(lineColorDefault);
    		if (pos != 0 && (pos - 1) == i) {
    			color = Color.parseColor(lineColorSelect);
    		}
	    	PolylineOptions lineOptions = new PolylineOptions();
			ArrayList<LatLng> points = routeLatLng.get(i);
			lineOptions.addAll(points);
			lineOptions.color(color);
			lineOptions.width(7);
			
			if (pos != 0 && (pos - 1) == i) {
				selectLine = lineOptions;
    		}
			
			lines.add(map.addPolyline(lineOptions));
		}
    	
    	if (selectLine != null) {
    		lines.add(map.addPolyline(selectLine));
    	}
    }
    
    private String getGoogleMapApiUrl(LatLng origin, LatLng dest){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String travelMode = "driving";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=ja" + "&mode=" + travelMode; 
        String url = "https://maps.googleapis.com/maps/api/directions/xml?" + parameters;
        Log.d("URL GOOGLE MAP XML", url);
        return url;
    }
    
    private class CustomAdapter extends ArrayAdapter<GoogleMapPoint> {
		public Context mycontext;
	    private LayoutInflater layoutInflater;
	    private ArrayList<GoogleMapPoint> arrayList;
	    private int viewResId = R.layout.item_map;
	    
	    public CustomAdapter(Context context, ArrayList<GoogleMapPoint> list) {
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
	        
	        final int pos = position;
	        final GoogleMapPoint item = arrayList.get(position);
	        TextView t = (TextView)view.findViewById(R.id.textView1);
	        t.setText((position + 1) + " : " +item.point.name);
	        view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					item.moveCamera();
					drawRoute(pos);
				}
			});
	        
	        return view;
	    }
	 }
}