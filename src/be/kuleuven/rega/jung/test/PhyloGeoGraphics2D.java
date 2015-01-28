package be.kuleuven.rega.jung.test;

import java.awt.Image;
import java.awt.image.ImageObserver;

import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class PhyloGeoGraphics2D extends WebGraphics2D {

	WPainter painter = null;
	
	public PhyloGeoGraphics2D(WPainter painter) {
		super(painter);
		this.painter = painter;
	}
	
//	@Override
//	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
//		painter.drawImage(x, y, img);
//	}

}
