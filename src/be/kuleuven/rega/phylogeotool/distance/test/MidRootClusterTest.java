package be.kuleuven.rega.phylogeotool.distance.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;

import org.junit.Before;

import be.kuleuven.rega.phylogeotool.distance.MidRootCluster;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;

public class MidRootClusterTest {
	
	private SimpleRootedTree treeJebl = null;
	private MidRootCluster midRootCluster = null;
	
	@Before
	public void init() {
		
		String treeLocation = "/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.tre";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(treeLocation)));
			NewickImporter newickImporter = new NewickImporter(br, false);
			while (newickImporter.hasTree()) {
				treeJebl = (SimpleRootedTree)newickImporter.importNextTree();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
		midRootCluster = new MidRootCluster(10);
	}
}
