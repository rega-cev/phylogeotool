package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jebl.evolution.trees.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;

public class PPlacer {

	private List<String> pplacerSequences = null;
	private Map<Node, Node> neighbours = null;
	private Tree tree;

	public PPlacer(String pplacedTreeLocation, List<String> pplacerSequences) {
		this.pplacerSequences = pplacerSequences;
		this.neighbours = getNeighbourSeqs(pplacedTreeLocation, pplacerSequences);
	}

	public Node getNeighbour(Node pplacerNode) {
		return neighbours.get(pplacerNode);
	}

	public boolean clusterContainsPPlacerSequence(Cluster cluster) {
//		System.out.println("Here: " + cluster.getRootId());
//		for (Node node:neighbours.values()) {
//			if (cluster != null && cluster.getTree().getAllNodes(cluster.getRoot()).contains(node)) {
////				System.out.println("Label: " + node.getId());
//				return true;
//			}
//		}
		
		for(Entry<Node, Node> neighbour:neighbours.entrySet()) {
			for(Node node:cluster.getTree().getAllNodes(cluster.getRoot())) {
				if(node.getId() == neighbour.getValue().getId()) {
					return true;
				}
			}
		}
		
		return false;
	}

	private Map<Node, Node> getNeighbourSeqs(String locationTree, List<String> pplacedSequences) {
		jebl.evolution.trees.Tree jeblTree = null;
		Map<Node, Node> neighbours = new HashMap<Node, Node>();
		try {
			jeblTree = ReadTree.readTree(new FileReader(locationTree));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, pplacedSequences);

		Node leaf;
		for (String introducedSequence : pplacedSequences) {
			leaf = tree.getLeafByLabel(introducedSequence);
			for (Node node : leaf.getParent().getImmediateChildren()) {
				if (!node.equals(leaf)) {
					neighbours.put(leaf, ReadTree.getTreeDrawTree().getLeastCommonAncestor(tree.getLeavesAsString(node)));
				}
			}
		}
		return neighbours;
	}
	
	public Map<Node, Node> getNeighbourSeqs() {
		return this.neighbours;
	}
	
	public static List<String> getPPlacerIds(String location) {
		List<String> pplacerIds = new ArrayList<String>();
		File file = new File(location + File.separator + "sequences.fasta");
		BufferedReader bufferedReader;
		try {
			String line;
			bufferedReader = new BufferedReader(new FileReader(file));
			while((line = bufferedReader.readLine()) != null) {
				if(line.startsWith(">")) {
					pplacerIds.add(line.substring(1).trim());
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pplacerIds;
	}

	public Tree getTree() {
		return this.tree;
	}
	
	public static void main(String[] args) {
		// Test situation
		List<String> pplacedIds = new ArrayList<String>();
		pplacedIds.add("TEST_1");
		pplacedIds.add("TEST_2");
		pplacedIds.add("TEST_3");
		PPlacer pplacer = new PPlacer("/var/folders/0q/jfc96hd17xzfy6ck_ghw9grh0000gn/T/pplacer.CnHlwGf6/sequences.tog.tre", pplacedIds);
		Map<Node, Node> neighbours = pplacer.getNeighbourSeqs("/var/folders/0q/jfc96hd17xzfy6ck_ghw9grh0000gn/T/pplacer.CnHlwGf6/sequences.tog.tre", pplacedIds);
		for(Entry<Node, Node> entry:neighbours.entrySet()) {
//			System.out.println("Key: " + entry.getKey().getLabel());
//			for(Node node:entry.getValue()) {
//				System.out.println(node.getLabel());
//			}
			System.out.println(entry.getKey().getLabel() + " neighbour: " + entry.getValue().getId());
		}
	}
}
