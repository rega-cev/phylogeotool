package be.kuleuven.rega.phylogeotool.tools;

import java.io.IOException;
import java.io.Reader;

import jebl.evolution.io.ImportException;
import jebl.evolution.trees.Tree;

public class TreeTools {

	public TreeTools() {
	}

	public static Tree readNewickTree(Reader reader) {
		NewickImporter newickImporter = new NewickImporter(reader, false);
		try {
			if (newickImporter.hasTree()) {
				return newickImporter.importNextTree();
			} else {
				return null;
			}
		} catch (IOException e) {
			System.err.println(TreeTools.class.getName() + ": Treefile not found");
		} catch (ImportException e) {
			System.err.println(TreeTools.class.getName() + ": Could not import the tree");
		}
		return null;
	}
	
//	public Node getLeastCommonAncestor(Tree tree, Node node1, Node node2) {
//		
//	}
}
