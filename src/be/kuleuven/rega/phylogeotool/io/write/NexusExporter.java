package be.kuleuven.rega.phylogeotool.io.write;

import java.awt.Color;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.RootedTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.rega.jebl.Utils;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.webapp.GraphProperties;

public class NexusExporter {

	private final static Logger LOGGER = LoggerFactory.getLogger(NexusExporter.class);
	
	public static void export(Cluster cluster, jebl.evolution.trees.Tree jeblTree, Writer writer, int minimumClusterSize, boolean colorLeafs) {
		// To print nxs file
		
		Map<String, Color> taxonNameToColor = new HashMap<String,Color>();
		if(colorLeafs) {
			LOGGER.info("Sequences exported to file.");
			Map<Node,Color> clusterToColor = GraphProperties.getClusterColor(cluster, minimumClusterSize);
			jebl.evolution.trees.RootedTree copyTree = Utils.copyTree((RootedTree) jeblTree);
			
			for(Node node:cluster.getBoundaries()) {
				for(Node leaf:cluster.getTree().getLeaves(node)) {
//					taxonNameToColor.put(leaf.getLabel(), "#" + String.format("%06x", clusterToColor.get(node).getRGB() & 0x00FFFFFF));
					taxonNameToColor.put(leaf.getLabel(), clusterToColor.get(node));
				}
			}
			
			for (Taxon taxon : copyTree.getTaxa()) {
				taxon.setAttribute("!color", taxonNameToColor.get(taxon.getName()));
				copyTree.getNode(taxon).setAttribute("!color", taxonNameToColor.get(taxon.getName()));
				jebl.evolution.graphs.Node parent = copyTree.getParent(copyTree.getNode(taxon));
				while (!copyTree.isRoot(parent) || parent.getAttribute("!color") != null) {
					parent.setAttribute("!color", taxonNameToColor.get(taxon.getName()));
					parent = copyTree.getParent(parent);
				}
			}
			
		}
	}
}
