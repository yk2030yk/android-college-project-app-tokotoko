package com.example.chat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MapActivity extends FragmentActivity {
	private SupportMapFragment mapFragment;
	private GoogleMap map;
	private GoogleMapArea initArea;
	private Point point;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getActionBar().hide();
                     
        LinearLayout backButton = (LinearLayout)findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        mapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap(); 
        MapsInitializer.initialize(this);
       
        Intent i = getIntent();
        Spot spot = (Spot) i.getExtras().getSerializable("SPOT_DATA");
        Hotel hotel = (Hotel) i.getExtras().getSerializable("HOTEL_DATA");
        Gourmet gourmet = (Gourmet) i.getExtras().getSerializable("GOURMET_DATA");
        
        if (spot != null) {
        	point = new Point(spot);
        } else if (hotel != null){
        	point = new Point(hotel);
        } else if (gourmet != null){
        	point = new Point(gourmet);
        }
        
        getActionBar().setTitle(point.name);
        initArea = new GoogleMapArea(map, point);
        initArea.moveCamera();
        initArea.setMarker();
        
        map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				return false;
			}
		});
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }
           
    private class GoogleMapArea {
    	private GoogleMap map;
    	private Point point = null;
    	private LatLng location= null;
    	public Marker marker = null;
    	
    	public GoogleMapArea(GoogleMap map, Point point) {
    		this.map = map;
    		this.point = point;
    		this.location = new LatLng(point.lat, point.lng);
    	}
    	    	
    	public boolean setMarker() {
    		MarkerOptions options = new MarkerOptions();
            options.position(location);
            options.title(point.name);
            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            options.icon(icon);
            if (this.marker == null) {
            	this.marker = this.map.addMarker(options);
            	this.marker.showInfoWindow();
            	return true;
            } else {
            	return false;
            }
    	}
    	
    	public void moveCamera(){
    		CameraPosition camerapos = new CameraPosition.Builder()
            .target(location)
        	.zoom(13.0f)
        	.build();
            CameraUpdate camera = CameraUpdateFactory
    	    		.newCameraPosition(camerapos);
            this.map.animateCamera(camera);
            if (this.marker != null) {
            	this.marker.showInfoWindow();
            }
    	}
    }
}
