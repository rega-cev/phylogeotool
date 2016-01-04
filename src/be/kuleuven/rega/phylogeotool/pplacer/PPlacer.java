package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;

public class PPlacer {
	
//	private String folderLocationNodeIds;
	private List<String> pplacerIds = null;
	private Map<String, String> neighbours = null;
	
//	public PPlacer(String folderLocationNodeIds, String folderLocationPPlacerIds, String pplacerId) {
////		this.folderLocationNodeIds = folderLocationNodeIds;
//		pplacerIds = new ArrayList<String>();
//		neighbours = new HashMap<String, String>();
//		if(pplacerId != null) {
//			this.read(folderLocationPPlacerIds + "/"+ pplacerId + ".xml");
//		}
//	}
	
	public PPlacer(String folderLocationNodeIds, String folderLocationPPlacerIds, String pplacerId) {
//		this.folderLocationNodeIds = folderLocationNodeIds;
		pplacerIds = new ArrayList<String>();
		neighbours = new HashMap<String, String>();
		if(pplacerId != null) {
			this.read(folderLocationPPlacerIds + "/"+ pplacerId + ".xml");
		}
	}
	
	public String getNeighbour(String pplacerNode) {
		return neighbours.get(pplacerNode);
	}
	
//	public Map<String, Boolean> clustersContainPPlacerSequence(List<Cluster> clusters) {
//		Map<Cluster, Boolean> toReturn = new HashMap<Cluster, Boolean>();
//		
//		for(Cluster cluster:clusters) {
//			for(String pplacerId:pplacerIds) {
//				if(!toReturn.containsKey(clusterId) || (toReturn.containsKey(clusterId) && !toReturn.get(clusterId))) {
//					toReturn.put(clusterId, clusterContainsPPlacerSequence(pplacerId, clusterId));
//				}
//			}
//		}
//		
//		return toReturn;
//	}
	
	public boolean clusterContainsPPlacerSequence(String clusterId) {
		for(String pplacerId:pplacerIds) {
			if(clusterContainsPPlacerSequence(pplacerId, clusterId)) {
				return true;
			}
		}
		return false;
	}
	
	// TODO: Update this method
	public boolean clusterContainsPPlacerSequence(String pplacerSequence, String clusterId) {
//		List<String> nodeIds = PreRendering.getNodeIdFromXML("", folderLocationNodeIds, clusterId, PreRendering.ID.NODEID);
//		if(nodeIds.contains(neighbours.get(pplacerSequence))) {
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}
	
	public static void write(String treeLocation, String folderLocationPPlacerIds, List<String> pplacerSequences) throws FileNotFoundException, UnsupportedEncodingException {
		jebl.evolution.trees.Tree jeblTree = null;
		Map<Node, Node> neighbours = new HashMap<Node, Node>();
		try {
			jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		} catch (FileNotFoundException e1) {
			System.err.println(PPlacer.class + ": PPlacer tree not found.");
			e1.printStackTrace();
		}
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, pplacerSequences);
		
		Node leaf;
		try {
			for(String introducedSequence: pplacerSequences) {
				leaf = tree.getLeafByLabel(introducedSequence);
				for(Node node:leaf.getParent().getImmediateChildren()) {
					if(!node.equals(leaf)) {
						neighbours.put(leaf, node);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrintWriter writer = new PrintWriter(folderLocationPPlacerIds, "UTF-8");
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<pplacer>\n");
		for(Node pplacerSeq:neighbours.keySet()) {
			writer.write("\t<node id=\"" + pplacerSeq.getId() + "\" label=\"" + pplacerSeq.getLabel() +"\">\n");
			writer.write("\t\t<neighbour id=\"" + neighbours.get(pplacerSeq).getId() + "\" />\n");
			writer.write("\t</node>\n");
		}
		writer.write("</pplacer>\n");
		writer.close();
	}
	
	// TODO: Move this method from here. It's not part of the core business of the PPlacer classes
	public void read(String folderLocationPPlacerIds) {
		if(!this.pplacerIds.isEmpty())
			this.pplacerIds = new ArrayList<String>();
		if(!neighbours.isEmpty())
			this.neighbours = new HashMap<String, String>();
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        if (new File(folderLocationPPlacerIds).exists()) {
	        try {
	            doc = builder.build(folderLocationPPlacerIds);
	        } catch (JDOMException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	
	        Element root = doc.getRootElement();
	
	        List<Element> children = root.getChildren("node");
	        
	        for(Element child:children) {
	        	pplacerIds.add(child.getAttributeValue("id"));
	        	neighbours.put(child.getAttributeValue("id"), child.getChild("neighbour").getAttributeValue("id"));
	        }
        } else {
        	// TODO: Create error message on the screen
	       System.err.println(PPlacer.class + ": " + folderLocationPPlacerIds + ", unknown file.");
	    }
	}

}
