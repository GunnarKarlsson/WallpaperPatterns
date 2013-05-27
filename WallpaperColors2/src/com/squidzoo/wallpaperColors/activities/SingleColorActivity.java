package com.squidzoo.wallpaperColors.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.types.ItemType;
import com.squidzoo.wallpaperColors.utils.FavoritesManager;
import com.squidzoo.wallpaperColors.utils.SDCardSaver;
import com.squidzoo.wallpaperColors.utils.ToastMaker;

public class SingleColorActivity extends Activity implements OnClickListener {
	public static String MY_DEBUG_TAG = "my_debug_tag";
	WallpaperManager mWallpaperManager;
	ImageView mImageView;
	int mColorInt;
	Bitmap mBitmap;
	String mIdValue;
	CustomBean mItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_item_activity);
		ProgressBar pgb = (ProgressBar)findViewById(R.id.progressBar);
		pgb.setVisibility(ProgressBar.GONE);
		setTitle("  Wallpaper Colors HD");
		Intent intent = getIntent();

		mItem = new com.squidzoo.wallpaperColors.beans.CustomBean();
		
		if(intent.hasExtra("type")){
			String type = intent.getStringExtra("type");
			if(type.equalsIgnoreCase(ItemType.COLOR.toString())){
				mItem.setType(ItemType.COLOR);
			}else{
				mItem.setType(ItemType.PATTERN);
			}
		}
		
		if (intent.hasExtra("creator")) {
			mItem.setCreator(intent.getStringExtra("creator"));
		}
		if (intent.hasExtra("idvalue")) {
			mItem.setId(intent.getStringExtra("idvalue"));
		}
		if (intent.hasExtra("badgeurl")) {
			mItem.setBadgeUrl(intent.getStringExtra("badgeurl"));
		}
		if (intent.hasExtra("imageurl")) {
			mItem.setImageUrl(intent.getStringExtra("imageurl"));
		}
		if (intent.hasExtra("name")) {
			mItem.setName(intent.getStringExtra("name"));
		}

		if (getIntent().hasExtra("hex")) {
			mItem.setHex(intent.getStringExtra("hex"));

			mImageView = (ImageView) findViewById(R.id.imageviewSingleItem);
			String extraString = getIntent().getStringExtra("hex");
			String hexString = "#" + extraString;
			mColorInt = Color.parseColor(hexString);
			mImageView.setDrawingCacheEnabled(true);
			mImageView.setBackgroundColor(mColorInt);
		}
		if (getIntent().hasExtra("idvalue")) {
			mIdValue = getIntent().getStringExtra("idvalue");
		}

		mWallpaperManager = WallpaperManager.getInstance(this);

		Button saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(this);
		Button setButton = (Button) findViewById(R.id.set_wallpaper_button);
		setButton.setOnClickListener(this);
		Button favsButton = (Button) findViewById(R.id.adjust_favs);
		favsButton.setOnClickListener(this);

		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

		if (prefs.contains(mItem.getId().toString())) {
			favsButton.setText("Remove from Favs");
		} else {
			favsButton.setText("Add to favs");
		}

	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.save_button:
			String newPicture = SDCardSaver.saveBitmapToSDCard(getBitmap(), this);
			startMediaScanner(newPicture);
			break;
		case R.id.set_wallpaper_button:
			setWallpaper();
			break;
		case R.id.adjust_favs:
			setFavs();
		}
	}

	private void setFavs() {
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		if (prefs.contains(mItem.getId().toString())) {
			FavoritesManager.removeFromFavs(prefs, mItem, this);
			Button favsButton = (Button) findViewById(R.id.adjust_favs);
			favsButton.setText("Add to favs");
		} else {
			FavoritesManager.addToFavs(prefs, mItem, this);
			Button favsButton = (Button) findViewById(R.id.adjust_favs);
			favsButton.setText("Remove from Favs");	
		}
	}

	private void startMediaScanner(String addedPicture) {
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + addedPicture)));
	}

	private void setWallpaper() {
		try {
			mWallpaperManager.setBitmap(getBitmap());
			ToastMaker.makeToast(this, "New wallpaper has been set");

		} catch (IOException e) {
			ToastMaker.makeToast(this, "Wallpaper could not be set");
		}
	}

	private Bitmap getBitmap() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Bitmap bitmapFromView = mImageView.getDrawingCache();
		mBitmap = Bitmap.createScaledBitmap(bitmapFromView, width, height,
				false);
		return mBitmap;
	}
}
