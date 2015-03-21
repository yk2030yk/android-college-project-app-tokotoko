package com.example.chat;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchGuideResultActivity extends FragmentActivity {
	private final int REQUEST_CODE_EDIT_GUIDE = 111;
	private String guideId = "1";
	private Hotel hotel = null;
	public Context context;
	private Resources res;
	private Vibrator vibrator;
	
	private Loading loading;
	
	private ViewPager pager;
	private GuidePagerAdapter pagerAdapter;
	private ArrayList<HashMap<String, String>> list;
	private ActionBar actionBar;
	
	//Header UI
	private LinearLayout headerView;
	private LinearLayout headerImage;
	private TextView titleText;
	private LinearLayout addButton;
	private LinearLayout editButton;
	private LinearLayout mapButton;
	
	//Tab UI
	private HorizontalScrollView scroll;
	private LinearLayout tabView;
	private TextView openMenuButton;
	private ArrayList<View> lines = new ArrayList<View>();
	private ArrayList<View> dividers = new ArrayList<View>();
	
	private boolean isOpenHeader = false;
	private int openStart = 1;
	private int displayWidth = 1;
	private int tabWidth = 1;
	private int pageCountInDiplay = 1;
	
	//Season Status
	private String seasonNumber = "400";
	private int imageHeader = R.drawable.guide_head_winter;
	private int colorTabBack = R.color.guide_tab_back_w;
	private int colorTabLine = R.color.guide_tab_line_w;
	private int colorTabDivider = R.color.guide_tab_divider_w;
	private int colorTabOpen = R.color.guide_tab_open_button_w;
	private int colorTabOpenText = R.color.guide_tab_open_text_w;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_details);
		
		context = this;
		res = getResources();
		vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.hide();
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		int dp = (int) (displayMetrics.density * 40);
		displayWidth = displayMetrics.widthPixels - dp;
		openStart = (int) (displayMetrics.density * 50);
		
		guideId = getIntent().getExtras().getString(IntentKey.GUIDE_ID);
		seasonNumber = getIntent().getExtras().getString(IntentKey.GUIDE_SEASON);
		String guideName = getIntent().getExtras().getString(IntentKey.GUIDE_NAME);
		
		headerView = (LinearLayout)findViewById(R.id.layout_header);
		headerImage = (LinearLayout)findViewById(R.id.image_header);
		scroll = (HorizontalScrollView)findViewById(R.id.scrollView_tab);
		tabView = (LinearLayout)findViewById(R.id.layout_tab);
		pager = (ViewPager)findViewById(R.id.pager_guide);
		titleText = (TextView)findViewById(R.id.textView_title);
		titleText.setText(guideName);
		
		loading = new Loading(this, "しおり情報の取得中...");
		loading.show();
		
		headerView.addView(createHeaderView());
		openMenuButton = (TextView)findViewById(R.id.textView_open_menu);
		openMenuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.vibrate(10);
				if (!isOpenHeader) {
					isOpenHeader = true;
					headerView.setVisibility(View.VISIBLE);
					headerView.setSelected(true);
					openMenuButton.setText("▲");
	    			TranslateAnimation translate = new TranslateAnimation(0, 0, -openStart, 0);
	    			translate.setDuration(100);
	    			translate.setFillAfter(true);
	    			headerView.startAnimation(translate);
    			} else {
    				isOpenHeader = false;
    				openMenuButton.setText("▼");
    				TranslateAnimation translate = new TranslateAnimation(0, 0, 0, -openStart);
	    			translate.setDuration(100);
	    			translate.setFillAfter(true);
	    			headerView.startAnimation(translate);
	    			headerView.setVisibility(View.GONE);
	    			headerView.setSelected(false);
    			}
			}
		});
		
		PostServerTask task = new PostServerTask(URLManager.GET_GUIDE_DAY_URL){

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);	
				list = xr.getGuideDayId();
				if (!list.isEmpty()) {
					pagerAdapter = new GuidePagerAdapter(getSupportFragmentManager(), list);
					pager.setAdapter(pagerAdapter);
					pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
					
					for (int i = 0; i < pagerAdapter.getCount(); i++) {
						actionBar.addTab(actionBar.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(new MyTabListener()));
					}
					
					if (list.size() < 4) {
						pageCountInDiplay = list.size() + 1;
						tabWidth = displayWidth / (list.size() + 1);
					} else {
						pageCountInDiplay = 4;
						tabWidth = displayWidth / 4;
					}
					
					for (int i = 0; i < list.size() + 1; i++) {
						if (list.size() != i) {
							tabView.addView(createTabView(i, true));
						} else {
							tabView.addView(createTabView(i, false));
						}
					}
					
					actionBar.setSelectedNavigationItem(0);
					changeTabLine(0);
					changeHeaderState(0);
					setTabSeasonColors(seasonNumber);
				}
				
				loading.hide();
			}
			
		};
		task.setPostData("GuideId", guideId);
		task.execute();
		
	}
	
    private View createHeaderView() {
    	LayoutInflater inflater = LayoutInflater.from(this);
	    View view = inflater.inflate(R.layout.item_guide_header, null);
	    final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    view.setLayoutParams(params);
	      
	    editButton = (LinearLayout)view.findViewById(R.id.linearLayout4);
	    editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int pos = pager.getCurrentItem();
	    		
	    		if (pos == 0) {
	    			Intent i = new Intent(SearchGuideResultActivity.this, EditGuideGeneralActivity.class);
	    			i.putExtra(IntentKey.GUIDE_ID, guideId);
	    			startActivityForResult(i, REQUEST_CODE_EDIT_GUIDE);
	    		} else {
	    			Intent i = new Intent(SearchGuideResultActivity.this, EditGuideRouteActivity.class);
	    			i.putExtra(IntentKey.GUIDE_ID, guideId);
	    			i.putExtra(IntentKey.GUIDE_DAY_ID, list.get(pos - 1).get("GuideDayId"));
	    			startActivity(i);
	    		}
			}
		});
	    
	    addButton = (LinearLayout)view.findViewById(R.id.linearLayout3);
	    addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int pos = pager.getCurrentItem();
	    		
	    		if (pos != 0) {
	    			Intent i = new Intent(SearchGuideResultActivity.this, FavoriteSelectActivity.class);
	    			i.putExtra(IntentKey.GUIDE_ID, guideId);
	    			i.putExtra(IntentKey.GUIDE_DAY_ID, list.get(pos - 1).get("GuideDayId"));
	    			startActivity(i);
	    		}
			}
		});
	    
	    mapButton = (LinearLayout)view.findViewById(R.id.linearLayout2);
	    mapButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int pos = pager.getCurrentItem();
	    		
	    		if (pos == 0) {
	    			if(hotel != null){
	    				Intent i = new Intent(SearchGuideResultActivity.this, MapActivity.class);
		    			i.putExtra("HOTEL_DATA", hotel);
		    			startActivity(i);
	    			}
	    		} else {
	    			Intent i = new Intent(SearchGuideResultActivity.this, RouteMapActivity.class);
	    			i.putExtra(IntentKey.GUIDE_ID, guideId);
	    			i.putExtra(IntentKey.GUIDE_DAY_ID, list.get(pos - 1).get("GuideDayId"));
	    			startActivity(i);
	    		}
			}
		});
	    
    	return view;
    }
    
    private void changeHeaderState(int pos) {
    	if (pos == 0) {
    		addButton.setVisibility(View.GONE);
    		editButton.setVisibility(View.GONE);
    		mapButton.setVisibility(View.VISIBLE);
    	} else {
    		addButton.setVisibility(View.GONE);
    		editButton.setVisibility(View.GONE);
    		mapButton.setVisibility(View.VISIBLE);
    	}
    }
    
    private View createTabView(int pos, boolean isDivider) {
    	final int position = pos;
		LayoutInflater inflater = LayoutInflater.from(context);
	    View view = inflater.inflate(R.layout.item_guide_tab, null);
	    view.setLayoutParams(new LinearLayout.LayoutParams(tabWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
	    TextView t = (TextView)view.findViewById(R.id.textView1);
		t.setText(pagerAdapter.getPageTitle(pos));
		View line = (View)view.findViewById(R.id.underline);
		lines.add(line);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(position);
				changeTabLine(position);
				changeHeaderState(position);
			}
		});
		
		View divider = (View)view.findViewById(R.id.divider);
		divider.setBackgroundColor(res.getColor(colorTabDivider));
		if (!isDivider) {
			LinearLayout l = (LinearLayout)view.findViewById(R.id.linearLayout);
			l.removeView(divider);
		} else {
			dividers.add(divider);
		}
		
		return view;
    }
    
    private void changeTabLine(int pos) {
    	if (scroll.getScrollX() > pos * tabWidth) {
    		scroll.setScrollX((pos) * tabWidth);
    	} else if (scroll.getScrollX() + (pos + (pageCountInDiplay - 1) * tabWidth) < pos * tabWidth) {
    		scroll.setScrollX(pos * tabWidth);
    	}
    	for (int i = 0; i < lines.size(); i++) {
    		View line = lines.get(i);
    		if (i == pos) {
    			line.setBackgroundColor(res.getColor(colorTabLine));
    		} else {
    			line.setBackgroundColor(res.getColor(R.color.line_unselected));
    		}
    	}
    }
    
    private void setTabSeasonColors(String season) {
		if (season.equals("100")) {
			pager.setBackgroundResource(R.drawable.icon_guide_backsp);
			imageHeader = R.drawable.guide_head_spring;
			colorTabBack = R.color.guide_tab_back_sp;
			colorTabLine = R.color.guide_tab_line_sp;
			colorTabDivider = R.color.guide_tab_divider_sp;
			colorTabOpen = R.color.guide_tab_open_button_sp;
			colorTabOpenText = R.color.guide_tab_open_text_sp;
		} else if (season.equals("200")) {
			pager.setBackgroundResource(R.drawable.icon_guide_backsu);
			imageHeader = R.drawable.guide_head_summer;
			colorTabBack = R.color.guide_tab_back_su;
			colorTabLine = R.color.guide_tab_line_su;
			colorTabDivider = R.color.guide_tab_divider_su;
			colorTabOpen = R.color.guide_tab_open_button_su;
			colorTabOpenText = R.color.guide_tab_open_text_su;
		} else if (season.equals("300")) {
			pager.setBackgroundResource(R.drawable.icon_guide_backau);
			imageHeader = R.drawable.guide_head_autumn;
			colorTabBack = R.color.guide_tab_back_a;
			colorTabLine = R.color.guide_tab_line_a;
			colorTabDivider = R.color.guide_tab_divider_a;
			colorTabOpen = R.color.guide_tab_open_button_a;
			colorTabOpenText = R.color.guide_tab_open_text_a;
		} else if (season.equals("400")) {
			pager.setBackgroundResource(R.drawable.icon_guide_backwi);
			imageHeader = R.drawable.guide_head_winter;
			colorTabBack = R.color.guide_tab_back_w;
			colorTabLine = R.color.guide_tab_line_w;
			colorTabDivider = R.color.guide_tab_divider_w;
			colorTabOpen = R.color.guide_tab_open_button_w;
			colorTabOpenText = R.color.guide_tab_open_text_w;
		}
		headerImage.setBackgroundResource(imageHeader);
		scroll.setBackgroundColor(res.getColor(colorTabBack));
		openMenuButton.setBackgroundColor(res.getColor(colorTabOpen));
		openMenuButton.setTextColor(res.getColor(colorTabOpenText));
		for (int i = 0; i < dividers.size(); i++) {
			View d = dividers.get(i);
			d.setBackgroundColor(res.getColor(colorTabDivider));
		}
		changeTabLine(pager.getCurrentItem());
	}
    
    public void setHotel(Hotel h) {
    	this.hotel = h;
    }
	
	private class MyTabListener implements ActionBar.TabListener {
		
		public MyTabListener() {
			
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			pager.setCurrentItem(tab.getPosition());
			changeTabLine(tab.getPosition());
			changeHeaderState(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			
		}
	}
	
	public class GuidePagerAdapter extends FragmentStatePagerAdapter {
		private int FRAGMENT_COUNT = 0;
		private ArrayList<HashMap<String, String>> dataList;
		private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		
		public GuidePagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> list) {
			super(fm);
			this.dataList = list;
			
			FRAGMENT_COUNT = dataList.size() + 1;
			
			GuideGeneralDetailsFragmentS ggdf = new GuideGeneralDetailsFragmentS();
			Bundle bundle = new Bundle();
			bundle.putString(IntentKey.GUIDE_ID, guideId);
			ggdf.setArguments(bundle);
			fragments.add(ggdf);
			
			for (int i = 0 ; i < dataList.size(); i++) {
				GuideDetailsFragment gdf = new GuideDetailsFragment();
				bundle = new Bundle();
				bundle.putString(IntentKey.GUIDE_DAY_ID, list.get(i).get("GuideDayId"));
				bundle.putString(IntentKey.GUIDE_SEASON, seasonNumber);
				gdf.setArguments(bundle);
				fragments.add(gdf);
			}
			
		}

		@Override
		public Fragment getItem(int i) {
			if (i < FRAGMENT_COUNT) {
				return fragments.get(i);
			} else {
				return new ExtraFragment();
		    }
		}

		@Override
		public int getCount() {
			return FRAGMENT_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title;
			if (position == 0) {
				title = "基本情報";
			} else if (position < FRAGMENT_COUNT) {
				String[] date = dataList.get(position - 1).get("Day").split("-");
				title = date[1] + "月" + date[2] + "日";
			} else {
				title = "余分なページ";
		    }
		    return title;
		}	
	}
	
}

