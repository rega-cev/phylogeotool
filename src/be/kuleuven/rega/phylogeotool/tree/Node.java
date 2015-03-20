package be.kuleuven.rega.phylogeotool.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.rega.comparator.PhyloClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;

public class Node {
	private String data;
	private String label;
	private Node parent;
	private List<Node> children;
	private List<Node> leafs;
	private NodeIndexProvider nodeIndexProvider;
	
	public Node() {
		this.children = new ArrayList<Node>();
	}
	
	public Node(NodeIndexProvider nodeIndexProvider) {
		this.children = new ArrayList<Node>();
		this.label = "";
		this.nodeIndexProvider = nodeIndexProvider;
	}
	
	public Node(String label, NodeIndexProvider nodeIndexProvider) {
		this.children = new ArrayList<Node>();
		this.label = label;
		this.nodeIndexProvider = nodeIndexProvider;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public List<Node> getChildren() {
		return children;
	}
	// Note that the biggest element has to be in the beginning and the lowest one in the end
	public List<Node> getChildrenBiggestFirst() {
		Collections.sort(this.getChildren(), new PhyloClusterSizeComparator());
		return this.getChildren();
	}	
	public void addChild(Node node) {
		this.children.add(node);
	}
	public boolean hasChildren() {
		return children.size() != 0;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getIndex() {
		return nodeIndexProvider.getIndex(label);
	}
	public List<Node> getLeaves() {
		if(leafs == null) {
			this.leafs = new ArrayList<Node>();
		} else if(leafs.size() > 0) {
			return leafs;
		}
		visitNode(this);
		return leafs;
	}
	public void visitNode(Node node) {
	    if(node.hasChildren() && node.getChildren().get(0) != null) {
	        visitNode(node.getChildren().get(0));
	    }
	    if(node.hasChildren() && node.getChildren().get(1) != null) {
	        visitNode(node.getChildren().get(1));
	    }
	    if(!node.hasChildren()) {
	        leafs.add(node);
	    }
	}
}
