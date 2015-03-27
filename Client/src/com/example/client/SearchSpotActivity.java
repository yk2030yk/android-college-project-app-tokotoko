package com.example.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchSpotActivity extends Activity {
	private Context context;
	private ListView listView;
	private EditText et;
	private ArrayList<Spot> list = new ArrayList<Spot>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getActionBar().setTitle("äœåıínåüçı");
		context = this;
		listView = (ListView) findViewById(R.id.listView1);
		et = (EditText) findViewById(R.id.editText1);
		Button search = (Button) findViewById(R.id.button1);

		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String key = et.getText().toString();
				if (!key.equals("")) {
					initList(key);
					et.setText("");
				}

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});

		initList("ãûìs");
	}

	void initList(String keyword) {
		list.clear();
		list.add(new Spot("Åu" + keyword + "ÅvÇÃåüçıåãâ "));

		PostServerTask task = new PostServerTask(URLManager.GET_SEARCH_SPOT_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Spot> list2 = xr.getSpotData();
				list.addAll(list2);
				if (list.size() == 1) {
					list.add(new Spot("åüçıåãâ Ç™0åèÇ≈Ç∑"));
				}
				CustomAdapter adapter = new CustomAdapter(context, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		};
		task.setPostData("SpotName", keyword);
		task.execute();
	}

	private class CustomAdapter extends ArrayAdapter<Spot> {
		public Context mycontext;
		private LayoutInflater layoutInflater;
		private ArrayList<Spot> arrayList;
		int viewResId = R.layout.item_drawer_favorite;

		public CustomAdapter(Context context, ArrayList<Spot> list) {
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

			final Spot itemData = arrayList.get(position);
			TextView myText = (TextView) view.findViewById(R.id.textView_name);
			myText.setText(itemData.name);

			myText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemData.spotID != -1) {
						Intent i = new Intent(SearchSpotActivity.this, SpotDialogActivity.class);
						i.putExtra(IntentKey.SPOT_ID, itemData.spotID);
						startActivity(i);
					}
				}
			});

			return view;
		}
	}
}
