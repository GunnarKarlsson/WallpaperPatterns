package com.squidzoo.wallpaperColors;


import com.squidzoo.wallpaperColors.activities.ColorListActivity;
import com.squidzoo.wallpaperColors.activities.ColorPickerActivity;
import com.squidzoo.wallpaperColors.activities.FavoritesActivity;
import com.squidzoo.wallpaperColors.activities.PatternListActivity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabContainer extends TabActivity {
	
	TabHost mTabHost;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
	
		setTitle("  Wallpaper Colors HD");
		
		Resources res = getResources(); // Resource object to get Drawables
		mTabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		Intent colorListActivityIntent = new Intent().setClass(this, ColorListActivity.class);
		Intent patternListActivityIntent = new Intent().setClass(this, PatternListActivity.class);
		Intent favoritesActivityIntent = new Intent().setClass(this, FavoritesActivity.class);
		Intent colorPickerActivityIntent = new Intent().setClass(this, ColorPickerActivity.class);

		Bitmap colorsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_color_icon);
		Drawable colorsIcon = new BitmapDrawable(colorsBitmap);
		
		Bitmap patternsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_pattern_icon);
		Drawable patternsIcon = new BitmapDrawable(patternsBitmap);
		
		Bitmap favsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_favs_icon);
		Drawable favsIcon = new BitmapDrawable(favsBitmap);
		
		Bitmap pickerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_color_picker);
		Drawable pickerIcon = new BitmapDrawable(pickerBitmap);
		
		setupTab(new TextView(this), "Colors", colorsIcon, colorListActivityIntent);
		setupTab(new TextView(this), "Patterns", patternsIcon, patternListActivityIntent);
		setupTab(new TextView(this), "Favs", favsIcon, favoritesActivityIntent);
		setupTab(new TextView(this), "Picker", pickerIcon, colorPickerActivityIntent);
	}
	
	private void setupTab(final View view, final String tag, Drawable drawable, Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, drawable);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
	    mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text, Drawable drawable) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		ImageView iv = (ImageView)view.findViewById(R.id.tab_icon);
		iv.setImageDrawable(drawable);
		tv.setText(text);
		return view;
	}
}		
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		
		/*
		spec = tabHost
				.newTabSpec("colors")
				.setIndicator("Colors",
						res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, PatternListActivity.class);
		spec = tabHost
				.newTabSpec("patterns")
				.setIndicator("Patterns",
						res.getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(2);
		
		*/

