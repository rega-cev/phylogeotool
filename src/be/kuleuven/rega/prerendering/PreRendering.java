package be.kuleuven.rega.prerendering;

import jam.controlpalettes.BasicControlPalette;
import jam.controlpalettes.ControlPalette;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jebl.evolution.io.ImportException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.tools.NexusExporter;
import be.kuleuven.rega.phylogeotool.tools.ReadNewickTree;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.phylogeotool.tree.test.ClusterDistance;
import be.kuleuven.rega.webapp.GraphProperties;
import be.kuleuven.rega.xml.XMLWriter;

import com.opencsv.CSVReader;
import com.thoughtworks.xstream.XStream;

import figtree.application.FigTreeNexusImporter;
import figtree.application.FigTreePanel;
import figtree.panel.SimpleLabelPainter;
import figtree.treeviewer.ExtendedTreeViewer;
import figtree.treeviewer.TreePane;
import figtree.treeviewer.treelayouts.RadialTreeLayout;

public class PreRendering {

	private XStream xStream = null;
	private String folderLocationClusters;
	private String folderLocationCsvs;
	private String folderLocationTreeView;
	private String folderLocationLeafIds;
	private String folderLocationNodeIds;
	public final static int CONTROL_PALETTE_WIDTH = 200;
	public static enum ID {
		LEAFID, NODEID
	}
	
	public PreRendering(String folderLocationClusters, String folderLocationCsvs, String folderLocationTreeView, String folderLocationLeafIds, String folderLocationNodeIds) {
		this.xStream = new XStream();
		this.xStream.alias("tree", Tree.class);
		this.xStream.alias("node", Node.class);
		this.xStream.alias("edge", Edge.class);
//		xStream.setMode(XStream.ID_REFERENCES);
		this.folderLocationClusters = folderLocationClusters;
		this.folderLocationCsvs = folderLocationCsvs;
		this.folderLocationTreeView = folderLocationTreeView;
		this.folderLocationLeafIds = folderLocationLeafIds;
		this.folderLocationNodeIds = folderLocationNodeIds;
	}
	
