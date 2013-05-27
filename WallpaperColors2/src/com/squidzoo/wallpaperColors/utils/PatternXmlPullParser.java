package com.squidzoo.wallpaperColors.utils;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.squidzoo.wallpaperColors.beans.CustomBean;
import com.squidzoo.wallpaperColors.types.ItemType;

public class PatternXmlPullParser {
	private XmlPullParser parser;

	public static ArrayList<CustomBean> parsePattern(InputStream xml) throws Exception {
		return new PatternXmlPullParser().parse(xml);
	}

	public ArrayList<CustomBean> parse(InputStream xml) throws Exception {
		
		ArrayList<CustomBean> pattern = null;
		CustomBean currentPattern = null;
		parser = XmlPullParserFactory.newInstance().newPullParser();
		parser.setInput(xml, null);
		int eventType = parser.getEventType();
		boolean done = false;
		while (eventType != XmlPullParser.END_DOCUMENT && !done) {
			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				pattern = new ArrayList<CustomBean>();
				break;

			case XmlPullParser.START_TAG:
				name = parser.getName();

				if (name.equalsIgnoreCase("pattern")) {
					currentPattern = new CustomBean();
				}else if(currentPattern != null){
					if(name.equalsIgnoreCase("id")){
						currentPattern.setId(parser.nextText());
						currentPattern.setType(ItemType.PATTERN);
					}
					
					if(name.equalsIgnoreCase("hex")){
						currentPattern.setHex(parser.nextText());
					}
					if(name.equalsIgnoreCase("badgeUrl")){
						currentPattern.setBadgeUrl(parser.nextText());
					}
					
					if(name.equalsIgnoreCase("imageUrl")){
						currentPattern.setImageUrl(parser.nextText());
					}
					
					if(name.equalsIgnoreCase("title")){
						currentPattern.setName(parser.nextText());
					}
					if(name.equalsIgnoreCase("userName")){
						currentPattern.setCreator(parser.nextText());
					}
				}
				break;
			
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if(name.equalsIgnoreCase("pattern") && currentPattern != null){
					pattern.add(currentPattern);
				}else if(name.equalsIgnoreCase("patterns")){
					done = true;
				}
				break;
			}

			eventType = parser.next();
		}
		
		return pattern;
	}

}
