package be.kuleuven.rega.phylogeotool.distance;

import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class DistanceCalculateFromTree implements DistanceInterface {

	private Tree tree = null;
	
	public DistanceCalculateFromTree(Tree tree) {
		this.tree = tree;
	}
	
	@Override
	public double getDistance(Node node1, Node node2) {
		double distance = tree.getDistance(node1) + tree.getDistance(node2) - 2*tree.getDistance(tree.getLeastCommonAncestor(node1, node2));
		return distance;
	}

}
