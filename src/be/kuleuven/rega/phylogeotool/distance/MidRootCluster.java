package be.kuleuven.rega.phylogeotool.distance;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class MidRootCluster {
	
	private int numberOfIClusters;
	private Tree tree;
	
	public MidRootCluster(int numberOfIClusters) {
		this.numberOfIClusters = numberOfIClusters;
	}

	public Tree calculate(Tree tree, Node startNode, Comparator<Node> comparator, int minimumClusterSize) {
		this.tree = tree;
		LinkedList<Node> tempQueue = new LinkedList<Node>();
		
		Set<Node> clusteredNodes = new HashSet<Node>();
		Set<Edge> clusteredEdges = new HashSet<Edge>();
		
		clusteredNodes.add(startNode);
		
		if(this.getNumberOfIClusters() <= 2) {
			switch (this.getNumberOfIClusters()) {
			case 1:
				startNode.setSize(tree.getNodeById(startNode.getId()).getAllChildren().size());
				break;
			case 2:
				Node tempNode1 = startNode.getImmediateChildren().get(0);
				tempNode1.setSize(tempNode1.getLeaves().size());
				Node tempNode2 = startNode.getImmediateChildren().get(1);
				tempNode2.setSize(tempNode2.getLeaves().size());
				clusteredNodes.add(tempNode1);
				clusteredNodes.add(tempNode2);
				
				clusteredEdges.add(tree.getEdge(startNode, startNode.getImmediateChildren().get(0)));
				clusteredEdges.add(tree.getEdge(startNode, startNode.getImmediateChildren().get(1)));
				break;
			}
		} else {
			tempQueue.addAll(startNode.getImmediateChildren());
			Collections.sort(tempQueue, comparator);
		}
		Node node = null;
		int nrNodesNoChildren = 0;
		while(true) {
			// Inner nodes
//			if(tempQueue.size() > 0 && (tempQueue.size() + nrNodesNoChildren) < getNumberOfIClusters()) {
//			if(biggestClusters.size() < getNumberOfIClusters() && tempQueue.containsAll(biggestClusters)) {
//				for(int i = 1; i < Math.min(numberOfIClusters, tempQueue.size()); i++) {
//					biggestClusters.clear();
//					biggestClusters.add(tempQueue.get(i));
//				}
			if(!acceptableClusters(tree, tempQueue, minimumClusterSize) && !tempQueue.isEmpty()) {
				node = tempQueue.pop();
				clusteredNodes.add(node);
				clusteredEdges.add(tree.getEdge(node.getParent(), node));
				int nrChildren = node.getImmediateChildren().size();
				if(nrChildren == 0) {
					nrNodesNoChildren += 1;
				} else {
					tempQueue.addAll(node.getImmediateChildren());
					Collections.sort(tempQueue, comparator);
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
				break;
			}
		}
		
		Tree clonedTree = new Tree(clusteredNodes, clusteredEdges, startNode).clone();
//		System.out.println(clonedTree.getLeaves().iterator().next().getLeaves().size());
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
//		return (int) Math.sqrt(tree.getLeaves().size() / 2);
	}

	public void setNumberOfIClusters(int numberOfIClusters) {
		this.numberOfIClusters = numberOfIClusters;
	}
	
	public boolean acceptableClusters(Tree tree, LinkedList<Node> tempQueue, int minClusterSize) {
		int nrAcceptableClusters = 0;
		for(Node node:tempQueue) {
			if(node.getLeaves().size() >= minClusterSize) {
				nrAcceptableClusters++;
			}
		}
		
		if(nrAcceptableClusters >= getNumberOfIClusters()) {
//		if(tempQueue.size() >= getNumberOfIClusters()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean acceptableClustersPieterIdea(Tree tree, LinkedList<Node> tempQueue) {
		int nrChildren = 0;
		for(Node node:tempQueue) {
			if(node.hasChildren()) {
				nrChildren++;
			}
		}
		if(nrChildren >= getNumberOfIClusters()) {
//		if(tempQueue.size() >= getNumberOfIClusters()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean acceptableClustersGuyIdea(Tree tree, LinkedList<Node> tempQueue) {
		double minClusterSize = tree.getLeaves().size() * 0.01;
//		double minClusterSize = Math.floor(Math.sqrt(tree.getLeaves().size() / 2));
//		System.out.println(minClusterSize);
		int nrAcceptableClusters = 0;
		for(Node node:tempQueue) {
			if(node.getLeaves().size() > minClusterSize) {
				nrAcceptableClusters++;
			}
		}
		if(nrAcceptableClusters >= getNumberOfIClusters() || nrAcceptableClusters > 30) {
			return true;
		} else {
			return false;
		}
	}
}
