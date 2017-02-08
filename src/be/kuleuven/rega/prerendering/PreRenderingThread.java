package be.kuleuven.rega.prerendering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;

@SuppressWarnings("serial")
public class PreRenderingThread extends RecursiveTask<List<Double>> {
	
	private final int min;
	private final int max;
	private final Tree tree;
	private final Node startNode;
	private final Cluster parentalCluster;
	private final DistanceInterface distanceInterface;
	private final Comparator<Node> comparator;
	private int minimumClusterSize;
	
	public PreRenderingThread(int min, int max, Tree tree, Node startNode, Cluster parentalCluster, DistanceInterface distanceInterface, Comparator<Node> comparator, int minimumClusterSize) {
		this.min = min;
		this.max = max;
		this.tree = tree;
		this.startNode = startNode;
		this.parentalCluster = parentalCluster;
//		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
		this.distanceInterface = distanceInterface;
		this.comparator = comparator;
		this.minimumClusterSize = minimumClusterSize;
	}

	@Override
	protected List<Double> compute() {
		List<RecursiveTask<Double>> forks = new LinkedList<RecursiveTask<Double>>();
		List<Double> sdrDistances = new ArrayList<Double>();
		
		for(int i = min; i <= max; i++) {
			Cluster cluster = MidRootCluster.calculate(tree, startNode, parentalCluster, comparator, minimumClusterSize, i);
			if(cluster != null) {
				CalculateBestClusterTask calculateBestCluster = new CalculateBestClusterTask(i, cluster, distanceInterface, minimumClusterSize);
//				System.out.println("Adding task: " + i);
				forks.add(calculateBestCluster);
				calculateBestCluster.fork();
			}
		}
		
		for(RecursiveTask<Double> task:forks) {
			sdrDistances.add(task.join());
		}
		
		return sdrDistances;
	}
	
}
