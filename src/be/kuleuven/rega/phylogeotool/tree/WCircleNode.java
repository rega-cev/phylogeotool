package be.kuleuven.rega.phylogeotool.tree;

import java.awt.Point;

import eu.webtoolkit.jwt.WCircleArea;

public class WCircleNode extends WCircleArea {

	private Node node = null;
	private Point centerDrawing = null;
	
	public WCircleNode(int x, int y, int radius, Node node) {
		super(x,y,radius);
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
	
	/**
	 * Getter and setter to get the center of the whole tree drawing where this node belongs to
	 * @param centerDrawing
	 */
	public void setCenterDrawing(Point centerDrawing) {
		this.centerDrawing = centerDrawing;
	}
	
	public Point getCenterDrawing() {
		return this.centerDrawing;
	}
}
