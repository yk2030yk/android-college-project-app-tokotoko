package com.example.client;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditGuideRouteActivity extends Activity {
	private String guideDayId = "-1";

	public Context context;
	private LayoutInflater layoutInflater;
	private Resources res;
	private Loading loading;
	private LinearLayout mainLayout;

	private ArrayList<Route> myRoutes = new ArrayList<Route>();
	private int travelTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_guide_route);
		getActionBar().hide();

		context = this;
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		res = getResources();
		mainLayout = (LinearLayout) findViewById(R.id.layout_main);

		guideDayId = getIntent().getExtras().getString(IntentKey.GUIDE_DAY_ID);

		loading = new Loading(this, "読み込み中...");
		loading.show();

		Button finishButton = (Button) findViewById(R.id.button_finish);
		finishButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myRoutes.size() > 0) {
					PostServerTask task = new PostServerTask(URLManager.REGISTER_ROUTE_URL) {
						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							if (this.taskResult) {
								Toast.makeText(context, "登録に成功しましたYO", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(context, "登録に失敗しました", Toast.LENGTH_SHORT).show();
							}
						}
					};
					task.setPostData("guideDayId", guideDayId);
					for (int i = 0; i < myRoutes.size(); i++) {
						Route r = myRoutes.get(i);
						task.setPostData("priority[]", i);
						task.setPostData("spotId[]", r.destPoint.id);
						task.setPostData("Name[]", r.destPoint.name);
						task.setPostData("Lat[]", r.destPoint.lat);
						task.setPostData("Lng[]", r.destPoint.lng);
						task.setPostData("startTime[]", r.startTime);
						task.setPostData("endTime[]", r.endTime);
						task.setPostData("startTimeSec[]", r.startTimeSec);
						task.setPostData("endTimeSec[]", r.endTimeSec);
						task.setPostData("stayTimeSec[]", r.stayTimeSec);
						task.setPostData("distance[]", r.distance);
						task.setPostData("duration[]", r.duration);
						task.setPostData("durationSec[]", r.durationSec);
						task.setPostData("kind[]", r.destPoint.kind);
					}
					task.execute();
				} else {
					finish();
				}
			}
		});

		// guideDayIdでルートを取得
		PostServerTask task = new PostServerTask(URLManager.GET_ROUTE_URL) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader x = new XmlReader(this.httpData);
				myRoutes = x.getRoute();
				getAPIData(myRoutes);
				if (myRoutes.size() > 0) {
					mainLayout.removeAllViews();
					createRouteView();
				} else {
					mainLayout.removeAllViews();
					createNoRouteView();
				}
				loading.hide();
			}

		};
		task.setPostData("GuideDayId", guideDayId);
		task.execute();

	}

	private void getAPIData(ArrayList<Route> list) {

		for (int i = 0; i < list.size(); i++) {
			final Route r = list.get(i);
			// kind = 0 : hotel , kind = 1 : spot, kind = 2 :
			// gourmet.
			if (r.destPoint.kind == 0) {
				JalanApiURLCreator c = new JalanApiURLCreator();
				c.setHotelID(String.valueOf(r.destPoint.id));
				PostServerTask task = new PostServerTask(c.createHotelURL()) {

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Hotel> list = xr.getHotelData();
						if (!list.isEmpty()) {
							Hotel h = list.get(0);
							r.destPoint = new Point(h);
						}
						createRouteView();
					}

				};
				task.execute();
			} else if (r.destPoint.kind == 2) {
				LocaTouchApiURLCreator c = new LocaTouchApiURLCreator();
				c.setId(String.valueOf(r.destPoint.id));
				PostServerTask task = new PostServerTask(c.createUrl()) {

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						XmlReader xr = new XmlReader(this.httpData);
						ArrayList<Gourmet> list = xr.getGourmetLocaTouch();
						if (!list.isEmpty()) {
							Gourmet g = list.get(0);
							r.destPoint = new Point(g);
						}
						createRouteView();
					}

				};
				task.execute();
			}

		}
	}

	private void createRouteList() {
		travelTime = 9 * 60 * 60;

		for (int i = 0; i < myRoutes.size(); i++) {
			Route route = myRoutes.get(i);
			route.startTime = getTimeStr(route.durationSec);
			route.startTimeSec = travelTime;
			route.endTime = getTimeStr(route.stayTimeSec);
			route.endTimeSec = travelTime;
		}
	}

	@SuppressLint("CutPasteId")
	private void createRouteView() {
		mainLayout.removeAllViews();

		for (int i = 0; i < myRoutes.size(); i++) {
			final Route route = myRoutes.get(i);
			final int position = i;

			View view = layoutInflater.inflate(R.layout.item_edit_route, null);

			TextView durText = (TextView) view.findViewById(R.id.text_duration);
			durText.setText("距離:" + route.distance + "\n所要時間:約" + route.duration + "かかります" + guideDayId);

			TextView stayText = (TextView) view.findViewById(R.id.textView_time);
			stayText.setText(route.startTime + "\n~\n" + route.endTime);
			stayText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					changeStayTime(route);
				}
			});

			TextView nameText = (TextView) view.findViewById(R.id.textView_name);
			if (route.destPoint.name.equals("") || route.destPoint.name == null) {
				nameText.setText("読み込み中...");
			} else {
				nameText.setText(route.destPoint.name);
				nameText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteRoute(route);
					}
				});
				nameText.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// DragViewListener listener =
						// new DragViewListener(view);
						// nameText.setOnTouchListener(listener);
						return false;
					}
				});
			}

			Button moveButton = (Button) view.findViewById(R.id.moveButton);
			moveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					moveRoute(route, position);
				}
			});

			if (i == 0) {
				LinearLayout layout = (LinearLayout) view.findViewById(R.id.main_layout);
				LinearLayout dlayout = (LinearLayout) view.findViewById(R.id.layout_duration);
				stayText.setText(route.endTime + "\n\n出発");
				layout.removeView(dlayout);

				FrameLayout layout2 = (FrameLayout) view.findViewById(R.id.layout_frame);
				LinearLayout line = (LinearLayout) view.findViewById(R.id.layout_line);
				layout2.removeView(line);
			}

			if (i == myRoutes.size() - 1) {
				stayText.setText(route.startTime + "\n\n到着");
				LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.layout_line);
				View line = (View) view.findViewById(R.id.lineview);
				layout2.removeView(line);
			}

			View kindLine = (View) view.findViewById(R.id.view_line);
			if (route.destPoint.kind == 0) {
				kindLine.setBackgroundColor(res.getColor(R.color.route_hotel));
			} else if (route.destPoint.kind == 1) {
				kindLine.setBackgroundColor(res.getColor(R.color.route_spot));
			} else if (route.destPoint.kind == 2) {
				kindLine.setBackgroundColor(res.getColor(R.color.route_gourmet));
			}

			mainLayout.addView(view);
		}
	}

	private void createNoRouteView() {
		mainLayout.removeAllViews();
		View view = layoutInflater.inflate(R.layout.item_no_route, null);
		mainLayout.addView(view);
	}

	// private int getPx(int stayTime) {
	// int dp = ((stayTime / 60) / 30) * 5 + 50;
	// DisplayMetrics metrics = getResources().getDisplayMetrics();
	// int px = (int)(metrics.density * dp);
	// return px;
	// }

	private String getTimeStr(int dur) {
		String timeStr = "";
		travelTime += dur;
		int h = (travelTime / 60) / 60;
		int m = (travelTime / 60) % 60;
		h = h % 24;
		if (h < 10) {
			timeStr += "0";
		}
		timeStr += String.valueOf(h);
		timeStr += " : ";
		if (m < 10) {
			timeStr += "0";
		}
		timeStr += String.valueOf(m);

		return timeStr;
	}

	// private String getDurationStr(int dur) {
	// String str = "";
	// int h = (dur / 60) / 60;
	// int m = (dur / 60) % 60;
	// str = h + "時間" + m + "分";
	// return str;
	// }

	private void changeStayTime(Route r) {
		final Route route = r;
		final int h = r.stayTimeSec / (60 * 60);
		final int m = r.stayTimeSec / 60 - (h - 1);

		final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				route.stayTimeSec = (hourOfDay * 60 * 60) + (minute * 60);
				createRouteList();
				createRouteView();
			}
		}, h, m, true);
		timePickerDialog.show();
	}

	// 削除部分を詰める
	private void deleteRoute(Route r) {
		final Route route = r;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("ルートの削除").setMessage("このポイントを削除しますか?").setPositiveButton("はい", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myRoutes.remove(route);
				createRouteList();
				createRouteView();
			}
		}).setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void moveRoute(Route r, int pos) {
		final Route route = r;
		final int position = pos;

		if (position == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("移動").setMessage("移動しますか").setPositiveButton("キャンセル", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).setNegativeButton("下に移動", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Route cRoute = myRoutes.get(position + 1);
					myRoutes.set(position, cRoute);
					myRoutes.set(position + 1, route);
					createRouteList();
					createRouteView();
				}
			}).setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (position + 1 == myRoutes.size()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("移動").setMessage("移動しますか").setPositiveButton("上に移動", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Route cRoute = myRoutes.get(position - 1);
					myRoutes.set(position, cRoute);
					myRoutes.set(position - 1, route);
					createRouteList();
					createRouteView();
				}
			}).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("移動").setMessage("移動しますか").setPositiveButton("上に移動", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Route cRoute = myRoutes.get(position - 1);
					myRoutes.set(position, cRoute);
					myRoutes.set(position - 1, route);
					createRouteList();
					createRouteView();
				}
			}).setNegativeButton("下に移動", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Route cRoute = myRoutes.get(position + 1);
					myRoutes.set(position, cRoute);
					myRoutes.set(position + 1, route);
					createRouteList();
					createRouteView();
				}
			}).setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

}
