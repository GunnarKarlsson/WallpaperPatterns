package com.squidzoo.wallpaperColors.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMaker {
	public static void makeToast(Context context, String message){
		Toast msg = Toast.makeText(context, message,
				Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

}
