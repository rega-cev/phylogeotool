package be.kuleuven.rega.webapp.widgets;

import java.io.File;

import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WWidget;

public class WImageSDRMine {
	private WImage wImage = null;
	
	public WImageSDRMine(String SDRLocation, String clusterId, boolean SDR) {
	    int width = 300;
	    int height = 300;
	    WResource wResource = null;
	    if(SDR) {
		    if(new File(SDRLocation + File.separator + Integer.valueOf(clusterId) + "_sdr.png").exists()) {
		    	wResource = new WFileResource("PNG", SDRLocation + File.separator + clusterId + "_sdr.png");
		    }
	    } else {
	    	if(new File(SDRLocation + File.separator + Integer.valueOf(clusterId) + "_secondDerivative.png").exists()) {
		    	wResource = new WFileResource("PNG", SDRLocation + File.separator + clusterId + "_secondDerivative.png");
		    }
	    }
	    WLink wLink = new WLink(wResource);
		this.wImage = new WImage(wLink);
		this.wImage.resize(width, height);
	}
	
	public WWidget getWidget() {
		return this.wImage;
	}
}
