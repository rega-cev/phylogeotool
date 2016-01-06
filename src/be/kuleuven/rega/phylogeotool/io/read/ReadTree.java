package be.kuleuven.rega.phylogeotool.io.read;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jebl.evolution.io.ImportException;
import jebl.evolution.trees.Tree;
import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree.SimpleRootedNode;

public class ReadTree {
	
	public static Tree jeblTree = null;
	
	public static Tree readTree(Reader reader) {
		NewickImporterAdapted newickImporter = new NewickImporterAdapted(reader, false);
		try {
			if (newickImporter.hasTree()) {
				jeblTree = newickImporter.importNextTree();
				return jeblTree;
			} else {
				return null;
			}
		} catch (IOException e) {
			System.err.println(ReadTree.class.getName() + ": Treefile not found");
		} catch (ImportException e) {
			System.err.println(ReadTree.class.getName() + ": Could not import the tree");
		}
		return null;
	}
	
	public static be.kuleuven.rega.phylogeotool.core.Tree jeblToTreeDraw(SimpleRootedTree tree, List<String> pplacerSequences) {
		List<Node> nodes = new ArrayList<Node>();
		List<Edge> edges = new ArrayList<Edge>();
		i = 1;
		if(pplacerSequences == null) {
			pplacerSequences = new ArrayList<String>();
		} else {
			nrNodes = tree.getNodes().size() - (2*pplacerSequences.size() - 1);
		}
		preOrder((SimpleRootedNode)tree.getRootNode(), null, nodes, edges, pplacerSequences);
		return new be.kuleuven.rega.phylogeotool.core.Tree(new HashSet<Node>(nodes), new HashSet<Edge>(edges), nodes.iterator().next());
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
	private static int i = 1;
	private static int nrNodes;
	private static void preOrder (SimpleRootedNode root, Node parent, List<Node> nodes, List<Edge> edges, List<String> pplacerSequences) {
		if(root == null) return;
		
		Node tempStorage = null;
		String attribute = null;
		
		if(root.getAttribute("!name") != null && !root.getAttribute("!name").equals("")) {
			attribute = root.getAttribute("!name").toString();
		}
		
		if(root.getChildren().size() <= 0) {
			/* Special pplacer situation */
			if(pplacerSequences.contains(root.getTaxon().getName())) {
				tempStorage = new Node(nrNodes++, root.getTaxon().getName(), parent, attribute);
			} else {
				tempStorage = new Node(i++, root.getTaxon().getName(), parent, attribute);
			}
		} else {
			boolean pplacedChild = false;
			
			for(jebl.evolution.graphs.Node node:root.getChildren()) {
				if(((SimpleRootedNode)node).getChildren().size() <= 0 && pplacerSequences.contains(((SimpleRootedNode)node).getTaxon().toString())) {
					pplacedChild = true;
				}
			}
			if(pplacedChild) {
				tempStorage = new Node(nrNodes++, "", parent, attribute);
			} else {
				tempStorage = new Node(i++, "", parent, attribute);
			}
		}
		
		nodes.add(tempStorage);
		if(parent != null) {
			edges.add(new Edge("", parent, tempStorage, root.getLength()));
		}
		
		if(root.getChildren().size() > 0) {
			for(int j = root.getChildren().size() - 1; j >= 0; j-- ) {
				preOrder((SimpleRootedNode)root.getChildren().get(j), tempStorage, nodes, edges, pplacerSequences);
			}
		} else {
			preOrder(null, tempStorage, nodes, edges, pplacerSequences);
		}
	}
}
