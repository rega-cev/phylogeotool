package be.kuleuven.rega.phylogeotool.io.write;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.treeFormatTransformer.NexusExporterFigTree;
import be.kuleuven.rega.webapp.GraphProperties;

public class NexusExporter {

	private final static Logger LOGGER = LoggerFactory.getLogger(NexusExporter.class);
	
	public static String export(Cluster cluster, jebl.evolution.trees.Tree jeblTree, Writer writer, int minimumClusterSize, boolean colorLeafs) {
		// To print nxs file
		
		Map<String, String> taxonNameToColor = new HashMap<String,String>();
		if(colorLeafs) {
			LOGGER.info("Sequences exported to file.");
			Map<Node,Color> clusterToColor = GraphProperties.getClusterColor(cluster, minimumClusterSize);
			
			for(Node node:cluster.getBoundaries()) {
				for(Node leaf:cluster.getTree().getLeaves(node)) {
					taxonNameToColor.put(leaf.getLabel(), "#" + String.format("%06x", clusterToColor.get(node).getRGB() & 0x00FFFFFF));
				}
			}
		}
	
		NexusExporterFigTree nexusExporterFigTree = null;
		try {
			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
			if(jeblTree != null) {
				nexusExporterFigTree.exportTree(jeblTree);
			} else {
				nexusExporterFigTree.exportTree(ReadTree.jeblTree);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	
//	public static String export(Tree fullTree, List<Node> clusters, jebl.evolution.trees.Tree jeblTree, Writer writer) {
//		// To print nxs file
//		Map<String, String> taxonNameToColor = new HashMap<String,String>();
//		LOGGER.info("Sequences exported to file.");
//		
//		for(Node cluster:clusters) {
//			Node node = fullTree.getNodeById(cluster.getId());
//			for(String leaf:node.getLeavesAsString()) {
//				taxonNameToColor.put(leaf, "#" + String.format("%06x", node.getColor().getRGB() & 0x00FFFFFF));
//			}
//		}
//	
//		NexusExporterFigTree nexusExporterFigTree = null;
//		try {
//			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
//			if(jeblTree != null) {
//				nexusExporterFigTree.exportTree(jeblTree);
//			} else {
//				nexusExporterFigTree.exportTree(ReadTree.jeblTree);
//			}
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return writer.toString();
//	}
}
