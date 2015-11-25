package be.kuleuven.rega.clustering;

import java.io.File;
import java.io.IOException;

import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.distance.MidRootCluster;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;

public class ClusterAlgos {

	private NodeIndexProvider nodeIndexProvider = null;
//	private DistanceProvider distanceProvider = null;
	private MidRootCluster midRootCluster = null;

	public ClusterAlgos() {
	}
	
	public ClusterAlgos(File nodeIndexProvider, File distances) throws IOException {
		this.nodeIndexProvider = new NodeIndexProvider(nodeIndexProvider);
//		if(distances != null) {
//			this.distanceProvider = new DistanceProvider(distances);
//		}
	}

	public Tree getCluster(Tree tree, Node startNode, int nrClusters, int minimumClusterSize) {
		if(startNode == null) {
			return determineBestClusteringGuyIdea(tree.getRootNode(), nrClusters, tree, minimumClusterSize);
		} else {
			return determineBestClusteringGuyIdea(startNode, nrClusters, tree, minimumClusterSize);
		}
	}
	
	private Tree determineBestClusteringGuyIdea(Node startNode, int nrClusters, Tree tree, int minimumClusterSize) {
		Tree toReturn = null;
		midRootCluster = new MidRootCluster(nrClusters);
		toReturn = midRootCluster.calculate(tree, startNode, new ClusterSizeComparator(), minimumClusterSize);
		return toReturn;
	}
}
