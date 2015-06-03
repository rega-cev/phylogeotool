package be.kuleuven.rega.phylogeotool.distance;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class MidRootCluster {
	
	private int numberOfIClusters;
	
	public MidRootCluster(int numberOfIClusters) {
		this.numberOfIClusters = numberOfIClusters;
	}

	public Tree calculate(Tree tree, Node startNode) {
		LinkedList<Node> tempQueue = new LinkedList<Node>();
		ClusterSizeComparator clusterSizeComparator = new ClusterSizeComparator();
		
		Set<Node> clusteredNodes = new HashSet<Node>();
		Set<Edge> clusteredEdges = new HashSet<Edge>();
		
		clusteredNodes.add(startNode);
		
		tempQueue.addAll(startNode.getChildren());
		Collections.sort(tempQueue, clusterSizeComparator);
		Node node = null;
		int nrNodesNoChildren = 0;
		while(true) {
			// Inner nodes
			if(tempQueue.size() > 0 && (tempQueue.size() + nrNodesNoChildren) < getNumberOfIClusters()) {
				node = tempQueue.pop();
				clusteredNodes.add(node);
				clusteredEdges.add(tree.getEdge(node.getParent(), node));
				int nrChildren = node.getChildren().size();
				if(nrChildren == 0) {
					nrNodesNoChildren += 1;
				} else {
					tempQueue.addAll(node.getChildren());
					Collections.sort(tempQueue, clusterSizeComparator);
				}
			// Leafs
			} else {
//				totalAmountOfSequences = tree.getRootNode().getLeaves().size();
				// Remove the small clusters 
				//tempQueue = recluster(tempQueue, jungTree);
				for(Node tempNode:tempQueue) {
					tempNode.setSize(tempNode.getLeaves().size());
//					tempNode.removeChildren();
					clusteredNodes.add(tempNode);
					clusteredEdges.add(tree.getEdge(tempNode.getParent(), tempNode));
				}
				
				// TODO: Look at this part of the code. Why is it still needed
//				for(V vertix:jungTree.getVertices()) {
//					if((jungTree.getChildren(vertix).size() == 0) && JeblTools.getLeaves(this.tree, ((Node)vertix)).size() < (totalAmountOfSequences*0.001)) {
//						this.nrBadClusters++;
//					}
//				}
				
				break;
			}
		}
		
		Tree clonedTree = new Tree(clusteredNodes, clusteredEdges, startNode).clone();
		for(Node tempNode:clonedTree.getNodes()) {
			tempNode.setX(0.0);
			tempNode.setY(0.0);
			tempNode.setTheta(0.0);
		}
		return clonedTree;
	}
	
	/**
	 * This method cluster smaller clusters (contains < 0.1% of total amount of sequences) into their closest neighbor
	 * @return the newly clustered tree
	 */
//	private LinkedList<Node> recluster(LinkedList<Node> queue, DelegateTree<V,E> tree) {
//		int totalLeaves = JeblTools.getLeaves(this.tree, this.tree.getRootNode()).size();
//		LinkedList<Node> tempQueue = (LinkedList<Node>)queue.clone();
//		
//		for(Node node:queue) {
//			if(JeblTools.getLeaves(this.tree, node).size() < (totalLeaves*0.001)) {
//				Node parent = this.tree.getParent(node);
//				List<Node> children = this.tree.getChildren(parent);
//				for(Node childNode:children) {
//					if(childNode != node) {
//						if(queue.contains(childNode)) {
//							tempQueue.remove(tempQueue.indexOf(childNode));
//							tempQueue.remove(tempQueue.indexOf(node));
//						}
//					}
//				}
//			}
//		}
//		return tempQueue;
//	}
	
	public int getNumberOfIClusters() {
		return this.numberOfIClusters;
	}

	public void setNumberOfIClusters(int numberOfIClusters) {
		this.numberOfIClusters = numberOfIClusters;
	}
}
