package com.squidzoo.wallpaperColors.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

public class SDCardSaver {

	public static String saveBitmapToSDCard(Bitmap bitmap, Context context){
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
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		byte b[] = bytes.toByteArray();

		try {
			externalStorageFile.createNewFile();
			OutputStream filoutputStream = new FileOutputStream(
					externalStorageFile);
			filoutputStream.write(b);
			filoutputStream.flush();
			filoutputStream.close();
			createdFile.append(externalStorageFile.getAbsolutePath());

			Toast msg = Toast.makeText(context, "wallpaper has been saved as: "
					+ fileName, Toast.LENGTH_SHORT);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();

		} catch (Exception e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(context, "wallpaper could not be saved",
					Toast.LENGTH_LONG);

			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);

			msg.show();
		}

		return createdFile.toString();

	}
}
