package com.squidzoo.wallpaperColors.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GetPatternImageTask extends AsyncTask<String,Void,Bitmap> {
	
	private HttpURLConnection connection;
	private Handler handler;
	
	public GetPatternImageTask(Handler handler){
		this.handler = handler;
	}
	
	@Override protected Bitmap doInBackground(String ...urls){
		try{
			BitmapFactory.Options bmOptions;
			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;
			Bitmap bm = LoadImage(urls[0], bmOptions);
			return bm;
		}catch(Exception e){
			return null;
		}finally{
			if(connection != null){
				connection.disconnect();
			}		
		}
	}

	@Override
	protected void onPostExecute(Bitmap bitmap){
		Message message = new Message();
		Bundle data = new Bundle();
		data.putParcelable("bitmap", bitmap);
		message.setData(data);
		handler.sendMessage(message);
	}
	
	private Bitmap LoadImage(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e) {
		}
		return bitmap;
	}

	private InputStream OpenHttpConnection(String strURL) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception e) {
		}
		return inputStream;
	}

}