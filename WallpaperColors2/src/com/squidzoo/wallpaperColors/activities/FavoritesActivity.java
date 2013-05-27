package com.squidzoo.wallpaperColors.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.adapters.ItemArrayAdapter;
import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.types.ItemType;
import com.squidzoo.wallpaperColors.utils.ToastMaker;

public class FavoritesActivity extends Activity {

	public static String MY_DEBUG_TAG = "my_debug_tag";
	TextView mTextView;
	ArrayList<CustomBean> mFavs;
	ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list_favs);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		getFavs(prefs);
	}

	public void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		getFavs(prefs);
	}

	private void getFavs(SharedPreferences prefs) {
		mFavs = new ArrayList<CustomBean>();
		Map<String, ?> keys = prefs.getAll();
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			CustomBean currentItem = buildItem(entry.getValue().toString());

			if (null != currentItem) {
				mFavs.add(currentItem);
			}

			showFavsList();
		}
	}

	private void showFavsList() {
		if(mFavs.size() < 1){
			progressBar.setVisibility(ProgressBar.GONE);
			
			ToastMaker.makeToast(this, "No favorites added");
		}else{
		
		final ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new ItemArrayAdapter(this,
				android.R.layout.simple_list_item_1, mFavs));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CustomBean item = (CustomBean) listView
						.getItemAtPosition(position);
				
				Intent intent;
				if (item.getType().toString()
						.equalsIgnoreCase(ItemType.COLOR.toString())) {

					intent = new Intent(v.getContext(),
							SingleColorActivity.class);
				} else {
					intent = new Intent(v.getContext(),
							SinglePatternActivity.class);
				}

				intent.putExtra("hex", item.getHex().toString());
				intent.putExtra("idvalue", item.getId().toString());
				intent.putExtra("imageurl", item.getImageUrl().toString());
				intent.putExtra("badgeurl", item.getBadgeUrl().toString());
				intent.putExtra("name", item.getName().toString());
				intent.putExtra("creator", item.getCreator().toString());
				intent.putExtra("type", item.getType().toString());
				startActivity(intent);
			}
		});
		
		
		progressBar.setVisibility(ProgressBar.GONE);
		
		}
		
		
	}

	private CustomBean buildItem(String str) {
		CustomBean item = new CustomBean();
		List<String> items = Arrays.asList(str.split("\\s*,\\s*"));

		if (items.size()== 7) {

			item.setCreator(items.get(0));
			item.setHex(items.get(1));
			item.setId(items.get(2));
			item.setBadgeUrl(items.get(3));
			item.setName(items.get(4));
			if(items.get(5).toString().equalsIgnoreCase(ItemType.COLOR.toString())){
				item.setType(ItemType.COLOR);
			}else{				
				item.setType(ItemType.PATTERN);
			}
			item.setImageUrl(items.get(6));

			return item;

		} else {
			return null;
		}

	}

}
