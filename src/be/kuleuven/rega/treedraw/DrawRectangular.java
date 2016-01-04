package be.kuleuven.rega.treedraw;

import java.awt.Color;
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

public class DrawRectangular extends Draw {

	private Cluster cluster = null;
	private Shape shape = null;
	private double r = 20;
	
	public DrawRectangular(Cluster cluster, Shape shape) {
		this.shape = shape;
		this.cluster = cluster;
	}
	
	public List<WCircleNode> paint(WebGraphics2DMine graphics, Map<Node, DrawData> map, Map<Node, Color> nodeToColor, double paintAreaWidth, double paintAreaHeight) {
		double maxX = 0;
		double maxY = 0;
		/*
		 * Determine max x and max y
		 */
		for(Node node:cluster.getBoundaries()) {
			if(map.get(node).getX() > maxX) {
				maxX = map.get(node).getX();
			}
			if(map.get(node).getY() > maxY) {
				maxY = map.get(node).getY();
			}
		}
		// TODO: Look for height of component
		double factorX = (paintAreaWidth - r)/maxX;
		double factorY = (paintAreaHeight- r)/maxY;
		
//		int maxSize = 0;
//		for(Node node:cluster.getBoundaries()) {
//			if(cluster.getTree().getLeaves(node).size() > maxSize) {
//				maxSize = cluster.getTree().getLeaves(node).size();
//			}
//		}
		
//		for(Node node:nodes) {
//			graphics.draw(new Rectangle((int)Math.ceil(node.getX()*factorX), (int)Math.ceil(node.getY()*factorY), nodeWidth, nodeHeight));
//		}
		List<WCircleNode> circleNodes = new ArrayList<WCircleNode>();
		for (Edge edge : cluster.getEdges()) {
			graphics.setColor(Color.BLACK);
			Point2D corner = new Point2D.Double(Math.ceil(map.get(edge.getNode1()).getX()*factorX), Math.ceil(map.get(edge.getNode2()).getY()*factorY));
			Point2D start = new Point2D.Double(Math.ceil(map.get(edge.getNode1()).getX()*factorX), Math.ceil(map.get(edge.getNode1()).getY()*factorY));
			Point2D end = new Point2D.Double(Math.ceil(map.get(edge.getNode2()).getX()*factorX), Math.ceil(map.get(edge.getNode2()).getY()*factorY));
			graphics.draw(new Line2D.Double(start, corner));
			graphics.draw(new Line2D.Double(corner, end));
			
			// Draw circles at the end
			if (cluster.getBoundaries().contains(edge.getNode2())) {
//				graphics.setColor(edge.getNode2().getColor());
				if(nodeToColor != null && nodeToColor.containsKey(edge.getNode2())) {
					graphics.setColor(nodeToColor.get(edge.getNode2()));
				}
//				double r = getRadius(maxNodeSurface, maxSize, edge.getNode2().getSize());
				double x = end.getX();
				double y = end.getY() - r/2;
				Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r, r);
				graphics.fill(circle);
				// If you want the circle to be surrounded
				graphics.setColor(graphics.getColor().darker());
				graphics.draw(circle);
			
				final WCircleNode wCircle = new WCircleNode((int)(map.get(edge.getNode2()).getX()), (int)(map.get(edge.getNode2()).getY()), (int)(r/2), edge.getNode2());
				wCircle.setToolTip("Node " + edge.getNode2().getId());
				wCircle.setCursor(Cursor.CrossCursor);
				circleNodes.add(wCircle);
//				wPaintedWidget.addArea(wCircle);
				
//				graphics.drawString(Integer.toString(edge.getNode2().getSize()), (int)(x + r + 10), (int)(y + r/2 + 10));
//				graphics.drawString(Double.toString(map.get(edge.getNode2()).getX()*factorX), (int)(x + r + 10), (int)(y + r/2 + 10));
			}
		}
		return circleNodes;
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
