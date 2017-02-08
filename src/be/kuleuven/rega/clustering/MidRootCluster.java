package be.kuleuven.rega.clustering;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class MidRootCluster {
	
	public static Cluster calculate(Tree tree, Node startNode, Cluster parentalCluster, Comparator<Node> comparator, int minimumClusterSize, int minNrClusters) {
		LinkedList<Node> tempQueue = new LinkedList<Node>();
		
		Cluster cluster = null;
//		Set<Edge> clusteredEdges = new HashSet<Edge>();
		
//		clusteredNodes.add(startNode);
		
		tempQueue.addAll(startNode.getImmediateChildren());
		Collections.sort(tempQueue, comparator);
		
		Node node = null;
//		int nrNodesNoChildren = 0;
		while(true) {
			// Inner nodes
//			if(tempQueue.size() > 0 && (tempQueue.size() + nrNodesNoChildren) < getNumberOfIClusters()) {
//			if(biggestClusters.size() < getNumberOfIClusters() && tempQueue.containsAll(biggestClusters)) {
//				for(int i = 1; i < Math.min(numberOfIClusters, tempQueue.size()); i++) {
//					biggestClusters.clear();
//					biggestClusters.add(tempQueue.get(i));
//				}
			if(!tempQueue.isEmpty() && !acceptableClusters(tree, tempQueue, minimumClusterSize, minNrClusters)) {
				node = tempQueue.pop();
//				clusteredNodes.add(node);
//				clusteredEdges.add(tree.getEdge(node.getParent(), node));
				int nrChildren = node.getImmediateChildren().size();
				if(nrChildren != 0) {
					tempQueue.addAll(node.getImmediateChildren());
					Collections.sort(tempQueue, comparator);
				}
				
			// Leafs
			} else {
//				totalAmountOfSequences = tree.getRootNode().getLeaves().size();
				// Remove the small clusters 
				//tempQueue = recluster(tempQueue, jungTree);
				if(!tempQueue.isEmpty()) {
//					for(Node tempNode:tempQueue) {
//	//					tempNode.setSize(tree.getLeaves(tempNode).size());
//	//					tempNode.removeChildren();
//						clusters.add(new Cluster(tree, tempNode, tree.getLeaves(tempNode)));
//	//					clusteredEdges.add(tree.getEdge(tempNode.getParent(), tempNode));
//					}
					cluster = new Cluster(tree, startNode,parentalCluster,tempQueue);
				}
				break;
			}
		}
		
//		Tree clonedTree = new Tree(clusteredNodes, clusteredEdges, startNode).clone();
//		System.out.println(clonedTree.getLeaves().iterator().next().getLeaves().size());
//		for(Node tempNode:clonedTree.getNodes()) {
//			tempNode.setX(0.0);
//			tempNode.setY(0.0);
//			tempNode.setTheta(0.0);
//		}
		
		return cluster;
	}
	
	private static boolean acceptableClusters(Tree tree, LinkedList<Node> tempQueue, int minClusterSize, int minNrClusters) {
		int nrAcceptableClusters = 0;
		for(Node node:tempQueue) {
			if(tree.getLeaves(node).size() >= minClusterSize) {
				nrAcceptableClusters++;
			}
		}
		if(nrAcceptableClusters >= minNrClusters) {
			return true;
		} else {
			return false;
		}
	}
}
