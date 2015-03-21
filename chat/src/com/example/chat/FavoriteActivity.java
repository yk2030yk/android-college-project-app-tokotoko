package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoriteActivity extends FragmentActivity {
	public static final String CURRENT_ITEM_POSITION = "current_position";
	private ViewPager pager;
	private MyFragmentPagerAdapter pagerAdapter;
	public Context context;
	private Resources res;
	private ImageFileHelper imageFileHelper;
	private Vibrator vibrator;
	private LinearLayout deleteMenu;
	private TextView tab1;
	private TextView tab2;
	private TextView tab3;
	private View line1;
	private View line2;
	private View line3;
	
	public boolean isEditMode = false;
	public boolean isDataChangedS = false;
	public boolean isDataChangedG = false;
	public boolean isDataChangedH = false;
	ArrayList<HashMap<String, String>> spots = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> hotels = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> gourmets = new ArrayList<HashMap<String, String>>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		context = this;
		res = getResources();
		imageFileHelper = new ImageFileHelper(this);
		vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.hide();
		
		pager = (ViewPager)findViewById(R.id.pager_favorite);	
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		
		tab1 = (TextView)findViewById(R.id.textView_tab1);
		tab2 = (TextView)findViewById(R.id.textView_tab2); 
		tab3 = (TextView)findViewById(R.id.textView_tab3);
		line1 = (View)findViewById(R.id.underline1);
		line2 = (View)findViewById(R.id.underline2);
		line3 = (View)findViewById(R.id.underline3);
		
		deleteMenu = (LinearLayout)findViewById(R.id.layout_delete);
		deleteMenu.setVisibility(View.GONE);
		TextView t = (TextView)findViewById(R.id.textView_delete);
		t.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!checkEmpty()) {
					confirmDelete();
				}
			}
		});
				
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
		
		actionBar.setSelectedNavigationItem(0);
		changeTab(0);
		
		final LinearLayout mode = (LinearLayout)findViewById(R.id.layout_gm);
		mode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.vibrate(10);
				if (isEditMode) {
					isEditMode = false;
					deleteMenu.setVisibility(View.GONE);
					clearDeleteList();
				} else {
					isEditMode = true;
					deleteMenu.setVisibility(View.VISIBLE);
				}
				pagerAdapter.renew();
			}
		});
	}
	
	public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);	
			fragments.add(new FavoriteGourmetFragment());
			fragments.add(new FavoriteHotelFragment());
			fragments.add(new FavoriteSpotFragment());
		}
	
		@Override
		public Fragment getItem(int i) {
			switch(i){
			case 0:
				return fragments.get(0);
			case 1:
				return fragments.get(1);
			case 2:
				return fragments.get(2);
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
				title = "グルメ";
			} else if (position == 1) {
				title = "宿";
			} else if (position == 2){
				title = "観光地";
		    } else {
		    	title = "";
		    }
			
		    return title;
		}
		
		public void renew() {
			FavoriteGourmetFragment f = (FavoriteGourmetFragment)fragments.get(0);
			f.renew();
			FavoriteHotelFragment f2 = (FavoriteHotelFragment)fragments.get(1);
			f2.renew();
			FavoriteSpotFragment f3 = (FavoriteSpotFragment)fragments.get(2);
			f3.renew();
		}
		
		public void reload() {
			FavoriteGourmetFragment f = (FavoriteGourmetFragment)fragments.get(0);
			f.reload();
			FavoriteHotelFragment f2 = (FavoriteHotelFragment)fragments.get(1);
			f2.reload();
			FavoriteSpotFragment f3 = (FavoriteSpotFragment)fragments.get(2);
			f3.reload();
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
	
	void clearDeleteList() {
		spots.clear();
		hotels.clear();
		gourmets.clear();
	}
	
	boolean checkEmpty() {
		if (!spots.isEmpty()) {
			return false;
		}
		if (!hotels.isEmpty()) {
			return false;
		}
		if (!gourmets.isEmpty()) {
			return false;
		}
		return true;
	}
	
	void confirmDelete() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("削除してよろしいですか？");
        alertDialogBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete();
			}
        });
        alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
        });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	
	void delete () {
		final Loading loading = new Loading(this, "削除中...");
		loading.show();
		
		PostServerTask pst = new PostServerTask(URLManager.DELETE_FAVORITE_SELECT_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (this.taskResult) {
					isEditMode = false;
					
					isDataChangedS = true;
					isDataChangedH = true;
					isDataChangedG = true;
					
					pagerAdapter.reload();
					pagerAdapter.renew();
					clearDeleteList();
					deleteMenu.setVisibility(View.GONE);
				}
				loading.hide();
			}	
		};
		for (int i = 0; i < spots.size(); i++) {
			String id = spots.get(i).get("SpotId");
			pst.setPostData("spotId[]", id);
			String fileName = Spot.SPOT_IMAGE_FILE_NAME + id;
			deleteImage(fileName);
		}
		for (int i = 0; i < hotels.size(); i++) {
			String id = hotels.get(i).get("HotelId");
			pst.setPostData("hotelId[]", id);
			String fileName = Hotel.HOTEL_IMAGE_FILE_NAME + id;
			deleteImage(fileName);
		}
		for (int i = 0; i < gourmets.size(); i++) {
			String id = gourmets.get(i).get("GourmetId");
			pst.setPostData("gourmetId[]", id);
			String fileName = Gourmet.GOURMET_IMAGE_FILE_NAME + id;
			deleteImage(fileName);
		}
		pst.execute();	
	}
	
}