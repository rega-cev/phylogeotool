package be.kuleuven.rega.phylogeotool.tree.distance;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class TreeStatistics {
	
//	private DistanceInterface distanceInterface;
	
	public static double getIntraPairwiseDistance(Tree tree, Node cluster, DistanceInterface distanceInterface) {
		List<Node> children = tree.getLeaves(cluster);
		double sum = 0.0D;
		int amountOfRuns = 0;
		for(int i = 0; i < children.size(); i++) {
			for(int j = i + 1; j < children.size(); j++) {
				sum += distanceInterface.getDistance(tree, children.get(i), children.get(j));
				amountOfRuns++;
			}
		}
		if(amountOfRuns > 0) {
			return sum/amountOfRuns;
		} else {
			return 0.0D;
		}
	}
	
//	public Map<Double, Integer> getIntraPairwiseDistancePieter(Node cluster) {
//		List<Node> leafs = this.getLeaves(this.getNodeById(cluster.getId()));
//		double sum = 0.0D;
//		int amountOfRuns = 0;
//		for(int i = 0; i < leafs.size(); i++) {
//			for(int j = i + 1; j < leafs.size(); j++) {
//				sum += this.getDistance(leafs.get(i), leafs.get(j));
//				amountOfRuns++;
//			}
//		}
//		Map<Double,Integer> toReturn = new HashMap<Double, Integer>();
//		toReturn.put(sum, amountOfRuns);
//		return toReturn;
//	}
	
	/**
	 * @pre It is expected that the list of clusters contain viable clusters (they are bigger than the minimum cluster size)
	 * @param tree
	 * @param clusters
	 * @param distanceInterface
	 * @return
	 */
	public static double getInterClusterPairwiseDistance(Tree tree, List<Node> clusters, DistanceInterface distanceInterface) {
//		int minimumClusterSize = 1;
		if(clusters != null && clusters.size() > 0) {
			double d1 = 0.0D;
			double d2 = 0.0D;
			Node cluster1;
			Node cluster2;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					cluster1 = clusters.get(i);
					cluster2 = clusters.get(j);
//					if (cluster1.isAcceptableCluster() && cluster2.isAcceptableCluster()) {
					d2 += getInterClusterPairwiseDistance(tree, cluster1, cluster2, distanceInterface);
					d1 += 1.0D;
//					}
		        }
		      }
			return d2 / d1;
		}
		return 0.0D;
	}

	private static double getInterClusterPairwiseDistance(Tree tree, Node cluster1, Node cluster2, DistanceInterface distanceInterface) {
		List<Node> list1 = tree.getLeaves(cluster1);
		List<Node> list2 = tree.getLeaves(cluster2);
		if((list1 != null && list1.size() > 0) && (list2 != null && list2.size() > 0)){
			double d = 0.0D;
			
			for (int i = 0; i < list1.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					d += distanceInterface.getDistance(tree, list1.get(i), list2.get(j));
				}
			}
			double toReturn = (d / (list1.size() * list2.size()));
			return toReturn;
		} else {
			return 0.0D;
		}
	}
	
	public static double calculateStatistics(Cluster cluster, DistanceInterface distanceInterface, int minimumClusterSize) {
		List<Node> acceptableClusters = getAcceptableClusters(cluster, minimumClusterSize);
		double inter = getInterClusterPairwiseDistance(cluster.getTree(), acceptableClusters, distanceInterface);
//		System.out.println("Inter: " + inter);
		List<Double> intraClusterDistances = new ArrayList<Double>();
		for (int i = 0; i < acceptableClusters.size(); i++) {
			double intra = getIntraPairwiseDistance(cluster.getTree(), acceptableClusters.get(i), distanceInterface);
//			System.out.println("intra: " + intra);
			intraClusterDistances.add(intra);
		}
		return calculateAverage(intraClusterDistances)/inter;
	}
	
	private static List<Node> getAcceptableClusters(Cluster cluster, int minimumClusterSize) {
		List<Node> toReturn = new ArrayList<Node>();
		for(Node node:cluster.getBoundaries()) {
			if(cluster.getTree().getLeaves(node).size() >= minimumClusterSize) {
				toReturn.add(node);
			}
		}
		return toReturn;
	}
	
	private static double calculateAverage(List<Double> distances) {
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
