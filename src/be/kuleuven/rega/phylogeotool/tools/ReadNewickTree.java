package be.kuleuven.rega.phylogeotool.tools;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jebl.evolution.io.ImportException;
import jebl.evolution.trees.Tree;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree.SimpleRootedNode;

public class ReadNewickTree {
	
	public static Tree jeblTree = null;
	
	public static Tree readNewickTree(Reader reader) {
		NewickImporter newickImporter = new NewickImporter(reader, false);
		try {
			if (newickImporter.hasTree()) {
				jeblTree = newickImporter.importNextTree();
				return jeblTree;
			} else {
				return null;
			}
		} catch (IOException e) {
			System.err.println(ReadNewickTree.class.getName() + ": Treefile not found");
		} catch (ImportException e) {
			System.err.println(ReadNewickTree.class.getName() + ": Could not import the tree");
		}
		return null;
	}
	
	public static List<Node> nodes = new ArrayList<Node>();
	public static List<Edge> edges = new ArrayList<Edge>();
	public static int i = 1;
	public static Node rootNode = null;
	
	public static be.kuleuven.rega.phylogeotool.tree.Tree jeblToTreeDraw(SimpleRootedTree tree) {
		preOrder((SimpleRootedNode)tree.getRootNode(), null);
		// TODO: Check why we need a set
		return new be.kuleuven.rega.phylogeotool.tree.Tree(new HashSet<Node>(nodes), new HashSet<Edge>(edges), rootNode);
	}
	
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
		} else {
			nodes.add(tempStorage);
		}
		
		if(i == 2) {
			rootNode = tempStorage;
		}
		
		if(root.getChildren().size() > 0) {
			preOrder((SimpleRootedNode)root.getChildren().get(0), tempStorage);
			preOrder((SimpleRootedNode)root.getChildren().get(1), tempStorage);
		} else {
			preOrder(null, tempStorage);
		}
	}
}
