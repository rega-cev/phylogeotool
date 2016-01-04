package be.kuleuven.rega.phylogeotool.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public class Tree {
	private final Set<Node> nodes;
	private final Set<Edge> edges;
	
	private final Node rootNode;
	
	private final Map<Node, Edge> edgeToParent;
//	private final Map<Node, Integer> nrLeaves;
	
	public Tree(Set<Node> nodes, Set<Edge> edges, Node rootNode) {
		this.nodes = nodes;
		this.edges = edges;
		this.rootNode = rootNode;
		
		// TODO: Figure out if necessary here
		edgeToParent = new HashMap<Node, Edge>();
		for(Node node:nodes) {
			if(node.getParent() != null) {
				edgeToParent.put(node, this.getEdge(node, node.getParent()));
			}
		}
		
//		System.out.println("Start Calc");
//		nrLeaves = new HashMap<Node, Integer>();
//		for(Node node:nodes) {
//			nrLeaves.put(node, getLeaves(node).size());
//		}
//		System.out.println("Finished calc");
//		System.out.println(nrLeaves.size());
	}
		
	public Node getRootNode() {
		return this.rootNode;
	}
	
	public ImmutableList<Node> getNodes() {
		return ImmutableList.copyOf(nodes);
	}

	public ImmutableList<Edge> getEdges() {
		return ImmutableList.copyOf(edges);
	}

	public Edge getEdgeToParent(Node node) {
		return edgeToParent.get(node);
	}
	
	public Edge getEdge(Node node1, Node node2) {
		for(Edge edge:edges) {
			if(edge.getNode1().equals(node1) && edge.getNode2().equals(node2)) {
				return edge;
			} else if(edge.getNode1().equals(node2) && edge.getNode2().equals(node1)) {
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
	
	public Node getLeafByLabel(String label) {
		for(Node node:nodes) {
			if(node.getLabel().equals(label)) {
				return node;
			}
		}
		return null;
		//throw new Exception("String with label: " + label + " not found.");
	}
	
//	public double getDistance(Node node) {
//		double distance = 0.0;
////		System.out.println("Node: " + node.getId());
//		while(node.hasParent()) {
//			distance += edgeToParent.get(node).getDistance();
//			node = node.getParent();
//		}
//		return distance;
//	}
//	
//	public double getDistance(Node node1, Node node2) {
//		return distanceInterface.getDistance(node1, node2);
//	}
//	
//	private void setDistanceInterface(DistanceInterface distanceInterface) {
//		this.distanceInterface = distanceInterface;
//	}
	
	public Node getLeastCommonAncestor(Node node1, Node node2) {
		List<Node> parents1 = this.getAllParents(node1);
		List<Node> parents2 = this.getAllParents(node2);
		parents1.retainAll(parents2);
		if(parents1.size() > 0) {
			return parents1.iterator().next();
		} else {
			if(parents2.size() == 0) {
				return node2;
			} else {
				return node1;
			}
		}
	}
	
	public List<Node> getLeaves() {
//		Set<Node> toReturn = new HashSet<Node>();
//		for(Node node:nodes) {
//			if(node.getImmediateChildren().size() == 0 && node.getId() != this.getRootNode().getId()) {
//				toReturn.add(node);
//			}
//		}
//		return toReturn;
		return this.getLeaves(this.getRootNode());
	}
	
	// SHOULD ONLY BE CALLED WHEN CREATING A CLUSTER
	public List<Node> getLeaves(Node node) {
		return visitNode(node, new ArrayList<Node>());
	}
	
	public List<Node> getAllParents(Node node) {
		List<Node> parents = new ArrayList<Node>();
		while (node.getParent() != null) {
			parents.add(node.getParent());
			node = node.getParent();
		}
		return parents;
	}
	
	private List<Node> visitNode(Node node, List<Node> leafs) {
		if (node.hasChildren() && node.getImmediateChildren().get(0) != null) {
			visitNode(node.getImmediateChildren().get(0), leafs);
		}
		if (node.hasChildren() && node.getImmediateChildren().get(1) != null) {
			visitNode(node.getImmediateChildren().get(1), leafs);
		}
		if (!node.hasChildren()) {
			leafs.add(node);
		}
		return leafs;
	}
	
//	public Set<Node> getLeaves() {
//		Set<Node> toReturn = new HashSet<Node>();
//		for(Node node:nodes) {
//			if(node.getImmediateChildren().size() == 0 && node.getSize() > 1) {
//				toReturn.add(node);
//			}
//		}
//		return toReturn;
//	}
	
	public HashMap<Integer, Node> getLeavesMap() {
		HashMap<Integer, Node> leaves = new HashMap<Integer, Node>();
		for(Node node:this.getLeaves()) {
			leaves.put(node.getId(), node);
		}
		return leaves;
	}
//	
//	public Tree clone() {
//		List<Node> nodesClones = new ArrayList<Node>();
//		List<Edge> edgesClones = new ArrayList<Edge>();
//
////		nodesClones.clear();
////		edgesClones.clear();
//		this.preOrder(this.getRootNode(), null, nodesClones, edgesClones);
//		System.out.println(nodesClones.size());
//		System.out.println(edgesClones.size());
////		System.out.println(rootNodeClone.getId());
//		return new Tree(new HashSet<Node>(nodesClones), new HashSet<Edge>(edgesClones), this.rootNode);
//	}
//	
//	
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
//	public void preOrder (Node root, Node parent, List<Node> nodesClones, List<Edge> edgesClones) {
//		if(root == null || !nodes.contains(root)) {
//			parent.removeChildren();
//			return;
//		}
//		
//		Node tempStorage = null;
//		tempStorage = root.clone();
//		
//		if(parent != null) {
//			tempStorage.setParent(parent);
//			parent.removeChild(root);
//			parent.addChild(tempStorage);
//			nodesClones.add(tempStorage);
//			edgesClones.add(new Edge("", parent, tempStorage, this.getEdge(parent.getId(), tempStorage.getId()).getDistance()));
//		} else {
//			tempStorage.setParent(null);
//			nodesClones.add(tempStorage);
//			this.rootNode = tempStorage;
////			System.out.println("rootNode: " + rootNode);
//		}
//		
//		if(root.getImmediateChildren().size() > 0) {
//			preOrder(root.getImmediateChildren().get(0), tempStorage, nodesClones, edgesClones);
//			preOrder(root.getImmediateChildren().get(1), tempStorage, nodesClones, edgesClones);
//		} else {
//			preOrder(null, tempStorage, nodesClones, edgesClones);
//		}
//	}
	
	@Override
    public boolean equals(Object other){
		Tree otherTree = (Tree)other;
		if(other != null && (this.getRootNode().getId() == otherTree.getRootNode().getId()
				&& this.getEdges().size() == otherTree.getEdges().size()
				&& this.getNodes().size() == otherTree.getNodes().size())) {
			for(Edge edge:this.getEdges()) {
				if(!otherTree.getEdges().contains(edge)) {
					return false;
				}
			}
			
			for(Node node:this.getNodes()) {
				if(!otherTree.getNodes().contains(node)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
    }
	
	/**
	 * Clusters which contain a minimum amount of sequences
	 * @return
	 */
//	public List<Node> getAcceptableClusters(int minimumClusterSize) {
//		List<Node> acceptableClusters = new ArrayList<Node>();
//		for(Node leaf:this.getLeaves()) {
////			System.out.println("Size: " + leaf.getSize());
//			if(leaf.getSize() >= minimumClusterSize) {
//				acceptableClusters.add(leaf);
//			}
//		}
//		return acceptableClusters;
//	}
	
//	public void printTree(Node tmpRoot) {
//
//        Queue<Node> currentLevel = new LinkedList<Node>();
//        Queue<Node> nextLevel = new LinkedList<Node>();
//
//        currentLevel.add(tmpRoot);
//
//        while (!currentLevel.isEmpty()) {
//            Iterator<Node> iter = currentLevel.iterator();
//            while (iter.hasNext()) {
//                Node currentNode = iter.next();
//                if (currentNode.getChildren().get(0) != null) {
//                    nextLevel.add(currentNode.getChildren().get(0));
//                }
//                if (currentNode.getChildren().get(1) != null) {
//                    nextLevel.add(currentNode.getChildren().get(1));
//                }
//                System.out.print(currentNode.getId() + " ");
//            }
//            System.out.println();
//            currentLevel = nextLevel;
//            nextLevel = new LinkedList<Node>();
//
//        }
//
//    }
}
