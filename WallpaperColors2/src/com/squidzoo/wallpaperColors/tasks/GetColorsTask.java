package com.squidzoo.wallpaperColors.tasks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.utils.ColorXmlPullParser;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetColorsTask extends AsyncTask<String,Void,ArrayList<CustomBean>> {
	
	private HttpURLConnection connection;
	private Handler handler;
	
	public GetColorsTask(Handler handler){
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
			
			ArrayList<CustomBean> items = new ArrayList<CustomBean>();
			items = ColorXmlPullParser.parseItem(connection.getInputStream());
			
			return items;
		}catch(Exception e){
			return null;
		}finally{
			if(connection != null){
				connection.disconnect();
			}
			
		}
	}

	@Override
	protected void onPostExecute(ArrayList<CustomBean> items){
		Message message = new Message();
		Bundle data = new Bundle();
		data.putParcelableArrayList("itemsArrayList", items);
		message.setData(data);
		handler.sendMessage(message);
	}

}
