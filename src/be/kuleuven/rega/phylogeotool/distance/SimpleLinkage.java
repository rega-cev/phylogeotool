package be.kuleuven.rega.phylogeotool.distance;

import java.util.List;

import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class SimpleLinkage {

	private Tree tree;

	public SimpleLinkage(Tree tree) {
		this.tree = tree;
	}
	
	public boolean compare(double arg0, double arg1) {
		if (arg0 < arg1) {
			return true;
		} else {
			return false;
		}
	}

	public double measure(ICluster cluster1, ICluster cluster2) {
		double currentClosest = Double.MAX_VALUE;

		List<Node> leavesCluster1 = cluster1.getLeaves();
		List<Node> leavesCluster2 = cluster2.getLeaves();

		for (Node node1 : leavesCluster1) {
			for (Node node2 : leavesCluster2) {
				double distance = tree.getDistance(node1, node2);
				if (distance < currentClosest) {
					currentClosest = distance;
				}
			}
		}

		return currentClosest;
	}

}
