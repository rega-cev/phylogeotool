package be.kuleuven.rega.clustering;

import java.io.File;
import java.io.IOException;

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

	public Tree getCluster(Tree tree, Node startNode, int nrClusters) {
		
		//kMedoids = new KMedoids(this.getNrOfClusters(), 100, allData);
		//Map<ICluster, List<ICluster>> associationMap = kMedoids.calculate(true);
		if(startNode == null) {
			return determineBestClusteringPieterIdea(tree.getRootNode(), nrClusters, tree);
		} else {
			return determineBestClusteringPieterIdea(startNode, nrClusters, tree);
		}
	}

	// TODO: Rework determination of best clusters
	private Tree determineBestClustering(Node startNode, int nrClusters, Tree tree) {
		Tree toReturn = null;
		midRootCluster = new MidRootCluster(nrClusters);
//		for(int i = 1; i < nrClusters; i++) {
//			midRootCluster.setNumberOfIClusters(i);
		toReturn = midRootCluster.calculate(tree, startNode);
			// TODO: Check if this part of the code is still necessary
//			if(i >= 7 && midRootCluster.getNrBadClusters() >= ((1/6d)*i)) {
//				midRootCluster.setNumberOfIClusters(i-1);
//				toReturn = midRootCluster.calculate(startNode);
//				return toReturn;
//			}
//		}
		return toReturn;
	}
	
	// TODO: Rework determination of best clusters
		private Tree determineBestClusteringPieterIdea(Node startNode, int nrClusters, Tree tree) {
			Tree toReturn = null;
//			LinkedList<V> sortedClusters = new LinkedList<V>();
			midRootCluster = new MidRootCluster(nrClusters);
//			for(int i = 1; i < nrClusters; i++) {
//				midRootCluster.setNumberOfIClusters(i);
			toReturn = midRootCluster.calculate(tree, startNode);
				//TODO: Figure out why it is slow and if we still need this
//				// Sort the clusters to get the biggest ones in front
//				for(V vertix:toReturn.getVertices()) {
//					sortedClusters.add(vertix);
//				}
//				Collections.sort(sortedClusters, new JungClusterSizeComparator<V>(rootedTree));
//				
//				int numberOfSeqInClusters = 0;
//				for(int j = 0; j < Math.floor(sortedClusters.size() * 0.50);j++) {
//					numberOfSeqInClusters += JeblTools.getLeaves(rootedTree, ((jebl.evolution.graphs.Node)sortedClusters.get(j))).size();
//				}
//				
//				if(numberOfSeqInClusters < Math.ceil(midRootCluster.getTotalAmountOfSequences() * 0.50)) {
//					return toReturn;
//				}
//				
//			}
			return toReturn;
		}
}
