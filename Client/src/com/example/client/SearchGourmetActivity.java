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

public class SearchGourmetActivity extends Activity {
	private Context context;
	private ListView listView;
	private EditText et;
	private ArrayList<Gourmet> list = new ArrayList<Gourmet>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getActionBar().setTitle("ÉOÉãÉÅåüçı");
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

	private void initList(String keyword) {
		list.clear();
		list.add(new Gourmet("Åu" + keyword + "ÅvÇÃåüçıåãâ "));

		LocaTouchApiURLCreator c = new LocaTouchApiURLCreator();
		c.setName(keyword);
		String url = c.createUrl();
		PostServerTask task = new PostServerTask(url) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Gourmet> list2 = xr.getGourmetLocaTouch();
				list.addAll(list2);
				if (list.size() == 1) {
					Gourmet g = new Gourmet("åüçıåãâ Ç™0åèÇ≈Ç∑");
					list.add(g);
				}
				CustomAdapter adapter = new CustomAdapter(context, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		};
		task.execute();
	}

	private class CustomAdapter extends ArrayAdapter<Gourmet> {
		public Context mycontext;
		private LayoutInflater layoutInflater;
		private ArrayList<Gourmet> arrayList;
		int viewResId = R.layout.item_drawer_favorite;

		public CustomAdapter(Context context, ArrayList<Gourmet> list) {
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

			final Gourmet itemData = arrayList.get(position);
			TextView myText = (TextView) view.findViewById(R.id.textView_name);
			myText.setText(itemData.name);

			myText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!itemData.gourmetId.equals("-1")) {
						Intent i = new Intent(SearchGourmetActivity.this, GourmetDialogActivity.class);
						i.putExtra(IntentKey.GOURMET_ID, itemData.gourmetId);
						startActivity(i);
					}
				}
			});

			return view;
		}
	}
}
