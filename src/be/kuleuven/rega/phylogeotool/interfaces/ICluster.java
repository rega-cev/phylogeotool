package be.kuleuven.rega.phylogeotool.interfaces;

import java.util.Set;

import be.kuleuven.rega.phylogeotool.tree.Node;

public interface ICluster {
	
	public Set<Node> getLeaves();
//	public DistanceProvider getDistanceProvider();
	public Node getRoot();

}
