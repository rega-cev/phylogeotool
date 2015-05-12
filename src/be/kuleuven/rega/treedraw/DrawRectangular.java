package be.kuleuven.rega.treedraw;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class DrawRectangular extends WPaintedWidget {

	private WebGraphics2DMine graphics = null;
	private List<Node> nodes = null;
	private List<Edge> edges = null;
	private int nodeHeight = 10;
	private int nodeWidth = 10;
	
	public DrawRectangular(WContainerWidget parent, List<Node> nodes, List<Edge> edges) {
		super(parent);
		this.nodes = nodes;
		this.edges = edges;
		this.resize(new WLength(550), new WLength(550));
	}
	
	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		//painter.setBrush(new WBrush(WColor.blue).clone());
		graphics = new WebGraphics2DMine(painter);
		double maxX = 0;
		double maxY = 0;
		/*
		 * Determine max x and max y
		 */
		for(Node node:nodes) {
			if(node.getX() > maxX) {
				maxX = node.getX();
			}
			if(node.getY() > maxY) {
				maxY = node.getY();
			}
		}
		
		// TODO: Look for height of component
		double factorX = 500d / maxX;
		double factorY = 500d / maxY;
//		for(Node node:nodes) {
//			graphics.draw(new Rectangle((int)Math.ceil(node.getX()*factorX), (int)Math.ceil(node.getY()*factorY), nodeWidth, nodeHeight));
//		}
		
		for(Edge edge:edges) {
			Point2D corner = new Point2D.Double(Math.ceil(edge.getNode1().getX()*factorX) + nodeWidth/2, Math.ceil(edge.getNode2().getY()*factorY + nodeHeight/2));
			Point2D start = new Point2D.Double(Math.ceil(edge.getNode1().getX()*factorX) + nodeWidth/2, Math.ceil(edge.getNode1().getY()*factorY + nodeHeight/2));
			Point2D end = new Point2D.Double(Math.ceil(edge.getNode2().getX()*factorX) + nodeWidth/2, Math.ceil(edge.getNode2().getY()*factorY + nodeHeight/2));
			graphics.draw(new Line2D.Double(start, corner));
			graphics.draw(new Line2D.Double(corner, end));
		}
	}

}
