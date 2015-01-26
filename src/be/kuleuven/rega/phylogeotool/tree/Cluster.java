//package be.kuleuven.rega.phylogeotool.tree;
//
//import java.util.Set;
//
//import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
//import be.kuleuven.rega.phylogeotool.tree.tools.DistanceProvider;
//
//public class Cluster implements ICluster {
//
//	private Tree tree;
//	private DistanceProvider distanceProvider;
//	
//	public Cluster(Tree tree, DistanceProvider distanceProvider) {
//		this.tree = tree;
//		this.distanceProvider = distanceProvider;
//	}
//	
//	@Override
//	public Set<Node> getLeaves() {
//		return tree.getLeaves();
//	}
//	
//	@Override
//	public Tree getRoot() {
//		return tree;
//	}
//	
//	@Override
//	public DistanceProvider getDistanceProvider() {
//		return this.distanceProvider;
//	}
//
//}
