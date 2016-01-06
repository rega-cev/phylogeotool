package be.kuleuven.rega.prerendering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.io.write.NexusExporter;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceMatrixDistance;

import com.opencsv.CSVReader;
import com.thoughtworks.xstream.XStream;

public class PreRendering {

	private XStream xStream = null;
	private String folderLocationTree;
	private String folderLocationClusters;
	private String folderLocationCsvs;
	private String folderLocationTreeView;
//	private String folderLocationLeafIds;
//	private String folderLocationNodeIds;
	public final static int CONTROL_PALETTE_WIDTH = 200;
	public static enum ID {
		LEAFID, NODEID
	}
	
	public PreRendering(String folderLocationTree, String folderLocationClusters, String folderLocationCsvs, String folderLocationTreeView, String folderLocationLeafIds, String folderLocationNodeIds) {
		this.xStream = new XStream();
		this.xStream.alias("cluster", Cluster.class);
		this.xStream.alias("tree", Tree.class);
		this.xStream.alias("node", Node.class);
		this.xStream.alias("edge", Edge.class);
		this.xStream.omitField(Cluster.class, "tree");
		this.xStream.omitField(Cluster.class, "root");
		this.xStream.omitField(Cluster.class, "boundaries");
//		xStream.setMode(XStream.ID_REFERENCES);
		this.folderLocationTree = folderLocationTree;
		this.folderLocationClusters = folderLocationClusters;
		this.folderLocationCsvs = folderLocationCsvs;
		this.folderLocationTreeView = folderLocationTreeView;
//		this.folderLocationLeafIds = folderLocationLeafIds;
//		this.folderLocationNodeIds = folderLocationNodeIds;
	}
	
	public void writeTreeToXML(Tree tree) {
		String xml = xStream.toXML(tree);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(this.folderLocationTree + File.separator + tree.getRootNode().getId() + ".xml"));
			fileWriter.write(xml);
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(PreRendering.class + " : Error with writing the tree " + tree.getRootNode().getId() + " to an xml file.");
		}
	}
	
	public void writeClusterToXML(Cluster cluster) {
		String xml = xStream.toXML(cluster);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(this.folderLocationClusters + File.separator + cluster.getRoot().getId() + ".xml"));
			fileWriter.write(xml);
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(PreRendering.class + " : Error with writing the cluster " + cluster.getRoot().getId() + " to an xml file.");
		}
	}
	
//	public void writeNodeIdsToXML(Cluster cluster, ID id) {
//		FileWriter fileWriter = null;
//		List<Node> ids = null;
//		try {
//			switch(id) {
//				case LEAFID:
//					fileWriter = new FileWriter(new File(this.folderLocationLeafIds + File.separator + cluster.getRoot().getId() + ".xml"));
//					ids = cluster.getTree().getLeaves(cluster.getRoot());
//					break;
//				// TODO: Has to be updates to all the inner nodes
//				case NODEID:
//					fileWriter = new FileWriter(new File(this.folderLocationNodeIds + File.separator + cluster.getRoot().getId() + ".xml"));
//					ids = cluster.getAllNodes();
//					break;
//			}
//			fileWriter.write("<xml>" + "\n");
//			
//			StringBuffer stringBuffer = new StringBuffer();
//			for(Node node:ids) {
//				switch(id) {
//					case LEAFID:
//						stringBuffer.append(node.getLabel() + ",");
//						break;
//					case NODEID:
//						stringBuffer.append(node.getId() + ",");
//						break;
//				}
//			}
//			
//			String nodeIds = stringBuffer.toString();
//			if(nodeIds.length() > 0) {
//				fileWriter.write("\t<tree id=\"nodeIds\">" + nodeIds.substring(0, nodeIds.length()-1) + "</tree>" + "\n");
//			} else {
//				fileWriter.write("\t<tree id=\"nodeIds\"></tree>" + "\n");
//			}
//			fileWriter.write("</xml>");
//			fileWriter.close();
//		} catch (IOException e) {
//			System.err.println(PreRendering.class + " : Error with writing the tree to an xml file.");
//		}
//	}
	
