//package be.kuleuven.rega.phylogeotool.trash;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import be.kuleuven.rega.clustering.MidRootCluster;
//import be.kuleuven.rega.comparator.ClusterSizeComparator;
//import be.kuleuven.rega.phylogeotool.core.Cluster;
//import be.kuleuven.rega.phylogeotool.core.Node;
//import be.kuleuven.rega.phylogeotool.core.Tree;
//import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
//import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
//import be.kuleuven.rega.phylogeotool.tree.distance.DistanceMatrixDistance;
//
//public class ClusterDistanceTest {
//
////	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
////	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/testTree.phylo";
////	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.500.midpoint.solved.newick";
////	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.1000.midpoint.solved.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.997.midpoint.solved.newick";
////	private String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree";
////	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
////	private String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree";
////	private String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/RAxML_bipartitions.fullPortugal.final.tree";
//	private double [][] distances = null ;
//	private HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
//	private Tree tree = null;
//	private jebl.evolution.trees.Tree jeblTree;
//	private List<Node> leafs = new ArrayList<Node>();
//	
//	@Before
//	public void init() throws Exception {
//		jeblTree = ReadTree.readTree(new FileReader(treeLocation));
////		System.out.println(((SimpleRootedTree)jeblTree).getChildren(((SimpleRootedTree)jeblTree).getRootNode()).size());
//		tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>(), new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.EUResist.shortened.txt"));
//		
//		int index = 0;
//		for (Node leaf : tree.getLeaves()) {
////			System.out.println("leaf: " + leaf.getLabel());
//			leafs.add(leaf);
//			translatedNodeNames.put(leaf.getLabel(), index++);
//		}
//		
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.test.txt"));
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.500.txt"));
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.1000.txt"));
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.997.txt"));
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.portugal.midpoint.txt"));
////		tree.setDistanceInterface(new DistanceMatrixDistance(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.test.other.txt"));
////		tree.setDistanceInterface();
//	}
////	
////	@Test
////	public void testDistances() throws Exception {
////		System.out.println("Distance: " + tree.getDistance(tree.getLeafByLabel("1"), tree.getLeafByLabel("19")));
////		System.out.println("Distance: " + tree.getDistance(tree.getLeafByLabel("9"), tree.getLeafByLabel("11")));
////	}
////	
////	@Test
////	public void quickTest() {
////		System.out.println(Integer.MAX_VALUE);
////		System.out.println(translatedNodeNames.keySet().size() * translatedNodeNames.keySet().size());
////		distances = new double[translatedNodeNames.keySet().size()/3][translatedNodeNames.keySet().size()/3];
////	}
//	//TODO: Divide the size of the matrix by 3
////	@Test
////	public void createDistanceMatrix() throws IOException {
////		String distanceMatrixLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.test.txt";
////		
////		FileWriter fw = new FileWriter(new File(distanceMatrixLocation));
////		
//////		distances = new double[translatedNodeNames.keySet().size()][translatedNodeNames.keySet().size()];
////		long startTime = java.lang.System.nanoTime();
////		for(int i = 0; i < translatedNodeNames.keySet().size(); i++) {
//////		for(int i = 0; i < 1; i++) {
////			for(int j = 0; j < translatedNodeNames.keySet().size(); j++) {
////				if(j >= i + 1) {
////					fw.write(tree.getDistance(leafs.get(i), leafs.get(j)) + ";");
////				} else {
////					fw.write("0.0;");
////				}
//////				System.out.println("i: " + i + " j: " + j + " distance: " + distances[i][j]);
////			}
//////			System.out.println("i: " + i);
////			fw.write("\n");
////		}
////		fw.close();
//////		this.writeDistanceMatrix(distanceMatrixLocation);
//////		distances = this.readDistanceMatrix(translatedNodeNames, "/Users/ewout/git/phylogeotool/lib/Test/distanceMatrix.txt");
//////		printDistanceMatrix(distances);
////		// Run some code;
////		long stopTime = java.lang.System.nanoTime();
////		System.out.println((stopTime - startTime)/1000000000);
////	}
////	
////	@Test
////	public void tempTest() {
////		List<Double> distances = new ArrayList<Double>();
////		int min = 2;
////		int max = 50;
////		long startTime = java.lang.System.nanoTime();
////		Map<Integer, Tree> treeMapper = new HashMap<Integer, Tree>();
////		for(int i = min; i <= max; i++) {
////			System.out.println("*** " + i + " Clusters ***");
////			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i);
////			if(!treeMapper.isEmpty() && clusteredTree.equals(treeMapper.get(i - 1))) {
////				break;
////			} else {
////				for(Node node:clusteredTree.getLeaves()) {
////					System.out.println("**Cluster**");
////					for(String string:node.getLeavesAsString()) {
////						System.out.print(string + ", ");
////					}
////					System.out.print("\n");
////				}
////				treeMapper.put(i, clusteredTree);
////			}
////		}
////		
////		long stopTime = java.lang.System.nanoTime();
//////		System.out.println((stopTime - startTime)/1000000000);
////	}
////	
////	@Test
////	public void generalTest() {
////		List<Double> distances = new ArrayList<Double>();
////		int min = 2;
////		int max = 50;
//////		long startTime = java.lang.System.nanoTime();
////		Map<Integer, Tree> treeMapper = new HashMap<Integer, Tree>();
////		for(int i = min; i <= max; i++) {
//////			System.out.println("*** " + i + " Clusters ***");
////			Cluster cluster = MidRootCluster.calculate(tree, tree.getRootNode(), new ClusterSizeComparator(), 2, i);
//////			for(Node leaf:clusteredTree.getLeaves()) {
//////				System.out.println("*Cluster*");
//////				for(String leafString:leaf.getLeavesAsString()) {
//////					System.out.print(leafString + ", ");
//////				}
//////				System.out.print("\n");
//////			}
////			if(!treeMapper.isEmpty() && cluster.equals(treeMapper.get(i - 1))) {
////				break;
////			} else {
////				double value = calculateStatistics(cluster);
////				distances.add(value);
////	//			System.out.print(getVariance(distances) + ", ");
////				System.out.print(value + ", ");
////				treeMapper.put(i, clusteredTree);
////			}
////		}
////		
//////		long stopTime = java.lang.System.nanoTime();
//////		System.out.println((stopTime - startTime)/1000000000);
////	}
////	
////	@Test
////	public void calculateElbow() {
////		List<Double> distances = new ArrayList<Double>();
////		int min = 2;
////		int max = 19;
////		
////		int nrUselessClusters = 0;
////		
////		for(int i = min; i <= max; i++) {
//////			System.out.println("i: " + i);
////			Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), i);
////			//Small analysis
//////			int uselessCluster = 0;
//////			for(Node node:clusteredTree.getLeaves()) {
//////				if(node.getLeaves().size() <= 1) {
////////					System.out.println("Here");
//////					uselessCluster++;
//////				} else {
////////					System.out.println("node.getLeaves().size(): " + node.getLeaves().size());
//////				}
//////			}
//////			
//////			if(uselessCluster > nrUselessClusters) {
//////				nrUselessClusters = uselessCluster;
//////			} else {
////				double value = calculateStatistics(clusteredTree);
////				System.out.print(value + ", ");
////				distances.add(value);
//////			}
////		}
//////		System.out.println("Nr Useless Cluster: " + nrUselessClusters);
////		
////		System.out.print("\n");
////		for(int i = 0; i <= max-min; i++) {
//////		System.out.println("nrClusters: " + (i+min) + ", " + DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)));
////			System.out.print(DistancePointSegmentExample.distanceToSegment((i+min), distances.get(i), min, distances.get(0), max, distances.get(max-min)) + ", ");
////		}
////	}
//	
//	private void printDistanceMatrix(double [][] distances) throws IOException {
//		
//		for (int i = 0; i < distances.length; i++) {
//		    for (int j = 0; j < distances[0].length; j++) {
//		    	System.out.print(distances[i][j] + ";");
//		    }
//		    System.out.print("\n");
//		}
//	}
//	
//	private void writeDistanceMatrix(String fileName, boolean append) throws IOException {
//		FileWriter fw = new FileWriter(new File(fileName), append);
//		
//		for (int i = 0; i < translatedNodeNames.keySet().size(); i++) {
//		    for (int j = 0; j < translatedNodeNames.keySet().size(); j++) {
//		    	fw.write(distances[i][j] + ";");
//		    }
//		    fw.write("\n");
//		}
//		fw.close();
//	}
//	
//	private double[][] readDistanceMatrix(HashMap<String, Integer> translatedNodeNames, String fileName) throws IOException {
//		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
//		double [][] distances = new double[translatedNodeNames.keySet().size()][translatedNodeNames.keySet().size()];
//		
//		String line;
//		int i = 0;
//		int j = 0;
//		while ((line = bufferedReader.readLine()) != null) {
//			j = 0;
//			String [] distancesLine = line.split(";");
//			for(String distance:distancesLine) {
//				distances[i][j++] = new Double(distance);
//			}
//			i++;
//		}
//		bufferedReader.close();
//		return distances;
//	}
//	
//	private double calculateStatistics(Cluster cluster) {
//		List<Node> clusters = cluster.getAcceptableClusters(2);
////		List<Node> clusters = new ArrayList<Node>();
////		int j = 1;
////		for (Node leaf : clusteredTree.getLeaves()) {
//////			System.out.println("Cluster #: " + j++);
//////			System.out.print("Leafnames: ");
//////			 If less than 2 leaves we don't consider this as a cluster
////			if(leaf.getLeaves().size() > 1) {
////				clusters.add(leaf);
//////				System.out.println("Cluster size: " + leaf.getLeaves().size());
////			} else {
//////				System.out.println("Cluster of size 1: " + leaf.getLabel());
////			}
//////			System.out.print("\n");
////		}
//		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		List<Double> intraClusterDistances = new ArrayList<Double>();
////		List<Double> intraOnInterClusterDistances = new ArrayList<Double>();
//		for (int i = 0; i < clusters.size(); i++) {
//			double intra = tree.getIntraPairwiseDistance(clusters.get(i));
//			intraClusterDistances.add(intra);
////			intraOnInterClusterDistances.add(intra/inter);
////			System.out.println("Intra Pairwise Distance " + i + ": " + intra);
//		}
////		System.out.println("Inter: " + inter);
////		System.out.println("SDR: " + this.calculateAverage(intraClusterDistances)/inter);
////		System.out.println("SDV: " + getVariance(intraOnInterClusterDistances));
//		
//		return this.calculateAverage(intraClusterDistances)/inter;
////		return getVariance(intraOnInterClusterDistances);
//	}
//	
//	private double calculateStatisticsPieter(Tree clusteredTree) {
//		List<Node> clusters = new ArrayList<Node>();
//		int j = 1;
//		for (Node leaf : clusteredTree.getLeaves()) {
////			System.out.println("Cluster #: " + j++);
////			System.out.print("Leafnames: ");
//			// If less than 2 leaves we don't consider this as a cluster
//			if(leaf.getLeaves().size() > 1) {
//				clusters.add(leaf);
//				for(String leafName: leaf.getLeavesAsString()) {
////					 System.out.print(leafName +", ");
//				}
//			} else {
////				System.out.println("Cluster of size 1: " + leaf.getLabel());
//			}
////			System.out.print("\n");
//		}
//		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		double intra = 0.0D;
//		int amountOfRuns = 0;
//		Map<Double, Integer> result = null;
//		for (int i = 0; i < clusters.size(); i++) {
//			result = tree.getIntraPairwiseDistancePieter(clusters.get(i));
//			double intraTemp = result.keySet().iterator().next();
//			intra += intraTemp;
//			amountOfRuns += result.get(intraTemp);
////			System.out.println("Intra Pairwise Distance " + i + ": " + intra);
//		}
//		
////		System.out.println("Inter: " + inter);
////		System.out.println("SDR: " + this.calculateAverage(intraClusterDistances)/inter);
////		System.out.println("SDV: " + getVariance(intraOnInterClusterDistances));
//		
//		return ((intra/amountOfRuns)/inter);
////		System.out.print(getVariance(intraOnInterClusterDistances) + ",");
//	}	
//	
//	private double calculateAverage(List<Double> distances) {
//		Double sum = 0.0D;
//		if (!distances.isEmpty()) {
//			for (Double distance : distances) {
//				sum += distance;
//			}
//			return sum.doubleValue() / distances.size();
//		}
//		return sum;
//	}
//	
//	private double getVariance(List<Double> values) {
//		double d1 = calculateAverage(values);
//		double d2 = 0.0D;
//		for (int i = 0; i < values.size(); i++) {
//			if (values.get(i) != 0.0D) {
//				d2 += Math.pow(values.get(i) - d1, 2.0D);
//			}
//		}
////		System.out.println("D2: " + d2 + " Values.size(): " + values.size() + " Result: " + );
//		return d2 / (values.size() - 1);
//	}
//}
