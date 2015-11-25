package be.kuleuven.rega.phylogeotool.tree.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.tools.NexusExporter;
import be.kuleuven.rega.phylogeotool.tools.ReadNewickTree;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.webapp.GraphProperties;

public class ClusterTest {

//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/middle_tree.phylo";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/testTree.phylo";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.1000.midpoint.solved.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.997.midpoint.solved.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.500.midpoint.solved.newick";
//	private String treeLocation = "/Users/ewout/Documents/TDRDetector/temp/test/phylo.tree";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/test/RAxML_bestTree.solved.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/test/midpoint.newick";
//	private String raxmlDistanceFile = "/Users/ewout/git/phylogeotool/lib/Test/test/RAxML_distances.midpoint.newick";
	private String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree";
//	private String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree";
	
	private Tree tree = null;
	private ClusterAlgos clusterAlgos = new ClusterAlgos();
	private jebl.evolution.trees.Tree jeblTree;
	
	private HashMap<String, HashMap<String, Double>> distancesRAxML = new HashMap<String, HashMap<String, Double>>();
	private HashMap<String, HashMap<String, Double>> distancesOur = new HashMap<String, HashMap<String, Double>>();
	
	@Before
	public void init() throws FileNotFoundException {
		jeblTree = ReadNewickTree.readNewickTree(new FileReader(treeLocation));
//		System.out.println(((SimpleRootedTree) jeblTree).getChildren(((SimpleRootedTree) jeblTree).getRootNode()).size());
		// Cluster
		tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
	}

	// private void clusteringTwoClusters() {
	// clusters.addAll(tree.getRootNode().getImmediateChildren());
	// }
	//
	// private void clusteringThreeClusters() {
	// clusters.add(tree.getRootNode().getImmediateChildren().get(0));
	// clusters.addAll(tree.getRootNode().getImmediateChildren().get(1).getImmediateChildren());
	// }
	//
	// private void clusteringFourClusters() {
	// clusters.addAll(tree.getRootNode().getImmediateChildren().get(0).getImmediateChildren());
	// clusters.addAll(tree.getRootNode().getImmediateChildren().get(1).getImmediateChildren());
	// }
	//
	// @Test
	// public void testGetIntraCluster() {
	// System.out.println("*** Two Clusters ***");
	// clusters.clear();
	// this.clusteringTwoClusters();
	// double distance = tree.getInterClusterPairwiseDistance(clusters);
	// System.out.println("Inter Pairwise Distance 1: " + distance);
	// double intra1 = tree.getIntraPairwiseDistance(clusters.get(0));
	// System.out.println("Intra Pairwise Distance 1: " + intra1);
	// double intra2 = tree.getIntraPairwiseDistance(clusters.get(1));
	// System.out.println("Intra Pairwise Distance 2: " + intra2);
	// System.out.println("SDR: " + ((intra1 + intra2)/2)/distance);
	//
	// System.out.println("\n\n*** Three Clusters ***");
	// clusters.clear();
	// this.clusteringThreeClusters();
	// distance = tree.getInterClusterPairwiseDistance(clusters);
	// System.out.println("Inter Pairwise Distance 1: " + distance);
	// System.out.println("Size: " +
	// clusters.get(0).getLeavesAsString().size());
	// intra1 = tree.getIntraPairwiseDistance(clusters.get(0));
	// System.out.println("Intra Pairwise Distance 1: " + intra1);
	// intra2 = tree.getIntraPairwiseDistance(clusters.get(1));
	// System.out.println("Intra Pairwise Distance 2: " + intra2);
	// double intra3 = tree.getIntraPairwiseDistance(clusters.get(2));
	// System.out.println("Intra Pairwise Distance 3: " + intra3);
	//
	// System.out.println("SDR: " + ((intra1 + intra2 + intra3)/3)/distance);
	//
	// System.out.println("\n\n*** Four Clusters ***");
	// clusters.clear();
	// this.clusteringFourClusters();
	// distance = tree.getInterClusterPairwiseDistance(clusters);
	// System.out.println("Inter Pairwise Distance 1: " + distance);
	// intra1 = tree.getIntraPairwiseDistance(clusters.get(0));
	// System.out.println("Intra Pairwise Distance 1: " + intra1);
	// intra2 = tree.getIntraPairwiseDistance(clusters.get(1));
	// System.out.println("Intra Pairwise Distance 2: " + intra2);
	// intra3 = tree.getIntraPairwiseDistance(clusters.get(2));
	// System.out.println("Intra Pairwise Distance 3: " + intra3);
	// double intra4 = tree.getIntraPairwiseDistance(clusters.get(3));
	// System.out.println("Intra Pairwise Distance 4: " + intra4);
	// System.out.println("SDR: " + ((intra1 + intra2 + intra3 +
	// intra4)/4)/distance);
	// }
//
//	@Test
//	public void testClusteringTwoClusters() throws FileNotFoundException {
//		System.out.println("*** Two Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 2));
//	}
//	
//	@Test
//	public void testClusteringThreeClusters() throws FileNotFoundException {
//		System.out.println("*** Three Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 3));
//	}
//	
//	@Test
//	public void testClusteringFourClusters() throws FileNotFoundException {
//		System.out.println("*** Four Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 4));
//	}
//	
//	@Test
//	public void testClusteringFiveClusters() throws FileNotFoundException {
//		System.out.println("*** Five Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 5));
//	}
//	
//	@Test
//	public void testClusteringSixClusters() throws FileNotFoundException {
//		System.out.println("*** Six Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 6));
//	}
//	
//	@Test
//	public void testClusteringSevenClusters() throws FileNotFoundException {
//		System.out.println("*** Seven Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 7));
//	}
//	
//	@Test
//	public void testClusteringEightClusters() throws FileNotFoundException {
//		System.out.println("*** Eight Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 8));
//	}
//	
//	@Test
//	public void testClusteringNineClusters() throws FileNotFoundException {
//		System.out.println("*** Nine Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 9));
//	}
//	
//	@Test
//	public void testClusteringTenClusters() throws FileNotFoundException {
//		System.out.println("*** Ten Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 10));
//	}
//	
//	@Test
//	public void testClusteringElevenClusters() throws FileNotFoundException {
//		System.out.println("*** Eleven Clusters ***");
//		calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), 11));
//	}
//	@Test
//	public void testNNodes() {
//		System.out.println(tree.getLeaves().size());
//	}
	