//	public static List<String> getNodeIdFromXML(String folderLocationLeafIds, String folderLocationNodeIds, String clusterId, ID id) {
//		List<String> idsList = new ArrayList<String>();
//		String[] ids = null;
//		try {
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = null;
//			switch(id) {
//				case LEAFID:
//					doc = dBuilder.parse(new File(folderLocationLeafIds + File.separator + clusterId + ".xml"));
//					break;
//				case NODEID:
//					doc = dBuilder.parse(new File(folderLocationNodeIds + File.separator + clusterId + ".xml"));
//					break;
//			}
//			
//			NodeList nList = doc.getElementsByTagName("tree");
//		
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				org.w3c.dom.Node nNode = nList.item(temp);
//				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
//					Element eElement = (Element) nNode;
//					
//					if(eElement.getAttribute("id").equals("nodeIds")) {
//						ids = eElement.getTextContent().split(",");
//					}
//				}
//			}
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//		idsList.addAll(Arrays.asList(ids));
//		return idsList;
//	}
	
	public Tree getTreeFromXML(String treeId) {
		Tree newTree = null;
//			byte[] encoded = Files.readAllBytes(Paths.get(folderLocation + File.separator + clusterId + ".xml"));
//			newTree = (Tree)xStream.fromXML(new String(encoded));
		newTree = (Tree)xStream.fromXML(new File(folderLocationTree + File.separator + treeId + ".xml"));
		return newTree;
	}
	
	public Cluster getClusterFromXML(String clusterId) {
		Cluster cluster = null;
//			byte[] encoded = Files.readAllBytes(Paths.get(folderLocation + File.separator + clusterId + ".xml"));
//			newTree = (Tree)xStream.fromXML(new String(encoded));
		cluster = (Cluster)xStream.fromXML(new File(folderLocationClusters + File.separator + clusterId + ".xml"));
		Tree tree = getTreeFromXML("1");
		return new Cluster(tree, cluster.getRootId(), cluster.getBoundariesIds());
	}
	
	public Cluster getClusterFromXML(Tree tree, String clusterId) {
		Cluster cluster = null;
//			byte[] encoded = Files.readAllBytes(Paths.get(folderLocation + File.separator + clusterId + ".xml"));
//			newTree = (Tree)xStream.fromXML(new String(encoded));
		cluster = (Cluster)xStream.fromXML(new File(folderLocationClusters + File.separator + clusterId + ".xml"));
		return new Cluster(tree, cluster.getRootId(), cluster.getBoundariesIds());
	}
	
	public void preRender(String treeLocation, String csvLocation, String distanceMatrixLocation) throws IOException {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		int minimumClusterSize = 2;
		this.writeTreeToXML(tree);
		HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
		DistanceInterface distanceInterface = null;
		boolean showNA = true;
		
		int index = 0;
		for (Node leaf : tree.getLeaves()) {
			translatedNodeNames.put(leaf.getLabel(), index++);
		}
		
		if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
			distanceInterface = new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
		} else {
			distanceInterface = new DistanceCalculateFromTree();
		}
		
		LinkedList<Node> toDo = new LinkedList<Node>();
		toDo.add(tree.getRootNode());
//		toDo.add(tree.getNodeById(12996));
		Node currentNode;
		
		while(toDo.peek() != null) {
			currentNode = toDo.pop();
			// Do multi thread here
			Cluster cluster = BestClusterMultiThread.getBestCluster(minimumClusterSize, 50, 2, tree, currentNode, distanceInterface);
			if(cluster != null) {
	//			Cluster cluster = MidRootCluster.calculate(tree, tree.getRootNode(), new ClusterSizeComparator(tree), minimumClusterSize, 11);
	//			Tree tempTree = clusterAlgos.getCluster(tree,tree.getNodeById(currentNode.getId()), 12);
				this.writeClusterToXML(cluster);
				this.prepareCSV(cluster.getRoot().getId(), tree.getLeaves(cluster.getRoot()), csvLocation, showNA);
				NexusExporter.export(cluster, jeblTree, new FileWriter(new File(this.folderLocationTreeView + File.separator + cluster.getRoot().getId() + ".nexus")), minimumClusterSize, true);
	//			this.writeNodeIdsToXML(cluster, ID.LEAFID);
	//			this.writeNodeIdsToXML(cluster, ID.NODEID);
				
				List<Node> nodesList = cluster.getBoundaries();
				for(Node node:nodesList) {
					// Inner node
					if(node.getImmediateChildren().size() != 0) {
						toDo.add(tree.getNodeById(node.getId()));
					// Leaf
					} else {
						// What do we want to do with leaves?
					}
				}
			}
			
//			toDo.addAll(tempTree.getLeaves());
//			break;
		}
	}
	
