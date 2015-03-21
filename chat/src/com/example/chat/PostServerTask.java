package com.example.chat;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PostServerTask extends AsyncTask<Integer, Integer, Boolean> {
	private final String TAG = "";
	
	private String url = "";
	public ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	
	private DefaultHttpClient client;
	
	public String httpData = null;
	public boolean taskResult = false;
	
	public PostServerTask(String url) {
		this.url = url;
	}
	
	@Override
	protected Boolean doInBackground(Integer... arg0) {
		try {
            URI uri = new URI(url);
            HttpPost request = new HttpPost(uri);
            request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            
            if (status == HttpStatus.SC_OK) {
            	taskResult = true;
            	httpData = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else if (status == HttpStatus.SC_NOT_FOUND){
                Log.d(TAG, "no data");
                taskResult = false;
            } else {
            	Log.d(TAG, "status code = " + status);
                taskResult = false;
            }
            
        } catch (URISyntaxException e) {
        	Log.e(TAG, e.toString());
        } catch (ClientProtocolException e) {
        	Log.e(TAG, e.toString());
        } catch (ConnectException e) {
        	Log.e(TAG, e.toString());
        } catch( IllegalArgumentException e ) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } finally {
        	client.getConnectionManager().shutdown();
        }
		
		return null;
	}
	
	public void setPostData(String key, String value) {
		params.add(new BasicNameValuePair(key, value));
	}
	
	public void setPostData(String key, int value) {
		params.add(new BasicNameValuePair(key, Integer.toString(value)));
	}
	
	public void setPostData(String key, double value) {
		params.add(new BasicNameValuePair(key, Double.toString(value)));
	}
	
	public void setPostArrayData(String key, ArrayList<String> list) {
		for (int i =0 ; i < list.size(); i++) {
			params.add(new BasicNameValuePair(key, list.get(i)));
		}
	}
	
	public static void checkCocnection(Context c) {
    	final Context context = c;
    	PostServerTask psTask = new PostServerTask(URLManager.CHECK_SERVER_URL){
        	
    		@Override
        	protected void onPostExecute(Boolean result) {
        		if (!taskResult) {
        			Toast.makeText(context, "ネットワークに接続されてません", Toast.LENGTH_SHORT).show();
        		}
        	}	
        };
        psTask.execute();
    }
	
}
