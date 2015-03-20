package be.kuleuven.rega.phylogeotool.tree.tools;

import java.util.ArrayList;
import java.util.List;

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
}
