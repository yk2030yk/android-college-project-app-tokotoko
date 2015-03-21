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

public class GourmetDialogActivity extends Activity {
	private String gourmetId = "-1";
    public Context context;
    private LayoutInflater layoutInflater;
    
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
    
    ImageFileHelper imageFileHelper;
    
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
		
		//��ʂ�padding��ݒ�
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		paddingX = size.x / 20;
		paddingY = size.y / 20;
		LinearLayout wholeLayout = (LinearLayout)findViewById(R.id.linearlayout_whole);
		wholeLayout.setPadding(paddingX, 2 * paddingY, paddingX, 2 * paddingY);
		
		gourmetId = getIntent().getExtras().getString(IntentKey.GOURMET_ID);
		
		titleText.setText("�ǂݍ��ݒ�...");
		checkFavorite();
		
		LocaTouchApiURLCreator c = new LocaTouchApiURLCreator();
    	c.setId(gourmetId);
    	String url = c.createUrl();
		
	    PostServerTask psTask = new PostServerTask(url) {
	    	
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		XmlReader xr = new XmlReader(httpData);
	    		ArrayList<Gourmet> list = xr.getGourmetLocaTouch();
	    		
	    		if (list.size() > 0) {
	    			final Gourmet gourmet = list.get(0);
	    			titleText.setText(gourmet.name);
		    		expText.setText("");
		    		
		    		setFavoriteEvent(gourmet.name, gourmet.lat, gourmet.lng);
		    		
		    		addItem("�J�e�S��", gourmet.category);
		    		addItem("���ϕ]��", gourmet.rate);
		    		addItem("�Z��", gourmet.address);
		    		addItem("�A�N�Z�X", gourmet.access);
		    		addItem("�d�b�ԍ�", gourmet.tel);
		    		String openTime = "";
		    		openTime += "�@�����@�@:" + gourmet.weekday + "\n"; 
		    		openTime += "�@�y�j���@:" + gourmet.saturday + "\n"; 
		    		openTime += "���j�E�j��:" + gourmet.holiday + "\n"; 
		    		addItem("�c�Ǝ���", openTime);
		    		addItem("��x��", gourmet.close);
		    		addItem("�����`�̗\�Z", gourmet.dinner);
		    		addItem("�f�B�i�[�̗\�Z", gourmet.lunch);
		    		addItem("�摜", gourmet.imageURL);
	    			
	    			addItemWebPage("web�y�[�W", gourmet.url);
	    			
	    			String fileName = Gourmet.GOURMET_IMAGE_FILE_NAME + gourmet.gourmetId;
	    			int iconSize = ImageLoadTask.getPxFromDp(context, 50);
	    	        Bitmap bitmap = imageFileHelper.loadBitmap(fileName, iconSize, iconSize);
	    		    if (bitmap != null) {
	    		    	progressBar.setVisibility(View.GONE);
	    		        image.setImageBitmap(bitmap);
	    		    } else {
	    		    	loadImage(gourmet.imageURL); 
	    		    }
	    			
	    			mapButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(GourmetDialogActivity.this, MapActivity.class);
							i.putExtra("GOURMET_DATA", gourmet);
							startActivity(i);
						}
					});
	    		} else {
	    			titleText.setText("�ǂݍ��݂̎��s");
	    		}
	       	}
	    	
	    };
	    psTask.setPostData("GourmetId", gourmetId);
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
				PostServerTask pst = new PostServerTask(URLManager.ADD_FAVORITE_URL_GOURMET){
					
					@Override
		        	protected void onPostExecute(Boolean result) {
						if (taskResult) {
							Toast.makeText(context, "���C�ɓ��肪�������܂���", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "���C�ɓ��肪���s���܂���", Toast.LENGTH_SHORT).show();
						}
						checkFavorite();
		        	}
					
				};
				pst.setPostData("GourmetId", gourmetId);
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
	    PostServerTask psTask = new PostServerTask(URLManager.CHECK_EXIST_FAVORITE_GOURMET_URL) {
	    	
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		XmlReader xr = new XmlReader(httpData);
	    		boolean res = false;
	    		String label = "";
	    		String color = "#f0f0f0";
	    		if (xr.getResult()) {
	    			res = false;
	    			label = "��";
	    			color = "#ffd700";
	    		} else {
	    			res = true;
	    			label = "��";
	    		}
	    		okButton.setEnabled(res);
	    		okButton.setText(label);
	    		okButton.setTextColor(Color.parseColor(color));
	       	}
	    	
	    };
	    psTask.setPostData("GourmetId", gourmetId);
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
    	//linkMask��setText�̑O�ɐݒ肷��
    	valueView.setAutoLinkMask(Linkify.WEB_URLS);
    	valueView.setText(value);
    	detailLayout.addView(view);
    }
    
    private void loadImage(String url) {
	    (new ImageLoadTask(url, image, this) {
	    	@Override
	       	protected void onPostExecute(Boolean result) {
	    		if (bitmap != null) {
	    			image.setImageBitmap(bitmap);
				} else {
					image.setImageResource(R.drawable.loadfailure);
				}
	    		progressBar.setVisibility(View.GONE);
				//int s = getPxFromDp(context, 200);
				//image.setLayoutParams(new LinearLayout.LayoutParams(s, s));
			
	       	}
	    }).execute();
    }
}
