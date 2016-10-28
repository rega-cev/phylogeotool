package be.kuleuven.rega.prerendering;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;

import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tree.ClusterDistance;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;


public class BestClusterMultiThread {
	
	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();
//	private static String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt";
//	private static HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	
//	public static void main(String[] args) throws FileNotFoundException {
//		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
////		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.997.midpoint.solved.newick"));
////		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/testTree.phylo"));
//		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
//		DistanceInterface distanceInterface = null;
//		
//		int index = 0;
//		for (Node leaf : tree.getLeaves()) {
//			translatedNodeNames.put(leaf.getLabel(), index++);
//		}
//		
//		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
//			distanceInterface = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
//		} else {
//			distanceInterface = new DistanceCalculateFromTree();
//		}
//		
//		List<Double> distances = forkJoinPool.invoke(new PreRenderingThread(2, 50, tree, tree.getRootNode(), distanceInterface, new ClusterSizeComparator(tree), 2));
//		ClusterDistance clusterDistance = new ClusterDistance(tree);
//		Entry<Integer, Double> maxSecondDerivative = clusterDistance.getMaxSecondDerivative(distances);
//		System.out.println("MaxEntry: " + maxSecondDerivative.getKey() + " Value: " + maxSecondDerivative.getValue());
//	}
	
	public static Cluster getBestCluster(Settings settings, int minClusters, int maxClusters, int minClusterSize, Tree tree, Node startNode, DistanceInterface distanceInterface) {
		List<Double> distances = forkJoinPool.invoke(new PreRenderingThread(minClusters, maxClusters, tree, startNode, distanceInterface, new ClusterSizeComparator(tree), minClusterSize));
		
		// Calculate SDR plot here
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Double s : distances) {
		    sb.append(s);
		    sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		
		Runtime rt = Runtime.getRuntime();
		// First print the SDR in R
		String argsSDR[] = {"/usr/local/bin/Rscript", settings.getScriptFolder() + File.separator + "SDR.R", sb.toString(), Integer.toString(startNode.getId()), settings.getBasePath()};
		
		// Second print the First Derivative in R
		String argsFirstDerivative[] = {"/usr/local/bin/Rscript", settings.getScriptFolder() + File.separator + "FirstDerivative.R", sb.toString(), Integer.toString(startNode.getId()), settings.getBasePath()};
		
		// Finally do Sgolay in R
		String argsSGolay[] = {"/usr/local/bin/Rscript", settings.getScriptFolder() + File.separator + "sgolay.R", sb.toString(), Integer.toString(startNode.getId()), settings.getBasePath()};
		
		// If there is a second derivative, plot it in R
		String argsSecondDerivative[] = {"/usr/local/bin/Rscript", settings.getScriptFolder() + File.separator + "SecondDerivative.R", sb.toString(), Integer.toString(startNode.getId()), settings.getBasePath()};

		
		List<Double> sgolay = new ArrayList<Double>();
		try {
			rt.exec(argsSDR);
			rt.exec(argsFirstDerivative);
			Process process = rt.exec(argsSGolay);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;

//			System.out.println("Output: ");
			while ((line = br.readLine()) != null) {
				System.out.println("Sgolay: " + line);
				String [] sgolayArray = line.split(" ");
				for(String value:sgolayArray) {
					try {
						sgolay.add(Double.parseDouble(value));
					} catch(NumberFormatException  e) {
						System.err.println("/Users/ewout/sgolay.R returned a strange value. Check!");
						throw new RuntimeException();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ClusterDistance clusterDistance = new ClusterDistance(tree);

		if(distances.size() >= 2) {
			// Calculate the first derivative on the sgolay function
			List<Double> firstDerivatives = ClusterDistance.getFirstDerivatives(sgolay);
			boolean isFunctionBumpy = clusterDistance.isFunctionBumpy(firstDerivatives);
			
			if(isFunctionBumpy) {
				int best = ClusterDistance.getMinValueFromList(firstDerivatives);
				System.out.println("Bumpy function for: " + startNode.getId() + " MaxEntry: " + best + " Value: " + distances.get(best));
				return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, best);
			} else {
				if(distances.size() >= 3) {
					Entry<Integer, Double> maxSecondDerivative = clusterDistance.getMaxSecondDerivative(distances);
					System.out.println("MaxEntry: " + maxSecondDerivative.getKey() + " Value: " + maxSecondDerivative.getValue());
					try {
						rt.exec(argsSecondDerivative);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, maxSecondDerivative.getKey());
				} else {
					System.out.println("MaxEntry: " + (distances.size() + 1) + " Value: No second derivative");
					return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, (distances.size() + 1));
				}
			}
		} else {
			// If distances is of size 0, this means that there are no more clusters big enough to be considered for the SDR distance
			// If distances is of size 1, this means that there is only one cluster big enough to be further split up but obviously we cannot
			// take the first nor second derivative of this function.
			System.out.println("MaxEntry: " + (distances.size() + 1) + " Value: No first derivative for " + startNode.getId());
			return MidRootCluster.calculate(tree, startNode, new ClusterSizeComparator(tree), minClusterSize, (distances.size() + 1));
		}
	}
}
