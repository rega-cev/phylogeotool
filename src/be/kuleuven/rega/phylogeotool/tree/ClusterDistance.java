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
//			clusterDistance.getBestClustering(clusterDistance.getTree(), clusterDistance.getTree().getNodeById(1), 2, 50, 2);
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
	
	public boolean isFunctionBumpy(List<Double> firstDerivatives) {
		List<Double> positiveFirstDerivatives = ClusterDistance.getPositiveFirstDerivatives(firstDerivatives);
		List<Double> negativeFirstDerivatives = ClusterDistance.getNegativeFirstDerivatives(firstDerivatives);
		
		double positiveSum = ClusterDistance.getSumOfList(positiveFirstDerivatives);
		double negativeSum = ClusterDistance.getSumOfList(negativeFirstDerivatives);
		
		double percentage = (Math.abs(positiveSum) / (Math.abs(positiveSum) + Math.abs(negativeSum))) * 100;
		
		// TODO: Make this variable interchangeable
		if(percentage > 20) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Calculate the first derivative of a list of double values (SDR scores, y-values) with the list index being the x-axis value
	 * (yi+1−yi−1)/(xi+1−xi−1)
	 * @param distances: List containing all the SDR scores
	 * @return list with the first derivatives
	 */
	public static List<Double> getFirstDerivatives(List<Double> distances) {
		List<Double> firstDerivatives = new ArrayList<Double>();
		// Calculate the first derivative for the first point
		// (y2-y1)/(x2-x1)
		firstDerivatives.add(calculateFirstDerivative(1, 2, distances.get(0), distances.get(1)));
//		System.out.print(calculateFirstDerivative(1, 2, distances.get(0), distances.get(1)));
		
		// Calculate the first derivative for all the coming points (but the last one)
		for(int i = 2; i < distances.size(); i++) {
			firstDerivatives.add(calculateFirstDerivative(i - 1, i + 1, distances.get(i - 2), distances.get(i)));
//			System.out.print(calculateFirstDerivative(i - 1, i + 1, distances.get(i - 2), distances.get(i)) + ", ");
		}
		
		// Calculate the first derivative for the last point
		// (yn-yn-1)/(xn-xn-1)
		firstDerivatives.add(calculateFirstDerivative(distances.size() - 1, distances.size(), distances.get(distances.size() - 2), distances.get(distances.size() - 1)));
//		System.out.print(calculateFirstDerivative(distances.size() - 1, distances.size(), distances.get(distances.size() - 2), distances.get(distances.size() - 1)));

		return firstDerivatives;
	}
	
	public static List<Double> getPositiveFirstDerivatives(List<Double> firstDerivatives) {
		List<Double> positiveFirstDerivatives = new ArrayList<Double>();
		for(double firstDerivative:firstDerivatives) {
			if(firstDerivative > 0) {
				positiveFirstDerivatives.add(firstDerivative);
			}
		}
		return positiveFirstDerivatives;
	}
	
	public static List<Double> getNegativeFirstDerivatives(List<Double> firstDerivatives) {
		List<Double> negativeFirstDerivatives = new ArrayList<Double>();
		for(double firstDerivative:firstDerivatives) {
			if(firstDerivative < 0) {
				negativeFirstDerivatives.add(firstDerivative);
			}
		}
		return negativeFirstDerivatives;
	}
	
	public static double getSumOfList(List<Double> values) {
		double sum = 0.0;
		for(double value:values) {
			sum += value;
		}
		return sum;
	}
	/**
	 * This method returns the optimal amount of clusters based on the first derivative
	 * @param firstDerivatives
	 * @return optimal amount of clusters
	 */
	public static int getMinValueFromList(List<Double> firstDerivatives) {
		double previousFirstDerivative = 0;
		
		for(int i = 0; i < firstDerivatives.size(); i++) {
			// if the first derivatives still have the same sign
			if(previousFirstDerivative != 0 && (previousFirstDerivative<0) == (firstDerivatives.get(i)<0)) {
				previousFirstDerivative = firstDerivatives.get(i);
			} else {
				// We get to a local min, we return the value
				// i = 0 are 2 clusters => i + 1 because previous point was minimum
				if(previousFirstDerivative < 0) {
					return i + 1;
				// We get to a local max and keep searching
				} else {
					previousFirstDerivative = firstDerivatives.get(i);
				}
			}
		}
		// If the function keeps increasing and we never have a local minimum, we take 2 clusters as the best clustering
		return 2;
	}
	
	private static double calculateFirstDerivative(double previousX, double nextX, double previousY, double nextY) {
		return ((nextY - previousY) / (nextX - previousX));
	}
	
	private double calculateSecondDerivative(double previous, double current, double next) {
		return (next + previous - (2 * current));
	}

}
