package be.kuleuven.rega.clustering;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.RootedTree;
import be.kuleuven.rega.phylogeotool.distance.KMedoids;
import be.kuleuven.rega.phylogeotool.distance.MidRootCluster;
import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tools.TreeTools;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.phylogeotool.tree.tools.DistanceProvider;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class KMedoidsToPhylo<V, E> {

	private Node node1 = null;
	private Node node2 = null;
	private Node node3 = null;
	private Node node4 = null;
	private Node node5 = null;
	private Node node6 = null;
	private Node node7 = null;
	private Node node8 = null;
	private Tree tree1 = null;
	private Tree tree2 = null;
	private Tree tree3 = null;
	private Tree tree4 = null;
	private Tree tree5 = null;
	private Tree tree6 = null;
	private Tree tree7 = null;
	private Tree tree8 = null;
	private NodeIndexProvider nodeIndexProvider = null;
	private DistanceProvider distanceProvider = null;
	private List<ICluster> allData = null;
	private KMedoids kMedoids = null;
	private List<ICluster> medoids = null;
	private int biggestCluster = 0;
	private int nrOfClusters;
	private MidRootCluster midRootCluster = null;

	public KMedoidsToPhylo(File nodeIndexProvider, File distances) throws IOException {
		this.nodeIndexProvider = new NodeIndexProvider(nodeIndexProvider);
		if(distances != null) {
			this.distanceProvider = new DistanceProvider(distances);
		}
	}

	public void createTestData() {
		node1 = new Node("Node1", nodeIndexProvider);
		node2 = new Node("Node2", nodeIndexProvider);
		node3 = new Node("Node3", nodeIndexProvider);
		node4 = new Node("Node4", nodeIndexProvider);
		node5 = new Node("Node5", nodeIndexProvider);
		node6 = new Node("Node6", nodeIndexProvider);
		node7 = new Node("Node7", nodeIndexProvider);
		node8 = new Node("Node8", nodeIndexProvider);
		tree1 = new Tree(node1, distanceProvider);
		tree2 = new Tree(node2, distanceProvider);
		tree3 = new Tree(node3, distanceProvider);
		tree4 = new Tree(node4, distanceProvider);
		tree5 = new Tree(node5, distanceProvider);
		tree6 = new Tree(node6, distanceProvider);
		tree7 = new Tree(node7, distanceProvider);
		tree8 = new Tree(node8, distanceProvider);
		allData = new ArrayList<ICluster>();
		allData.add(tree1);
		allData.add(tree2);
		allData.add(tree3);
		allData.add(tree4);
		allData.add(tree5);
		allData.add(tree6);
		allData.add(tree7);
		allData.add(tree8);
		this.setNrOfClusters(3);
		kMedoids = new KMedoids(this.getNrOfClusters(), 100, allData);
		medoids = new ArrayList<ICluster>();
	}

	public Forest<V, E> getJungGraph() {
		this.createTestData();
		Map<ICluster, List<ICluster>> associationMap = kMedoids.calculate(false);
		Forest<V, E> toReturn = new DelegateTree<V,E> ();
		medoids = new ArrayList<ICluster>(associationMap.keySet());

		int i = 1;
		for (ICluster medoid : medoids) {
			toReturn.addVertex((V) new GraphVertex(medoid, associationMap.get(medoid), Integer.toString(i)));
			if (associationMap.get(medoid).size() > biggestCluster) {
				setBiggestCluster(associationMap.get(medoid).size());
			}
			i++;
		}
		return toReturn;
	}

	public Forest<V, E> getJungGraph(RootedTree rootedTree, jebl.evolution.graphs.Node startNode, int nrClusters) {
		
		//kMedoids = new KMedoids(this.getNrOfClusters(), 100, allData);
		//Map<ICluster, List<ICluster>> associationMap = kMedoids.calculate(true);
		DelegateTree<V,E> toReturn = null;
		setNrOfClusters(nrClusters);
		if(startNode == null) {
			midRootCluster = new MidRootCluster(this.getNrOfClusters(), rootedTree);
			toReturn = midRootCluster.calculate(rootedTree.getRootNode());
		} else {
			midRootCluster = new MidRootCluster(this.getNrOfClusters(), rootedTree);
			toReturn = midRootCluster.calculate(startNode);
		}
		//Graph<V,E> toReturn = new SparseGraph<V, E>();
		setBiggestCluster(midRootCluster.getBiggestCluster());
		
		//medoids = new ArrayList<ICluster>(associationMap.keySet());
//		for (ICluster medoid : medoids) {
//			V graphVertex = (V) new GraphVertex(medoid, associationMap.get(medoid), Integer.toString(i));
//			toReturn.addVertex(graphVertex);
//			if(previousGraphVertex != null) {
//				toReturn.addEdge((E)("Edge" + Integer.toString(i)), graphVertex, previousGraphVertex);
//			}
//			previousGraphVertex = graphVertex;
//			if (associationMap.get(medoid).size() > biggestCluster) {
//				setBiggestCluster(associationMap.get(medoid).size());
//			}
//			i++;

		return toReturn;
	}
	
//	public Forest<V, E> getJungGraph(List<ICluster> allData, int nrClusters) {
//		this.setNrOfClusters(nrClusters);
//		biggestCluster = 0;
//		//kMedoids = new KMedoids(nrClusters, 100, allData);
//
//		//Map<ICluster, List<ICluster>> associationMap = kMedoids.calculate(true);
//		
//		midRootCluster = new MidRootCluster(this.getNrOfClusters(), (RootedTree)treeJebl);
//		DelegateTree<V,E> toReturn = midRootCluster.calculate();
//		
//		RootedTree rootedTree = (RootedTree)treeJebl;
//		midRootCluster = new MidRootCluster(this.getNrOfClusters(), (RootedTree)treeJebl);
//		DelegateTree<V,E> toReturn = midRootCluster.calculate(rootedTree.getRootNode());
//		//Graph<V,E> toReturn = new SparseGraph<V, E>();
//		setBiggestCluster(midRootCluster.getBiggestCluster());
//		
//		//medoids = new ArrayList<ICluster>(associationMap.keySet());
//		int i = 1;
//		V previousGraphVertex = null;
//		
////		medoids = new ArrayList<ICluster>(associationMap.keySet());
////		Graph<V, E> toReturn = new SparseGraph<V, E>();
////		int i = 1;
////		V previousGraphVertex = null;
////		for (ICluster medoid : medoids) {
////			V graphVertex = (V) new GraphVertex(medoid, associationMap.get(medoid), Integer.toString(i));
////			toReturn.addVertex(graphVertex);
////			if(previousGraphVertex != null) {
////				toReturn.addEdge((E)("Edge" + Integer.toString(i)), graphVertex, previousGraphVertex);
////			}
////			previousGraphVertex = graphVertex;
////			if (associationMap.get(medoid).size() > biggestCluster) {
////				setBiggestCluster(associationMap.get(medoid).size());
////			}
////			i++;
////		}
//
//		return toReturn;
//	}

	public int getBiggestCluster() {
		return biggestCluster;
	}
	
	public jebl.evolution.trees.RootedTree getJeblTree() {
		return this.midRootCluster.getJeblTree();
	}

	public void setBiggestCluster(int biggestCluster) {
		this.biggestCluster = biggestCluster;
	}

	public int getNrOfClusters() {
		return nrOfClusters;
	}

	public void setNrOfClusters(int nrOfClusters) {
		this.nrOfClusters = nrOfClusters;
	}
}
