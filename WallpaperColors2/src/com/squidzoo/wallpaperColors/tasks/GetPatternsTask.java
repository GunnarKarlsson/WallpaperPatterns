package com.squidzoo.wallpaperColors.tasks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.utils.PatternXmlPullParser;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetPatternsTask extends AsyncTask<String,Void,ArrayList<CustomBean>> {
	
	private HttpURLConnection connection;
	private Handler handler;
	
	public GetPatternsTask(Handler handler){
		this.handler = handler;
	}
	
	@Override protected ArrayList<CustomBean> doInBackground(String... params){
		try{
			URL url = new URL(params[0]);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int statusCode = connection.getResponseCode();
			
			if(statusCode != HttpURLConnection.HTTP_OK){
				return null;
			}
			
			ArrayList<CustomBean> patterns = new ArrayList<CustomBean>();
			patterns = PatternXmlPullParser.parsePattern(connection.getInputStream());
			
			return patterns;
		}catch(Exception e){
			return null;
		}finally{
			if(connection != null){
				connection.disconnect();
			}
			
		}
	}

	@Override
	protected void onPostExecute(ArrayList<CustomBean> patterns){
		Message message = new Message();
		Bundle data = new Bundle();
		data.putParcelableArrayList("itemsArrayList", patterns);
		message.setData(data);
		handler.sendMessage(message);
	}

}
