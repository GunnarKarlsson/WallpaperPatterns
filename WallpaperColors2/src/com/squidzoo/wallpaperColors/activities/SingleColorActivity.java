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
import com.squidzoo.wallpaperColors.beans.CustomBean;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SingleColorActivity extends Activity implements OnClickListener {
	public static String MY_DEBUG_TAG = "my_debug_tag";
	WallpaperManager mWallpaperManager;
	ImageView mImageView;
	int mColorInt;
	Bitmap mBitmap;
	String mIdValue;
	com.squidzoo.wallpaperColors.beans.CustomBean mColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_color);

		Intent intent = getIntent();

		mColor = new com.squidzoo.wallpaperColors.beans.CustomBean();
		if (intent.hasExtra("hex")) {
			mColor.setHex(intent.getStringExtra("hex"));
		}
		if (intent.hasExtra("creator")) {
			mColor.setCreator(intent.getStringExtra("creator"));
		}
		if (intent.hasExtra("idvalue")) {
			mColor.setId(intent.getStringExtra("idvalue"));
		}
		if (intent.hasExtra("badgeurl")) {
			Log.d(MY_DEBUG_TAG, intent.getStringExtra("badgeurl"));
			mColor.setBadgeUrl(intent.getStringExtra("badgeurl"));
		}
		if (intent.hasExtra("imageurl")) {
			Log.d(MY_DEBUG_TAG, intent.getStringExtra("imageurl"));
			mColor.setImageUrl(intent.getStringExtra("imageurl"));
		}
		if (intent.hasExtra("name")) {
			mColor.setName(intent.getStringExtra("name"));
		}

		if (getIntent().hasExtra("hex")) {
			mImageView = (ImageView) findViewById(R.id.imageviewSingleColor);
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

		if (prefs.contains(mColor.getId().toString())) {
			favsButton.setText("Remove from Favs");
		} else {
			favsButton.setText("Add to favs");
		}

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
		case R.id.adjust_favs:
			setFavs();
		}
	}

	private void setFavs() {
		SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		if (prefs.contains(mColor.getId().toString())) {
			removeFromFavs(prefs);
		} else {
			addToFavs(prefs);

		}
	}

	private void addToFavs(SharedPreferences prefs) {
		String serializedColor = mColor.getCreator() + "," + mColor.getHex()
				+ "," + mColor.getId() + "," + mColor.getBadgeUrl() + ","
				+ mColor.getName();
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.putString(mColor.getId(), serializedColor);
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
		prefEdit.remove(mColor.getId().toString());
		prefEdit.commit();
		Button favsButton = (Button) findViewById(R.id.adjust_favs);
		favsButton.setText("Add to favs");

		Toast msg = Toast.makeText(this, "wallpaper removed from favorites",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
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

			Toast msg = Toast.makeText(this, "wallpaper has been saved as: "
					+ fileName, Toast.LENGTH_SHORT);

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
			e.printStackTrace();
		}
	}

	private Bitmap getBitmap() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		// Resize bitmap
		Bitmap bitmapFromView = mImageView.getDrawingCache();
		mBitmap = Bitmap.createScaledBitmap(bitmapFromView, width, height,
				false);
		return mBitmap;
	}
}
