package be.kuleuven.rega.phylogeotool.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public class Tree {
	private final Set<Node> nodes;
	private final Set<Edge> edges;
	
	private final Node rootNode;
	
	private final Map<Node, Edge> edgeToParent;
	
	public Tree(Set<Node> nodes, Set<Edge> edges, Node rootNode) {
		this.nodes = nodes;
		this.edges = edges;
		this.rootNode = rootNode;
		
		edgeToParent = new HashMap<Node, Edge>();
	}
		
	public Node getRootNode() {
		return this.rootNode;
	}
	
	public ImmutableList<Node> getNodes() {
		return ImmutableList.copyOf(nodes);
	}

	public ImmutableList<Edge> getEdges() {
		return ImmutableList.copyOf(edges);
	}

	public Edge getEdgeToParent(Node node) {
		if(!edgeToParent.containsKey(node)) {
			edgeToParent.put(node, this.getEdge(node, node.getParent()));
		}
		return edgeToParent.get(node);
	}
	
	public Edge getEdge(Node node1, Node node2) {
		for(Edge edge:edges) {
			if(edge.getNode1().equals(node1) && edge.getNode2().equals(node2)) {
				return edge;
			} else if(edge.getNode1().equals(node2) && edge.getNode2().equals(node1)) {
				return edge;
			}
		}
		return null;
	}
	
	public Edge getEdge(int nodeId1, int nodeId2) {
		for(Edge edge:edges) {
			if(edge.getNode1().getId() == nodeId1 && edge.getNode2().getId() == nodeId2) {
				return edge;
			} else if(edge.getNode1().getId() == nodeId1 && edge.getNode2().getId() == nodeId2) {
				return edge;
			}
		}
		return null;
	}
	
	public Node getNodeById(int id) {
		for(Node node:nodes) {
			if(node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	public Node getLeafByLabel(String label) {
		for(Node node:nodes) {
			if(node.getLabel().equals(label)) {
				return node;
			}
		}
		return null;
		//throw new Exception("String with label: " + label + " not found.");
	}
	
	public Node getLeastCommonAncestor(Node node1, Node node2) {
		List<Node> parents1 = this.getAllParents(node1);
		List<Node> parents2 = this.getAllParents(node2);
		parents1.retainAll(parents2);
		if(parents1.size() > 0) {
			return parents1.iterator().next();
		} else {
			if(parents2.size() == 0) {
				return node2;
			} else {
				return node1;
			}
		}
	}
	
	public List<Node> getLeaves() {
		return this.getLeaves(this.getRootNode());
	}
	
	// SHOULD ONLY BE CALLED WHEN CREATING A CLUSTER
	public List<Node> getLeaves(Node node) {
		return visitLeafsRecursive(node, new ArrayList<Node>());
	}
	
	// Note: This is rootnode + inner nodes + leafs
	public List<Node> getAllNodes(Node node) {
		return visitNodes(node, new ArrayList<Node>());
	}
	
	public List<Node> getAllParents(Node node) {
		List<Node> parents = new ArrayList<Node>();
		while (node.getParent() != null) {
			parents.add(node.getParent());
			node = node.getParent();
		}
		return parents;
	}
	
	private List<Node> visitNodes(Node node, List<Node> leafs) {
		if (node.hasChildren() && node.getImmediateChildren().get(0) != null) {
			visitNodes(node.getImmediateChildren().get(0), leafs);
		}
		if (node.hasChildren() && node.getImmediateChildren().get(1) != null) {
			visitNodes(node.getImmediateChildren().get(1), leafs);
		}
		leafs.add(node);
		return leafs;
	}
	
	private List<Node> visitLeafsRecursive(Node node, List<Node> leafs) {
		if (node.hasChildren() && node.getImmediateChildren().get(0) != null) {
			visitLeafsRecursive(node.getImmediateChildren().get(0), leafs);
		}
		if (node.hasChildren() && node.getImmediateChildren().get(1) != null) {
			visitLeafsRecursive(node.getImmediateChildren().get(1), leafs);
		}
		if (!node.hasChildren()) {
			leafs.add(node);
		}
		return leafs;
	}
	
	public HashMap<Integer, Node> getLeavesMap() {
		HashMap<Integer, Node> leaves = new HashMap<Integer, Node>();
		for(Node node:this.getLeaves()) {
			leaves.put(node.getId(), node);
		}
		return leaves;
	}

	@Override
    public boolean equals(Object other){
		Tree otherTree = (Tree)other;
		if(other != null && (this.getRootNode().getId() == otherTree.getRootNode().getId()
				&& this.getEdges().size() == otherTree.getEdges().size()
				&& this.getNodes().size() == otherTree.getNodes().size())) {
			for(Edge edge:this.getEdges()) {
				if(!otherTree.getEdges().contains(edge)) {
					return false;
				}
			}
			
			for(Node node:this.getNodes()) {
				if(!otherTree.getNodes().contains(node)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
    }	
}
