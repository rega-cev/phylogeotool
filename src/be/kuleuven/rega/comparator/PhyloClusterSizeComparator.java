package be.kuleuven.rega.comparator;

import java.util.Comparator;

import be.kuleuven.rega.phylogeotool.tree.Node;

public class PhyloClusterSizeComparator implements Comparator<Node> {

	@Override
	public int compare(Node node1, Node node2) {
		if(node1.getLeaves().size() < node2.getLeaves().size()) {
			return 1;
		} else if(node1.getLeaves().size() > node2.getLeaves().size()) {
			return -1;
		} else {
			return 0;
		}
	}

}
