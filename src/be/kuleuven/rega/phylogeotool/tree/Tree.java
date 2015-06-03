package be.kuleuven.rega.phylogeotool.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Tree {
	private Set<Node> nodes = null;
	private Set<Edge> edges = null;
	
	private Node rootNode = null;
	
	public Tree(Set<Node> nodes, Set<Edge> edges, Node rootNode) {
		this.nodes = nodes;
		this.edges = edges;
		this.rootNode = rootNode;
	}

	public Node getRootNode() {
		return this.rootNode;
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public Edge getEdge(Node node1, Node node2) {
		for(Edge edge:edges) {
			if(edge.getNode1().equals(node1) && edge.getNode2().equals(node2)) {
				return edge;
			} else if(edge.getNode1().equals(node2) && edge.getNode2().equals(node2)) {
				return edge;
			}
		}
		return null;
	}
	
	public Edge getEdge(int nodeId1, int nodeId2) {
		for(Edge edge:edges) {
			if(edge.getNode1().getId() == nodeId1 && edge.getNode2().getId() == nodeId2) {
				return edge;
			} else if(edge.getNode1().getId() == nodeId1 && edge.getNode2().getId() == nodeId2) {
				return edge;
			}
		}
		return null;
	}
	
	public Node getNodeById(int id) {
		for(Node node:nodes) {
			if(node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	public Set<Node> getLeaves() {
		Set<Node> toReturn = new HashSet<Node>();
		for(Node node:nodes) {
			if(node.getChildren().size() == 0 && node.getId() != this.getRootNode().getId()) {
				toReturn.add(node);
			}
		}
		return toReturn;
	}
	
	public Tree clone() {
		nodesClones.clear();
		edgesClones.clear();
		this.preOrder(this.getRootNode(), null);
		return new Tree(new HashSet<Node>(nodesClones), new HashSet<Edge>(edgesClones), rootNodeClone);
	}
	
	public static List<Node> nodesClones = new ArrayList<Node>();
	public static List<Edge> edgesClones = new ArrayList<Edge>();
	public static Node rootNodeClone = null;
	
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
	public void preOrder (Node root, Node parent) {
		if(root == null || !nodes.contains(root)) {
			parent.removeChildren();
			return;
		}
		
		Node tempStorage = null;
		tempStorage = root.clone();
		
		if(parent != null) {
			tempStorage.setParent(parent);
			parent.removeChild(root);
			parent.addChild(tempStorage);
			nodesClones.add(tempStorage);
			edgesClones.add(new Edge("", parent, tempStorage, this.getEdge(parent.getId(), tempStorage.getId()).getDistance()));
		} else {
			tempStorage.setParent(null);
			nodesClones.add(tempStorage);
			rootNodeClone = tempStorage;
		}
		
		if(root.getChildren().size() > 0) {
			preOrder(root.getChildren().get(0), tempStorage);
			preOrder(root.getChildren().get(1), tempStorage);
		} else {
			preOrder(null, tempStorage);
		}
	}
	
	public void printTree(Node tmpRoot) {

        Queue<Node> currentLevel = new LinkedList<Node>();
        Queue<Node> nextLevel = new LinkedList<Node>();

        currentLevel.add(tmpRoot);

        while (!currentLevel.isEmpty()) {
            Iterator<Node> iter = currentLevel.iterator();
            while (iter.hasNext()) {
                Node currentNode = iter.next();
                if (currentNode.getChildren().get(0) != null) {
                    nextLevel.add(currentNode.getChildren().get(0));
                }
                if (currentNode.getChildren().get(1) != null) {
                    nextLevel.add(currentNode.getChildren().get(1));
                }
                System.out.print(currentNode.getId() + " ");
            }
            System.out.println();
            currentLevel = nextLevel;
            nextLevel = new LinkedList<Node>();

        }

    }
}
