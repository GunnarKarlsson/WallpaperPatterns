package com.squidzoo.wallpaperColors.activities;

import java.util.ArrayList;

import com.squidzoo.wallpaperColors.ItemArrayAdapter;
import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.R.id;
import com.squidzoo.wallpaperColors.R.layout;
import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.tasks.GetColorsTask;
import com.squidzoo.wallpaperColors.tasks.GetPatternsTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ItemListActivity extends Activity implements Callback {

	public enum ItemType {
		COLOR, PATTERN
	};

	private ItemType mItemType;
	private String mTopUrl;
	private String mNewUrl;
	public static String MY_DEBUG_TAG = "my_debug_tag";
	ProgressBar progressBar;
	WallpaperManager wallpaperManager;
	ArrayList<CustomBean> mItems;

	TextView responseText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);

		setType();
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);

		wallpaperManager = WallpaperManager.getInstance(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			getItems();
		} else {
			mItems = (ArrayList<CustomBean>) data;
			setList();
		}
	}

	protected void setType() {
		//Override
	}

	protected void setItemType(ItemType value) {
		mItemType = value;
	}

	public void setTopUrl(String value) {
		mTopUrl = value;
	}

	public void setNewUrl(String value) {
		mNewUrl = value;
	}

	public String getTopUrl() {
		return mTopUrl;
	}

	public String getNewUrl() {
		return mNewUrl;
	}

	private void getItems() {
		if (mItemType == ItemType.COLOR) {
			
			mTopUrl = "http://www.colourlovers.com/api/colors/top?numResults=100";
			mNewUrl = "http://www.colourlovers.com/api/colors/new?numResults=100";
			new GetColorsTask(new Handler(this)).execute(mTopUrl);
		} else {
			
			mTopUrl = "http://www.colourlovers.com/api/patterns/top?numResults=100";
			mNewUrl = "http://www.colourlovers.com/api/patterns/new?numResults=100";
			new GetPatternsTask(new Handler(this)).execute(mTopUrl);
		}
	}

	public boolean handleMessage(Message msg) {
		mItems = msg.getData().getParcelableArrayList("itemsArrayList");
		
		if(mItems == null || mItems.size() < 1){
			Toast toast = Toast.makeText(this, "No item to load",
					Toast.LENGTH_SHORT);

			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
					toast.getYOffset() / 2);

			toast.show();
			
			progressBar.setVisibility(ProgressBar.GONE);
			
		}else{			
			setList();
		}
		return false;
	}

	private void setList() {
		final ListView listView = (ListView) findViewById(R.id.list);
		// TODO check mColors isn't empty or null
		listView.setAdapter(new ItemArrayAdapter(this,
				android.R.layout.simple_list_item_1, mItems));

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CustomBean color = (CustomBean) listView
						.getItemAtPosition(position);
				ImageView imageView = (ImageView) parent
						.findViewById(R.id.itemImageView);
				Log.d(MY_DEBUG_TAG, color.getHex() + "was clicked");

				View view = listView.getAdapter().getView(position, v, parent);
				/*
				 * THIS GET THE --WRONG-- DRAWABLE. NOT RIGHT POSITION Drawable
				 * drawable = imageView.getDrawable(); Bitmap bitmap =
				 * ((BitmapDrawable)drawable).getBitmap(); Intent intent = new
				 * Intent(v.getContext(), SingleColorActivity.class);
				 * intent.putExtra("image",bitmap);
				 */
				Intent intent = new Intent(v.getContext(),
						SingleColorActivity.class);
				intent.putExtra("hex", color.getHex().toString());
				intent.putExtra("idvalue", color.getId().toString());
				intent.putExtra("imageurl", color.getImageUrl().toString());
				intent.putExtra("badgeurl", color.getBadgeUrl().toString());
				intent.putExtra("name", color.getName().toString());
				intent.putExtra("creator", color.getCreator().toString());
				startActivity(intent);
			}
		});

		progressBar.setVisibility(ProgressBar.GONE);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final ArrayList<CustomBean> list = mItems;
		return list;
	}
}
