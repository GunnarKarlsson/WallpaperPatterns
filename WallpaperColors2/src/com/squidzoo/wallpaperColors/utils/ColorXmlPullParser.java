package com.squidzoo.wallpaperColors.utils;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.squidzoo.wallpaperColors.beans.CustomBean;


import android.util.Log;

public class ColorXmlPullParser {
	private XmlPullParser parser;

	public static String MY_DEBUG_TAG = "my_debug_tag";

	public static ArrayList<CustomBean> parseItem(InputStream xml) throws Exception {
		return new ColorXmlPullParser().parse(xml);
	}

	public ArrayList<CustomBean> parse(InputStream xml) throws Exception {
		
		ArrayList<CustomBean> items = null;
		CustomBean currentItem = null;
		parser = XmlPullParserFactory.newInstance().newPullParser();
		parser.setInput(xml, null);
		int eventType = parser.getEventType();
		boolean done = false;
		while (eventType != XmlPullParser.END_DOCUMENT && !done) {
			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				//Log.d(MY_DEBUG_TAG, "in_start document");
				items = new ArrayList<CustomBean>();
				break;

			case XmlPullParser.START_TAG:
				name = parser.getName();
				//Log.d(MY_DEBUG_TAG, "in start tag: " + name);

				if (name.equalsIgnoreCase("color")) {
					Log.d(MY_DEBUG_TAG, "creating new item object");	
					currentItem = new CustomBean();
				}else if(currentItem != null){
					if(name.equalsIgnoreCase("hex")){
						//Log.d(MY_DEBUG_TAG, "name equals hex");	
						currentItem.setHex(parser.nextText());
					}
					if(name.equalsIgnoreCase("badgeUrl")){
						currentItem.setBadgeUrl(parser.nextText());
					}
					if(name.equalsIgnoreCase("imageUrl")){
						currentItem.setImageUrl(parser.nextText());
					}
					if(name.equalsIgnoreCase("title")){
						currentItem.setName(parser.nextText());
					}
					if(name.equalsIgnoreCase("userName")){
						currentItem.setCreator(parser.nextText());
					}
					if(name.equalsIgnoreCase("id")){
						currentItem.setId(parser.nextText());
					}
				}
				break;
			
			case XmlPullParser.END_TAG:
				name = parser.getName();
				//Log.d(MY_DEBUG_TAG, "in end tag: " + name);
				if((name.equalsIgnoreCase("color") || name.equalsIgnoreCase("pattern")) && currentItem != null){
					items.add(currentItem);
				}else if(name.equalsIgnoreCase("colors") || name.equalsIgnoreCase("patterns")){
					done = true;
				}
				break;
			}

			eventType = parser.next();
		}
		return items;
	}

}
