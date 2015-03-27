package com.example.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyGuideActivity extends Activity {
	public Context context;
	private Vibrator vibrator;
	private Resources res;
	private Drawable iconSp;
	private Drawable iconSu;
	private Drawable iconAu;
	private Drawable iconWi;
	private ListView list;

	private GuideAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_guide);
		getActionBar().hide();

		context = this;
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		res = getResources();

		iconSp = res.getDrawable(R.drawable.icon_spring);
		iconSu = res.getDrawable(R.drawable.icon_summer);
		iconAu = res.getDrawable(R.drawable.icon_autumn);
		iconWi = res.getDrawable(R.drawable.icon_winter);

		list = (ListView) findViewById(R.id.listView_guide);
		LinearLayout btn = (LinearLayout) findViewById(R.id.button_add);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MyGuideActivity.this, CreateGuideActivity.class);
				int requestCode = 111;
				startActivityForResult(i, requestCode);
			}
		});

		PostServerTask.checkCocnection(this);
		initList();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initList() {
		PostServerTask task = new PostServerTask(URLManager.GET_GUIDE_ID_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<HashMap<String, String>> glist = xr.getGuideId();
				if (!glist.isEmpty()) {
					adapter = new GuideAdapter(context, glist);
					list.setAdapter(adapter);
				} else {
					HashMap<String, String> noDataMap = new HashMap<String, String>();
					noDataMap.put("", "");
					noDataMap.put("GuideId", "-1");
					noDataMap.put("GuideName", "ÇµÇ®ÇËÇ™Ç†ÇËÇ‹ÇπÇÒ");
					noDataMap.put("Season", "");
					adapter = new GuideAdapter(context, glist);
					adapter.add(noDataMap);
					list.setAdapter(adapter);
				}
			}
		};
		task.execute();
	}

	private void deleteGuide(String id) {
		final String guideId = id;

		vibrator.vibrate(20); // 0.02 second

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("ÇµÇ®ÇËÇÃçÌèú").setMessage("Ç±ÇÃÇµÇ®ÇËÇçÌèúÇµÇƒÇÊÇÎÇµÇ¢Ç≈Ç∑Ç©?\nID = " + guideId).setPositiveButton("ÇÕÇ¢", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!guideId.equals("") || guideId != null) {
					PostServerTask task = new PostServerTask(URLManager.DELETE_MY_GUIDE_URL) {

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							initList();
						}

					};
					task.setPostData("id", guideId);
					task.execute();
				}
			}
		}).setNegativeButton("Ç¢Ç¢Ç¶", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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
			String name = itemData.get("GuideName");
			if (name != null) {
				myText.setText(name);
			}

			TextView dateText = (TextView) view.findViewById(R.id.textView_date);
			String start = getDateStr(itemData.get("StartDay"));
			String end = getDateStr(itemData.get("EndDay"));
			dateText.setText(start + " - " + end);

			ImageView image = (ImageView) view.findViewById(R.id.imageView1);
			String season = itemData.get("Season");
			if (season.equals("100")) {
				image.setImageDrawable(iconSp);
			} else if (season.equals("200")) {
				image.setImageDrawable(iconSu);
			} else if (season.equals("300")) {
				image.setImageDrawable(iconAu);
			} else if (season.equals("400")) {
				image.setImageDrawable(iconWi);
			}

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MyGuideActivity.this, GuideActivity.class);
					i.putExtra(IntentKey.GUIDE_ID, itemData.get("GuideId"));
					i.putExtra(IntentKey.GUIDE_NAME, itemData.get("GuideName"));
					i.putExtra(IntentKey.GUIDE_SEASON, itemData.get("Season"));
					int requestCode = 112;
					startActivityForResult(i, requestCode);
				}
			});

			view.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					deleteGuide(itemData.get("GuideId"));
					return false;
				}
			});

			return view;
		}

		private String getDateStr(String d) {
			String date = "";
			if (!d.equals("") || d != null) {
				String ymd[] = d.split("-");

				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyîNMMåéddì˙(E)", Locale.JAPANESE);
				date = sdf.format(cal.getTime());
			}

			return date;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 111:
			if (resultCode == RESULT_OK) {
				initList();
			}
			break;
		case 112:
			if (resultCode == RESULT_OK) {
				initList();
			}
			break;
		default:
			break;
		}
	}
}
