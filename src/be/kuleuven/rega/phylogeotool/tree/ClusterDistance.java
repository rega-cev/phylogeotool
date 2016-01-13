package be.kuleuven.rega.phylogeotool.tree;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jebl.evolution.trees.SimpleRootedTree;
import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceMatrixDistance;
import be.kuleuven.rega.phylogeotool.tree.distance.TreeStatistics;

public class ClusterDistance {

	private HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	
	private Tree tree;
	private DistanceInterface distanceInterface = null;
	
	public static void main(String[] args) {
		
//		String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree";
		String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree";
//		String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.500.midpoint.solved.newick";
//		String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/testTree.phylo";
//		String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/tree.phylo";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.EUResist.shortened.txt";
		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.500.txt";
//		String distanceMatrixLocation = "";
		
		try {
//			ClusterDistance clusterDistance = new ClusterDistance(treeLocation, null);
			ClusterDistance clusterDistance = new ClusterDistance(treeLocation, distanceMatrixLocation);
			clusterDistance.getBestClustering(clusterDistance.getTree(), clusterDistance.getTree().getNodeById(1), 2, 50, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Tree getTree() {
		return this.tree;
	}

	public ClusterDistance(Tree tree) {
		this.tree = tree;
	}
	
	public ClusterDistance(String treeLocation, String distanceMatrixLocation) {
		try {
			this.tree = this.init(treeLocation, distanceMatrixLocation);
		} catch (IOException e) {
			System.err.println(ClusterDistance.class + " IOException.");
			e.printStackTrace();
		}
	}
	
	private Tree init(String treeLocation, String distanceMatrixLocation) throws IOException {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
			distanceInterface = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
		} else {
			distanceInterface = new DistanceCalculateFromTree();
		}
		return tree;
	}
	
	public Cluster getBestClustering(Tree tree, Node startNode, int minNrClusters, int maxNrClusters, int minClusterSize) {
		List<Double> distances = new ArrayList<Double>();
		int min = minNrClusters;
		int max = maxNrClusters;
//		TreeMap<Integer, List<Cluster>> clusterMapper = new TreeMap<Integer, List<Cluster>>();
		Cluster lastClustering = null;
		
		/**
		 * Calculate SDR's
		 */
		for(int i = min; i <= max; i++) {
			Cluster cluster = MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), 2, i);
//			System.out.println(clusteredTree.getAcceptableClusters(2).size());
			if(cluster == null) {
				cluster = lastClustering;
				break;
			} else {
				if(cluster.getBoundaries().size() >= 2) {
					double value = TreeStatistics.calculateStatistics(cluster, distanceInterface, minClusterSize);
					distances.add(value);
//					clusterMapper.put(i, clusters);
					lastClustering = cluster;
				} else if(cluster.getBoundaries().size() < 2 && distances.size() >= 3){
					break;
				} else {
					return cluster;
				}
			}
		}
		
		Entry<Integer, Double> maxSecondDerivative = this.getMaxSecondDerivative(distances);
		// This R-Script should be changed location
		String args1[] = {"/usr/local/Cellar/R/3.2.1/bin/Rscript", "/Users/ewout/Test.R", distances.toString(), Integer.toString(startNode.getId())};
		
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(args1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		long stopTime = java.lang.System.nanoTime();
//		System.out.println((stopTime - startTime)/1000000000);
		return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), 2, maxSecondDerivative.getKey());
	}
	
	/**
	 * @pre It is expected that the list of distances given to this method exceeds size 3
	 * @param distances
	 * @return Entry where key is the number of clusters for which the second derivative is the highest
	 * 					   value is the calculated second derivative for the best clustering
	 */
	public Entry<Integer, Double> getMaxSecondDerivative(List<Double> distances) {
		/**
		 * Calculate Second derivatives
		 */
		Map<Integer, Double> secondDerivative = new HashMap<Integer, Double>();
		for(int i = 1; i <= distances.size() - 2; i++) {
			secondDerivative.put(i + 2,calculateSecondDerivative(distances.get(i - 1), distances.get(i), distances.get(i + 1)));
		}
		
//		/**
//		 * Print to screen
//		 */
//		Iterator<Integer> iterator = secondDerivative.keySet().iterator();
//		while(iterator.hasNext()) {
//			int i = iterator.next();
//		}
		
		/**
		 * Max second derivative
		 */
		Map.Entry<Integer, Double> maxEntry = null;
		for (Map.Entry<Integer, Double> entry : secondDerivative.entrySet()) {
		    if (maxEntry == null || (entry.getValue() > maxEntry.getValue())) {
		        maxEntry = entry;
		    }
		}
		
//		System.out.println("MaxEntry: " + maxEntry.getKey() + " Value: " + maxEntry.getValue());
		return maxEntry;
	}
	
	private double calculateSecondDerivative(double previous, double current, double next) {
		return (next + previous - (2 * current));
	}

}
