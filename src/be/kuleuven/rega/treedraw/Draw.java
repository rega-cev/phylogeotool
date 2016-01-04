package be.kuleuven.rega.treedraw;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.WebGraphics2DMine;

public abstract class Draw {

	public abstract List<WCircleNode> paint(WebGraphics2DMine graphics, Map<Node, DrawData> map, Map<Node, Color> nodeToColor, double paintAreaWidth, double paintAreaHeight);
//	public abstract void paint(GraphWebApplication graphWebApplication, WPaintedWidget wPaintedWidget, WebGraphics2DMine graphics, double width, double height, double paintAreaWidth, double paintAreaHeigth);
	
	public static double polarToEucledianX(double X, double theta) {
		return X * Math.cos(theta);
	}

	public static double polarToEucledianY(double X, double theta) {
		return X * Math.sin(theta);
	}
}
