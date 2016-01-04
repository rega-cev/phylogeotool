package be.kuleuven.rega.prerendering;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;

import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.ClusterDistance;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceMatrixDistance;


public class BestClusterMultiThread {
	
	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();
	private static String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt";
	private static HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws FileNotFoundException {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
//		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.997.midpoint.solved.newick"));
//		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/testTree.phylo"));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
		DistanceInterface distanceInterface = null;
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
			distanceInterface = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
		} else {
			distanceInterface = new DistanceCalculateFromTree();
		}
		
		List<Double> distances = forkJoinPool.invoke(new PreRenderingThread(2, 50, tree, tree.getRootNode(), distanceInterface, new ClusterSizeComparator(tree), 2));
		ClusterDistance clusterDistance = new ClusterDistance(tree);
		Entry<Integer, Double> maxSecondDerivative = clusterDistance.getMaxSecondDerivative(distances);
		System.out.println("MaxEntry: " + maxSecondDerivative.getKey() + " Value: " + maxSecondDerivative.getValue());
	}
	
	public static Cluster getBestCluster(int minClusters, int maxClusters, int minClusterSize, Tree tree, Node startNode, DistanceInterface distanceInterface) throws FileNotFoundException {
		List<Double> distances = forkJoinPool.invoke(new PreRenderingThread(minClusters, maxClusters, tree, startNode, distanceInterface, new ClusterSizeComparator(tree), minClusterSize));
		ClusterDistance clusterDistance = new ClusterDistance(tree);
		if(distances.size() >= 3) {
			Entry<Integer, Double> maxSecondDerivative = clusterDistance.getMaxSecondDerivative(distances);
			System.out.println("MaxEntry: " + maxSecondDerivative.getKey() + " Value: " + maxSecondDerivative.getValue());
			return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, maxSecondDerivative.getKey());
		} else {
			System.out.println("MaxEntry: " + (distances.size() + 1) + " Value: No second derivative");
			return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, (distances.size() + 1));
		}
	}
}
