package com.squidzoo.wallpaperColors.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DecodingType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.beans.CustomBean;

public class ItemArrayAdapter extends ArrayAdapter<CustomBean> {
	private ArrayList<CustomBean> items;

	public ItemArrayAdapter(Context context, int textViewResourceId,
			ArrayList<CustomBean> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item, null);
		} else {
			row = convertView;
		}

		CustomBean item = items.get(position);

		if (item != null) {
			String imageUrl = item.getBadgeUrl();
			ImageView imageView = (ImageView) row
					.findViewById(R.id.itemImageView);
			ImageLoader imageLoader = ImageLoader.getInstance();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getContext())
					.maxImageWidthForMemoryCache(800)
					.maxImageHeightForMemoryCache(480)
					.httpConnectTimeout(5000)
					.httpReadTimeout(30000)
					.threadPoolSize(5)
					.threadPriority(Thread.MIN_PRIORITY + 2)
					.denyCacheImageMultipleSizesInMemory()
					.defaultDisplayImageOptions(
							DisplayImageOptions.createSimple()).build();
			// Initialize ImageLoader with created configuration. Do it once.
			imageLoader.init(config);

			// Creates display image options for custom display task
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory().cacheOnDisc()
					.decodingType(DecodingType.MEMORY_SAVING).build();
			// Load and display image
			imageLoader.displayImage(imageUrl, imageView, options);
		}
		return row;
	}

}
