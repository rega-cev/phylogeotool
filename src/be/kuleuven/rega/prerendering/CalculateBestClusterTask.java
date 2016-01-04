package be.kuleuven.rega.prerendering;

import java.util.concurrent.RecursiveTask;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;
import be.kuleuven.rega.phylogeotool.tree.distance.TreeStatistics;

@SuppressWarnings("serial")
public class CalculateBestClusterTask extends RecursiveTask<Double> {
	
	private final int i;
	private final Cluster cluster;
	private final DistanceInterface distanceInterface;
	private final int minimumClusterSize;
	
	public CalculateBestClusterTask(int i, Cluster cluster, DistanceInterface distanceInterface, int minimumClusterSize) {
		this.i = i;
		this.cluster = cluster;
		this.distanceInterface = distanceInterface;
		this.minimumClusterSize = minimumClusterSize;
	}

	@Override
	protected Double compute() {
//		System.out.println("Started Computation on: " + i);
		return TreeStatistics.calculateStatistics(cluster, distanceInterface, minimumClusterSize);
	}
	
}
