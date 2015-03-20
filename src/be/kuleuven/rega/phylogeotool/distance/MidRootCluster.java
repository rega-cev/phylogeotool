package be.kuleuven.rega.phylogeotool.distance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;
import edu.uci.ics.jung.graph.DelegateTree;

public class MidRootCluster<V,E> {
	
	private int numberOfIClusters;
	private RootedTree tree;
	private int biggestCluster = 0;
	
	public MidRootCluster(int numberOfIClusters, RootedTree tree) {
		this.numberOfIClusters = numberOfIClusters;
		this.tree = tree;
	}

	public DelegateTree<V,E> calculate(Node rootNode) {
		LinkedList<Node> tempQueue = new LinkedList<Node>();
		DelegateTree<V,E> jungTree = new DelegateTree<V,E>();
		jungTree.addVertex((V)rootNode);
		
		tempQueue.addAll(getChildrenBiggestFirst(tree, rootNode));
		Node node = null;
		int i = 1;
		int nrNodesNoChildren = 0;
		while(true) {
			if(tempQueue.size() > 0 && (tempQueue.size() + nrNodesNoChildren) < getNumberOfIClusters()) {
				node = tempQueue.pop();
				jungTree.addChild((E) ("Edge" + Integer.toString(i)), ((V)tree.getParent(node)), ((V)node));
				int nrChildren = getChildrenBiggestFirst(tree, node).size();
				if(nrChildren == 0) {
					nrNodesNoChildren += 1;
				} else {
					tempQueue.addAll(getChildrenBiggestFirst(tree, node));
				}
			} else {
				if(tempQueue.size() != 0) {
					//TODO: Need method to define number of children from
					//biggestCluster = ((SimpleRootedNode)tempQueue.peek()).
				} else {
					biggestCluster = 0;
				}
				for(Node tempNode:tempQueue) {
					jungTree.addChild((E) ("Edge" + Integer.toString(i)), ((V)tree.getParent(tempNode)), ((V)tempNode));
					i++;
				}
				break;
			}
			i++;
		}
		return jungTree;
	}
	
	public int getNumberOfIClusters() {
		return numberOfIClusters;
	}

	public void setNumberOfIClusters(int numberOfIClusters) {
		this.numberOfIClusters = numberOfIClusters;
	}
	
	public List<Node> getChildrenBiggestFirst(RootedTree tree, Node node) {
		List<Node> sortedList = tree.getChildren(node);
		Collections.sort(sortedList, new ClusterSizeComparator(tree));
		return sortedList;
	}
	
	public be.kuleuven.rega.phylogeotool.tree.Node jeblToPhyloGeoNode(jebl.evolution.graphs.Node node, NodeIndexProvider nodeIndexProvider) {
		// TODO: Implement method to go from JEBL to PhyloGeoTree
		be.kuleuven.rega.phylogeotool.tree.Node tempNode = new be.kuleuven.rega.phylogeotool.tree.Node();
		if(tree.getTaxon(node) != null) {
			tempNode.setLabel(tree.getTaxon(node).getName());
		}
		
		if(tree.getChildren(node).size() != 0) {
			for(Node child:tree.getChildren(node)) {
				be.kuleuven.rega.phylogeotool.tree.Node phylogeoNode = new be.kuleuven.rega.phylogeotool.tree.Node(nodeIndexProvider);
				tempNode.addChild(phylogeoNode);
				fillTree(child, phylogeoNode, nodeIndexProvider);
			}
		} else {
			if(tree.getTaxon(node) != null) {
				tempNode.setLabel(tree.getTaxon(node).getName());
			}
		}
		
		return tempNode;
	}
	
	private void fillTree(jebl.evolution.graphs.Node node, be.kuleuven.rega.phylogeotool.tree.Node rootNode, NodeIndexProvider nodeIndexProvider) {
		if(tree.getChildren(node).size() != 0) {
			for(Node child:tree.getChildren(node)) {
				be.kuleuven.rega.phylogeotool.tree.Node phylogeoNode = new be.kuleuven.rega.phylogeotool.tree.Node(nodeIndexProvider);
				rootNode.addChild(phylogeoNode);
				phylogeoNode.setParent(rootNode);
				fillTree(child, phylogeoNode, nodeIndexProvider);
			}
		} else {
			if(tree.getTaxon(node) != null) {
				rootNode.setLabel(tree.getTaxon(node).getName());
			}
		}
	}

	public RootedTree getJeblTree() {
		return this.tree;
	}
	
	public int getBiggestCluster() {
		return biggestCluster;
	}
}
