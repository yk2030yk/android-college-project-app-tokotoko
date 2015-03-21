package com.example.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HotelDialogActivity extends Activity {
	private int hotelId = -1;
    public Context context;
    private LayoutInflater layoutInflater;
    private ImageFileHelper imageFileHelper;
    private String hotel = "";
    private TextView titleText;
    private TextView expText;
    private LinearLayout detailLayout;
    
    private TextView okButton;
    private Button closeButton;
    private Button mapButton;
    private ImageView image;
    
    private ProgressBar progressBar;
    
    private int paddingX;
    private int paddingY;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_dialog);
		context = this;
		layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageFileHelper = new ImageFileHelper(this);	
		
		titleText = (TextView)findViewById(R.id.textView_title);
		expText = (TextView)findViewById(R.id.explanation);
		detailLayout = (LinearLayout)findViewById(R.id.layout_detail);
		
		okButton = (TextView)findViewById(R.id.textView_ok);
		closeButton = (Button)findViewById(R.id.btn_cancel);
		mapButton = (Button)findViewById(R.id.button_map);
		
		image = (ImageView)findViewById(R.id.imageView_spot);
		progressBar = (ProgressBar)findViewById(R.id.progressbar1);
		
		//画面のpaddingを設定
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		paddingX = size.x / 20;
		paddingY = size.y / 20;
		LinearLayout wholeLayout = (LinearLayout)findViewById(R.id.linearlayout_whole);
		wholeLayout.setPadding(paddingX, 2 * paddingY, paddingX, 2 * paddingY);
		
		hotelId = getIntent().getExtras().getInt(IntentKey.HOTEL_ID);
		hotel = String.valueOf(hotelId);
		
		titleText.setText("読み込み中...");
		checkFavorite();
		
		JalanApiURLCreator c = new JalanApiURLCreator();
    	c.setHotelID(hotel);
    	String url = c.createHotelURL();
		
	    PostServerTask psTask = new PostServerTask(url) {
	    	
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		XmlReader xr = new XmlReader(this.httpData);
	    		ArrayList<Hotel> list = xr.getHotelData();
	    		
	    		if (list.size() > 0) {
	    			final Hotel hotel = list.get(0);
	    			titleText.setText(hotel.name);
		    		expText.setText(hotel.hotelCaption);
		    		
		    		setFavoriteEvent(hotel.name, hotel.lat, hotel.lng); 
		    		
		    		addItem("宿タイプ", hotel.hotelType);
	    			addItem("チェックイン", hotel.check_in);
	    			addItem("チェックアウト", hotel.check_out);
	    			addItem("住所", hotel.address);
	    			addItem("郵便番号", hotel.postCode);
	    			addItem("キャッチコピー", hotel.catchCopy);
	    			addItem("画像", hotel.imageUrl);
	    			
	    			String access = "";
	    			for (int i = 0; i < hotel.accessInformation.size(); i++) {
	    				access += hotel.accessInformationTitle.get(i) + ":" + hotel.accessInformation.get(i) + "\n";
	    			}
	    			addItem("アクセス", access);
	    			addItemWebPage("webページ", hotel.hotelURL);
	    			
	    			String fileName = Hotel.HOTEL_IMAGE_FILE_NAME + hotel.hotelID;
	    			int iconSize = ImageLoadTask.getPxFromDp(context, 50);
	    	        Bitmap bitmap = imageFileHelper.loadBitmap(fileName, iconSize, iconSize);
	    		    if (bitmap != null) {
	    		    	progressBar.setVisibility(View.GONE);
	    		        image.setImageBitmap(bitmap);
	    		    } else {
	    		    	loadImage(hotel.imageUrl); 
	    		    }
	    			
	    			mapButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(HotelDialogActivity.this, MapActivity.class);
							i.putExtra("HOTEL_DATA", hotel);
							startActivity(i);
						}
					});
	    		} else {
	    			titleText.setText("読み込みの失敗");
	    		}
	       	}
	    	
	    };
	    psTask.setPostData("hotel_id", hotelId);
	    psTask.execute();
	    
	    closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	    
	}
    
    @Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.animator.disappearance);
	}
    
    void setFavoriteEvent(String n, double lt, double lg) {
    	final String name = n;
    	final double lat = lt;
    	final double lng = lg;
    	
    	okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				okButton.setEnabled(false);
				PostServerTask pst = new PostServerTask(URLManager.ADD_FAVORITE_URL_HOTEL){
					
					@Override
		        	protected void onPostExecute(Boolean result) {
						if (taskResult) {
							Toast.makeText(context, "お気に入りが完了しました", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "お気に入りが失敗しました", Toast.LENGTH_SHORT).show();
						}
						checkFavorite();
		        	}
					
				};
				pst.setPostData("HotelId", hotelId);
				pst.setPostData("Name", name);
				pst.setPostData("Lat", lat);
				pst.setPostData("Lng", lng);
				pst.setPostData("group_id", 1);
				pst.execute();
			}
			
		});
    }
    
    private void checkFavorite() {
    	okButton.setEnabled(false);
	    PostServerTask psTask = new PostServerTask(URLManager.CHECK_EXIST_FAVORITE_HOTEL_URL) {
	    	
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		XmlReader xr = new XmlReader(httpData);
	    		boolean res = false;
	    		String label = "";
	    		String color = "#f0f0f0";
	    		if (xr.getResult()) {
	    			res = false;
	    			label = "★";
	    			color = "#ffd700";
	    		} else {
	    			res = true;
	    			label = "★";
	    		}
	    		okButton.setEnabled(res);
	    		okButton.setText(label);
	    		okButton.setTextColor(Color.parseColor(color));
	       	}
	    	
	    };
	    psTask.setPostData("HotelId", hotelId);
	    psTask.execute();
    }
    
    private void addItem(String title, String value) {
    	View view = layoutInflater.inflate(R.layout.item_detail, null);
    	TextView tView = (TextView)view.findViewById(R.id.item_detail_title);
    	TextView valueView = (TextView)view.findViewById(R.id.item_detail_value);
    	tView.setAutoLinkMask(Linkify.WEB_URLS);
    	tView.setText(title);
    	valueView.setText(value);
    	detailLayout.addView(view);
    }
    
    private void addItemWebPage(String title, String value) {
    	View view = layoutInflater.inflate(R.layout.item_detail, null);
    	TextView tView = (TextView)view.findViewById(R.id.item_detail_title);
    	TextView valueView = (TextView)view.findViewById(R.id.item_detail_value);
    	tView.setText(title);
    	//linkMaskはsetTextの前に設定する
    	valueView.setAutoLinkMask(Linkify.WEB_URLS);
    	valueView.setText(value);
    	detailLayout.addView(view);
    }
    
    private void loadImage(String url) {
	    (new ImageLoadTask(url, image, this) {
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		progressBar.setVisibility(View.GONE);
	    		if (bitmap != null) {
	    			image.setImageBitmap(bitmap);
				} else {
					image.setImageResource(R.drawable.loadfailure);
				}
				//int s = getPxFromDp(context, 200);
				//image.setLayoutParams(new LinearLayout.LayoutParams(s, s));
			
	       	}
	    }).execute();
    }
}
