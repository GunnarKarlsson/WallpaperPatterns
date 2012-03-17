package com.squidzoo.wallpaperColors;

import java.io.IOException;

import com.squidzoo.wallpaperColors.activities.ItemListActivity;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class WallpaperColors2Activity extends Activity implements OnClickListener{
    final static private int[] mColors =
            {Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN,
                    Color.YELLOW, Color.WHITE};

    Drawable wallpaperDrawable;
    ImageView imageView;
    WallpaperManager wallpaperManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        wallpaperManager = WallpaperManager.getInstance(this);
        wallpaperDrawable = wallpaperManager.getDrawable();
        imageView = (ImageView) findViewById(R.id.imageview);
        
        
       imageView.setDrawingCacheEnabled(true);
       imageView.setImageDrawable(wallpaperDrawable);
        Button randomButton = (Button)findViewById(R.id.randomizeButton);
        randomButton.setOnClickListener(this);
        Button launchListButton = (Button)findViewById(R.id.launchListButton);
        launchListButton.setOnClickListener(this);
        Button setWallButton = (Button)findViewById(R.id.setwallpaperButton);
        setWallButton.setOnClickListener(this);
        
    }
    
    
    public void onClick(View view) {
    	
				switch(view.getId()){
				case R.id.randomizeButton:
					randomize();
					break;
				case R.id.setwallpaperButton:
					setNewWallpaper();
					break;
				case R.id.launchListButton:
					launchList(view);
					break;
				}
	}

    
	private void launchList(View view) {
				 Intent intent = new Intent(view.getContext(), ItemListActivity.class);
				 startActivityForResult(intent, 0);
	}

	private void setNewWallpaper() {
				  try {
	                    wallpaperManager.setBitmap(imageView.getDrawingCache());
	                    finish();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	}

	private void randomize() {
				int mColor = (int) Math.floor(Math.random() * mColors.length);
                wallpaperDrawable.setColorFilter(mColors[mColor], PorterDuff.Mode.SRC);
                imageView.setImageDrawable(wallpaperDrawable);
                imageView.invalidate();
			}
	
}  
