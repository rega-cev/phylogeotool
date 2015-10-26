package be.kuleuven.rega.phylogeotool.tree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.distance.DistanceMatrixDistance;
import be.kuleuven.rega.phylogeotool.tools.ReadNewickTree;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class ClusterDistanceTest {

//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/testTree.phylo";
	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.500.midpoint.solved.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.1000.midpoint.solved.newick";
	private double [][] distances = null ;
	private HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	private Tree tree = null;
	private jebl.evolution.trees.Tree jeblTree;
	private ClusterAlgos clusterAlgos = new ClusterAlgos();
	private List<Node> leafs = new ArrayList<Node>();
	
	@Before
	public void init() throws Exception {
		jeblTree = ReadNewickTree.readNewickTree(new FileReader(treeLocation));
		System.out.println(((SimpleRootedTree)jeblTree).getChildren(((SimpleRootedTree)jeblTree).getRootNode()).size());
		tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
//			System.out.println("leaf: " + leaf.getLabel());
			leafs.add(leaf);
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
//		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/distanceMatrix.txt"));
//		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.500.txt"));
//		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.1000.txt"));
	}
	
//	@Test
//	public void testDistances() throws Exception {
//		System.out.println("Distance: " + tree.getDistance(tree.getLeafByLabel("1"), tree.getLeafByLabel("2")));
//		System.out.println("Distance: " + tree.getDistance(tree.getLeafByLabel("1"), tree.getLeafByLabel("10")));
//	}
//	
//	@Test
//	public void createDistanceMatrix() throws IOException {
//		distances = new double[translatedNodeNames.keySet().size()][translatedNodeNames.keySet().size()];
//		long startTime = java.lang.System.nanoTime();
//		for(int i = 0; i < translatedNodeNames.keySet().size(); i++) {
//			for(int j = i+1; j < translatedNodeNames.keySet().size(); j++) {
//				distances[i][j] = tree.getDistance(leafs.get(i), leafs.get(j));
//			}
//		}
//		this.writeDistanceMatrix("/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.5000.txt");
////		distances = this.readDistanceMatrix(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/distanceMatrix.txt");
//		printDistanceMatrix(distances);
//		// Run some code;
//		long stopTime = java.lang.System.nanoTime();
//		System.out.println((stopTime - startTime)/1000000000);
//	}
//	
	@Test
	public void generalTest() {
		List<Double> distances = new ArrayList<Double>();
		int min = 2;
		int max = 50;
		long startTime = java.lang.System.nanoTime();
		for(int i = min; i <= max; i++) {
//			System.out.println("*** " + i + " Clusters ***");
			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i);
			double value = calculateStatistics(clusteredTree);
			distances.add(value);
			System.out.print(value + ", ");
		}
		
		long stopTime = java.lang.System.nanoTime();
//		System.out.println((stopTime - startTime)/1000000000);
	}
	
//	@Test
//	public void calculateElbow() {
//		List<Double> distances = new ArrayList<Double>();
//		int min = 2;
//		int max = 50;
//		
//		for(int i = min; i <= max; i++) {
//			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i);
//			double value = calculateStatistics(clusteredTree);
//			distances.add(value);
//		}
//		
//		for(int i = 0; i <= max-min; i++) {
////		System.out.println("nrClusters: " + (i+min) + ", " + DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)));
//			System.out.print(DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)) + ", ");
//		}
//	}
	
	private void printDistanceMatrix(double [][] distances) throws IOException {
		
		for (int i = 0; i < distances.length; i++) {
		    for (int j = 0; j < distances[0].length; j++) {
		    	System.out.print(distances[i][j] + ";");
		    }
		    System.out.print("\n");
		}
	}
	
	private void writeDistanceMatrix(String fileName) throws IOException {
		FileWriter fw = new FileWriter(new File(fileName));
		
		for (int i = 0; i < translatedNodeNames.keySet().size(); i++) {
		    for (int j = 0; j < translatedNodeNames.keySet().size(); j++) {
		    	fw.write(distances[i][j] + ";");
		    }
		    fw.write("\n");
		}
		fw.close();
	}
	
	private double[][] readDistanceMatrix(HashMap<String, Integer> translatedNodeNames, String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
		double [][] distances = new double[translatedNodeNames.keySet().size()][translatedNodeNames.keySet().size()];
		
		String line;
		int i = 0;
		int j = 0;
		while ((line = bufferedReader.readLine()) != null) {
			j = 0;
			String [] distancesLine = line.split(";");
			for(String distance:distancesLine) {
				distances[i][j++] = new Double(distance);
			}
			i++;
		}
		bufferedReader.close();
		return distances;
	}
	
	private double calculateStatistics(Tree clusteredTree) {
		List<Node> clusters = new ArrayList<Node>();
		for (Node leaf : clusteredTree.getLeaves()) {
			// If less than 2 leaves we don't consider this as a cluster
			if(leaf.getLeaves().size() > 1)
				clusters.add(leaf);
		}
		double inter = tree.getInterClusterPairwiseDistance(clusters);
		List<Double> intraClusterDistances = new ArrayList<Double>();
		List<Double> intraOnInterClusterDistances = new ArrayList<Double>();
		for (int i = 0; i < clusters.size(); i++) {
			double intra = tree.getIntraPairwiseDistance(clusters.get(i));
			intraClusterDistances.add(intra);
			intraOnInterClusterDistances.add(intra/inter);
//			System.out.println("Intra Pairwise Distance " + i + ": " + intra);
		}
//		System.out.println("SDR: " + this.calculateAverage(intraClusterDistances)/inter);
//		System.out.println("SDV: " + getVariance(intraOnInterClusterDistances));
		return this.calculateAverage(intraClusterDistances)/inter;
//		System.out.print(getVariance(intraOnInterClusterDistances) + ",");
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
}
