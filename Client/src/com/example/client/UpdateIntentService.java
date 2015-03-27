package com.example.client;

import android.app.IntentService;
import android.content.Intent;

public class UpdateIntentService extends IntentService {

	public UpdateIntentService() {
		super("UpdateIntentService");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ChatActivity.MY_ACTION_UPDATE);
		getBaseContext().sendBroadcast(broadcastIntent);
	}

}
