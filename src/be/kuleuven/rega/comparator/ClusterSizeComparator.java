package be.kuleuven.rega.comparator;

import java.util.Comparator;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;

public class ClusterSizeComparator implements Comparator<Node> {

	private RootedTree tree;
	
	public ClusterSizeComparator(RootedTree tree) {
		this.tree = tree;
	}
	
	@Override
	public int compare(Node node1, Node node2) {
		if(tree.getChildren(node1).size() > tree.getChildren(node2).size()) {
			return 1;
		} else if(tree.getChildren(node1).size() < tree.getChildren(node2).size()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
