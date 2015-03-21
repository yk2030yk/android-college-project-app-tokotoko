package com.example.chat;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GuideGeneralDetailsFragmentS extends Fragment {
	Context context;
	private View frgLayout;
	private TextView dayText;
	private TextView inText;
	private TextView outText;
	private TextView hNameText;
	private TextView postText;
	private TextView addressText;
	private TextView sampleRateText;
	private TextView memoText;
	private TextView urlButton;
	private String guideId = "1";
	String memo = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		frgLayout = inflater.inflate(R.layout.fragment_guide_general_details, container, false);
		return frgLayout;
	}
	  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@SuppressLint("CutPasteId")
	@Override
	public void onStart() {
		super.onStart();
		
		Bundle bundle = getArguments();
	    guideId = bundle.getString(IntentKey.GUIDE_ID);
		
		dayText = (TextView)frgLayout.findViewById(R.id.textView_day);
		inText = (TextView)frgLayout.findViewById(R.id.textView_check_in);
		outText = (TextView)frgLayout.findViewById(R.id.textView_check_out);
		postText = (TextView)frgLayout.findViewById(R.id.textView_post_code);
		addressText = (TextView)frgLayout.findViewById(R.id.textView_address);
		urlButton = (TextView)frgLayout.findViewById(R.id.button_url);
		sampleRateText = (TextView)frgLayout.findViewById(R.id.textView_sample_rate);
		hNameText = (TextView)frgLayout.findViewById(R.id.textView_hotel_name);
		memoText = (TextView)frgLayout.findViewById(R.id.textView_memo);
		
		PostServerTask task = new PostServerTask(URLManager.GET_GUIDE_VALUE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				HashMap<String, String> map = xr.getGuideId().get(0);
				
				dayText.setText(getDateStr(map.get("StartDay")) + " ~ " + getDateStr(map.get("EndDay")));
				memoText.setText(map.get("Memo"));
				memo = map.get("Memo");
				
				JalanApiURLCreator urlcreator = new JalanApiURLCreator();
				urlcreator.setHotelID(map.get("HotelId"));
				
				PostServerTask task = new PostServerTask(urlcreator.createHotelURL()) {

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Hotel> list = xr.getHotelData();
						if (list.size() > 0) {
							final Hotel h = list.get(0);
							
							SearchGuideResultActivity act = (SearchGuideResultActivity)getActivity();
							if (act != null) {
								act.setHotel(h);
							}
							
							hNameText.setText("宿名 : " + h.name);
							inText.setText("チェックイン　: " + h.check_in);
							outText.setText("チェックアウト: " + h.check_out);
							sampleRateText.setText("参考価格 : " + checkBlank(h.sampleRate) + " 円");
							postText.setText("〒" + checkBlank(h.postCode));
							addressText.setText(checkBlank(h.address));
							
							urlButton.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Uri uri = Uri.parse(h.hotelURL);
									Intent i = new Intent(Intent.ACTION_VIEW,uri);
									startActivity(i);
								}
							});
						}
					}	
				};
				task.execute();
			}
			
		};
		task.setPostData("GuideId", guideId);
		task.execute();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	String checkBlank(String s) {
		String str = s;
		if (str == null) {
			str = "-";
		}
		
		if (str.equals("")) {
			str = "-";
		}
		return str;
	}
	
	private String getDateStr(String d) {
    	String date = "";
    	String ymd[] = d.split("-");
    	
    	Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.JAPANESE);
		date = sdf.format(cal.getTime());
		
    	return date;
    }
}

