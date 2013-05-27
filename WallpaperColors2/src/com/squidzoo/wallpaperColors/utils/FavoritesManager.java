package com.squidzoo.wallpaperColors.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import com.squidzoo.wallpaperColors.beans.CustomBean;

public class FavoritesManager {
	
	public static void addToFavs(SharedPreferences prefs, CustomBean item, Context context) {
		
		String serializedColor = item.getCreator() + "," + item.getHex()
				+ "," + item.getId() + "," + item.getBadgeUrl() + ","
				+ item.getName() + "," + item.getType().toString() + "," + item.getImageUrl().toString();
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.putString(item.getId(), serializedColor);
		prefEdit.commit();
		prefEdit.commit();
		

		Toast msg = Toast.makeText(context, "wallpaper added to favorites",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
	}

	public static void removeFromFavs(SharedPreferences prefs, CustomBean item, Context context) {
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.remove(item.getId().toString());
		prefEdit.commit();
		
		Toast msg = Toast.makeText(context, "wallpaper removed from favorites",
				Toast.LENGTH_SHORT);

		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);

		msg.show();
	}
}
