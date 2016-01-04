package be.kuleuven.rega.phylogeotool.tree.distance.test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceMatrixDistance;

public class DistanceMatrixDistanceTest {
	
	private String treeLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.500.midpoint.solved.newick";
	private String distanceMatrixLocation = "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.500.txt";
	private Map<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
	private DistanceInterface distanceInterfaceMatrix;
	private DistanceInterface distanceInterfaceTree;
	private Tree tree;
	
	@Before
	public void init() {
		jebl.evolution.trees.Tree jeblTree = null;
		try {
			jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		} catch (FileNotFoundException e) {
			System.err.println("Tree not found");
			e.printStackTrace();
		}
		tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		distanceInterfaceMatrix = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
		distanceInterfaceTree = new DistanceCalculateFromTree();
	}
	
	@Test
	public void testDistanceMatrix() {
		assertEquals("", distanceInterfaceMatrix.getDistance(tree, tree.getLeafByLabel("121359_584452"), tree.getLeafByLabel("84958_583923")), distanceInterfaceTree.getDistance(tree, tree.getLeafByLabel("121359_584452"), tree.getLeafByLabel("84958_583923")), 0.0001);
	}

}
