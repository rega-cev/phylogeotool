package be.kuleuven.rega.phylogeotool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Cluster {

	@XStreamOmitField
	private final Tree tree;
	@XStreamOmitField
	private final Node root;
	private final int rootId;
	private final int parentalClusterId;
	@XStreamOmitField
	private final List<Node> boundaries;
	private final List<Integer> boundariesIds;
	
	public Cluster(Tree tree, Node root, Cluster parentalCluster, List<Node> boundaries) {
		this.tree = tree;
		this.root = root;
		this.rootId = root.getId();
		if(parentalCluster != null)
			this.parentalClusterId = parentalCluster.getRootId();
		else
			this.parentalClusterId = 1;
		this.boundaries = boundaries;
		this.boundariesIds = new ArrayList<Integer>();
		for(Node boundary:boundaries) {
			boundariesIds.add(boundary.getId());
		}
	}
	
	public Cluster(Tree tree, int rootId, int parentalClusterId, List<Integer> boundariesIds) {
		this.tree = tree;
		this.root = tree.getNodeById(rootId);
		this.rootId = rootId;
		this.parentalClusterId = parentalClusterId;
		this.boundariesIds = boundariesIds;
		this.boundaries = new ArrayList<Node>();
		for(Integer boundaryId:boundariesIds) {
			this.boundaries.add(tree.getNodeById(boundaryId));
		}
	}
	
	public Tree getTree() {
		return tree;
	}
	public Node getRoot() {
		return this.root;
	}
	public int getRootId() {
		return rootId;
	}
	public int getParentalClusterRootId() {
		return parentalClusterId;
	}
	public List<Node> getBoundaries() {
		return Collections.unmodifiableList(this.boundaries);
	}
	public List<Integer> getBoundariesIds() {
		return Collections.unmodifiableList(this.boundariesIds);
	}
	public List<Node> getAllNodes() {
		List<Node> allNodes = new ArrayList<Node>();
		allNodes.add(this.root);
		allNodes.addAll(getInnerNodes());
		allNodes.addAll(this.boundaries);
		return Collections.unmodifiableList(allNodes);
	}
	public List<Node> getInnerNodes() {
		List<Node> innerNodes = new ArrayList<Node>();
		this.getInnerNodes(root, innerNodes);
		return Collections.unmodifiableList(innerNodes);
	}
	public List<Edge> getEdges() {
		Set<Edge> edges = new HashSet<Edge>();
		
		Node tempNode;
		for(Node node:getBoundaries()) {
			tempNode = node;
			while(!tempNode.equals(root)) {
				edges.add(tree.getEdgeToParent(tempNode));
				if(tree.getEdgeToParent(tempNode).getNode1() != tempNode) {
					tempNode = tree.getEdgeToParent(tempNode).getNode1();
				} else {
					tempNode = tree.getEdgeToParent(tempNode).getNode2();
				}
			}
		}
		return Collections.unmodifiableList(new ArrayList<Edge>(edges));
	}
	/**
	 * A cluster is considered acceptable when it has more than 2 leaves.
	 * @return true if cluster contains enough leaves
	 * 		   false if cluster doesn't contain enough leaves
	 */
	public boolean isAcceptableCluster() {
		if(this.getBoundaries().size() >= 2) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public boolean equals(Object other){
		Cluster otherCluster = (Cluster)other;
		if(other != null && (this.getRoot().getId() == otherCluster.getRoot().getId())) {
			for(Node tempNode:this.getBoundaries()) {
				if(!otherCluster.getBoundaries().contains(tempNode)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
    }
	
	private void getInnerNodes(Node root, List<Node> innerNodes) {
		if(this.boundaries.contains(root)) {
			return;
		} else {
			if(!root.equals(this.root))
				innerNodes.add(root);
			for(Node child:root.getImmediateChildren()) {
				getInnerNodes(child, innerNodes);
			}
		}
	}
}