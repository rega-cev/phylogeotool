package be.kuleuven.rega.treedraw;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.WPaintedWidget;

public class DrawRectangular implements Draw {

	private WebGraphics2DMine graphics = null;
	private Tree tree;
	private Set<Node> nodes = null;
	private Set<Edge> edges = null;
	private Shape shape = null;
	private int nodeWidth = 10;
	private int nodeHeight = 10;
	
	public DrawRectangular(Tree tree, Shape shape) {
		this.tree = tree;
		this.nodes = tree.getNodes();
		this.edges = tree.getEdges();
		this.shape = shape;
	}
	
	public void paint(GraphWebApplication graphWebApplication, WPaintedWidget wPaintedWidget, WebGraphics2DMine graphics, double width, double height, double paintAreaWidth, double paintAreaHeigth) {
		this.graphics = graphics;
//		double moveRight = width / 2;
//		double moveDown = height / 2;
		
		double moveRight = paintAreaWidth;
		double moveDown = paintAreaHeigth;
		
//		Determine how to determine the factor. Now based on amount of height of screen and amount of nodes
//		double factor = (height*2)/(nodes.size()*1.5);
//		double factor = 20;
		
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
//		double factorX = width / maxX;
//		double factorY = height / maxY;
		double factorX = 50;
		double factorY = 50;
		
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
		
//		for(Node node:nodes) {
//			graphics.draw(new Rectangle((int)Math.ceil(node.getX()*factorX), (int)Math.ceil(node.getY()*factorY), nodeWidth, nodeHeight));
//		}
		
		for(Edge edge:edges) {
			graphics.setColor(Color.BLACK);
			Point2D corner = new Point2D.Double(Math.ceil(edge.getNode1().getX()*factorX), Math.ceil(edge.getNode2().getY()*factorY));
			Point2D start = new Point2D.Double(Math.ceil(edge.getNode1().getX()*factorX), Math.ceil(edge.getNode1().getY()*factorY));
			Point2D end = new Point2D.Double(Math.ceil(edge.getNode2().getX()*factorX), Math.ceil(edge.getNode2().getY()*factorY));
			graphics.draw(new Line2D.Double(start, corner));
			graphics.draw(new Line2D.Double(corner, end));
			
			// Draw circles at the end
			if (!edge.getNode2().hasChildren()) {
				graphics.setColor(edge.getNode2().getColor());
//				double r = getRadius(maxNodeSurface, maxSize, edge.getNode2().getSize());
				double r = 20;
				double x = end.getX();
				double y = end.getY() - r/2;
				Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r, r);
				graphics.fill(circle);
				// If you want the circle to be surrounded
				graphics.setColor(graphics.getColor().darker());
				graphics.draw(circle);
			
//				final WCircleNode wCircle = new WCircleNode((int)(edge.getNode2().getX()), (int)(edge.getNode2().getY()), (int)(r/2), edge.getNode2());
//				wCircle.setToolTip("Node " + edge.getNode2().getId());
//				wCircle.setCursor(Cursor.CrossCursor);
//				
//				wPaintedWidget.addArea(wCircle);
				
//				graphics.drawString(Integer.toString(edge.getNode2().getSize()), (int)(x + r + 10), (int)(y + r/2 + 10));
				graphics.drawString(edge.getNode2().getLabel(), (int)(x + r + 10), (int)(y + r/2 + 10));
			}
		}
	}
	
	public double getRadius(double maxNodeSurface, double maxSize, int nrSequences) {
		double toReturn = Math.sqrt((maxNodeSurface*(nrSequences/maxSize))/Math.PI);
		if(toReturn < 5) {
			return 5;
		} else {
			return toReturn;
		}
	}

}
