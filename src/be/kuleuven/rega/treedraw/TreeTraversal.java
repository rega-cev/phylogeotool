package be.kuleuven.rega.treedraw;

import java.util.Set;

import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;

public class TreeTraversal {
	
	public static int y = 0;

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
	public static void preOrder (Node root, Set<Edge> edges, Shape shape) {
		if(root == null) return;
		
		// Calculate x || Calculate r
		if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
			if(root.getParent() != null) {
				for(Edge edge:edges) {
					if(edge.getNode2() == root) {
						root.setX(root.getParent().getX() + edge.getDistance());
						break;
					}
				}
			} else {
				root.setX(0.0);
			}
		} else if(shape == Shape.RECTANGULAR_CLADOGRAM) {
			if(root.getParent() != null) {
				root.setX(nodeLevel(root, 0));
			} else {
				root.setX(0.0);
			}
		} else if(shape == Shape.RADIAL) {
			if(root.getParent() != null) {
				
				for(Edge edge:edges) {
					if(edge.getNode2() == root) {
						root.setX(root.getParent().getX() + edge.getDistance() * Math.cos(root.getTheta()));
						root.setY(root.getParent().getY() + edge.getDistance() * Math.sin(root.getTheta()));
						break;
					}
				}
			} else {
				root.setX(0.0);
				root.setY(0.0);
			}
		}
		
		if(root.getImmediateChildren().size() > 0) {
			preOrder(root.getImmediateChildren().get(0), edges, shape);
			preOrder(root.getImmediateChildren().get(1), edges, shape);
		} else {
			preOrder(null, edges, shape);
		}
	}

	/**
	 * 1.  Traverse the left subtree
	 * 2.  Traverse the right subtree
	 * 3.  Visit the root
	 * @param simpleRootedTree
	 * @param root
	 */
	public static void postOrder (Node root, Shape shape, int nrLeaves, int deepestLevel) {
		if(root == null) return;
		// Inner node
		if(root.getImmediateChildren().size() > 0) {
			postOrder(root.getImmediateChildren().get(0), shape, nrLeaves, deepestLevel);
			postOrder(root.getImmediateChildren().get(1), shape, nrLeaves, deepestLevel);
			if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.RECTANGULAR_CLADOGRAM) {
				root.setY((root.getImmediateChildren().get(0).getY() + root.getImmediateChildren().get(1).getY())/2);
			} else if(shape == Shape.CIRCULAR_PHYLOGRAM) {
				root.setTheta((root.getImmediateChildren().get(0).getTheta() + root.getImmediateChildren().get(1).getTheta())/2);
			} else if(shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.RADIAL) {
				if(root.getParent() != null) {
					root.setX(nodeLevel(root, 0));
					root.setTheta((root.getImmediateChildren().get(0).getTheta() + root.getImmediateChildren().get(1).getTheta())/2);
				} else {
					root.setX(0.0);
					root.setY(0.0);
				}
			}
		// Leaf
		} else {
			postOrder(null, shape, nrLeaves, deepestLevel);
			if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.RECTANGULAR_CLADOGRAM) {
				root.setY(++y);
			} else if(shape == Shape.CIRCULAR_PHYLOGRAM || shape == Shape.RADIAL) {
				root.setTheta(2*Math.PI*((double)++y/nrLeaves));
			} else if(shape == Shape.CIRCULAR_CLADOGRAM) {
				root.setX(deepestLevel);
				root.setTheta(2*Math.PI*((double)++y/nrLeaves));
			}
		}
	}
	
	public static int nodeLevel(Node node, int nodeLevel) {
		if(node.getParent() == null) {
			return nodeLevel;
		} else {
			return nodeLevel(node.getParent(), ++nodeLevel);
		}
	}
}