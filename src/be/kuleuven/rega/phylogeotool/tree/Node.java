package be.kuleuven.rega.phylogeotool.tree;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;

public class Node {
	private String data;
	private String label;
	private Node parent;
	private List<Node> children;
	private NodeIndexProvider nodeIndexProvider;
	
	public Node() {
		this.children = new ArrayList<Node>();
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
}
