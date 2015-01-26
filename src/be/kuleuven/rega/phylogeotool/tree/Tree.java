package be.kuleuven.rega.phylogeotool.tree;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tree.tools.DistanceProvider;

public class Tree implements ICluster {
	private Node root;
	private Set<Node> leafs = new HashSet<Node>();
	private DistanceProvider distanceProvider;

	public Tree(DistanceProvider distanceProvider) {
		root = new Node();
		setLeaves(root);
		this.distanceProvider = distanceProvider; 
	}
	
	public Tree(Node rootNode, DistanceProvider distanceProvider) {
		root = rootNode;
		setLeaves(root);
		this.distanceProvider = distanceProvider;
	}
	
	public Tree(String rootData, DistanceProvider distanceProvider) {
		root = new Node();
		root.setData(rootData);
		setLeaves(root);
		this.distanceProvider = distanceProvider;
	}

	public Set<Node> getLeaves() {
		return new HashSet<Node>(leafs);
	}

	public void setLeaves(Node currentNode) {
		if(currentNode.hasChildren()) {
			for (Node child : currentNode.getChildren()) {
				setLeaves(child);
			}
		} else {
			leafs.add(currentNode);
		}
	}

	@Override
	public DistanceProvider getDistanceProvider() {
		return this.distanceProvider;
	}

	@Override
	public Node getRoot() {
		return this.root;
	}

}