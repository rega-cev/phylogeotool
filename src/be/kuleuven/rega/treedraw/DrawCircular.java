package be.kuleuven.rega.treedraw;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import be.kuleuven.rega.listeners.WCircleMouseWentOutListener;
import be.kuleuven.rega.listeners.WCircleMouseWentOverListener;
import be.kuleuven.rega.listeners.WCircleNodeClickListener;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.WebGraphics2DMine;

import com.itextpdf.text.Font;

import eu.webtoolkit.jwt.Cursor;
import eu.webtoolkit.jwt.WPaintedWidget;

public class DrawCircular implements Draw {

	private WebGraphics2DMine graphics = null;
	private Tree tree;
	private Set<Node> nodes = null;
	private Set<Edge> edges = null;
	private Shape shape = null;
	private Point centerDrawing = null;

	public DrawCircular(Tree tree, Shape shape) {
		this.tree = tree;
		this.nodes = tree.getNodes();
		this.edges = tree.getEdges();
		this.shape = shape;
	}

	public void paint(GraphWebApplication graphWebApplication, WPaintedWidget wPaintedWidget, WebGraphics2DMine graphics, double width, double height, double paintAreaWidth, double paintAreaHeight) {
		// TODO: Look for height of component
		this.graphics = graphics;
//		double moveRight = width / 2;
//		double moveDown = height / 2;
		
		double moveRight = paintAreaWidth / 2;
		double moveDown = paintAreaHeight / 2;
		
//		Determine how to determine the factor. Now based on amount of height of screen and amount of nodes
//		double factor = (height*2)/(nodes.size()*1.5);

		// TODO: Make this dynamic
		double factor = 15;
//		double factor = 9;
//		System.out.println("Here1");
//		if(paintAreaWidth < 500 && paintAreaHeight < 500) {
//			factor = 12;
//			System.out.println("Here2");
//		} else if(paintAreaWidth < 600 && paintAreaHeight < 600) {
//			factor = 14;
//			System.out.println("Here3");
//		} else if(paintAreaWidth < 700 && paintAreaHeight < 700) {
//			factor = 17;
//			System.out.println("Here4");
//		} else if(paintAreaWidth < 800 && paintAreaHeight < 800) {
//			factor = 20;
//			System.out.println("Here5");
//		} else if(paintAreaWidth < 900 && paintAreaHeight < 900) {
//			factor = 23;
//			System.out.println("Here6");
//		} else if(paintAreaWidth < 1000 && paintAreaHeight < 1000) {
//			factor = 26;
//			System.out.println("Here7");
//		} else if(paintAreaWidth > 1000 && paintAreaHeight > 800) {
//			factor = 29;
//			System.out.println("Here8");
//		}
		
		double maxRadius = 80;
		// TODO: Change the node sizes
//		if(TreeTraversal.nodeLevel(edges.iterator().next().getNode1(), 0) < 5) {
//			System.out.println("Here");
//			maxRadius = 50;
//		}
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
		List<WCircleNode> circleNodes = new ArrayList<WCircleNode>();
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
					circleWidth = (int) (2 * Math.sqrt((Math.pow(((Node.polarToEucledianX(edge.getNode1()) * factor + moveRight)-(Node.polarToEucledianX(tree.getRootNode()) * factor + moveRight)), 2) + Math.pow(((Node.polarToEucledianY(edge.getNode1()) * factor + moveRight)-(Node.polarToEucledianY(tree.getRootNode()) * factor + moveRight)), 2))));
				}
				Rectangle boundingBox = new Rectangle((int) (Node.polarToEucledianX(tree.getRootNode()) * factor + moveRight - circleWidth / 2), (int) (Node.polarToEucledianY(tree.getRootNode()) * factor + moveDown - circleWidth / 2), circleWidth, circleWidth);
				graphics.draw(new Arc2D.Double(boundingBox, 360 - Math.toDegrees(edge.getNode1().getTheta()), Math.toDegrees(edge.getNode1().getTheta()) - Math.toDegrees(edge.getNode2().getTheta()), Arc2D.Double.OPEN));
				graphics.draw(new Line2D.Double(corner, end));
//				graphics.drawString(Integer.toString(edge.getNode2().getId()), (int)edge.getNode2().getX(), (int)edge.getNode2().getY());
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
//				graphics.setStroke(new BasicStroke(5));
				graphics.draw(circle);
				
				final WCircleNode wCircle = new WCircleNode((int)(x), (int)(y), (int)(r/2), edge.getNode2());
				circleNodes.add(wCircle);
				wCircle.setToolTip("Node " + edge.getNode2().getId());
				wCircle.setCursor(Cursor.CrossCursor);
				
				// TODO: Handle the clicks. How to?
				if(wCircle.getNode().getSize() > 1) {
					wCircle.clicked().addListener(wPaintedWidget, new WCircleNodeClickListener(wCircle, graphWebApplication));
				}
//				wCircle.doubleClicked().addListener(wPaintedWidget, new WCircleNodeDoubleClickListener(wCircle, graphWebApplication));
				wCircle.mouseWentOver().addListener(wPaintedWidget, new WCircleMouseWentOverListener(wCircle, graphWebApplication));
				wCircle.mouseWentOut().addListener(wPaintedWidget, new WCircleMouseWentOutListener(wCircle, graphWebApplication));
				
				wPaintedWidget.addArea(wCircle);
				
				//TODO: Simplify this method (create one big arraylist)
				if(graphWebApplication.getPPlacer().clusterContainsPPlacerSequence(Integer.toString(edge.getNode2().getId()))) {
					graphics.setFont(graphics.getFont().deriveFont(Font.BOLDITALIC, 11.0f));
					graphics.drawString(new String(Integer.toString(edge.getNode2().getSize()) + "*"), (int)(x - 20), (int)(y + r/2 + 20));
				} else {
					graphics.setFont(graphics.getFont().deriveFont(Font.NORMAL, 11.0f));
					graphics.drawString(Integer.toString(edge.getNode2().getSize()), (int)(x - 20), (int)(y + r/2 + 20));
				}
				
			}
			if(!edge.getNode1().hasParent()) {
				int r = 5;
				Ellipse2D.Double center = new Ellipse2D.Double(Node.polarToEucledianX(edge.getNode1()) * factor + moveRight - (r / 2), Node.polarToEucledianY(edge.getNode1()) * factor + moveDown - (r / 2), r, r);
				graphics.setColor(Color.BLACK);
				graphics.fill(center);
				this.centerDrawing = new Point((int)center.getCenterX(), (int)center.getCenterY());
			}
		}
		
		for(WCircleNode wCircleNode:circleNodes) {
			wCircleNode.setCenterDrawing(this.centerDrawing);
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