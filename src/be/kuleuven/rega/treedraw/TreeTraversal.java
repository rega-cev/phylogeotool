package be.kuleuven.rega.treedraw;

import java.util.List;
import java.util.Map;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
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
	public static void preOrder (Cluster cluster, Node root, Map<Node, DrawData> nodeToDrawData, List<Edge> edges, Shape shape) {
		if(root == null) return;
		
		// Calculate x || Calculate r
		if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.CIRCULAR_PHYLOGRAM) {
			if(root.getParent() != null) {
				for(Edge edge:edges) {
					if(edge.getNode2() == root) {
						updateMap(nodeToDrawData, root, nodeToDrawData.get(root.getParent()).getX() + edge.getDistance(), null, null);
//						root.setX(root.getParent().getX() + edge.getDistance());
						break;
					}
				}
			} else {
				updateMap(nodeToDrawData, root, 0.0, null, null);
//				root.setX(0.0);
			}
		} else if(shape == Shape.RECTANGULAR_CLADOGRAM) {
			if(root.getParent() != null) {
				updateMap(nodeToDrawData, root, (double)nodeLevel(cluster.getRoot(), root, 0), null, null);
//				root.setX(nodeLevel(root, 0));
			} else {
				updateMap(nodeToDrawData, root, 0.0, null, null);
//				root.setX(0.0);
			}
		} else if(shape == Shape.RADIAL) {
			if(root.getParent() != null) {
				
				for(Edge edge:edges) {
					if(edge.getNode2() == root) {
						updateMap(nodeToDrawData, root,
								nodeToDrawData.get(root.getParent()).getX() + edge.getDistance() * Math.cos(nodeToDrawData.get(root).getTheta()),
								nodeToDrawData.get(root.getParent()).getX() + edge.getDistance() * Math.sin(nodeToDrawData.get(root).getTheta()), null);
//						root.setX(root.getParent().getX() + edge.getDistance() * Math.cos(root.getTheta()));
//						root.setY(root.getParent().getY() + edge.getDistance() * Math.sin(root.getTheta()));
						break;
					}
				}
			} else {
				updateMap(nodeToDrawData, root, 0.0, 0.0, null);
//				root.setX(0.0);
//				root.setY(0.0);
			}
		}
		
		if(!cluster.getBoundaries().contains(root)) {
			preOrder(cluster, root.getImmediateChildren().get(0), nodeToDrawData, edges, shape);
			preOrder(cluster, root.getImmediateChildren().get(1), nodeToDrawData, edges, shape);
		} else {
			preOrder(cluster, null, nodeToDrawData, edges, shape);
		}
	}

	/**
	 * 1.  Traverse the left subtree
	 * 2.  Traverse the right subtree
	 * 3.  Visit the root
	 * @param simpleRootedTree
	 * @param root
	 */
	public static void postOrder (Cluster cluster, Node root, Map<Node, DrawData> nodeToDrawData, Shape shape, int nrLeaves, int deepestLevel) {
		if(root == null) return;
		// Inner node
		if(!cluster.getBoundaries().contains(root)) {
			postOrder(cluster, root.getImmediateChildren().get(0), nodeToDrawData, shape, nrLeaves, deepestLevel);
			postOrder(cluster, root.getImmediateChildren().get(1), nodeToDrawData, shape, nrLeaves, deepestLevel);
			if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.RECTANGULAR_CLADOGRAM) {
				updateMap(nodeToDrawData, root, null, (nodeToDrawData.get(root.getImmediateChildren().get(0)).getY() + nodeToDrawData.get(root.getImmediateChildren().get(1)).getY())/2, null);
//				root.setY((root.getImmediateChildren().get(0).getY() + root.getImmediateChildren().get(1).getY())/2);
			} else if(shape == Shape.CIRCULAR_PHYLOGRAM) {
				updateMap(nodeToDrawData, root, null, null, (nodeToDrawData.get(root.getImmediateChildren().get(0)).getTheta() + nodeToDrawData.get(root.getImmediateChildren().get(1)).getTheta())/2);
//				root.setTheta((root.getImmediateChildren().get(0).getTheta() + root.getImmediateChildren().get(1).getTheta())/2);
			} else if(shape == Shape.CIRCULAR_CLADOGRAM || shape == Shape.RADIAL) {
				if(root.getParent() != null) {
					updateMap(nodeToDrawData, root, (double)nodeLevel(cluster.getRoot(), root, 0), null, 
							(nodeToDrawData.get(root.getImmediateChildren().get(0)).getTheta() + nodeToDrawData.get(root.getImmediateChildren().get(1)).getTheta())/2);
//					root.setX(nodeLevel(root, 0));
//					root.setTheta((root.getImmediateChildren().get(0).getTheta() + root.getImmediateChildren().get(1).getTheta())/2);
				} else {
					updateMap(nodeToDrawData, root, 0.0, 0.0, null);
//					root.setX(0.0);
//					root.setY(0.0);
				}
			}
		// Leaf
		} else {
			postOrder(cluster, null, nodeToDrawData, shape, nrLeaves, deepestLevel);
			if(shape == Shape.RECTANGULAR_PHYLOGRAM || shape == Shape.RECTANGULAR_CLADOGRAM) {
				updateMap(nodeToDrawData, root, null, (double)++y, null);
//				root.setY(++y);
			} else if(shape == Shape.CIRCULAR_PHYLOGRAM || shape == Shape.RADIAL) {
				updateMap(nodeToDrawData, root, null, null, 2*Math.PI*((double)++y/nrLeaves));
//				root.setTheta(2*Math.PI*((double)++y/nrLeaves));
			} else if(shape == Shape.CIRCULAR_CLADOGRAM) {
				updateMap(nodeToDrawData, root, (double)deepestLevel, null, 2*Math.PI*((double)++y/nrLeaves));
//				root.setX(deepestLevel);
//				root.setTheta(2*Math.PI*((double)++y/nrLeaves));
			}
		}
	}
	
	public static void updateMap(Map<Node, DrawData> map, Node node, Double x, Double y, Double theta) {
		if(map.containsKey(node)) {
			if(x != null)
				map.get(node).setX(x);
			if(y != null)
				map.get(node).setY(y);
			if(theta != null)
				map.get(node).setTheta(theta);
		} else {
			DrawData drawData = new DrawData(0.0, 0.0, 0.0);
			if(x != null) {
				drawData.setX(x);
			}
			if(y != null) {
				drawData.setY(y);
			}
			if(theta != null) {
				drawData.setTheta(theta);
			}
			map.put(node, drawData);
		}
	}
	
	public static int nodeLevel(Node root, Node node, int nodeLevel) {
		if(node.equals(root)) {
			return nodeLevel;
		} else {
			return nodeLevel(root, node.getParent(), ++nodeLevel);
		}
	}
}