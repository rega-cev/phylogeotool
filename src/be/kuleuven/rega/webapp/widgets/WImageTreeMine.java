package be.kuleuven.rega.webapp.widgets;

import java.io.File;

import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WWidget;

public class WImageTreeMine {
	
	private WImage wImage = null;
	
	public WImageTreeMine(String treeRenderLocation, String clusterId) {
	    int width = 500;
	    int height = 500;
	    WResource wResource = null;
//	    if(new File(treeRenderLocation + File.separator + Integer.valueOf(clusterId) + ".svg").exists()) {
	    if(new File(treeRenderLocation + File.separator + Integer.valueOf(clusterId) + ".png").exists()) {
	    	wResource = new WFileResource("PNG", treeRenderLocation + File.separator + clusterId + ".png");
//	    	System.out.println(treeRenderLocation + File.separator + clusterId + ".pdf");
//	    	wResource = new WFileResource("pdf", treeRenderLocation + File.separator + clusterId + ".pdf");
//	    	wResource = new WSvgImage(new WLength(width), new WLength(height));
//	    	wSvgImage.set
	    } else {
	    	wResource = new WFileResource("PNG", treeRenderLocation + File.separator + "default.png");
	    }
	    WLink wLink = new WLink(wResource);
		this.wImage = new WImage(wLink);
		this.wImage.resize(width, height);
	}
	
	public WWidget getWidget() {
		return this.wImage;
	}
}
