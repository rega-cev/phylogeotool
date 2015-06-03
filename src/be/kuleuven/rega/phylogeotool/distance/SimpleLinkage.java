package be.kuleuven.rega.phylogeotool.distance;

import java.util.Set;

import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tree.Node;

public class SimpleLinkage {

	public boolean compare(double arg0, double arg1) {
		if (arg0 < arg1) {
			return true;
		} else {
			return false;
		}
	}

	// TODO: CHECK IF STILL NEEDED
	public double measure(ICluster cluster1, ICluster cluster2) {
		double currentClosest = Double.MAX_VALUE;

//		Set<Node> leavesCluster1 = cluster1.getLeaves();
//		Set<Node> leavesCluster2 = cluster2.getLeaves();
//
//		for (Node node1 : leavesCluster1) {
//			for (Node node2 : leavesCluster2) {
//				double distance = cluster1.getDistanceProvider().getDistance(node1, node2);
//				if (distance < currentClosest) {
//					currentClosest = distance;
//				}
//			}
//		}

		return currentClosest;
	}

}
