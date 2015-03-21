package com.example.chat;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FavoriteSelectActivity extends FragmentActivity {
	public static final String CURRENT_ITEM_POSITION = "current_position";
	private ViewPager pager;
	private MyFragmentPagerAdapter pagerAdapter;
	public Context context;
	private Resources res;
	private ImageFileHelper imageFileHelper;
	public ArrayList<Point> routePoints = new ArrayList<Point>();
	private String guideDayId;
	private String guideId;
	
	private TextView tab1;
	private TextView tab2;
	private TextView tab3;
	private View line1;
	private View line2;
	private View line3;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_favorite);
		context = this;
		res = getResources();
		imageFileHelper = new ImageFileHelper(this);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.hide();
		
		guideDayId = getIntent().getExtras().getString(IntentKey.GUIDE_DAY_ID);
		guideId = getIntent().getExtras().getString(IntentKey.GUIDE_ID);
		
		pager = (ViewPager)findViewById(R.id.pager_favorite);	
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		
		tab1 = (TextView)findViewById(R.id.textView_tab1);
		tab2 = (TextView)findViewById(R.id.textView_tab2); 
		tab3 = (TextView)findViewById(R.id.textView_tab3);
		line1 = (View)findViewById(R.id.underline1);
		line2 = (View)findViewById(R.id.underline2);
		line3 = (View)findViewById(R.id.underline3);
				
		tab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(0);
				changeTab(0);
			}
		});
		
		tab2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(1);
				changeTab(1);
			}
		});
		
		tab3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(2);
				changeTab(2);
			}
		});
		
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(new MyTabListener()));
		}
		
		actionBar.setSelectedNavigationItem(2);
		changeTab(2);
		
		Button createButton = (Button)findViewById(R.id.button_create);
		createButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FavoriteSelectActivity.this, CreateRouteActivity.class);
				i.putExtra(IntentKey.LIST_DATA, routePoints);
				i.putExtra(IntentKey.GUIDE_ID, guideId);
				i.putExtra(IntentKey.GUIDE_DAY_ID, guideDayId);
				int requestCode = 111;
			    startActivityForResult(i, requestCode);
			}
		});
	}
	
	public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
		
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);	
		}
	
		@Override
		public Fragment getItem(int i) {
			switch(i){
			case 0:
				return new FavoriteSelectGourmetFragment();
			case 1:
				return new FavoriteSelectHotelFragment();
			case 2:
				return new FavoriteSelectSpotFragment();
			default:
				return new ExtraFragment();
			}
		}
	
		@Override
		public int getCount() {
			return 3;
		}
		
		public CharSequence getPageTitle(int position) {
			String title = "";
			if (position == 0) {
				title = "ƒOƒ‹ƒ";
			} else if (position == 1) {
				title = "h";
			} else if (position == 2){
				title = "ŠÏŒõ’n";
		    } else {
		    	title = "";
		    }
			
		    return title;
		}	
	}
	
	
	
	private class MyTabListener implements ActionBar.TabListener {
		
		public MyTabListener() {
			
		}
		
		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			
		}

		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	        pager.setCurrentItem(tab.getPosition());
	        changeTab(tab.getPosition());
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			
		}
	}
	
	private void changeTab(int position) {
		if (position == 0) {
			line1.setBackgroundColor(res.getColor(R.color.line_selected));
			line2.setBackgroundColor(res.getColor(R.color.line_unselected));
			line3.setBackgroundColor(res.getColor(R.color.line_unselected));
		} else if (position == 1) {
			line1.setBackgroundColor(res.getColor(R.color.line_unselected));
			line2.setBackgroundColor(res.getColor(R.color.line_selected));
			line3.setBackgroundColor(res.getColor(R.color.line_unselected));
		} else if (position == 2) {
			line1.setBackgroundColor(res.getColor(R.color.line_unselected));
			line2.setBackgroundColor(res.getColor(R.color.line_unselected));
			line3.setBackgroundColor(res.getColor(R.color.line_selected));
		}
	}
	
	void deleteImage(String fname) {
		imageFileHelper.delete(fname);
	}
	
	public void addPoint(Point p){
		routePoints.add(p);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	   
	    switch (requestCode) {
	    case 111:
	      if (resultCode == RESULT_OK) {
	    	  finish();
	      }
	      break;
	 
	    default:
	      break;
	    }
	}
	
}