//package be.kuleuven.rega.phylogeotool.tools;
//
//import jebl.evolution.graphs.Node;
//import jebl.evolution.trees.SimpleRootedTree;
////TODO: Check if this class still necessary
//public class TreeTraversal {
//	
//	/**
//	 * 1.  Print out the root's value, regardless of whether you are at the actual root or 
//	 * 	   just the subtree's root.
//	 * 2.  Go to the left child node, and then perform a pre-order 
//	 * 	   traversal on that left child node's subtree.
//	 * 3.  Go to the right child node, and then perform a 
//	 * 	   pre-order traversal on that right child node's subtree.
//	 * 4.  Do this recursively.
//	 * @param simpleRootedTree
//	 * @param root
//	 */
//	public static void preOrder (SimpleRootedTree simpleRootedTree, Node root) {
//		if(root == null) return;
//		root.toString();
//		preOrder(simpleRootedTree, simpleRootedTree.getChildren(root).get(0));
//		preOrder(simpleRootedTree, simpleRootedTree.getChildren(root).get(1));
//	}
//
//	/**
//	 * 1.  Traverse the left subtree
//	 * 2.  Traverse the right subtree
//	 * 3.  Visit the root
//	 * @param simpleRootedTree
//	 * @param root
//	 */
//	public static void postOrder (SimpleRootedTree simpleRootedTree, Node root) {
//		if(root == null) return;
//		postOrder( simpleRootedTree, simpleRootedTree.getChildren(root).get(0) );
//		postOrder( simpleRootedTree, simpleRootedTree.getChildren(root).get(1) ); 
//		root.toString();  
//	}
//	
//}
