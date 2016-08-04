package be.kuleuven.rega.phylogeotool.tree;

import java.awt.Color;

import be.kuleuven.rega.phylogeotool.core.Node;
import eu.webtoolkit.jwt.WCircleArea;

public class WCircleNode extends WCircleArea {

	private Node node = null;
	private Color color = null;
	
	public WCircleNode(int x, int y, int radius, Node node, Color color) {
		super(x,y,radius);
		this.node = node;
		this.color = color;
	}

	public Node getNode() {
		return node;
	}
	
	public Color getColor() {
		return color;
	}
}
