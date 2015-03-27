package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteSelectSpotFragment extends Fragment {
	private Context context;
	private View frgLayout;
	private static MyCustomAdapter adapter;
	private ArrayList<Point> spots = new ArrayList<Point>();
	private ListView listView;
	private final String SAVE_KEY = "SPOTS";
	private Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		frgLayout = inflater.inflate(R.layout.fragment_favorite, container, false);
		return frgLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity().getApplicationContext();
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		if (savedInstanceState != null) {
			spots = (ArrayList<Point>) savedInstanceState.getSerializable(SAVE_KEY);
		}
	}

	public void onStart() {
		super.onStart();
		listView = (ListView) frgLayout.findViewById(R.id.listView1);
		adapter = new MyCustomAdapter(context, spots);
		listView.setAdapter(adapter);

		if (spots.isEmpty()) {
			PostServerTask psTask = new PostServerTask(URLManager.GET_FAVORITE_SPOT_URL) {
				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					XmlReader xr = new XmlReader(this.httpData);
					ArrayList<HashMap<String, String>> list = xr.getFavoriteSpotId();
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, String> s = list.get(i);
						Spot spot = new Spot();
						spot.spotID = Integer.parseInt(s.get("SpotId"));
						spot.name = s.get("Name");
						spot.lat = Double.parseDouble(s.get("Lat"));
						spot.lng = Double.parseDouble(s.get("Lng"));
						adapter.add(new Point(spot));
					}
				}
			};
			psTask.setPostData("group_id", 1);
			psTask.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVE_KEY, spots);
		Log.d("saveInstavceState", "ÉZÅ[ÉuÇ≥ÇÍÇΩÇÊ");
	}

	private class MyCustomAdapter extends ArrayAdapter<Point> {
		public Context mycontext;
		private LayoutInflater layoutInflater;
		private ArrayList<Point> arrayList;
		private int viewResId = R.layout.item_select_favorite;
		private MyCustomAdapter adapter;

		public MyCustomAdapter(Context context, ArrayList<Point> list) {
			super(context, 0, list);
			mycontext = context;
			layoutInflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrayList = list;
			adapter = this;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = layoutInflater.inflate(viewResId, null);
			}

			final Point itemData = arrayList.get(position);
			final TextView nameText = (TextView) view.findViewById(R.id.item_favorite_content);
			final TextView addButton = (TextView) view.findViewById(R.id.item_favorite_del);
			// final ImageView image =
			// (ImageView)view.findViewById(R.id.item_favorite_image);
			nameText.setText(itemData.name);

			final FavoriteSelectActivity activity = (FavoriteSelectActivity) getActivity();
			if (activity.routePoints.contains(itemData)) {
				int rank = activity.routePoints.indexOf(itemData) + 1;
				addButton.setText("" + rank);
			} else {
				addButton.setText("");
			}

			addButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					vibrator.vibrate(10);
					if (activity.routePoints.contains(itemData)) {
						activity.routePoints.remove(itemData);
					} else {
						activity.routePoints.add(itemData);
					}
					adapter.notifyDataSetChanged();
				}
			});

			nameText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, SpotDialogActivity.class);
					i.putExtra(IntentKey.SPOT_ID, Integer.parseInt(itemData.id));
					startActivity(i);
				}
			});

			return view;
		}
	}

}