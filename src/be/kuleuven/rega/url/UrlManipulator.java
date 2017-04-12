package be.kuleuven.rega.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.webtoolkit.jwt.WApplication;

public class UrlManipulator {

	public UrlManipulator() {
		
	}
	
	public static String getId(String url) {
//		System.out.println("Bookmark URL: " + WApplication.getInstance().getBookmarkUrl());
//		System.out.println("Url: " + url);
		String pattern = "/(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(url);
		
		if (m.find( )) {
			return m.group(0).split("/")[1];
		} else {
			return "1";
		}
	}
	
}