//	public void preRender(String treeLocation, String csvLocation, String distanceMatrixLocation) {
////		if(checkFoldersEmpty()) {
//		HashMap<String, Integer> translatedNodeNames = new HashMap<String, Integer>();
//			try {
//				jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
//				Tree tree = null;
//				int index = 0;
//				for (Node leaf : tree.getLeaves()) {
//					translatedNodeNames.put(leaf.getLabel(), index++);
//				}
//				if(distanceMatrixLocation != null && !distanceMatrixLocation.equals("")) {
//					new DistanceMatrixDistance(translatedNodeNames, distanceMatrixLocation);
//				}
//				tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
//				this.writeTreeToXML(tree);
//				
//				
//				// TODO: Check if the arrayList is still necessary
////				Tree tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
//				ClusterDistance clusterDistance = new ClusterDistance(treeLocation, distanceMatrixLocation);
////				Tree tree = null;
////				try {
////					tree = clusterDistance.init(treeLocation, distanceMatrixLocation);
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
////				Tree tempTree = clusterDistance.getBestClustering(tree, tree.getRootNode());
////				ClusterAlgos clusterAlgos = new ClusterAlgos();
//				GraphProperties graphProperties = new GraphProperties();
//				// TODO: Dynamically set the number of clusters
//				LinkedList<Node> toDo = new LinkedList<Node>();
//				toDo.add(tree.getRootNode());
//				Node currentNode;
//				while(toDo.peek() != null) {
//					currentNode = toDo.pop();
//					Cluster cluster = clusterDistance.getBestClustering(tree, currentNode);
////					Tree tempTree = clusterAlgos.getCluster(tree,tree.getNodeById(currentNode.getId()), 12);
//					this.writeClusterToXML(cluster);
//					this.prepareCSV(currentNode.getId(), tree.getNodeById(currentNode.getId()).getLeavesAsString(), csvLocation);
//					if(cluster.getAcceptableClusters(2).size() > 1) {
//						// TODO: Set minimumClusterSize
//						graphProperties.getClusterColor(cluster, 2);
////						this.prepareFullTreeView(currentNode.getId(), tree, tempTree, jeblTree);
//					}
//					this.writeNodeIdsToXML(cluster, ID.LEAFID);
//					this.writeNodeIdsToXML(cluster, ID.NODEID);
//					
//					List<Node> nodesList = new ArrayList<Node>(cluster.getLeaves());
//					for(Node node:nodesList) {
//						if(node.getImmediateChildren().size() == 0 && node.getSize() > 1) {
//							toDo.add(tree.getNodeById(node.getId()));
//						}
//					}
//					
////					toDo.addAll(tempTree.getLeaves());
//					break;
//				}
//			} catch (FileNotFoundException e) {
//				System.err.println(PreRendering.class + ": " + "The tree file cannot be found in the given location.");
//			}
////		} else {
////			System.err.println("The folders seems to have files in them. Maybe they are hidden? Please empty the folders first.");
////			System.err.println("Check paths: ");
////			System.err.println(folderLocationClusters);
////			System.err.println(folderLocationCsvs);
////			System.exit(0);
////		}
//	}
	
