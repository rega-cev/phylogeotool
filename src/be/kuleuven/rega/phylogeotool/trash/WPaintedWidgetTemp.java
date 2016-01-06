package be.kuleuven.rega.phylogeotool.trash;

import java.awt.Color;
import java.util.Map;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.treedraw.DrawCircular;
import be.kuleuven.rega.treedraw.DrawData;
import be.kuleuven.rega.treedraw.DrawRectangular;
import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class WPaintedWidgetTemp extends WPaintedWidget {

	private Map<Node, DrawData> nodeToData;
	private Map<Node, Color> nodeToColor;
	private Cluster cluster;
	private Shape shape;
	
	public WPaintedWidgetTemp(Map<Node, DrawData> nodeToData, Map<Node, Color> nodeToColor, Cluster cluster, Shape shape) {
		this.nodeToData = nodeToData;
		this.nodeToColor = nodeToColor;
		this.cluster = cluster;
		this.shape = shape;
	}
	
	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		
		WebGraphics2DMine webGraphics2DMine = new WebGraphics2DMine(painter);
		
		 if(shape == Shape.RECTANGULAR_CLADOGRAM || shape == Shape.RECTANGULAR_PHYLOGRAM) {
			 DrawRectangular drawRectangular = new DrawRectangular(cluster, shape);
			 drawRectangular.paint(webGraphics2DMine, nodeToData, nodeToColor, 1800, 860);
		 } else {
			 DrawCircular drawCircular = new DrawCircular(cluster, shape);
			 drawCircular.paint(webGraphics2DMine, nodeToData, nodeToColor, 1800, 860);
		 }
	}
	
}
