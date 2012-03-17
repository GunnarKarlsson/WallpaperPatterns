package com.squidzoo.wallpaperColors.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.squidzoo.wallpaperColors.ItemArrayAdapter;
import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.R.id;
import com.squidzoo.wallpaperColors.R.layout;
import com.squidzoo.wallpaperColors.beans.CustomBean;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FavoritesActivity extends Activity {

	public static String MY_DEBUG_TAG = "my_debug_tag";
	TextView mTextView;
	ArrayList<CustomBean> mFavs;
	ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		getFavs(prefs);
	}
	
	public void onResume(){
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		getFavs(prefs);
	}

	private void getFavs(SharedPreferences prefs) {
		Log.d(MY_DEBUG_TAG,"getFavs");
		mFavs = new ArrayList<CustomBean>();
		//StringBuilder sb = new StringBuilder();
		Map<String, ?> keys = prefs.getAll();
			
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
				CustomBean currentItem = buildColor(entry.getValue().toString());
				
				if(null != currentItem){
					mFavs.add(currentItem);
				}
				
			showFavsList();
		}
	}
	
	
	private void showFavsList(){
		Log.d(MY_DEBUG_TAG,"show color list");
		final ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new ItemArrayAdapter(this,
				android.R.layout.simple_list_item_1, mFavs));
		progressBar.setVisibility(ProgressBar.GONE);
	}
	

	private CustomBean buildColor(String str) {
		CustomBean color = new CustomBean();
		List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
		
		if(items.size() == 5){
				
			color.setCreator(items.get(0));
			color.setHex(items.get(1));
			color.setId(items.get(2));
			color.setBadgeUrl(items.get(3));
			color.setName(items.get(4));
			
			return color;
			
		}else{
			return null;
		}
		
	}

}
