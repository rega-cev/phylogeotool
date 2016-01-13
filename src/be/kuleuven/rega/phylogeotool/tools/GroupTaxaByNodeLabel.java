package be.kuleuven.rega.phylogeotool.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import jebl.evolution.graphs.Node;
import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.RootedTree;
import jebl.evolution.trees.Tree;

public class GroupTaxaByNodeLabel {
	public static void main(String [] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: CsvColorTree tree.[newick|nexus]");
			System.exit(0);
		}
		
		File treeFile = new File(args[0]);
		
		Tree tree = ReadTree.readTree(new FileReader(treeFile));
		
		List<Node> namedNodes = new ArrayList<Node>();
		for (Node n : tree.getNodes()) {
			if (getName(n) != null) {
				namedNodes.add(n);
			}
		}
		
		System.out.println("classification,taxon");
		for (Node n : namedNodes) {
			List<Taxon> taxa = listTaxa((RootedTree)tree, n);
			for (Taxon t : taxa) {
				System.out.println(getName(n) + "," + t.getName());
			}
		}
	}
	
	private static String getName(Node n) {
		return (String)n.getAttribute("!name");
	}
	
	private static List<Taxon> listTaxa(RootedTree tree, Node root) {
		List<Taxon> taxa = new ArrayList<Taxon>();
		
		if (tree.getTaxon(root) != null) {
			taxa.add(tree.getTaxon(root));
		} else {
			for (Node n : tree.getChildren(root)) {
				Taxon t = tree.getTaxon(n); 
				if (t == null) {
					taxa.addAll(listTaxa(tree, n));
				} else {
					taxa.add(t);
				}
			}
		}
		
		return taxa;
	}
}
