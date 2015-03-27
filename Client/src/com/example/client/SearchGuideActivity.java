package com.example.client;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class SearchGuideActivity extends Activity {
	private EditText et;
	private Spinner season, stay;
	private HashMap<String, String> map = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_guide);
		getActionBar().hide();

		et = (EditText) findViewById(R.id.editText1);
		season = (Spinner) findViewById(R.id.spinner_season);
		stay = (Spinner) findViewById(R.id.spinner_stay);
		Button search = (Button) findViewById(R.id.button1);
		ImageView recomend1 = (ImageView) findViewById(R.id.imageButton1);
		ImageView recomend2 = (ImageView) findViewById(R.id.imageButton2);
		ImageView recomend4 = (ImageView) findViewById(R.id.imageButton4);

		search.setFocusable(true);
		search.requestFocus();

		map.put("‹Gß‘I‘ğ", null);
		map.put("t", "100");
		map.put("‰Ä", "200");
		map.put("H", "300");
		map.put("“~", "400");
		map.put("—·s“ú”‘I‘ğ", null);
		map.put("1“ú", "1");
		map.put("2“ú", "2");
		map.put("3“ú", "3");
		map.put("4“ú", "4");
		map.put("5“úˆÈã", "5");

		ArrayAdapter<String> adapterSeason = new ArrayAdapter<String>(this, R.layout.spinner_item);
		adapterSeason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterSeason.add("‹Gß‘I‘ğ");
		adapterSeason.add("t");
		adapterSeason.add("‰Ä");
		adapterSeason.add("H");
		adapterSeason.add("“~");
		season.setAdapter(adapterSeason);
		season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		ArrayAdapter<String> adapterStay = new ArrayAdapter<String>(this, R.layout.spinner_item);
		adapterStay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterStay.add("—·s“ú”‘I‘ğ");
		adapterStay.add("1“ú");
		adapterStay.add("2“ú");
		adapterStay.add("3“ú");
		adapterStay.add("4“ú");
		adapterStay.add("5“úˆÈã");
		stay.setAdapter(adapterStay);
		stay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SearchGuideActivity.this, SearchGuideResultListActivity.class);
				String key = et.getText().toString();
				if (!key.equals("")) {
					i.putExtra("KEY", key);
				} else {
					i.putExtra("KEY", "");
				}
				String item = (String) season.getSelectedItem();
				String seasonValue = map.get(item);
				i.putExtra("SEASON", seasonValue);
				item = (String) stay.getSelectedItem();
				String stayValue = map.get(item);
				i.putExtra("STAY", stayValue);

				boolean isData = true;
				if (key.equals("") && seasonValue == null && stayValue == null) {
					isData = false;
				}
				if (isData) {
					startActivity(i);
				}

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});

		recomend1.setOnClickListener(new View.OnClickListener() {
			Intent i = new Intent(SearchGuideActivity.this, SearchGuideResultListActivity.class);

			@Override
			public void onClick(View v) {
				String key = "‹àŠt›";
				i.putExtra("KEY", key);
				i.putExtra("SEASON", "400");
				startActivity(i);
			}
		});

		recomend2.setOnClickListener(new View.OnClickListener() {
			Intent i = new Intent(SearchGuideActivity.this, SearchGuideResultListActivity.class);

			@Override
			public void onClick(View v) {
				String key = "‹âŠt›";
				i.putExtra("KEY", key);
				i.putExtra("SEASON", "300");
				startActivity(i);
			}
		});

		recomend4.setOnClickListener(new View.OnClickListener() {
			Intent i = new Intent(SearchGuideActivity.this, SearchGuideResultListActivity.class);

			@Override
			public void onClick(View v) {
				String key = "´…›";
				i.putExtra("KEY", key);
				i.putExtra("SEASON", "400");
				startActivity(i);
			}
		});
	}

}