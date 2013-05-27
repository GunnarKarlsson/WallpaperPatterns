package com.squidzoo.wallpaperColors.beans;

import com.squidzoo.wallpaperColors.types.ItemType;

import android.os.Parcelable;

public interface ICustomBean extends Parcelable{
		public void setId(String value);
		public String getId();
		public void setName(String value);
		public String getName();
		public void setHex(String value);
		public String getHex();
		public void setCreator(String value);
		public String getCreator();
		public void setImageUrl(String value);
		public String getImageUrl();
		public void setBadgeUrl(String value);
		public String getBadgeUrl();
		public void setType(ItemType value);
		public ItemType getType();
}
