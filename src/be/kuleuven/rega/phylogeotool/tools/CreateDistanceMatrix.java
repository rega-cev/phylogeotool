package be.kuleuven.rega.phylogeotool.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jebl.evolution.trees.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;

public class CreateDistanceMatrix {

	private HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	private Tree tree = null;
	private List<Node> leafs = new ArrayList<Node>();
	
	public static void main(String[] args) {
		
//		String treeLocation = args[0];
//		String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/RAxML_bipartitions.fullPortugal.final.tree";
//		String treeLocation = "/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree";
		String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.solved.midpoint.newick";
//		String treeLocation = "/Users/ewout/git/phylogeotool/lib/Test/tree.phylo";
//		String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.500.midpoint.solved.newick";
//		String distanceMatrixLocation = args[1];
//		String distanceMatrixLocation = "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.EUResist.txt";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.test.other.txt";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.500.txt";
		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.EUResist.txt";
//		String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt";
		
		System.out.println("TreeLocation: " + treeLocation);
		System.out.println("Distance Matrix Location: " + distanceMatrixLocation);
		
		CreateDistanceMatrix createDistanceMatrix = new CreateDistanceMatrix();
		try {
			createDistanceMatrix.init(treeLocation);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			createDistanceMatrix.createDistanceMatrix(distanceMatrixLocation, new DistanceCalculateFromTree());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init(String treeLocation) throws FileNotFoundException {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		System.out.println(((SimpleRootedTree) jeblTree).getNodes().size());
		System.out.println(((SimpleRootedTree) jeblTree).getChildren(((SimpleRootedTree) jeblTree).getRootNode()));
		tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			leafs.add(leaf);
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		System.out.println("Size: " + translatedNodeNames.keySet().size());
	}
	
	public void createDistanceMatrix(String distanceMatrixLocation, DistanceInterface distanceInterface) throws IOException {
		FileWriter fw = new FileWriter(new File(distanceMatrixLocation));
		DecimalFormat df = new DecimalFormat("#.####");
		for(int i = 0; i < translatedNodeNames.keySet().size(); i++) {
			for(int j = 0; j < translatedNodeNames.keySet().size(); j++) {
				if(j >= i + 1) {
					fw.write(df.format(distanceInterface.getDistance(tree, leafs.get(i), leafs.get(j))) + ";");
				} else {
					fw.write(";");
				}
			}
			System.out.println("i: " + i);
			fw.write("\n");
		}
		fw.close();
	}
	
//	public void createDistanceMatrix(String distanceMatrixLocation) throws IOException {
//		FileWriter fw = new FileWriter(new File(distanceMatrixLocation));
//		DecimalFormat df = new DecimalFormat("#.###");
//		for(int i = 0; i < translatedNodeNames.keySet().size(); i++) {
//			for(int j = translatedNodeNames.keySet().size() - 1; j > 0; j--) {
//				if(j >= i + 1) {
//					fw.write(df.format(tree.getDistance(leafs.get(i), leafs.get(j))) + ";");
//				} else {
//					fw.write(";");
//				}
//			}
//			System.out.println("i: " + i);
//			fw.write("\n");
//		}
//		fw.close();
//	}
	
}
