package be.kuleuven.rega.comparator;

import java.util.Comparator;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class ClusterDistanceComparator implements Comparator<Node> {

	private Tree tree;
	
	public ClusterDistanceComparator(Tree tree) {
		this.tree = tree;
	}
	
	@Override
	public int compare(Node node1, Node node2) {
//		System.out.println("Distance " + node1.getLabel() + ": " + tree.getEdge(node1.getParent(), node1).getDistance());
//		System.out.println("Distance " + node2.getLabel() + ": " + tree.getEdge(node2.getParent(), node2).getDistance());
		if(tree.getEdge(node1.getParent(), node1).getDistance() < tree.getEdge(node2.getParent(), node2).getDistance()) {
			return 1;
		} else if(tree.getEdge(node1.getParent(), node1).getDistance() > tree.getEdge(node2.getParent(), node2).getDistance()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}