	public void writeTreeToXML(Tree tree) {
		String xml = xStream.toXML(tree);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(this.folderLocationClusters + File.separator + tree.getRootNode().getId() + ".xml"));
			fileWriter.write(xml);
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(PreRendering.class + " : Error with writing the tree " + tree.getRootNode().getId() + " to an xml file.");
		}
	}
	
	public void writeNodeIdsToXML(Tree tree, Tree tempTree, ID id) {
		FileWriter fileWriter = null;
		List<Node> ids = null;
		try {
			switch(id) {
				case LEAFID:
					fileWriter = new FileWriter(new File(this.folderLocationLeafIds + File.separator + tempTree.getRootNode().getId() + ".xml"));
					ids = tree.getNodeById(tempTree.getRootNode().getId()).getLeaves();
					break;
				case NODEID:
					fileWriter = new FileWriter(new File(this.folderLocationNodeIds + File.separator + tempTree.getRootNode().getId() + ".xml"));
					ids = tree.getNodeById(tempTree.getRootNode().getId()).getAllChildren();
					break;
			}
			fileWriter.write("<xml>" + "\n");
			
			StringBuffer stringBuffer = new StringBuffer();
			for(Node node:ids) {
				switch(id) {
					case LEAFID:
						stringBuffer.append(node.getLabel() + ",");
						break;
					case NODEID:
						stringBuffer.append(node.getId() + ",");
						break;
				}
			}
			
			String nodeIds = stringBuffer.toString();
			if(nodeIds.length() > 0) {
				fileWriter.write("\t<tree id=\"nodeIds\">" + nodeIds.substring(0, nodeIds.length()-1) + "</tree>" + "\n");
			} else {
				fileWriter.write("\t<tree id=\"nodeIds\"></tree>" + "\n");
			}
			fileWriter.write("</xml>");
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(PreRendering.class + " : Error with writing the tree to an xml file.");
		}
	}
	
	public static List<String> getNodeIdFromXML(String folderLocationLeafIds, String folderLocationNodeIds, String clusterId, ID id) {
		List<String> idsList = new ArrayList<String>();
		String[] ids = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			switch(id) {
				case LEAFID:
					doc = dBuilder.parse(new File(folderLocationLeafIds + File.separator + clusterId + ".xml"));
					break;
				case NODEID:
					doc = dBuilder.parse(new File(folderLocationNodeIds + File.separator + clusterId + ".xml"));
					break;
			}
			
			NodeList nList = doc.getElementsByTagName("tree");
		
			for (int temp = 0; temp < nList.getLength(); temp++) {
				org.w3c.dom.Node nNode = nList.item(temp);
				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					if(eElement.getAttribute("id").equals("nodeIds")) {
						ids = eElement.getTextContent().split(",");
					}
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		idsList.addAll(Arrays.asList(ids));
		return idsList;
	}
	
	public Tree getTreeFromXML(String clusterId) {
		Tree newTree = null;
//			byte[] encoded = Files.readAllBytes(Paths.get(folderLocation + File.separator + clusterId + ".xml"));
//			newTree = (Tree)xStream.fromXML(new String(encoded));
		newTree = (Tree)xStream.fromXML(new File(folderLocationClusters + File.separator + clusterId + ".xml"));
		return newTree;
	}
	
	public void preRender(String treeLocation, String csvLocation, String distanceMatrixLocation) {
//		if(checkFoldersEmpty()) {
			try {
				jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader(treeLocation));
				// TODO: Check if the arrayList is still necessary
//				Tree tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
				ClusterDistance clusterDistance = new ClusterDistance();
				Tree tree = null;
				try {
					tree = clusterDistance.init(treeLocation, distanceMatrixLocation);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				Tree tempTree = clusterDistance.getBestClustering(tree, tree.getRootNode());
//				ClusterAlgos clusterAlgos = new ClusterAlgos();
				GraphProperties graphProperties = new GraphProperties();
				// TODO: Dynamically set the number of clusters
				LinkedList<Node> toDo = new LinkedList<Node>();
				toDo.add(tree.getRootNode());
				Node currentNode;
				while(toDo.peek() != null) {
					currentNode = toDo.pop();
					Tree tempTree = clusterDistance.getBestClustering(tree, currentNode);
//					Tree tempTree = clusterAlgos.getCluster(tree,tree.getNodeById(currentNode.getId()), 12);
					this.writeTreeToXML(tempTree);
					this.prepareCSV(currentNode.getId(), tree.getNodeById(currentNode.getId()).getLeavesAsString(), csvLocation);
					if(tempTree.getNodes().size() > 1) {
						// TODO: Set minimumClusterSize
						graphProperties.setNodeColor(tempTree, 2);
						this.prepareFullTreeView(currentNode.getId(), tree, tempTree, jeblTree);
					}
					this.writeNodeIdsToXML(tree, tempTree, ID.LEAFID);
					this.writeNodeIdsToXML(tree, tempTree, ID.NODEID);
					
					List<Node> nodesList = new ArrayList<Node>(tempTree.getNodes());
					for(Node node:nodesList) {
						if(node.getImmediateChildren().size() == 0 && node.getSize() > 1) {
							toDo.add(tree.getNodeById(node.getId()));
						}
					}
					
//					toDo.addAll(tempTree.getLeaves());
				}
			} catch (FileNotFoundException e) {
				System.err.println(PreRendering.class + ": " + "The tree file cannot be found in the given location.");
			}
//		} else {
//			System.err.println("The folders seems to have files in them. Maybe they are hidden? Please empty the folders first.");
//			System.err.println("Check paths: ");
//			System.err.println(folderLocationClusters);
//			System.err.println(folderLocationCsvs);
//			System.exit(0);
//		}
	}
	
	public boolean checkFoldersEmpty() {
		File folder = new File(folderLocationClusters);
		File folderCsvs = new File(folderLocationCsvs);
		File folderTreeView = new File(folderLocationTreeView);
		File folderLeafIds = new File(folderLocationLeafIds);
		File folderNodeIds = new File(folderLocationNodeIds);
		
		if(folder.isDirectory() && folderCsvs.isDirectory() && folderTreeView.isDirectory() && folderLeafIds.isDirectory() && folderNodeIds.isDirectory()){
			if(folder.list().length>0 || folderCsvs.list().length>0 || folderTreeView.list().length>0 || folderLeafIds.list().length>0 || folderNodeIds.list().length>0){
				return false;
			} else {
				return true;
			}
		} else if(folder.isFile()) {
			System.out.println(PreRendering.class + ": The path " + folderLocationClusters + " to the folder seems to direct to a file.");
			return false;
		} else if(folderCsvs.isFile()) { 
			System.out.println(PreRendering.class + ": The path " + folderLocationCsvs + " to the folder seems to direct to a file.");
			return false;
		} else if(folderTreeView.isFile()) {
			System.out.println(PreRendering.class + ": The path " + folderLocationTreeView + " to the folder seems to direct to a file.");
			return false;
		} else if(folderLeafIds.isFile()) {
			System.out.println(PreRendering.class + ": The path " + folderLocationLeafIds + " to the folder seems to direct to a file.");
			return false;
		} else if(folderNodeIds.isFile()) {
			System.out.println(PreRendering.class + ": The path " + folderLocationNodeIds + " to the folder seems to direct to a file.");
			return false;
		} else {
			folder.mkdirs();
			folderCsvs.mkdirs();
			folderTreeView.mkdirs();
			folderLeafIds.mkdirs();
			folderNodeIds.mkdirs();
			return true;
		}
	}
	
	public void prepareCSV(int clusterId, List<String> ids, String csvLocation) {
		CSVReader csvReader;
		try {
			csvReader = new CSVReader(new FileReader(new File(csvLocation)), ';');
			String[] header = csvReader.readNext();
			FileWriter fileWriter = new FileWriter(new File(this.folderLocationCsvs + File.separator + clusterId + ".xml"));
			fileWriter.write("<xml>" + "\n");
			for(String key:header) {
				if(!key.equalsIgnoreCase("id")) {
					HashMap<String,Integer> tempHashMap = CsvUtils.csvToHashMapStringInteger(new File(csvLocation), ';', ids, key);
					for(String hashMapKey:tempHashMap.keySet()) {
						fileWriter.write("\t<" + key + " id=\"" + hashMapKey + "\">" + tempHashMap.get(hashMapKey) + "</" + key + ">" + "\n");
					}
				}
			}
			fileWriter.write("</xml>");
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.err.println(XMLWriter.class + ": " + "File could not be found.");
		} catch (IOException e) {
			System.err.println(XMLWriter.class + ": " + "IOException in the CSVReader.");
		}
	}
	
	public void prepareFullTreeView(int clusterId, Tree fullTree, Tree clusteredTree, jebl.evolution.trees.Tree jeblTree) {
		
		StringWriter stringWriter = null;
		try {
//			FileWriter fileWriter = new FileWriter(new File(this.folderLocationTreeView + File.separator + clusterId + ".nexus"));
			stringWriter = new StringWriter();
			String export = NexusExporter.export(fullTree, clusteredTree, jeblTree, stringWriter);
//			fileWriter.write(stringWriter.toString());
//			fileWriter.close();
//			System.out.println(export);
			
			Map<String, Object> settings = new HashMap<String, Object>();
	
	        ExtendedTreeViewer treeViewer = new ExtendedTreeViewer();
	        ControlPalette controlPalette = new BasicControlPalette(CONTROL_PALETTE_WIDTH, BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
	        // This is important for the coloring.
	        new FigTreePanel(null, treeViewer, controlPalette);
	        // First of all, fully populate the settings map so that
	        // all the settings have defaults
	        controlPalette.getSettings(settings);
	
	        List<jebl.evolution.trees.Tree> trees = new ArrayList<jebl.evolution.trees.Tree>();
	
	        FigTreeNexusImporter importer = new FigTreeNexusImporter(new StringReader(export));
	        trees.add(importer.importNextTree());
	
	            // Try to find a figtree block and if found, parse the settings
	        while (true) {
	        	try {
	        		importer.findNextBlock();
	                if (importer.getNextBlockName().equalsIgnoreCase("FIGTREE")) {
	                    importer.parseFigTreeBlock(settings);
	                }
	            } catch (EOFException ex) {
	               break;
	            }
	        }
	
	        treeViewer.setTrees(trees);
	        controlPalette.setSettings(settings);
	        
	        int width = 650;
	        int height = 650;
	        treeViewer.getContentPane().setSize(width, height);
	
	        JComponent comp = treeViewer.getContentPane();
	        ((TreePane)comp).setTreeLayout(new RadialTreeLayout());
	        SimpleLabelPainter simpleLabelPainter = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
	        simpleLabelPainter.setVisible(false);
	        ((TreePane)comp).setTipLabelPainter(simpleLabelPainter);
	        
	        ((TreePane)comp).setBranchStroke(new BasicStroke(2.0f));
	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics g = bi.createGraphics();
	        comp.paint(g);
	        g.dispose();
	        
	        File file = new File(this.folderLocationTreeView + File.separator + clusterId + ".png");
			ImageIO.write(bi, "PNG", file);
        } catch(IOException ioe) {
        	throw new RuntimeException("Error writing graphic file: " + ioe);
        } catch (ImportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> readCsv(int clusterId, String key, boolean readNA) {
		HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			System.out.println(new File(folderLocationCsvs + File.separator + clusterId + ".xml"));
			if(folderLocationCsvs != null && !folderLocationCsvs.equals("") && new File(folderLocationCsvs).list().length>0) {
				Document doc = dBuilder.parse(new File(folderLocationCsvs + File.separator + clusterId + ".xml"));
				NodeList nList = doc.getElementsByTagName(key);
			
				for (int temp = 0; temp < nList.getLength(); temp++) {
					org.w3c.dom.Node nNode = nList.item(temp);
					if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(!readNA && !eElement.getAttribute("id").equals("XX") && !eElement.getAttribute("id").equals("X1") && !eElement.getAttribute("id").equals("X2") &&
								!eElement.getAttribute("id").equals("X3") && !eElement.getAttribute("id").equals("X4") && !eElement.getAttribute("id").equals("X5") &&
								!eElement.getAttribute("id").equals("X6") && !eElement.getAttribute("id").equals("")) {
							hashMap.put(eElement.getAttribute("id"), Integer.parseInt(eElement.getTextContent()));
						} else if(readNA) {
							hashMap.put(eElement.getAttribute("id"), Integer.parseInt(eElement.getTextContent()));
						}
	//					System.out.println("Subtype : " + eElement.getAttribute("id"));
	//					System.out.println("Value : " + eElement.getTextContent());
					}
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return hashMap;
	}
	
	public static void main(String[] args) {
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/portugal/clusters", "/Users/ewout/Documents/phylogeo/portugal/xml", "/Users/ewout/Documents/phylogeo/portugal/treeview", "/Users/ewout/Documents/phylogeo/portugal/leafIds", "/Users/ewout/Documents/phylogeo/portugal/nodeIds");
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/portugal/Test/clusters", "/Users/ewout/Documents/phylogeo/portugal/Test/xml", "/Users/ewout/Documents/phylogeo/portugal/Test/treeview", "/Users/ewout/Documents/phylogeo/portugal/Test/leafIds", "/Users/ewout/Documents/phylogeo/portugal/Test/nodeIds");
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/EUResist_New/clusters", "/Users/ewout/Documents/phylogeo/EUResist_New/xml", "/Users/ewout/Documents/phylogeo/EUResist_New/treeview");
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Test/clusters", "/Users/ewout/Documents/phylogeo/Test/xml", "/Users/ewout/Documents/phylogeo/Test/treeview", "/Users/ewout/Documents/phylogeo/Test/leafIds");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick", "/Users/ewout/Documents/phylogeo/EUResist_New/EUResist.metadata.csv");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick", "/Users/ewout/Documents/phylogeo/EUResist/EUResist.metadata.cleaned.csv");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/portugal/RAxML_bipartitions.final_tree", "/Users/ewout/Documents/phylogeo/portugal/final.csv");
//		preRendering.preRender("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.tree", "/Users/ewout/git/phylogeotool/lib/EwoutTrees/temp.csv");
//		preRendering.getLeafIdFromXML("1");
	
		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Configs/Portugal/clusters", "/Users/ewout/Documents/phylogeo/Configs/Portugal/xml", "/Users/ewout/Documents/phylogeo/Configs/Portugal/treeview", "/Users/ewout/Documents/phylogeo/Configs/Portugal/leafIds", "/Users/ewout/Documents/phylogeo/Configs/Portugal/nodeIds");
		preRendering.preRender("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree", "/Users/ewout/Documents/TDRDetector/fullPortugal/allSequences_cleaned_ids.csv", "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.midpoint.txt");
//		preRendering.preRender("/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.500.midpoint.solved.newick", "", "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.500.txt");
	}
}
