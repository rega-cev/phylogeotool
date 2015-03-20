package be.kuleuven.rega.phylogeotool.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.Tree;
import be.kuleuven.rega.treeFormatTransformer.NexusExporterFigTree;

public class NexusExporter {

	public static void export(Tree tree, HashMap<Node, Color> mappedColorNode) {
		// To print nxs file
		Map<String, String> taxonNameToColor = new HashMap<String,String>();
		System.out.println("Sequences exported to file.");
		for(Node node:mappedColorNode.keySet()) {
			taxonNameToColor.put(tree.getTaxon(node).getName(), "#" + String.format("%06x", mappedColorNode.get(node).getRGB() & 0x00FFFFFF));
		}
	
		NexusExporterFigTree nexusExporterFigTree = null;
		Writer writer = null;
		try {
			writer = new FileWriter(new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.nxs"));
			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
			nexusExporterFigTree.exportTree(tree);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
