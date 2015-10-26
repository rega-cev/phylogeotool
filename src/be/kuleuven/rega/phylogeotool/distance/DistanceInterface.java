package be.kuleuven.rega.phylogeotool.distance;

import be.kuleuven.rega.phylogeotool.tree.Node;

public interface DistanceInterface {

	public double getDistance(Node node1, Node node2);
	
}