//	public void preRender(String treeLocation, String csvLocation, String distanceMatrixLocation) {
//		
//	}
	
	public boolean checkFoldersEmpty() {
		File folder = new File(folderLocationClusters);
		File folderCsvs = new File(folderLocationCsvs);
		File folderTreeView = new File(folderLocationTreeView);
//		File folderLeafIds = new File(folderLocationLeafIds);
//		File folderNodeIds = new File(folderLocationNodeIds);
		
		if(folder.isDirectory() && folderCsvs.isDirectory() && folderTreeView.isDirectory()){
			if(folder.list().length>0 || folderCsvs.list().length>0 || folderTreeView.list().length>0){
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
//		} else if(folderLeafIds.isFile()) {
//			System.out.println(PreRendering.class + ": The path " + folderLocationLeafIds + " to the folder seems to direct to a file.");
//			return false;
//		} else if(folderNodeIds.isFile()) {
//			System.out.println(PreRendering.class + ": The path " + folderLocationNodeIds + " to the folder seems to direct to a file.");
//			return false;
		} else {
			folder.mkdirs();
			folderCsvs.mkdirs();
			folderTreeView.mkdirs();
//			folderLeafIds.mkdirs();
//			folderNodeIds.mkdirs();
			return true;
		}
	}
	
	public void prepareCSV(int clusterId, List<Node> nodes, String csvLocation, boolean showNA) {
		CSVReader csvReader;
		List<String> ids = new ArrayList<String>();
		for(Node node:nodes) {
			ids.add(node.getLabel());
		}
		try {
			csvReader = new CSVReader(new FileReader(new File(csvLocation)), ';');
			String[] header = csvReader.readNext();
			FileWriter fileWriter = new FileWriter(new File(this.folderLocationCsvs + File.separator + clusterId + ".xml"));
			fileWriter.write("<xml>" + "\n");
			for(String key:header) {
				if(!key.equalsIgnoreCase("id")) {
					HashMap<String,Integer> tempHashMap = CsvUtils.csvToHashMapStringInteger(new File(csvLocation), ';', ids, key, showNA);
					for(String hashMapKey:tempHashMap.keySet()) {
						fileWriter.write("\t<" + key + " id=\"" + hashMapKey + "\">" + tempHashMap.get(hashMapKey) + "</" + key + ">" + "\n");
					}
				}
			}
			fileWriter.write("</xml>");
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.err.println(PreRendering.class + ": " + "PrepareCSV, File could not be found.");
		} catch (IOException e) {
			System.err.println(PreRendering.class + ": " + "PrepareCSV, IOException in the CSVReader.");
		}
	}
	
//	public void prepareFullTreeView(int clusterId, Cluster cluster, jebl.evolution.trees.Tree jeblTree) {
//		
//		StringWriter stringWriter = null;
//		try {
////			FileWriter fileWriter = new FileWriter(new File(this.folderLocationTreeView + File.separator + clusterId + ".nexus"));
//			stringWriter = new StringWriter();
//			String export = NexusExporter.export(cluster, jeblTree, stringWriter);
////			fileWriter.write(stringWriter.toString());
////			fileWriter.close();
////			System.out.println(export);
//			
//			Map<String, Object> settings = new HashMap<String, Object>();
//	
//	        ExtendedTreeViewer treeViewer = new ExtendedTreeViewer();
//	        ControlPalette controlPalette = new BasicControlPalette(CONTROL_PALETTE_WIDTH, BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
//	        // This is important for the coloring.
//	        new FigTreePanel(null, treeViewer, controlPalette);
//	        // First of all, fully populate the settings map so that
//	        // all the settings have defaults
//	        controlPalette.getSettings(settings);
//	
//	        List<jebl.evolution.trees.Tree> trees = new ArrayList<jebl.evolution.trees.Tree>();
//	
//	        FigTreeNexusImporter importer = new FigTreeNexusImporter(new StringReader(export));
//	        trees.add(importer.importNextTree());
//	
//	            // Try to find a figtree block and if found, parse the settings
//	        while (true) {
//	        	try {
//	        		importer.findNextBlock();
//	                if (importer.getNextBlockName().equalsIgnoreCase("FIGTREE")) {
//	                    importer.parseFigTreeBlock(settings);
//	                }
//	            } catch (EOFException ex) {
//	               break;
//	            }
//	        }
//	
//	        treeViewer.setTrees(trees);
//	        controlPalette.setSettings(settings);
//	        
//	        int width = 650;
//	        int height = 650;
//	        treeViewer.getContentPane().setSize(width, height);
//	
//	        JComponent comp = treeViewer.getContentPane();
//	        ((TreePane)comp).setTreeLayout(new RadialTreeLayout());
//	        SimpleLabelPainter simpleLabelPainter = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
//	        simpleLabelPainter.setVisible(false);
//	        ((TreePane)comp).setTipLabelPainter(simpleLabelPainter);
//	        
//	        ((TreePane)comp).setBranchStroke(new BasicStroke(2.0f));
//	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//	        Graphics g = bi.createGraphics();
//	        comp.paint(g);
//	        g.dispose();
//	        
//	        File file = new File(this.folderLocationTreeView + File.separator + clusterId + ".png");
//			ImageIO.write(bi, "PNG", file);
//        } catch(IOException ioe) {
//        	throw new RuntimeException("Error writing graphic file: " + ioe);
//        } catch (ImportException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
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
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/EUResist/clusters", "/Users/ewout/Documents/phylogeo/EUResist/xml", "/Users/ewout/Documents/phylogeo/EUResist/treeview", "/Users/ewout/Documents/phylogeo/EUResist/leafIds", "/Users/ewout/Documents/phylogeo/EUResist/nodeIds");
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Test/clusters", "/Users/ewout/Documents/phylogeo/Test/xml", "/Users/ewout/Documents/phylogeo/Test/treeview", "/Users/ewout/Documents/phylogeo/Test/leafIds");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick", "/Users/ewout/Documents/phylogeo/EUResist_New/EUResist.metadata.csv");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick", "/Users/ewout/Documents/phylogeo/EUResist/EUResist.metadata.cleaned.csv");
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/portugal/RAxML_bipartitions.final_tree", "/Users/ewout/Documents/phylogeo/portugal/final.csv");
//		preRendering.preRender("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.tree", "/Users/ewout/git/phylogeotool/lib/EwoutTrees/temp.csv");
//		preRendering.getLeafIdFromXML("1");
	
		String treeLocation = "";
		String csvLocation = "";
		String distanceMatrixLocation = "";
		String treeRenderLocation = "";
		String clusterRenderLocation = "";
		String csvRenderLocation = "";
		String treeViewRenderLocation = "";
