/*
 * Copyright (C) 2010 Daniel Nilsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squidzoo.wallpaperColors.activities;

import java.io.IOException;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.R.id;
import com.squidzoo.wallpaperColors.R.layout;
import com.squidzoo.wallpaperColors.beans.CustomBean;

import afzkl.development.mColorPicker.views.ColorPanelView;
import afzkl.development.mColorPicker.views.ColorPickerView;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ColorPickerActivity extends Activity implements
		View.OnClickListener {

	public final static String INTENT_DATA_INITIAL_COLOR = "color";
	public final static String RESULT_COLOR = "color";

	private ColorPickerView mColorPickerView;
	private ColorPanelView mOldColorPanel;
	private ColorPanelView mNewColorPanel;

	private Button mCancelButton;
	private Button mOkButton;

	public static String MY_DEBUG_TAG = "my_debug_tag";
	WallpaperManager mWallpaperManager;
	ImageView mImageView;
	int mColorInt;
	Bitmap mBitmap;
	String mIdValue;
	com.squidzoo.wallpaperColors.beans.CustomBean mColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// To fight color branding.
		getWindow().setFormat(PixelFormat.RGBA_8888);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_picker);

		mWallpaperManager = WallpaperManager.getInstance(this);
		
		Bundle b = getIntent().getExtras();
		int initialColor = 0xff000000;
		mColorInt = initialColor;

		if (b != null) {
			initialColor = b.getInt(INTENT_DATA_INITIAL_COLOR);
		}

		setUp(initialColor);

	}

	private void setUp(int color) {
		mColorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
		mOldColorPanel = (ColorPanelView) findViewById(R.id.old_color_panel);
		mNewColorPanel = (ColorPanelView) findViewById(R.id.new_color_panel);
		mOkButton = (Button) findViewById(R.id.ok_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);

		((LinearLayout) mOldColorPanel.getParent()).setPadding(
				Math.round(mColorPickerView.getDrawingOffset()), 0,
				Math.round(mColorPickerView.getDrawingOffset()), 0);

		mColorPickerView
				.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {

					@Override
					public void onColorChanged(int color) {
						mNewColorPanel.setColor(color);
					}
				});

		mOldColorPanel.setColor(color);
		mColorPickerView.setColor(color, true);
		mColorPickerView.setAlphaSliderVisible(true);
		mColorPickerView.setSliderTrackerColor(0xffCECECE);
		mColorPickerView.setBorderColor(0xff7E7E7E);
		mOldColorPanel.setBorderColor(mColorPickerView.getBorderColor());
		mNewColorPanel.setBorderColor(mColorPickerView.getBorderColor());

		mOkButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ok_button:

			mColorInt = mColorPickerView.getColor();
			Log.d(MY_DEBUG_TAG,Integer.toString(mColorInt));	
			setWallpaper();

			break;
		}

	}

	private void setWallpaper() {
		try {
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
		//hack to get single color bitmap
		ImageView imageView = (ImageView)findViewById(R.id.bitmapImageView);
		imageView.setDrawingCacheEnabled(true);
		imageView.setBackgroundColor(mColorInt);
		Bitmap bitmapFromView = imageView.getDrawingCache();
		
		mBitmap = Bitmap.createScaledBitmap(bitmapFromView, width, height,
				false);
		return mBitmap;
	}

}
