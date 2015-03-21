package com.example.chat;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	public final static String PROJECT_ID = "883134891257";
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
	private SharedPreferencesHelper spHelper;
	private TextView infoText;
	private EditText nameEdit, myidEdit;
	private Button signUpButton;
	private Button backButton;
	private String userName = "";
	private String userId = "";
	private String registrationId = "";
	
	public GoogleCloudMessaging gcm;
	private Context context;
	
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		context = this;
		spHelper = new SharedPreferencesHelper(this);
		
		if (spHelper.getMemberVersion() == 0) {
			ImageFileHelper sif = new ImageFileHelper(this);
			Resources res = this.getResources();
			Member m = Member.getSystemMember();
			Bitmap bitmap = BitmapFactory.decodeResource(res, m.resId);
			sif.save(bitmap, m.myId);
			m = Member.getUnknownMember();
			bitmap = BitmapFactory.decodeResource(res, m.resId);
			sif.save(bitmap, m.myId);
		}
		
		infoText = (TextView)findViewById(R.id.textView_info);
		nameEdit = (EditText)findViewById(R.id.editText_name);
		myidEdit = (EditText)findViewById(R.id.editText_myid);
		signUpButton = (Button)findViewById(R.id.button_sign_up);
		backButton = (Button)findViewById(R.id.button_back);
		nameEdit.setText(spHelper.getUserName());
		myidEdit.setText(spHelper.getUserId());
		infoText.setText("");
		signUpButton.setEnabled(false);
		
		if (!spHelper.getUserId().equals("")) {
			myidEdit.setEnabled(false);
		}
		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userName = nameEdit.getText().toString();
				userId = myidEdit.getText().toString();
				registrationId = spHelper.getRegistrationId();
				
				if (checkPlayServices()) {
		        	int appVersion = spHelper.getAppVersion();
		    	    int currentVersion = getAppVersion(context);
		    	    if (appVersion != currentVersion) {
		    	    	spHelper.editRegistrationId("");
		    	    }
		    	    
		    		if (registrationId.equals("")) {
		    			infoText.setText("google play serviceに登録中です...");
		    			signUpGCM();
		            } else {
		            	signUpAccount(userName, userId, registrationId);
		            }
		    	    
		        } else {
		        	infoText.setText("この端末では Google Play Service が利用できません\n登録ができません");
		        }
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
    
    @Override
	protected void onStart() {
		checkConnection();
		super.onStart();
	}
    
    @Override
	public void finish() {
		String uname = spHelper.getUserName();
		String uid = spHelper.getUserId();
		String rid = spHelper.getRegistrationId();
		if (!uname.equals("") && !uid.equals("") && !rid.equals("")) {
			super.finish();
		} else {
			Toast.makeText(context, "登録を完了してください", Toast.LENGTH_SHORT).show();
		}
	}
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
	
    private void checkConnection() {
    	signUpButton.setEnabled(false);
    	
    	PostServerTask psTask = new PostServerTask(URLManager.CHECK_SERVER_URL) {
        	
    		@Override
        	protected void onPostExecute(Boolean result) {
        		if (taskResult) {
        			signUpButton.setEnabled(true);
        		} else {
        			signUpButton.setEnabled(false);
        			infoText.setText("インターネットに接続ができません\n設定を確認してください");
        		}
        	}
    		
        };
        psTask.execute();
    }
    
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {	
            }
            return false;
        }
        return true;
    }
     
    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("package not found : " + e);
        }      
    }
    
    private ArrayList<String> checkValidation(String name, String id, String regiId) {
    	ArrayList<String> result = new ArrayList<String>();
    	if (name.equals("")) {
    		result.add("ユーザー名が未入力です");
    	}
    	
    	if (id.equals("")) {
    		result.add("ユーザーIDが未入力です");
    	} else {
    		if (!id.matches("^[0-9A-Za-z]+$")) {
    			result.add("ユーザーIDに半角英数字以外が含まれています");
    		}
    	}
    	
    	if (regiId.equals("")) {
    		result.add("GCMの登録に失敗してます");
    	}
    	
    	return result;
    }
    
    private void signUpGCM() {
    	AsyncTask<Void, Void, String> registTask = new AsyncTask<Void, Void, String>() {
        	@Override
            protected String doInBackground(Void... params) {
        		gcm = GoogleCloudMessaging.getInstance(context);
                try {
                    registrationId = gcm.register(PROJECT_ID);
                } catch (IOException e) {
                    e.printStackTrace();
                }   
                return null;
        	}

        	@Override
        	protected void onPostExecute(String result) {
        		spHelper.editRegistrationId(registrationId);
        		spHelper.editAppVersion(getAppVersion(context));
        		
                PostServerTask postTask = new PostServerTask(URLManager.REGISTER_GCM_URL) {
                	@Override
                	protected void onPostExecute(Boolean result) {
                		if (taskResult) {
                			userName = nameEdit.getText().toString();
            				userId = myidEdit.getText().toString();
            				registrationId = spHelper.getRegistrationId();
                			signUpAccount(userName, userId, registrationId);
                		} else {
                			infoText.setText("google play serviceの登録に失敗しました");
                			spHelper.editRegistrationId("");
                		}
                	}
                };
                postTask.setPostData("regi_id", registrationId) ;
                postTask.execute();          
        	}
        };
        registTask.execute(null, null, null);
    }
    
    private void signUpAccount(String uname, String uid, String regiId){
    	infoText.setText("アカウント登録中...");
    	final String name = uname;
    	final String id = uid;
    	ArrayList<String> result = checkValidation(uname, uid, regiId);
    	if (result.size() == 0) {
    		
    		PostServerTask task = new PostServerTask(URLManager.SIGN_UP_URL) {
    			@Override
            	protected void onPostExecute(Boolean result) {
            		if (taskResult) {
            			spHelper.editUserName(name);
            			spHelper.editUserId(id);
            			infoText.setText("アカウントの登録が完了しました");
            			infoText.setText("メンバー情報を更新しています");
            			(new UpdateMemberTask(context, infoText)).execute();
            		} else {
            			infoText.setText("アカウントの登録に失敗しました");
            		}
            	}
			};
			task.setPostData("name", uname);
			task.setPostData("my_id", uid);
			task.setPostData("regi_id", regiId);
			task.execute();
    	} else {
    		String res = "アカウントの登録に失敗しました";
    		for (int i = 0; i < result.size(); i++) {
    			res += "\n" + result.get(i);
    		}
    		infoText.setText(res);
    	}
    }
    
}
