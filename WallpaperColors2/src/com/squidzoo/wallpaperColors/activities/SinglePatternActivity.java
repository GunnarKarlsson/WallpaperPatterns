package com.squidzoo.wallpaperColors.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.tasks.GetPatternImageTask;
import com.squidzoo.wallpaperColors.types.ItemType;
import com.squidzoo.wallpaperColors.utils.FavoritesManager;
import com.squidzoo.wallpaperColors.utils.SDCardSaver;

public class SinglePatternActivity extends Activity implements OnClickListener, Callback{

	public static String MY_DEBUG_TAG = "my_debug_tag";
	ProgressBar mProgressBar;
	WallpaperManager mWallpaperManager;
	ImageView mImageView;
	int mColor;
	Bitmap mBitmap;
	CustomBean mItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_item_activity);
		setTitle("  Wallpaper Colors HD");
		Intent intent = getIntent();
		mItem = new com.squidzoo.wallpaperColors.beans.CustomBean();
		
		mWallpaperManager = WallpaperManager.getInstance(this);
		mImageView = (ImageView) findViewById(R.id.imageviewSingleItem);
		
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			if (getIntent().hasExtra("imageurl")) {
				String imageUrl = getIntent().getStringExtra("imageurl");
				getImage(imageUrl);	
			}
		} else {
			Bitmap bitmap = (Bitmap)data;
			mBitmap = bitmap;
			setPattern(bitmap);
		}
		
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
	
	private void getImage(String imageUrl){
		try{
			new GetPatternImageTask(new Handler(this)).execute(imageUrl);
		}catch(Exception e){
			showErrorToRetrieveToast();
		}
	}

	private void showErrorToRetrieveToast(){
		Toast msg = Toast.makeText(this, "wallpaper could not be retrieved",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
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

			Toast msg = Toast.makeText(this, "New wallpaper has been set",
					Toast.LENGTH_LONG);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();

		} catch (IOException e) {
			Toast msg = Toast.makeText(this,
					"wallpaper could not be set", Toast.LENGTH_SHORT);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();
		}
	}

	private Bitmap getBitmap() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		mImageView.setDrawingCacheEnabled(true);
		Bitmap bitmapFromView = mImageView.getDrawingCache();
		Bitmap bitmap = Bitmap.createScaledBitmap(bitmapFromView, width, height,
				false);
		return bitmap;
	}
	
	public boolean handleMessage(Message msg) {
		
		ProgressBar pgb = (ProgressBar)findViewById(R.id.progressBar);
		pgb.setVisibility(ProgressBar.GONE);
		
		try{
		Bitmap bitmap = msg.getData().getParcelable("bitmap");
		mBitmap = bitmap;//saved in field for reorientation activity management
		setPattern(bitmap);
		}catch(Exception e){
			showErrorToRetrieveToast();
		}
		
		
		
		return false;
	}
	
	private void setPattern(Bitmap bitmap){
		
		BitmapDrawable bmd = new BitmapDrawable(bitmap);
		  
		  bmd.setTileModeX(TileMode.REPEAT);
		  bmd.setTileModeY(TileMode.REPEAT);

		  mImageView.setBackgroundDrawable(bmd);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		final Bitmap bitmap  = mBitmap;
		return bitmap;
	}

}
