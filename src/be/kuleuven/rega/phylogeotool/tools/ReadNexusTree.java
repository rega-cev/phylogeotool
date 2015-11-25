package be.kuleuven.rega.phylogeotool.tools;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jebl.evolution.io.ImportException;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree.SimpleRootedNode;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class ReadNexusTree {

	public static jebl.evolution.trees.Tree jeblTree = null;
	
	public static jebl.evolution.trees.Tree readNexusTree(Reader reader) {
		NexusImporter nexusImporter = new NexusImporter(reader);
		try {
			if (nexusImporter.hasTree()) {
				jeblTree = nexusImporter.importNextTree();
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
	
	public static Tree jeblToTreeDraw(SimpleRootedTree tree, List<String> pplacerSequences) {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		i = 1;
		rootNode = null;
		preOrder((SimpleRootedNode)tree.getRootNode(), null, pplacerSequences);
		
		for(Node node:sequencesToBeEdited) {
			node.setId(i++);
		}
		// TODO: Check why we need a set
		return new Tree(new HashSet<Node>(nodes), new HashSet<Edge>(edges), rootNode);
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
	private static int nrNodes;
	private static List<Node> sequencesToBeEdited = new ArrayList<Node>();
	public static void preOrder (SimpleRootedNode root, Node parent, List<String> pplacerSequences) {
		if(root == null) return;
		
		Node tempStorage = null;
		if(root.getChildren().size() <= 0) {
			/* Special pplacer situation */
			if(pplacerSequences.contains(root.getTaxon().getName())) {
				tempStorage = new Node(root.getTaxon().getName(), nrNodes);
				sequencesToBeEdited.add(parent);
				sequencesToBeEdited.add(tempStorage);
				// Check if parent has other child. If yes, correct their id as they got a wrong one due to the introduced sequence.
				if(parent.getImmediateChildren().size() == 1) {
					fixIdPPlacerChildren(parent.getImmediateChildren().get(0));
				}
				// The parental sequence of the pplacer sequence was introduced in the new tree as well. Due to the nature of the algorithm,
				// the parental node increased our counting while this shouldn't be the case. We correct this here.
				i--;
			} else {
				tempStorage = new Node(root.getTaxon().getName(), i++);
			}
		} else {
			tempStorage = new Node("", i++);
		}
		
		if(root.getAttribute("!name") != null && !root.getAttribute("!name").equals("")) {
			tempStorage.setAttribute(root.getAttribute("!name").toString());
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
			preOrder((SimpleRootedNode)root.getChildren().get(0), tempStorage, pplacerSequences);
			preOrder((SimpleRootedNode)root.getChildren().get(1), tempStorage, pplacerSequences);
		} else {
			preOrder(null, tempStorage, pplacerSequences);
		}
	}
	
	public static void fixIdPPlacerChildren (Node root) {
		if(root == null) return;
		
		root.setId(root.getId() - 1);
		
		if(root.getImmediateChildren().size() > 0) {
			fixIdPPlacerChildren(root.getImmediateChildren().get(0));
			fixIdPPlacerChildren(root.getImmediateChildren().get(1));
		} else {
			fixIdPPlacerChildren(null);
		}
	}
	
}
