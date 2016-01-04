package be.kuleuven.rega.comparator;

import java.util.Comparator;
import java.util.List;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class ClusterSizeComparator implements Comparator<Node> {

	private Tree tree;
	
	public ClusterSizeComparator(Tree tree) {
		this.tree = tree;
	}
	
	@Override
	public int compare(Node node1, Node node2) {
		List<Node> leafs1 = tree.getLeaves(node1);
		List<Node> leafs2 = tree.getLeaves(node2);
		if(leafs1.size() < leafs2.size()) {
			return 1;
		} else if(leafs1.size() > leafs2.size()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
