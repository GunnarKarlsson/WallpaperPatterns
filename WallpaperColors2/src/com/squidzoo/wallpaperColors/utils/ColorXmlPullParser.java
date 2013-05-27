package com.squidzoo.wallpaperColors.utils;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.types.ItemType;


import android.util.Log;

public class ColorXmlPullParser {
	private XmlPullParser parser;

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
				items = new ArrayList<CustomBean>();
				break;

			case XmlPullParser.START_TAG:
				name = parser.getName();

				if (name.equalsIgnoreCase("color")) {
					currentItem = new CustomBean();
				}else if(currentItem != null){
					if(name.equalsIgnoreCase("hex")){
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
						currentItem.setType(ItemType.COLOR);
					}
				}
				break;
			
			case XmlPullParser.END_TAG:
				name = parser.getName();
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
