package be.kuleuven.rega.treedraw;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.WebGraphics2DMine;
import eu.webtoolkit.jwt.Cursor;

public class DrawCircular extends Draw {

	private Cluster cluster = null;
	private Shape shape = null;

	public DrawCircular(Cluster cluster, Shape shape) {
		this.cluster = cluster;
		this.shape = shape;
	}

	public List<WCircleNode> paint(WebGraphics2DMine graphics, Map<Node, DrawData> map, Map<Node, Color> nodeToColor, double paintAreaWidth, double paintAreaHeight) {	
	// TODO: Look for height of component
		
		double moveRight = paintAreaWidth / 2;
		double moveDown = paintAreaHeight / 2;
		
		double maxRadius = 50;
		double maxDistance = 0.0;
//		d(p,q) = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2))
		for(Node node:cluster.getBoundaries()) {
			double distance = Math.sqrt(
					Math.pow(polarToEucledianX(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) - polarToEucledianX(map.get(node).getX(), map.get(node).getTheta()),2) 
					+ Math.pow(polarToEucledianY(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) - polarToEucledianY(map.get(node).getX(), map.get(node).getTheta()),2));
			if(distance > maxDistance) {
				maxDistance = distance;
			}
		}
		
		double factor = 0.0;
		if(paintAreaWidth > paintAreaHeight) {
			factor = (paintAreaHeight - (maxRadius * 2))/(2*maxDistance);
		} else {
			factor = (paintAreaWidth - (maxRadius * 2))/(2*maxDistance);
		}
		
		// TODO: Change the node sizes
//		if(TreeTraversal.nodeLevel(edges.iterator().next().getNode1(), 0) < 5) {
//			System.out.println("Here");
//			maxRadius = 50;
//		}
		double maxNodeSurface = Math.PI * Math.pow(maxRadius, 2);
		int maxSize = 0;
		for(Node node:cluster.getBoundaries()) {
			if(cluster.getTree().getLeaves(node).size() > maxSize) {
				maxSize = cluster.getTree().getLeaves(node).size();
			}
		}
		
		// To draw a rectangle on top of every node
//		 for(Node node:nodes) {
//			 graphics.drawString(node.getLabel(), (int)Math.ceil(Node.polarToEucledianX(node)*factor) + moveRight, (int)Math.ceil(Node.polarToEucledianY(node)*factor) + moveRight);
//			 graphics.draw(new Rectangle((int)Math.ceil(Node.polarToEucledianX(node)*factor) + moveRight, (int)Math.ceil(Node.polarToEucledianY(node)*factor) + moveRight, nodeWidth, nodeHeight));
//		 }
		List<WCircleNode> circleNodes = new ArrayList<WCircleNode>();
		for (Edge edge : cluster.getEdges()) {
			graphics.setColor(Color.BLACK);
			Point2D corner = null;
			Point2D end = null;
			
			if (shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
				DrawData drawData = new DrawData(map.get(edge.getNode1()).getX(), 0.0, map.get(edge.getNode2()).getTheta());
				corner = new Point2D.Double(polarToEucledianX(drawData.getX(), drawData.getTheta()) * factor + moveRight, polarToEucledianY(drawData.getX(), drawData.getTheta()) * factor + moveDown);
				end = new Point2D.Double(polarToEucledianX(map.get(edge.getNode2()).getX(), map.get(edge.getNode2()).getTheta()) * factor + moveRight, polarToEucledianY(map.get(edge.getNode2()).getX(), map.get(edge.getNode2()).getTheta()) * factor + moveDown);
				int circleWidth = 0;
				if(shape == Shape.CIRCULAR_CLADOGRAM) {
					circleWidth = (int) (2 * TreeTraversal.nodeLevel(cluster.getRoot(), edge.getNode1(), 0) * factor);
//					System.out.println(circleWidth);
				} else {
					// Calculate the eucledian distance from the center of the tree to the startnode (this to know the width of the circle)
					// d(p,q) = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2))
//					System.out.println(polarToEucledianX(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()));
//					circleWidth = (int) (2 * Math.sqrt((Math.pow(((polarToEucledianX(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()) * factor + moveRight)-(polarToEucledianX(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) * factor + moveRight)), 2) + Math.pow(((polarToEucledianY(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()) * factor + moveRight)-(polarToEucledianY(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) * factor + moveRight)), 2))));
					double diameter = (2 * Math.sqrt((Math.pow(polarToEucledianX(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()) - polarToEucledianX(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()), 2) + Math.pow(polarToEucledianY(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta())-polarToEucledianY(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()), 2))));
					circleWidth = (int) (diameter * factor);
				}
				Rectangle boundingBox = new Rectangle((int) (polarToEucledianX(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) * factor + moveRight - circleWidth / 2), (int) (polarToEucledianY(map.get(cluster.getRoot()).getX(), map.get(cluster.getRoot()).getTheta()) * factor + moveDown - circleWidth / 2), circleWidth, circleWidth);
				graphics.draw(new Arc2D.Double(boundingBox, 360 - Math.toDegrees(map.get(edge.getNode1()).getTheta()), Math.toDegrees(map.get(edge.getNode1()).getTheta()) - Math.toDegrees(map.get(edge.getNode2()).getTheta()), Arc2D.Double.OPEN));
				graphics.draw(new Line2D.Double(corner, end));
//				graphics.drawString(Integer.toString(edge.getNode2().getId()), (int)map.get(edge.getNode2()).getX(), (int)map.get(edge.getNode2()).getY());
			} else if (shape == Shape.RADIAL) {
				graphics.draw(new Line2D.Double(map.get(edge.getNode1()).getX() * factor + moveRight, map.get(edge.getNode1()).getY() * factor + moveDown, map.get(edge.getNode2()).getX() * factor + moveRight, map.get(edge.getNode2()).getY() * factor + moveDown));
			}
			// Draw the clusters at the end of the leaves
			if (cluster.getBoundaries().contains(edge.getNode2())) {
				if(nodeToColor != null && nodeToColor.containsKey(edge.getNode2())) {
					graphics.setColor(nodeToColor.get(edge.getNode2()));
				}
				double r = getRadius(maxNodeSurface, maxSize, cluster.getTree().getLeaves(edge.getNode2()).size());
				double x = 0.0;
				double y = 0.0;
				if (shape == Shape.RADIAL) {
					x = map.get(edge.getNode2()).getX() * factor + moveRight + r / 2 * Math.cos(map.get(edge.getNode2()).getTheta());
					y = map.get(edge.getNode2()).getY() * factor + moveDown + r / 2 * Math.sin(map.get(edge.getNode2()).getTheta());
				} else if (shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
					x = end.getX() + r / 2 * Math.cos(map.get(edge.getNode2()).getTheta());
					y = end.getY() + r / 2 * Math.sin(map.get(edge.getNode2()).getTheta());
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
//				if(wCircle.getNode().getSize() > 1) {
//					wCircle.clicked().addListener(wPaintedWidget, new WCircleNodeClickListener(wCircle, graphWebApplication));
//				}
//				wCircle.doubleClicked().addListener(wPaintedWidget, new WCircleNodeDoubleClickListener(wCircle, graphWebApplication));
//				wCircle.mouseWentOver().addListener(wPaintedWidget, new WCircleMouseWentOverListener(wCircle, graphWebApplication));
//				wCircle.mouseWentOut().addListener(wPaintedWidget, new WCircleMouseWentOutListener(wCircle, graphWebApplication));
				
//				wPaintedWidget.addArea(wCircle);
				
				//TODO: Simplify this method (create one big arraylist)
//				if(graphWebApplication.getPPlacer().clusterContainsPPlacerSequence(Integer.toString(edge.getNode2().getId()))) {
//					graphics.setFont(graphics.getFont().deriveFont(Font.BOLDITALIC, 11.0f));
//					graphics.drawString(new String(Integer.toString(edge.getNode2().getSize()) + "*"), (int)(x - 20), (int)(y + r/2 + 20));
//				} else {
//					graphics.setFont(graphics.getFont().deriveFont(Font.NORMAL, 11.0f));
//					graphics.drawString(Integer.toString(edge.getNode2().getSize()), (int)(x - 20), (int)(y + r/2 + 20));
//				}
				
			}
//			if(!edge.getNode1().hasParent()) {
//				int r = 5;
//				Ellipse2D.Double center = new Ellipse2D.Double(polarToEucledianX(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()) * factor + moveRight - (r / 2), polarToEucledianY(map.get(edge.getNode1()).getX(), map.get(edge.getNode1()).getTheta()) * factor + moveDown - (r / 2), r, r);
//				graphics.setColor(Color.BLACK);
//				graphics.fill(center);
//				this.centerDrawing = new Point((int)center.getCenterX(), (int)center.getCenterY());
//			}
		}
		return circleNodes;
		
//		for(WCircleNode wCircleNode:circleNodes) {
//			wCircleNode.setCenterDrawing(this.centerDrawing);
//		}
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