package com.example.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class UpdateBroadcastReceiver extends BroadcastReceiver {
	private Handler handler;
	private Runnable runnable;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (handler != null) {
			if (runnable != null) {
				handler.post(runnable);
			}
		}
	}
	
	public void setHandler(Handler handler) {
	    this.handler = handler;
	}
	
	public void setRunnable(Runnable runnable) {
	    this.runnable = runnable;
	}
}
