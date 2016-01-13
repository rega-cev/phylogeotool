package be.kuleuven.rega.prerendering;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

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

public class MainSingleThread {
	
	public static void main(String[] args) throws FileNotFoundException {
		MainSingleThread main = new MainSingleThread();
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
		
		DistanceInterface distanceInterface = null;
		HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt";
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
			distanceInterface = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
		} else {
			distanceInterface = new DistanceCalculateFromTree();
		}
		
		main.process(tree, distanceInterface);
	}
	
	public void process(Tree tree, DistanceInterface distanceInterface) {
		
//		List<Tree> clusters = new ArrayList<Tree>();
		Cluster[] clusters = new Cluster[50];
		
		for(int k = 2; k < 50; k++) {
			clusters[k] = MidRootCluster.calculate(tree, tree.getRootNode(), new ClusterSizeComparator(tree), 2, k);
		}
		
		Double[] sdrValues = new Double[50];
		for(int k = 2; k < 50; k++) {
			sdrValues[k] = TreeStatistics.calculateStatistics(clusters[k], distanceInterface, 2);
		}
		
		for(int k = 2; k < 50; k++) {
			System.out.println(sdrValues[k]);
		}
	}
}
