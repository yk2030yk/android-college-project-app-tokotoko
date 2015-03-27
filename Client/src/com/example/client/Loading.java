package com.example.client;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Loading {
	public AlertDialog dialog;

	public Loading(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_loading, null);
		TextView t = (TextView) layout.findViewById(R.id.textView);
		t.setText(text);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);
		dialog = builder.create();
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}
}
