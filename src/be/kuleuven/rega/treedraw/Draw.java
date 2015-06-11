package be.kuleuven.rega.treedraw;

import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.WPaintedWidget;

public interface Draw {

	public void paint(GraphWebApplication graphWebApplication, WPaintedWidget wPaintedWidget, WebGraphics2DMine graphics, double width, double height, double paintAreaWidth, double paintAreaHeigth);
	
}
