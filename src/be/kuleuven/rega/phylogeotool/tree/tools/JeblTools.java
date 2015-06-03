package be.kuleuven.rega.phylogeotool.tree.tools;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree.SimpleRootedNode;
import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;

public class JeblTools {
	
	public static List<Node> getLeaves(RootedTree tree, Node node) {
		List<Node> leafs = new ArrayList<Node>();
		visitNode(tree, node, leafs, null);
		return leafs;
	}
	
	public static List<String> getLeafNames(RootedTree tree, Node node) {
		List<String> leafs = new ArrayList<String>();
		visitNode(tree, node, null, leafs);
		return leafs;
	}
	
	private static void visitNode(RootedTree tree, Node node, List<Node> leafs, List<String> leafNames) {
		if(tree.getChildren(node).size() != 0) {
	        for(Node tempNode:tree.getChildren(node)) {
	        	visitNode(tree, tempNode, leafs, leafNames);
	        }
	    } else {
	    	if(leafs != null) {
	    		leafs.add(node);
	    	}
	    	if(leafNames != null) {
	    		leafNames.add(tree.getTaxon(node).getName());
	    	}
	    }
	}
	
	static Node nodeToReturn = null;
	
	public static Node getNodeById(RootedTree tree, int id) {
		nodeToReturn = null;
		visitChildren(tree, tree.getRootNode(), id);
		return nodeToReturn;
	}
	
	private static void visitChildren(RootedTree tree, Node node, int id) {
		if(((SimpleRootedNode)node).getId() == id) {
			nodeToReturn = node;
		} else if(nodeToReturn == null){
			for(Node tempNode:tree.getChildren(node)) {
				visitChildren(tree, tempNode, id);
			}
		}
	}
}
