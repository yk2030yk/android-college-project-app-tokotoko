package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditGuideMemoActivity extends Activity {
	private String guideId = "";
	private Context context;
	private Loading loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_guide_memo);
		getActionBar().hide();

		context = this;

		final EditText et = (EditText) findViewById(R.id.editText1);
		Button btn = (Button) findViewById(R.id.button1);

		guideId = getIntent().getStringExtra(IntentKey.GUIDE_ID);
		String memo = getIntent().getStringExtra("Memo");
		et.setText(memo);

		loading = new Loading(this, "ÉÅÉÇÇÃï€ë∂íÜÇ≈Ç∑...");

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String memo = et.getText().toString();
				if (!memo.equals("")) {
					loading.show();

					PostServerTask task = new PostServerTask(URLManager.EDIT_GUIDE_MEMO_URL) {

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							if (this.taskResult) {
								finish();
							} else {
								Toast.makeText(context, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
							}
							loading.hide();
						}

					};
					task.setPostData("GuideId", guideId);
					task.setPostData("Memo", memo);
					task.execute();
				}
			}
		});
	}

}
