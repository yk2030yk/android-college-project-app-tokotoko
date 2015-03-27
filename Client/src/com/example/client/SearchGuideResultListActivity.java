package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchGuideResultListActivity extends Activity {
	private Context context;
	private Loading loading;
	private ListView listView;
	private TextView label;
	private GuideAdapter adapter;
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_guide_result_list);
		getActionBar().hide();

		context = this;
		res = getResources();
		listView = (ListView) findViewById(R.id.listView_guide);
		label = (TextView) findViewById(R.id.textView1);

		loading = new Loading(context, "åüçıíÜ...");
		loading.show();

		Intent i = getIntent();
		String key = i.getStringExtra("KEY");
		String season = i.getStringExtra("SEASON");
		String stay = i.getStringExtra("STAY");
		setLabel(key);
		initList(key, season, stay);
	}

	private void initList(String key, String season, String stay) {
		PostServerTask task = new PostServerTask(URLManager.GET_SEARCH_GUIDE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<HashMap<String, String>> list = xr.getGuideId();
				if (list.isEmpty()) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("GuideId", "-1");
					map.put("GuideName", "åüçıåãâ Ç™Ç†ÇËÇ‹ÇπÇÒ");
					map.put("Season", "");
					list.add(map);
				}
				adapter = new GuideAdapter(context, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				loading.hide();
			}

		};
		if (!key.equals("")) {
			String[] words = key.replaceAll("Å@", " ").split(" ", 0);
			for (int k = 0; k < words.length; k++) {
				task.setPostData("keyword[]", words[k]);
			}
		}
		if (season != null) {
			task.setPostData("Season", season);
		}
		if (stay != null) {
			task.setPostData("StayCount", stay);
		}
		task.execute();
	}

	void setLabel(String key) {
		String l = "";
		String[] words = key.replaceAll("Å@", " ").split(" ", 0);
		for (int i = 0; i < words.length; i++) {
			l += words[i] + " ";
		}
		l += "ÇÃåüçıåãâ ";
		label.setText(l);
	}

	private class GuideAdapter extends ArrayAdapter<HashMap<String, String>> {
		public Context mycontext;
		private LayoutInflater layoutInflater;
		private ArrayList<HashMap<String, String>> arrayList;
		int viewResId = R.layout.item_guide_list;

		public GuideAdapter(Context context, ArrayList<HashMap<String, String>> list) {
			super(context, 0, list);
			mycontext = context;
			layoutInflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrayList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = layoutInflater.inflate(viewResId, null);
			}

			final HashMap<String, String> itemData = arrayList.get(position);
			TextView myText = (TextView) view.findViewById(R.id.textView_name);
			myText.setText(itemData.get("GuideName"));

			LinearLayout l = (LinearLayout) view.findViewById(R.id.layout);
			TextView dateText = (TextView) view.findViewById(R.id.textView_date);
			l.removeView(dateText);

			ImageView image = (ImageView) view.findViewById(R.id.imageView1);
			String season = itemData.get("Season");
			if (season.equals("100")) {
				image.setImageDrawable(res.getDrawable(R.drawable.icon_spring));
			} else if (season.equals("200")) {
				image.setImageDrawable(res.getDrawable(R.drawable.icon_summer));
			} else if (season.equals("300")) {
				image.setImageDrawable(res.getDrawable(R.drawable.icon_autumn));
			} else if (season.equals("400")) {
				image.setImageDrawable(res.getDrawable(R.drawable.icon_winter));
			}

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!itemData.get("GuideId").equals("-1")) {
						Intent i = new Intent(SearchGuideResultListActivity.this, SearchGuideResultActivity.class);
						i.putExtra(IntentKey.GUIDE_ID, itemData.get("GuideId"));
						i.putExtra(IntentKey.GUIDE_NAME, itemData.get("GuideName"));
						i.putExtra(IntentKey.GUIDE_SEASON, itemData.get("Season"));
						startActivity(i);
					}
				}
			});

			return view;
		}

	}
}
