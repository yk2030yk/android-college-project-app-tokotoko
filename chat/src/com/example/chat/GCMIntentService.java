package com.example.chat;

import java.util.ArrayList;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    
    public GCMIntentService() {
        super("GCMIntentService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
 
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("GCM INTENT SERVICE","messageType(error): " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("GCM INTENT SERVICE","messageType(deleted): " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.e("GCM INTENT SERVICE","messageType(message): " + messageType + ",body:" + extras.toString());
                
                String msg = extras.getString("message");
                String postedRegistrationId = extras.getString("senderId");
                String kind = extras.getString("kind");
                String spotId = extras.getString("spotId");
                String time = extras.getString("time");
                String gCateId = extras.getString("gourmetCateId");
                String gAreaId = extras.getString("gourmetAreaId");
                String pos = extras.getString("position");
                
                SharedPreferencesHelper sph = new SharedPreferencesHelper(this);
                String myRegistrationId = sph.getRegistrationId();
                
                //kind=0:自分, kind=1:メンバー, kind=2:観光地, kind=3:グルメ
                if (myRegistrationId.equals(postedRegistrationId)) {
                	addData(postedRegistrationId, msg, "0", "-1", time);
                } else {
                	if (kind.equals("2")) {
                		addData(postedRegistrationId, msg, kind, spotId, time);
                	} else if (kind.equals("3")) {
                		addGourmetData(gAreaId, gCateId, time, pos);
                	} else {
                		addData(postedRegistrationId, msg, kind, spotId, time);
                	}
                	
                	if (sph.getNotificationMode()) {
                		sendNotification(msg, Integer.parseInt(kind));
                    }
                }
                
                int cnt = sph.getCount();
                cnt++;
                sph.editCount(cnt);
                
                Intent updateService = new Intent(this, UpdateIntentService.class);
        		startService(updateService);
            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);  
    }
    
    private void sendNotification(String msg, int kind) {
    	int imageId = R.drawable.icon_app3;
    	if (kind == 2) {
    		imageId = R.drawable.icon_system3;
    	}
    	
        notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Intent intent = new Intent(this, ChatActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        .setSmallIcon(imageId)
        .setContentTitle("メッセージ")
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg);

        builder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    
    private void addData(String regiId, String talk, String kind, String spotId, String time) {
    	SQLiteOpenHelper helper = new SQLiteHelper(getApplicationContext(), "my.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("REGI_ID", regiId);
		values.put("TALK", talk);
		values.put("KIND", kind);
		values.put("SPOT_ID", spotId);
		values.put("TIME", time);
		db.insert("CHAT_TABLE", null, values);
	}
    
    private void addGourmetData(String area, String cate, String t, String pos) {
    	final String time = t;
    	final String p = pos;
    	LocaTouchApiURLCreator creator = new LocaTouchApiURLCreator();
    	
    	if (area != null) {
    		if (!area.equals("")) {
    			creator.setArea(area);
    		}
    	} else {
    		creator.setArea(LocaTouchApiURLCreator.AREA_DEFAULT);
    	}
    	
    	if (cate != null) {
    		if (!cate.equals("")) {
    			creator.setCategory(cate);
    		}
    	}
    	creator.setSortType(LocaTouchApiURLCreator.SORT_TYPE_TOTAL);
    	creator.setPage("1");
    	
    	PostServerTask task = new PostServerTask(creator.createUrl()) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				XmlReader xr = new XmlReader(this.httpData);
				ArrayList<Gourmet> list = xr.getGourmetLocaTouch();
				int position = 1;
				if (p != null) {
		    		if (!p.equals("")) {
		    			position = Integer.parseInt(p);
		    		}
		    	}
				if (list.size() > position) {
					int itemPosition = position;
					Gourmet g = list.get(itemPosition);
					String msg = g.name;
					addData("-1", msg, "3", g.gourmetId, time);
					
					Intent updateService = new Intent(GCMIntentService.this, UpdateIntentService.class);
	        		startService(updateService);
				}
			}
    		
    	};
    	task.execute();
    }
    
}