	@Test
	public void drawClusters() throws IOException {
		GraphProperties graphProperties = new GraphProperties();
		for(int i = 3; i < 4; i++) {
//			System.out.println("*** " + i + " Clusters ***");
			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i, 2);
//			System.out.println(clusteredTree.getRootNode().getImmediateChildren().get(1).getLeavesAsString().get(0));
//			if(clusteredTree.getNodes().size() > 1) {
			
//			List<Node> clusters = new ArrayList<Node>();
			List<Node> clusters = clusteredTree.getAcceptableClusters(2);
//			for (Node leaf : clusteredTree.getLeaves()) {
//				// If less than 2 leaves we don't consider this as a cluster
////				if(leaf.getLeaves().size() > 1)
//					clusters.add(leaf);
//			}
			tree = graphProperties.setNodeColor(tree, clusters);
			
//			for (Node leaf : clusters) {
//				// If less than 2 leaves we don't consider this as a cluster
//				if(leaf.getLeaves().size() < 1)
//					clusters.remove(leaf);
//			}
			
//			}
			FileWriter fileWriter = new FileWriter("/Users/ewout/Documents/phylogeo/TestCases/Portugal/" + i + ".nxs");
			NexusExporter.export(tree, clusters, jeblTree, fileWriter);
//			calculateStatisticsPieter(clusteredTree);
//			calculateStatistics(clusterAlgos.getCluster(tree, tree.getRootNode(), i));
		}
	}
	
//	@Test
//	public void generalTest() {
//		List<Double> distances = new ArrayList<Double>();
//		int min = 2;
//		int max = 50;
//		
//		for(int i = min; i <= max; i++) {
////			System.out.println("*** " + i + " Clusters ***");
//			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i);
//			double value = calculateStatistics(clusteredTree);
//			distances.add(value);
////			System.out.print(value + ", ");
//		}
//		
//		for(int i = 0; i <= max-min; i++) {
//			
////			System.out.print("i+min: " + (i+min) + " ");
////			System.out.println("distances.get(i): " + distances.get(i));
////			System.out.println("distances.get(min): " + distances.get(min));
////			System.out.println("distances.get(max-min): " + distances.get(max-min));
////			
////			System.out.println("nrClusters: " + (i+min) + ", " + DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)));
//			System.out.print(DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)) + ", ");
//		}
//	}
	
//	@Test
//	public void printDistances() {
//		List<Node> nodes = new ArrayList<Node>();
//		for (Node leaf : tree.getLeaves()) {
//			nodes.add(leaf);
//		}
//		
//		for(int i = 0; i < nodes.size(); i++) {
//			for(int j = i+1; j < nodes.size(); j++) {
//				System.out.println(nodes.get(i).getLabel() + " " + nodes.get(j).getLabel() + "\t" + tree.getDistance(nodes.get(i), nodes.get(j)));
//			}
//		}
//	}
	
