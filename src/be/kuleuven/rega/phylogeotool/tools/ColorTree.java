package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.io.write.NexusExporter;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;

public class ColorTree {

	private jebl.evolution.trees.Tree jeblTree;
	
	public static void main(String[] args) {
		
		String treeLocation = null;
		int nrClusters = 0;
		int minimumClusterSize = 2;
		
		if(args.length > 1) {
			treeLocation = args[0];
			nrClusters = new Integer(args[1]);
		} else {
			System.err.println("java -jar ColorTree.jar phylo.tree nrClusters");
			System.exit(0);
		}
		
		ColorTree colorTree = new ColorTree();
		Tree tree;
		try {
			tree = colorTree.init(treeLocation);
			colorTree.colorTree(tree, nrClusters, minimumClusterSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Tree init(String treeLocation) throws FileNotFoundException {
		jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		return tree;
	}
	
	private void colorTree(Tree tree, int nrClusters, int minimumClusterSize) throws IOException {
		Cluster cluster = MidRootCluster.calculate(tree, tree.getRootNode(), new ClusterSizeComparator(tree), minimumClusterSize, nrClusters);
//		List<Node> clusters = cluster.getAcceptableClusters(minimumClusterSize);
		FileWriter fileWriter = new FileWriter("/Users/ewout/Documents/phylogeo/TestCases/Portugal/" + nrClusters + ".nxs");
		System.out.println("Writing to: " + "/Users/ewout/Documents/phylogeo/TestCases/Portugal/" + nrClusters + ".nxs");
		NexusExporter.export(cluster, jeblTree, fileWriter, 2, true);
	}
	
}
