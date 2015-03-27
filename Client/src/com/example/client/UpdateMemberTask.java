package com.example.client;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.TextView;

public class UpdateMemberTask extends AsyncTask<Integer, Integer, Boolean> {
	private Context context;
	private SQLiteDatabase db;
	private SharedPreferencesHelper spHelper;
	private ImageFileHelper ifHelper;
	private int currentVersion;
	private int nowVersion;
	private TextView textView = null;
	private Loading load;
	private int memberCount = 0;
	private int loadCount = 0;

	public UpdateMemberTask(Context context) {
		this.context = context;
		SQLiteOpenHelper helper = new SQLiteHelper(context.getApplicationContext(), "my.db", null, 1);
		db = helper.getWritableDatabase();
		spHelper = new SharedPreferencesHelper(context);
		ifHelper = new ImageFileHelper(context);
	}

	public UpdateMemberTask(Context context, TextView text) {
		this.context = context;
		this.textView = text;
		SQLiteOpenHelper helper = new SQLiteHelper(context.getApplicationContext(), "my.db", null, 1);
		db = helper.getWritableDatabase();
		spHelper = new SharedPreferencesHelper(context);
		ifHelper = new ImageFileHelper(context);
	}

	void setLoad(Loading load) {
		this.load = load;
	}

	void hide() {
		if (load != null) {
			load.hide();
		}
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		if (!spHelper.getRegistrationId().equals("")) {
			PostServerTask task = new PostServerTask(URLManager.GET_VERSION_URL) {
				@Override
				protected void onPostExecute(Boolean result) {
					if (taskResult) {
						XmlReader xr = new XmlReader(httpData);
						currentVersion = xr.getMemberVersion();
						nowVersion = spHelper.getMemberVersion();
						if (nowVersion != currentVersion) {
							addMember();
						} else {
							hide();
						}
					} else {
						hide();
					}
				}
			};
			task.execute();
		} else {
			hide();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (textView != null) {
			textView.setText("メンバー情報を更新しました");
		}
	}

	private void addMember() {
		PostServerTask task = new PostServerTask(URLManager.GET_MEMBER_URL) {
			@Override
			protected void onPostExecute(Boolean result) {
				if (taskResult) {
					XmlReader xr = new XmlReader(httpData);
					final ArrayList<Member> list = xr.getMemberData();

					if (list.size() != 0) {
						db.delete("MEMBER_TABLE", null, null);
						memberCount = list.size();

						for (int i = 0; i < list.size(); i++) {
							final Member m = list.get(i);
							if (m.regiId != null && m.myId != null && m.name != null && m.imageFileName != null) {
								ContentValues values = new ContentValues();
								values.put("REGI_ID", m.regiId);
								values.put("MY_ID", m.myId);
								values.put("NAME", m.name);
								values.put("IMAGE", m.imageFileName);
								db.insert("MEMBER_TABLE", null, values);

								String imageUrl = "http://kproject.php.xdomain.jp/chat/image/member_icon/" + m.imageFileName + ".jpg";
								ImageLoadTask task = new ImageLoadTask(imageUrl, context) {
									@Override
									protected void onPostExecute(Boolean result) {
										ifHelper.save(this.bitmap, m.myId);

										loadCount++;
										if (loadCount == memberCount) {
											hide();
										}
									}
								};
								task.execute();
							}
							hide();
						}

						spHelper.editMemberVersion(currentVersion);
					}
				} else {

				}
			}
		};
		task.execute();
	}

}
