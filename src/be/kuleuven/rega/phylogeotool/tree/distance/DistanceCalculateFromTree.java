package be.kuleuven.rega.phylogeotool.tree.distance;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class DistanceCalculateFromTree implements DistanceInterface {

	public double getDistance(Tree tree, Node node) {
		double distance = 0.0;
		while(node.hasParent()) {
			distance += tree.getEdgeToParent(node).getDistance();
			node = node.getParent();
		}
		return distance;
	}
	
	public double getDistance(Tree tree, Node node1, Node node2) {
		double distance = this.getDistance(tree, node1) + this.getDistance(tree, node2) - 2*this.getDistance(tree, tree.getLeastCommonAncestor(node1, node2));
		return distance;
	}
}
