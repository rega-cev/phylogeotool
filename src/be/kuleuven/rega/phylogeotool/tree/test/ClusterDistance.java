package be.kuleuven.rega.phylogeotool.tree.test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.distance.DistanceMatrixDistance;
import be.kuleuven.rega.phylogeotool.tools.ReadNewickTree;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class ClusterDistance {

	private HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	private ClusterAlgos clusterAlgos = new ClusterAlgos();
	
	public static void main(String[] args) {
		ClusterDistance clusterDistance = new ClusterDistance();
//		String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree";
		String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree";
//		String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.500.midpoint.solved.newick";
//		String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
//		String distanceMatrixLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.EUResist.shortened.txt";
		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.midpoint.txt";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.500.txt";
//		String distanceMatrixLocation = "";
		
		try {
			Tree tree = clusterDistance.init(treeLocation, distanceMatrixLocation);
			clusterDistance.getBestClustering(tree, tree.getNodeById(14909));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Tree init(String treeLocation, String distanceMatrixLocation) throws IOException {
		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader(treeLocation));
		Tree tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
			tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation));
		}
		return tree;
	}
	
	public Tree getBestClustering(Tree tree, Node startNode) {
		List<Double> distances = new ArrayList<Double>();
		int min = 2;
		int max = 50;
		Map<Integer, Tree> treeMapper = new HashMap<Integer, Tree>();
		Map<Integer, Double> secondDerivative = new HashMap<Integer, Double>();
		Tree lastTree = null;
		/**
		 * Calculate SDR's
		 */
		for(int i = min; i <= max; i++) {
			Tree clusteredTree = clusterAlgos.getCluster(tree, startNode, i, 2);
//			System.out.println(clusteredTree.getAcceptableClusters(2).size());
			if(!treeMapper.isEmpty() && clusteredTree.equals(treeMapper.get(i - 1))) {
				lastTree = clusteredTree;
				break;
			} else {
				if(clusteredTree.getAcceptableClusters(2).size() >= 2) {
					double value = calculateStatistics(tree, clusteredTree);
					distances.add(value);
					treeMapper.put(i, clusteredTree);
				} else if(clusteredTree.getAcceptableClusters(2).size() < 2 && distances.size() >= 3){
					break;
				} else {
					return clusteredTree;
				}
			}
		}
		
		/**
		 * Calculate Second derivatives
		 */
		if(distances.size() <= 2) {
			return lastTree;
		} else {
			for(int i = 1; i <= distances.size() - 2; i++) {
				secondDerivative.put(i + 2,calculateSecondDerivative(distances.get(i - 1), distances.get(i), distances.get(i + 1)));
			}
		}
		
//		/**
//		 * Print to screen
//		 */
//		Iterator<Integer> iterator = secondDerivative.keySet().iterator();
//		while(iterator.hasNext()) {
//			int i = iterator.next();
//			System.out.println(i + ": " + secondDerivative.get(i));
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
		
		String args1[] = {"/usr/local/Cellar/R/3.2.1/bin/Rscript", "/Users/ewout/Test.R", distances.toString(), Integer.toString(startNode.getId())};
		
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(args1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("MaxEntry: " + maxEntry.getKey() + " Value: " + maxEntry.getValue());
//		long stopTime = java.lang.System.nanoTime();
//		System.out.println((stopTime - startTime)/1000000000);
		return treeMapper.get(maxEntry.getKey());
	}
	
	private double calculateStatistics(Tree tree, Tree clusteredTree) {
		List<Node> clusters = clusteredTree.getAcceptableClusters(2);
		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		System.out.println("Inter: " + inter);
		List<Double> intraClusterDistances = new ArrayList<Double>();
		for (int i = 0; i < clusters.size(); i++) {
			double intra = tree.getIntraPairwiseDistance(clusters.get(i));
//			System.out.println("intra: " + intra);
			intraClusterDistances.add(intra);
		}
		return this.calculateAverage(intraClusterDistances)/inter;
	}
	
	private double calculateAverage(List<Double> distances) {
		Double sum = 0.0D;
		if (!distances.isEmpty()) {
			for (Double distance : distances) {
				sum += distance;
			}
			return sum.doubleValue() / distances.size();
		}
		return sum;
	}
	
	private double calculateSecondDerivative(double previous, double current, double next) {
		return Math.abs(next + previous - 2 * current);
	}
}
