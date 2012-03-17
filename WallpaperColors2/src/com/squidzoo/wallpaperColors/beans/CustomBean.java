package com.squidzoo.wallpaperColors.beans;


import android.os.Parcel;
import android.os.Parcelable;

public class CustomBean implements Parcelable, ICustomBean{
	private String mName;
	private String mHex;
	private String mCreator;
	private String mImageUrl;
	private String mId;
	private String mBadgeUrl;
	private String mType;
	
	public CustomBean(Parcel in){
		mName = in.readString();
		mHex = in.readString();
		mCreator = in.readString();
		mImageUrl = in.readString();
	}
	
	public CustomBean(){
		
	}
	
	public void setType(String value){
		mType = value;
	}
	
	public String getType(){
		return mType;
	}
	
	public int describeContents(){
		return 0;
	}
	
	public void setId(String idValue){
		if(idValue != null){
			mId = idValue;
		}
	}
	
	public String getId(){
		return mId;
	}
	
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		if(name != null){			
			this.mName = name;
		}
	}
	public String getHex() {
		return mHex;
	}
	public void setHex(String hex) {
		if(hex !=  null){			
			this.mHex = hex;
		}
	}
	public String getCreator() {
		return mCreator;
	}
	public void setCreator(String creator) {
		if(creator != null){			
			this.mCreator = creator;
		}
	}
	public String getImageUrl(){
		return mImageUrl;
	}
	public void setImageUrl(String imageUrl){
		if(imageUrl != null){			
			this.mImageUrl = imageUrl;
		}
	}
	
	public void setBadgeUrl(String value){
		mBadgeUrl = value;
	}
	
	public String getBadgeUrl(){
		return mBadgeUrl;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeString(mName);
		out.writeString(mHex);
		out.writeString(mCreator);
		out.writeString(mImageUrl);
	}
	


}


