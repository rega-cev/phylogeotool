package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.webapp.GraphProperties;

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
		jeblTree = ReadNexusTree.readNexusTree(new FileReader(treeLocation));
		
		Tree tree = ReadNexusTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		return tree;
	}
	
	private void colorTree(Tree tree, int nrClusters, int minimumClusterSize) throws IOException {
		GraphProperties graphProperties = new GraphProperties();
		ClusterAlgos clusterAlgos = new ClusterAlgos();
		
		Tree clusteredTree = clusterAlgos.getCluster(tree, tree.getRootNode(), nrClusters, minimumClusterSize);
		List<Node> clusters = clusteredTree.getAcceptableClusters(minimumClusterSize);
		tree = graphProperties.setNodeColor(tree, clusters);
		FileWriter fileWriter = new FileWriter(nrClusters + ".nxs");
		NexusExporter.export(tree, clusters, jeblTree, fileWriter);
	}
	
}
