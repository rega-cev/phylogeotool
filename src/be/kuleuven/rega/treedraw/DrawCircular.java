package be.kuleuven.rega.treedraw;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class DrawCircular extends WPaintedWidget {

	private WebGraphics2DMine graphics = null;
	private List<Node> nodes = null;
	private List<Edge> edges = null;
	private Shape shape = null;

	public DrawCircular(WContainerWidget parent, List<Node> nodes, List<Edge> edges, Shape shape) {
		super(parent);
		this.nodes = nodes;
		this.edges = edges;
		this.shape = shape;
		this.resize(new WLength(1200), new WLength(700));
	}

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		graphics = new WebGraphics2DMine(painter);
		// TODO: Look for height of component
		double moveRight = getWidth().getValue() / 2;
		double moveDown = getHeight().getValue() / 2;
//		Determine how to determine the factor. Now based on amount of height of screen and amount of nodes
		double factor = (getHeight().getValue()*2)/(nodes.size()*1.5);
		
		double maxRadius = 80;
		double maxNodeSurface = Math.PI * Math.pow(maxRadius, 2);
		int maxSize = 0;
		for(Node node:nodes) {
			if(!node.hasChildren()) {
				if(node.getSize() > maxSize) {
					maxSize = node.getSize();
				}
			}
		}
		
		// To draw a rectangle on top of every node
//		 for(Node node:nodes) {
//			 graphics.drawString(node.getLabel(), (int)Math.ceil(Node.polarToEucledianX(node)*factor) + moveRight, (int)Math.ceil(Node.polarToEucledianY(node)*factor) + moveRight);
//			 graphics.draw(new Rectangle((int)Math.ceil(Node.polarToEucledianX(node)*factor) + moveRight, (int)Math.ceil(Node.polarToEucledianY(node)*factor) + moveRight, nodeWidth, nodeHeight));
//		 }
		for (Edge edge : edges) {
			graphics.setColor(Color.BLACK);
			Point2D corner = null;
			Point2D end = null;
			if (shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
				Node newNode = new Node();
				newNode.setX(edge.getNode1().getX());
				newNode.setTheta(edge.getNode2().getTheta());
				corner = new Point2D.Double(Node.polarToEucledianX(newNode) * factor + moveRight, Node.polarToEucledianY(newNode) * factor + moveDown);
				end = new Point2D.Double(Node.polarToEucledianX(edge.getNode2()) * factor + moveRight, Node.polarToEucledianY(edge.getNode2()) * factor + moveDown);
				int circleWidth = 0;
				if(shape == Shape.CIRCULAR_CLADOGRAM) {
					circleWidth = (int) (2 * TreeTraversal.nodeLevel(edge.getNode1(), 0) * factor);
				} else {
					// Calculate the eucledian distance from the center of the tree to the startnode (this to know the width of the circle)
					// d(p,q) = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2))
					circleWidth = (int) (2 * Math.sqrt((Math.pow(((Node.polarToEucledianX(edge.getNode1()) * factor + moveRight)-(Node.polarToEucledianX(nodes.get(nodes.size() - 1)) * factor + moveRight)), 2) + Math.pow(((Node.polarToEucledianY(edge.getNode1()) * factor + moveRight)-(Node.polarToEucledianY(nodes.get(nodes.size() - 1)) * factor + moveRight)), 2))));
				}
				Rectangle boundingBox = new Rectangle((int) (Node.polarToEucledianX(nodes.get(nodes.size() - 1)) * factor + moveRight - circleWidth / 2), (int) (Node.polarToEucledianY(nodes.get(nodes.size() - 1)) * factor + moveDown - circleWidth / 2), circleWidth, circleWidth);
				graphics.draw(new Arc2D.Double(boundingBox, 360 - Math.toDegrees(edge.getNode1().getTheta()), Math.toDegrees(edge.getNode1().getTheta()) - Math.toDegrees(edge.getNode2().getTheta()), Arc2D.Double.OPEN));
				graphics.draw(new Line2D.Double(corner, end));
			} else if (shape == Shape.RADIAL) {
				graphics.draw(new Line2D.Double(edge.getNode1().getX() * factor + moveRight, edge.getNode1().getY() * factor + moveDown, edge.getNode2().getX() * factor + moveRight, edge.getNode2().getY() * factor + moveDown));
			}
			// Draw the clusters at the end of the leaves
			if (!edge.getNode2().hasChildren()) {
				graphics.setColor(edge.getNode2().getColor());
				double r = getRadius(maxNodeSurface, maxSize, edge.getNode2().getSize());
				double x = 0.0;
				double y = 0.0;
				if (shape == Shape.RADIAL) {
					x = edge.getNode2().getX() * factor + moveRight + r / 2 * Math.cos(edge.getNode2().getTheta());
					y = edge.getNode2().getY() * factor + moveDown + r / 2 * Math.sin(edge.getNode2().getTheta());
				} else if (shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
					x = end.getX() + r / 2 * Math.cos(edge.getNode2().getTheta());
					y = end.getY() + r / 2 * Math.sin(edge.getNode2().getTheta());
				}
				Ellipse2D.Double circle = new Ellipse2D.Double(x - (r / 2), y - (r / 2), r, r);
				graphics.fill(circle);
				// If you want the circle to be surrounded
				graphics.setColor(graphics.getColor().darker());
				graphics.draw(circle);
			}
			if(!edge.getNode1().hasParent()) {
				int r = 5;
				Ellipse2D.Double circle = new Ellipse2D.Double(Node.polarToEucledianX(edge.getNode1()) * factor + moveRight - (r / 2), Node.polarToEucledianY(edge.getNode1()) * factor + moveDown - (r / 2), r, r);
				graphics.setColor(Color.BLACK);
				graphics.fill(circle);
			}
		}
	}
	
	public double getRadius(double maxNodeSurface, double maxSize, int nrSequences) {
		double toReturn = Math.sqrt((maxNodeSurface*(nrSequences/maxSize))/Math.PI);
		if(toReturn < 2.5) {
			return 2.5;
		} else {
			return toReturn;
		}
	}
}