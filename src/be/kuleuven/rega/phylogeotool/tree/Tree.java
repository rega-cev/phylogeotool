package be.kuleuven.rega.phylogeotool.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.distance.DistanceInterface;

public class Tree {
	private Set<Node> nodes = null;
	private Set<Edge> edges = null;
	
	private Node rootNode = null;
	
	private DistanceInterface distanceInterface = null;
	
	public Tree(Set<Node> nodes, Set<Edge> edges, Node rootNode) {
		this.nodes = nodes;
		this.edges = edges;
		this.rootNode = rootNode;
		this.distanceInterface = new DistanceCalculateFromTree(this);
	}
	
	public Tree(Set<Node> nodes, Set<Edge> edges, Node rootNode, DistanceInterface distanceInterface) {
		this.nodes = nodes;
		this.edges = edges;
		this.rootNode = rootNode;
		this.distanceInterface = distanceInterface;
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
	
	public Node getLeafByLabel(String label) throws Exception {
		for(Node node:nodes) {
			if(node.getLabel().equals(label)) {
				return node;
			}
		}
		throw new Exception("String with label: " + label + " not found.");
	}
	
	public double getDistance(Node node) {
		double distance = 0.0;
		
		while(node.hasParent()) {
			if(node.getEdgeToParent() == null) {
				node.setEdgeToParent(this.getEdge(node, node.getParent()));
			}
			distance += node.getEdgeToParent().getDistance();
			node = node.getParent();
		}
		return distance;
	}
	
	public double getDistance(Node node1, Node node2) {
		return distanceInterface.getDistance(node1, node2);
	}
	
	public void setDistanceInterface(DistanceInterface distanceInterface) {
		this.distanceInterface = distanceInterface;
	}
	
	public double getIntraPairwiseDistance(Node cluster) {
		List<Node> children = this.getNodeById(cluster.getId()).getLeaves();
		double sum = 0.0D;
		int amountOfRuns = 0;
		for(int i = 0; i < children.size(); i++) {
			for(int j = i + 1; j < children.size(); j++) {
				sum += this.getDistance(children.get(i), children.get(j));
				amountOfRuns++;
			}
		}
		if(amountOfRuns > 0) {
			return sum/amountOfRuns;
		} else {
			return 0.0D;
		}
	}
	
	public Map<Double, Integer> getIntraPairwiseDistancePieter(Node cluster) {
		List<Node> children = this.getNodeById(cluster.getId()).getLeaves();
		double sum = 0.0D;
		int amountOfRuns = 0;
		for(int i = 0; i < children.size(); i++) {
			for(int j = i + 1; j < children.size(); j++) {
				sum += this.getDistance(children.get(i), children.get(j));
				amountOfRuns++;
			}
		}
		Map<Double,Integer> toReturn = new HashMap<Double, Integer>();
		toReturn.put(sum, amountOfRuns);
		return toReturn;
	}
	
	public double getInterClusterPairwiseDistance(List<Node> clusters) {
		int minimumClusterSize = 1;
		if(clusters != null && clusters.size() > 0) {
			double d1 = 0.0D;
			double d2 = 0.0D;
			Node node1;
			Node node2;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					node1 = this.getNodeById(clusters.get(i).getId());
					node2 = this.getNodeById(clusters.get(j).getId());
					if ((node1.getLeaves().size() >= minimumClusterSize) && (node2.getLeaves().size() >= minimumClusterSize)) {
						d2 += getInterClusterPairwiseDistance(node1, node2);
						d1 += 1.0D;
					}
		        }
		      }
			return d2 / d1;
		}
		return 0.0D;
	}

	private double getInterClusterPairwiseDistance(Node node1, Node node2) {
		if((node1.getLeaves() != null && node1.getLeaves().size() > 0) && (node2.getLeaves() != null && node2.getLeaves().size() > 0)){
			List<Node> list1 = node1.getLeaves();
			List<Node> list2 = node2.getLeaves();
			double d = 0.0D;
			
			for (int i = 0; i < list1.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					d += this.getDistance(list1.get(i), list2.get(j));
				}
			}
			double toReturn = (d / (list1.size() * list2.size()));
			return toReturn;
		} else {
			return 0.0D;
		}
	}
	
	public Node getLeastCommonAncestor(Node node1, Node node2) {
		List<Node> parents1 = node1.getAllParents();
		List<Node> parents2 = node2.getAllParents();
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
	
	public Set<Node> getLeaves() {
		Set<Node> toReturn = new HashSet<Node>();
		for(Node node:nodes) {
			if(node.getImmediateChildren().size() == 0 && node.getId() != this.getRootNode().getId()) {
				toReturn.add(node);
			}
		}
		return toReturn;
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
		
		if(root.getImmediateChildren().size() > 0) {
			preOrder(root.getImmediateChildren().get(0), tempStorage);
			preOrder(root.getImmediateChildren().get(1), tempStorage);
		} else {
			preOrder(null, tempStorage);
		}
	}
	
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
	public List<Node> getAcceptableClusters(int minimumClusterSize) {
		List<Node> acceptableClusters = new ArrayList<Node>();
		for(Node leaf:this.getLeaves()) {
			if(leaf.getSize() >= minimumClusterSize) {
				acceptableClusters.add(leaf);
			}
		}
		return acceptableClusters;
	}
	
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
