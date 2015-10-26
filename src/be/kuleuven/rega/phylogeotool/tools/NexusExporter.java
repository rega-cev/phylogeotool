package be.kuleuven.rega.phylogeotool.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.treeFormatTransformer.NexusExporterFigTree;

public class NexusExporter {

	private final static Logger LOGGER = LoggerFactory.getLogger(NexusExporter.class);
	
	public static String export(Tree fullTree, Tree clusteredTree, jebl.evolution.trees.Tree jeblTree, Writer writer) {
		// To print nxs file
		Map<String, String> taxonNameToColor = new HashMap<String,String>();
		LOGGER.info("Sequences exported to file.");
		for(Node node:clusteredTree.getRootNode().getLeaves()) {
			Node tempNode = fullTree.getNodeById(node.getId());
			for(String leaf:tempNode.getLeavesAsString()) {
//				System.out.println("Leaf: " + leaf);
//				System.out.println(node.getColor());
				taxonNameToColor.put(leaf, "#" + String.format("%06x", node.getColor().getRGB() & 0x00FFFFFF));
//				System.out.println(leaf + "#" + String.format("%06x", node.getColor().getRGB() & 0x00FFFFFF));
			}
		}
	
		NexusExporterFigTree nexusExporterFigTree = null;
		try {
			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
			if(jeblTree != null) {
				nexusExporterFigTree.exportTree(jeblTree);
			} else {
				nexusExporterFigTree.exportTree(ReadNewickTree.jeblTree);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	
	public static String export(Tree fullTree, List<Node> clusters, jebl.evolution.trees.Tree jeblTree, Writer writer) {
		// To print nxs file
		Map<String, String> taxonNameToColor = new HashMap<String,String>();
		LOGGER.info("Sequences exported to file.");
		
		for(Node cluster:clusters) {
			Node node = fullTree.getNodeById(cluster.getId());
			for(String leaf:node.getLeavesAsString()) {
				taxonNameToColor.put(leaf, "#" + String.format("%06x", node.getColor().getRGB() & 0x00FFFFFF));
			}
		}
	
		NexusExporterFigTree nexusExporterFigTree = null;
		try {
			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
			if(jeblTree != null) {
				nexusExporterFigTree.exportTree(jeblTree);
			} else {
				nexusExporterFigTree.exportTree(ReadNewickTree.jeblTree);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
}
