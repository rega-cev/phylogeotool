package be.kuleuven.rega.phylogeotool.distance.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.RootedTree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.distance.MidRootCluster;
import be.kuleuven.rega.phylogeotool.tree.Node;

public class MidRootClusterTest {
	
	private jebl.evolution.trees.RootedTree treeJebl = null;
	private MidRootCluster midRootCluster = null;
	
	@Before
	public void init() {
		
		String treeLocation = "/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.tre";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(treeLocation)));
			NewickImporter newickImporter = new NewickImporter(br, false);
			while (newickImporter.hasTree()) {
				treeJebl = (RootedTree)newickImporter.importNextTree();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
		midRootCluster = new MidRootCluster(10, treeJebl);
	}

	@Test
	public void testJeblToPhyloGeoNode() {
		Node node = midRootCluster.jeblToPhyloGeoNode(treeJebl.getRootNode(), null);
		Assert.assertEquals(68, node.getLeaves().size());
	}
	
}
