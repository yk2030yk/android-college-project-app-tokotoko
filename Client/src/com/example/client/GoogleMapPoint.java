package com.example.client;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapPoint {
	private GoogleMap map;
	public Point point = null;
	public LatLng location = null;
	public Marker marker = null;

	public GoogleMapPoint(GoogleMap map, Point point) {
		this.map = map;
		this.point = point;
		this.location = new LatLng(point.lat, point.lng);
	}

	public boolean setMarker() {
		MarkerOptions options = new MarkerOptions();
		options.position(location);
		options.title(point.name);
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
		options.icon(icon);
		if (this.marker == null) {
			this.marker = this.map.addMarker(options);
			this.marker.showInfoWindow();
			return true;
		} else {
			return false;
		}
	}

	public boolean setMarker(float iconColor) {
		MarkerOptions options = new MarkerOptions();
		options.position(location);
		options.title(point.name);
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(iconColor);
		options.icon(icon);
		if (this.marker == null) {
			this.marker = this.map.addMarker(options);
			this.marker.showInfoWindow();
			return true;
		} else {
			return false;
		}
	}

	public void moveCamera() {
		CameraPosition camerapos = new CameraPosition.Builder().target(location).zoom(13.0f).build();
		CameraUpdate camera = CameraUpdateFactory.newCameraPosition(camerapos);
		this.map.animateCamera(camera);
		if (this.marker != null) {
			this.marker.showInfoWindow();
		}
	}
}