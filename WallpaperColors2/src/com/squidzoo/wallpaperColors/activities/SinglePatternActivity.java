package com.squidzoo.wallpaperColors.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.R.id;
import com.squidzoo.wallpaperColors.R.layout;
import com.squidzoo.wallpaperColors.beans.ICustomBean;
import com.squidzoo.wallpaperColors.tasks.GetPatternImageTask;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SinglePatternActivity extends Activity implements OnClickListener,Callback {

	public static String MY_DEBUG_TAG = "my_debug_tag";
	ProgressBar mProgressBar;
	WallpaperManager mWallpaperManager;
	ImageView mImageView;
	int mColor;
	Bitmap mBitmap;
	ICustomBean mPattern;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_pattern);

		if (getIntent().hasExtra("imageurl")) {
			Log.d(MY_DEBUG_TAG, "intent has imageUrl");
			mImageView = (ImageView) findViewById(R.id.imageviewSinglePattern);
			String imageUrl = getIntent().getStringExtra("imageurl");
			getImage(imageUrl);
			
		}

		mWallpaperManager = WallpaperManager.getInstance(this);

		Button saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(this);
		Button setButton = (Button) findViewById(R.id.set_wallpaper_button);
		setButton.setOnClickListener(this);

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
			String newPicture = saveToSDCard();
			startMediaScanner(newPicture);
			break;
		case R.id.set_wallpaper_button:
			setWallpaper();
			break;
		}
	}

	private String saveToSDCard() {
		// get random file name
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fileName = "Wallpaper-" + n + ".jpg";

		StringBuffer createdFile = new StringBuffer();
		File externalStorageFile = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				fileName);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		byte b[] = bytes.toByteArray();

		try {
			externalStorageFile.createNewFile();
			OutputStream filoutputStream = new FileOutputStream(
					externalStorageFile);
			filoutputStream.write(b);
			filoutputStream.flush();
			filoutputStream.close();
			createdFile.append(externalStorageFile.getAbsolutePath());

			Toast msg = Toast.makeText(this,
					"wallpaper has been saved as: "+fileName, Toast.LENGTH_SHORT);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();

		} catch (Exception e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(this, "wallpaper could not be saved",
					Toast.LENGTH_LONG);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();
		}

		return createdFile.toString();

	}

	private void startMediaScanner(String addedPicture) {
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + addedPicture)));
	}

	private void setWallpaper() {
		try {
			// Set bitmap it as wallpaper
			mWallpaperManager.setBitmap(getBitmap());

			Toast msg = Toast.makeText(this, "New wallpaper has been set",
					Toast.LENGTH_LONG);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();

			// finish();
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
		// Resize bitmap
		mImageView.setDrawingCacheEnabled(true);
		Bitmap bitmapFromView = mImageView.getDrawingCache();
		mBitmap = Bitmap.createScaledBitmap(bitmapFromView, width, height,
				false);
		return mBitmap;
	}
	
	public boolean handleMessage(Message msg) {
		
		try{
		Bitmap bitmap = msg.getData().getParcelable("bitmap");
		
		  BitmapDrawable bmd = new BitmapDrawable(bitmap);
		  
		  bmd.setTileModeX(TileMode.REPEAT);
		  bmd.setTileModeY(TileMode.REPEAT);

		  mImageView.setBackgroundDrawable(bmd);
		}catch(Exception e){
			showErrorToRetrieveToast();
		}
		
		
		
		return false;
	}
	
	private void addToFavs(SharedPreferences prefs) {
		String serializedColor = mPattern.getCreator() + "," + mPattern.getHex()
				+ "," + mPattern.getId() + "," + mPattern.getImageUrl() + ","
				+ mPattern.getName();
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.putString(mPattern.getId(), serializedColor);
		prefEdit.commit();
		prefEdit.commit();
		Button favsButton = (Button) findViewById(R.id.adjust_favs);
		favsButton.setText("Remove from Favs");

		Toast msg = Toast.makeText(this, "wallpaper added to favorites",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
	}

	private void removeFromFavs(SharedPreferences prefs) {
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.remove(mPattern.getId().toString());
		prefEdit.commit();
		Button favsButton = (Button) findViewById(R.id.adjust_favs);
		favsButton.setText("Add to favs");

		Toast msg = Toast.makeText(this, "wallpaper removed from favorites",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
	}

	
}
