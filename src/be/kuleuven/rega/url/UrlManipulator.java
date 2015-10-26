package be.kuleuven.rega.url;

import eu.webtoolkit.jwt.WApplication;

public class UrlManipulator {

	public UrlManipulator() {
		
	}
	
	public static String getId(String url) {
		if (WApplication.getInstance().getInternalPath().contains("/root")) {
			return url.split("/")[1].split("_")[1];
		} else {
			return "1";
		}
	}
	
}
