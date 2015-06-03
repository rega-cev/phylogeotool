package be.kuleuven.rega.phylogeotool.tree;

import eu.webtoolkit.jwt.WCircleArea;

public class WCircleNode extends WCircleArea {

	private Node node = null;
	
	public WCircleNode(int x, int y, int radius, Node node) {
		super(x,y,radius);
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
	
}
