package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree.SimpleRootedNode;

public class Test {

	public static List<Node> nodes = new ArrayList<Node>();
	public static List<Edge> edges = new ArrayList<Edge>();
	public static int i = 1;
	
	/**
	 * 1.  Print out the root's value, regardless of whether you are at the actual root or 
	 * 	   just the subtree's root.
	 * 2.  Go to the left child node, and then perform a pre-order 
	 * 	   traversal on that left child node's subtree.
	 * 3.  Go to the right child node, and then perform a 
	 * 	   pre-order traversal on that right child node's subtree.
	 * 4.  Do this recursively.
	 * @param simpleRootedTree
	 * @param root
	 */
	public static void preOrder (SimpleRootedNode root, Node parent) {
		if(root == null) return;
		
		Node tempStorage = null;
		if(root.getChildren().size() <= 0) {
			tempStorage = new Node(root.getTaxon().getName(), i++);
		} else {
			tempStorage = new Node("", i++);
		}
		
		
		if(parent != null) {
			tempStorage.setParent(parent);
			parent.addChild(tempStorage);
			nodes.add(tempStorage);
			edges.add(new Edge("", parent, tempStorage, root.getLength()));
		}
		
		if(root.getChildren().size() > 0) {
			preOrder((SimpleRootedNode)root.getChildren().get(0), tempStorage);
			preOrder((SimpleRootedNode)root.getChildren().get(1), tempStorage);
		} else {
			preOrder(null, null);
		}
	}
	
	public static void main(String[] args) {
		SimpleRootedTree simpleRootedTree = null;
		try {
			simpleRootedTree = (SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/example.tree"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		preOrder((SimpleRootedNode)simpleRootedTree.getRootNode(), null);
		for(Node node:nodes) {
			System.out.println(node.getId() + " " + node.getLabel());
		}
//		
//		for(Edge edge:edges) {
//			System.out.println(edge.toString());
//		}
	}
	
}
