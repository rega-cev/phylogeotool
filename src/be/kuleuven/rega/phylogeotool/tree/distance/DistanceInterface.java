package be.kuleuven.rega.phylogeotool.tree.distance;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public interface DistanceInterface {

	public double getDistance(Tree tree, Node node1, Node node2);
	
}