//		String sdrLocation = "";
		int minimumClusterSize = 2;
		
		if(args.length > 6) {
			treeLocation = args[0];
			csvLocation = args[1];
			distanceMatrixLocation = args[2];
			treeRenderLocation = args[3];
			clusterRenderLocation = args[4];
			csvRenderLocation = args[5];
			treeViewRenderLocation = args[6];
		} else {
			System.err.println("You need to have a java -version > 7");
			System.err.println("java -jar PreRendering.jar phylo.tree csvFile distance.matrix folderXMLTree folderXMLClusters folderXMLCsv folderFigtreeRep");
			System.exit(0);
		}
		
//		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Configs/Portugal/tree","/Users/ewout/Documents/phylogeo/Configs/Portugal/clusters", "/Users/ewout/Documents/phylogeo/Configs/Portugal/xml", "/Users/ewout/Documents/phylogeo/Configs/Portugal/treeview", "/Users/ewout/Documents/phylogeo/Configs/Portugal/leafIds", "/Users/ewout/Documents/phylogeo/Configs/Portugal/nodeIds");
		PreRendering preRendering = new PreRendering(treeRenderLocation, clusterRenderLocation, csvRenderLocation, treeViewRenderLocation, "", "");
		
		try {
//			preRendering.preRender("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree", "/Users/ewout/Documents/TDRDetector/fullPortugal/allSequences_cleaned_ids.out2.csv", "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.portugal.txt");
			preRendering.preRender(treeLocation, csvLocation, distanceMatrixLocation);
//			preRendering.preRender("/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.500.midpoint.solved.newick", "", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		preRendering.preRender("/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree", "/Users/ewout/Documents/phylogeo/EUResist_New/EUResist_one_seq_per_pat.csv", "/Users/ewout/Documents/phylogeo/TestCases/Portugal/distance.EUResist.shortened.txt");
//		preRendering.preRender("/Users/ewout/git/phylogeotool/lib/Test/Portugal/besttree.500.midpoint.solved.newick", "", "/Users/ewout/git/phylogeotool/lib/Test/Portugal/distance.500.txt");
	}
}
