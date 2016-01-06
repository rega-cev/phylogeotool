package be.kuleuven.rega.phylogeotool.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableList;

public class Node {
	private final int id;
	private final String label;
	private final Node parent;
	private final String attribute;
	private List<Node> children;
//	private double x = 0.0;
//	private double y = 0.0;
//	private double theta = 0.0;
//	private int size = 0;
//	private Color nodeColor;
//	private String attribute = null;

	public Node(int id, String label, Node parent, String attribute) {
		this.id = id;
		this.label = label;
		this.parent = parent;
		this.attribute = attribute;
		this.children = new ArrayList<Node>();
		if(parent != null) 
			parent.addChild(this);
	}
	
	public Node(int id, String label, Node parent, String attribute, List<Node> children) {
		this.id = id;
		this.label = label;
		this.parent = parent;
		this.attribute = attribute;
		this.children = children;
	}
	
	public int getId() {
		return id;
	}

	public Node getParent() {
		return parent;
	}
	
	public String getAttribute() {
		return this.attribute;
	}
	
	public void addChild(Node node) {
		this.children.add(node);
	}
	
	public List<Node> getImmediateChildren() {
		return ImmutableList.copyOf(children);
	}
	
	public boolean hasChildren() {
		return children.size() != 0;
	}

	public String getLabel() {
		return label;
	}
//	public double getX() {
//		return x;
//	}
//
//	private void setX(double x) {
//		this.x = x;
//	}
//
//	public double getY() {
//		return y;
//	}
//
//	private void setY(double y) {
//		this.y = y;
//	}
//
//	public double getTheta() {
//		return theta;
//	}
//
//	private void setTheta(double theta) {
//		this.theta = theta;
//	}
//	
//	private void setAttribute(String attribute) {
//		this.attribute = attribute;
//	}
//	
//	public String getAttribute() {
//		return this.attribute;
//	}
//
//	private void setLeaves(List<Node> leafs) {
//		this.leafs = leafs;
//	}
//	
//	public List<Node> getLeaves() {
//		if(allChildren == null) {
//			allChildren = new ArrayList<Node>();
//		}
//		if (leafs == null) {
//			this.leafs = new ArrayList<Node>();
//		} else if (leafs.size() > 0) {
//			return leafs;
//		}
//		visitNode(this);
//		return leafs;
//	}
//
//	public List<String> getLeavesAsString() {
//		if(leafsAsString != null) {
//			return leafsAsString;
//		} else {
//			leafsAsString = new ArrayList<String>();
//			if (leafs == null) {
//				this.getLeaves();
//			}
//			for(Node node:leafs) {
//				leafsAsString.add(node.getLabel());
//			}
//		}
//		return leafsAsString;
//	}
//
//	public void visitNode(Node node) {
//		if (node.hasChildren() && node.getImmediateChildren().get(0) != null) {
//			allChildren.add(node.getImmediateChildren().get(0));
//			visitNode(node.getImmediateChildren().get(0));
//		}
//		if (node.hasChildren() && node.getImmediateChildren().get(1) != null) {
//			allChildren.add(node.getImmediateChildren().get(1));
//			visitNode(node.getImmediateChildren().get(1));
//		}
//		if (!node.hasChildren()) {
//			leafs.add(node);
//		}
//	}
//
//	public int getSize() {
//		return this.size;
//	}
//
//	private void setSize(int size) {
//		this.size = size;
//	}
//
//	private void setColor(Color color) {
//		this.nodeColor = color;
//	}
//
//	public Color getColor() {
//		return nodeColor;
//	}
//	
//	private void setEdgeToParent(Edge edgeToParent) {
//		this.edgeToParent = edgeToParent;
//	}
//	
//	public Edge getEdgeToParent() {
//		return this.edgeToParent;
//	}

	public boolean hasParent() {
		return getParent() != null;
	}
	
	// TODO: Check if it's better to clone the leaves as well.
//	public Node clone() {
//		Node node = new Node(this.getLabel(), this.getId());
//		node.setColor(this.getColor());
//		node.setParent(this.getParent());
//		for(Node tempNode:this.getImmediateChildren()) {
//			node.addChild(tempNode);
//		}
//		node.setSize(this.getSize());
//		node.setTheta(this.getTheta());
//		node.setX(this.getX());
//		node.setY(this.getY());
////		node.setLeaves(this.getLeaves());
//		return node;
//	}
	
	@Override
	public boolean equals(Object other) {
		Node otherNode = (Node)other;
		if(this.getId() == otherNode.getId()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(id);
		return builder.toHashCode();
    }
}
