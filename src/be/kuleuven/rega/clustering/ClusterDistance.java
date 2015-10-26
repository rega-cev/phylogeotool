//package be.kuleuven.rega.clustering;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.Set;
//
//import be.kuleuven.rega.phylogeotool.tree.Edge;
//import be.kuleuven.rega.phylogeotool.tree.Node;
//import be.kuleuven.rega.phylogeotool.tree.Tree;
//
//public class ClusterDistance {
//
//	private int numberOfIClusters;
//	
//	public Tree calculate(Tree tree, Comparator<Node> comparator) {
//		LinkedList<Edge> tempQueue = new LinkedList<Edge>();
//		
//		Set<Node> clusteredNodes = new HashSet<Node>();
//		Set<Edge> clusteredEdges = new HashSet<Edge>();
//		
//		clusteredNodes.add(startNode);
//		
//		if(this.getNumberOfIClusters() <= 2) {
//			switch (this.getNumberOfIClusters()) {
//			case 1:
//				startNode.setSize(tree.getNodeById(startNode.getId()).getAllChildren().size());
//				break;
//			case 2:
//				Node tempNode1 = startNode.getImmediateChildren().get(0);
//				tempNode1.setSize(tempNode1.getLeaves().size());
//				Node tempNode2 = startNode.getImmediateChildren().get(1);
//				tempNode2.setSize(tempNode2.getLeaves().size());
//				clusteredNodes.add(tempNode1);
//				clusteredNodes.add(tempNode2);
//				
//				clusteredEdges.add(tree.getEdge(startNode, startNode.getImmediateChildren().get(0)));
//				clusteredEdges.add(tree.getEdge(startNode, startNode.getImmediateChildren().get(1)));
//				break;
//			}
//		} else {
//			tempQueue.addAll(tree.getEdges());
//			Collections.sort(tempQueue, comparator);
//		}
//		Node node = null;
//		int nrNodesNoChildren = 0;
//		while(true) {
//			// Inner nodes
//			if(tempQueue.size() > 0 && (tempQueue.size() + nrNodesNoChildren) < getNumberOfIClusters()) {
//				node = tempQueue.pop();
//				clusteredNodes.add(node);
//				clusteredEdges.add(tree.getEdge(node.getParent(), node));
//				int nrChildren = node.getImmediateChildren().size();
//				if(nrChildren == 0) {
//					nrNodesNoChildren += 1;
//				} else {
//					tempQueue.addAll(node.getImmediateChildren());
//					Collections.sort(tempQueue, comparator);
//				}
//			// Leafs
//			} else {
////				totalAmountOfSequences = tree.getRootNode().getLeaves().size();
//				// Remove the small clusters 
//				//tempQueue = recluster(tempQueue, jungTree);
//				for(Node tempNode:tempQueue) {
//					tempNode.setSize(tempNode.getLeaves().size());
////					tempNode.removeChildren();
//					clusteredNodes.add(tempNode);
//					clusteredEdges.add(tree.getEdge(tempNode.getParent(), tempNode));
//				}
//				break;
//			}
//		}
//		
//		Tree clonedTree = new Tree(clusteredNodes, clusteredEdges, startNode).clone();
//		for(Node tempNode:clonedTree.getNodes()) {
//			tempNode.setX(0.0);
//			tempNode.setY(0.0);
//			tempNode.setTheta(0.0);
//		}
//		return clonedTree;
//	}
//	
//	public int getNumberOfIClusters() {
//		return this.numberOfIClusters;
//	}
//
//	public void setNumberOfIClusters(int numberOfIClusters) {
//		this.numberOfIClusters = numberOfIClusters;
//	}
//	
//}