//	@Test
//	public void compareDistances() throws IOException {
//		
//		/*
//		 * Read RAxML File
//		 */
//		
//		File file = new File(raxmlDistanceFile);
//		FileReader fileReader = new FileReader(file);
//		BufferedReader bufferedReader = new BufferedReader(fileReader);
//		
//		String line;
//		/**
//		 * firstSplit[1] contains the distance
//		 * secondSplit[0] contains first id
//		 * secondSplit[1] contains second id
//		 */
//		while ((line = bufferedReader.readLine()) != null) {
//			String[] firstSplit = line.split("\t");
//			String[] secondSplit = firstSplit[0].split(" ");
//			
//			if(!distancesRAxML.containsKey(secondSplit[0])) {
//				distancesRAxML.put(secondSplit[0], new HashMap<String, Double>());
//			}
//			if(!distancesRAxML.containsKey(secondSplit[1])) {
//				distancesRAxML.put(secondSplit[1], new HashMap<String, Double>());
//			}
//			distancesRAxML.get(secondSplit[0]).put(secondSplit[1], Double.parseDouble(firstSplit[1]));
//			distancesRAxML.get(secondSplit[1]).put(secondSplit[0], Double.parseDouble(firstSplit[1]));
//		}
//		bufferedReader.close();
//		
//		/*
//		 * Get our distances
//		 */
//		
//		List<Node> nodes = new ArrayList<Node>();
//		for (Node leaf : tree.getLeaves()) {
//			nodes.add(leaf);
//		}
//		
//		for(int i = 0; i < nodes.size(); i++) {
//			for(int j = i+1; j < nodes.size(); j++) {
//				if(!distancesOur.containsKey(nodes.get(i).getLabel())) {
//					distancesOur.put(nodes.get(i).getLabel(), new HashMap<String, Double>());
//				}
//				if(!distancesOur.containsKey(nodes.get(j).getLabel())) {
//					distancesOur.put(nodes.get(j).getLabel(), new HashMap<String, Double>());
//				}
//				distancesOur.get(nodes.get(i).getLabel()).put(nodes.get(j).getLabel(), tree.getDistance(nodes.get(i), nodes.get(j)));
//				distancesOur.get(nodes.get(j).getLabel()).put(nodes.get(i).getLabel(), tree.getDistance(nodes.get(i), nodes.get(j)));
//			}
//		}
		
//		System.out.println(distancesRAxML.keySet().size());
//		System.out.println(distancesOur.keySet().size());
		
//		for(int i = 0; i < nodes.size(); i++) {
//			System.out.print("Label: " + nodes.get(i).getLabel() + ", ");
//			System.out.println(distancesRAxML.get(nodes.get(i).getLabel()).size() + ", " + distancesOur.get(nodes.get(i).getLabel()).size());
//			System.out.println();
//		}
//		
//		double min = Double.MAX_VALUE;
//		double max = 0.0D;
//		DecimalFormat df = new DecimalFormat("#.00");
//		
//		for(String key: distancesOur.keySet()) {
//			for(String value: distancesOur.get(key).keySet()) {
//				double distance = (distancesOur.get(key).get(value)/distancesRAxML.get(key).get(value));
////				System.out.println("(" + key + "," + value + ") " + distancesOur.get(key).get(value) + ", " + distancesRAxML.get(key).get(value) + ", " + df.format(distance));
//				System.out.print(df.format(distance) + ", ");
////				if(distance < min) {
////					min = distance;
////				}
////				if(distance > max) {
////					max = distance;
////				}
//			}
////			System.out.println("Min: " + min + " Max: " + max);
//		}
//	}
	
	private double calculateStatistics(Tree clusteredTree) {
		List<Node> clusters = new ArrayList<Node>();
		for (Node leaf : clusteredTree.getLeaves()) {
			// If less than 2 leaves we don't consider this as a cluster
			if(leaf.getLeaves().size() > 1)
				clusters.add(leaf);
		}
		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		System.out.println("Inter Pairwise Distance 1: " + distance);
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
	
	private void calculateStatisticsPieter(Tree clusteredTree) {
		List<Node> clusters = new ArrayList<Node>();
		for (Node leaf : clusteredTree.getLeaves()) {
			clusters.add(leaf);
		}
		double distance = tree.getInterClusterPairwiseDistance(clusters);
//		System.out.println("Inter Pairwise Distance 1: " + distance);
		double intra = 0.0D;
		int amountOfRuns = 0;
		Map<Double, Integer> result = null;
		for (int i = 0; i < clusters.size(); i++) {
			result = tree.getIntraPairwiseDistancePieter(clusters.get(i));
			double intraTemp = result.keySet().iterator().next();
			intra += intraTemp;
			amountOfRuns += result.get(intraTemp);
//			System.out.println("Intra Pairwise Distance " + i + ": " + intra);
		}
//		System.out.println("SDR: " + ((intra/amountOfRuns)/distance));
		System.out.print(((intra/amountOfRuns)/distance) + ",");
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
	
	private double getVariance(List<Double> values) {
		double d1 = calculateAverage(values);
		double d2 = 0.0D;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) != 0.0D) {
				d2 += Math.pow(values.get(i) - d1, 2.0D);
			}
		}
		return d2 / (values.size() - 1);
	}

}
