package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.prerendering.PreRendering;
import be.kuleuven.rega.treedraw.Draw;
import be.kuleuven.rega.treedraw.DrawCircular;
import be.kuleuven.rega.treedraw.DrawRectangular;
import be.kuleuven.rega.treedraw.TreeTraversal;
import eu.webtoolkit.jwt.WAbstractArea;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class GraphWidget extends WPaintedWidget {

	private ClusterAlgos kMedoidsToPhylo;
	private Tree treeClustered;
	private GraphProperties graphProperties;
	private int nrClusters = 11;
	private Stack<Integer> previousRootId = new Stack<Integer>();
	private Draw draw = null;
	private Shape shape = null;
	private GraphWebApplication graphWebApplication = null;
	private WebGraphics2DMine graphics = null;
	private PreRendering preRendering = null;
	
//	public GraphWidget(GraphWebApplication graphWebApplication, File locationNodeIndexProvider, File locationDistances, String clusterLocation) throws IOException {
//		this.graphWebApplication = graphWebApplication;
//		this.loadGraph(locationNodeIndexProvider, locationDistances, null);
//		init(clusterLocation);
//	}

	public GraphWidget(GraphWebApplication graphWebApplication, File locationNodeIndexProvider, File locationDistances, PreRendering preRendering) throws IOException {
		//TODO : Check if the trees have been prerendered
		this.graphWebApplication = graphWebApplication;
		init(preRendering);
		this.loadGraph(locationNodeIndexProvider, locationDistances);
	}

	public void init(PreRendering preRendering) {
		this.graphProperties = new GraphProperties();
		this.preRendering = preRendering;
	}

	public void loadGraph(File nodeIndexProvider, File locationDistances) throws IOException {
//		kMedoidsToPhylo = new ClusterAlgos(nodeIndexProvider, locationDistances);
//		fullTree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader(treeLocation)));
		String rootNodeId;
		if (WApplication.getInstance().getInternalPath().contains("/root")) {
			rootNodeId = WApplication.getInstance().getInternalPath().split("/")[2];
		} else {
			// TODO: Change this
//			rootNode = fullTree.getRootNode();
			rootNodeId = "1";
		}
		this.addPreviousRootId(Integer.parseInt(rootNodeId));
//		this.setTreeClustered(kMedoidsToPhylo.getGraph(fullTree, rootNode, nrClusters));
		this.setTreeClustered(preRendering.getTreeFromXML(rootNodeId));
			
//		preRendering.writeTreeToXML(this.getTreeClustered());
	}

	public ClusterAlgos getKMedoidsToPhylo() {
		return kMedoidsToPhylo;
	}

	// TODO: See if this is necessary
//	@Override
//	public void resize(WLength width, WLength height) {
//		super.resize(width, height);
//		// Also resizes the tree pane
//	}
//
//	@Override
//	protected void layoutSizeChanged(int width, int height) {
//		super.layoutSizeChanged(width, height);
//		// Also resizes the tree pane
//	}

	public Integer getPreviousRootId() {
		//TODO: Catch exception when stack is empty
		if(this.previousRootId.empty()) {
			// TODO : Return root node, not just 1
//			return this.getTree().getRootNode().getId();
			return 1;
		} else {
			return this.previousRootId.pop();
		}
	}

	public void addPreviousRootId(int previousRootId) {
		this.previousRootId.push(previousRootId);
	}
	
	public void emptyPreviousRootId() {
		this.previousRootId.empty();
	}
	
	public int getNrClusters() {
		return nrClusters;
	}

	public void setNrClusters(int nrClusters) {
		this.nrClusters = nrClusters;
	}
	
	public Tree getTreeClustered() {
		return this.treeClustered;
	}

	public void setTreeClustered(Tree treeClustered) {
		this.treeClustered = treeClustered;
	}

	//TODO: Check why this method is called 2x
	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		graphics = new WebGraphics2DMine(painter);
		int deepestLevel = 0;
		//TODO : See if we can get the rectangular representation in here
		//TODO: Do something about the fixed number 12
//		if(this.preRendering.getNodeById(this.getTreeClustered().getRootNode().getId()).getLeaves().size() < 12) {
		if(this.getTreeClustered().getLeaves().size() < 12) {
			shape = Shape.RECTANGULAR_CLADOGRAM;
			draw = new DrawRectangular(this.getTreeClustered(), shape);
		} else {
			shape = Shape.CIRCULAR_CLADOGRAM;
			draw = new DrawCircular(this.getTreeClustered(), shape);
			for(Node node:this.getTreeClustered().getNodes()) {
				int nodeLevel = TreeTraversal.nodeLevel(node, 0);
				if(nodeLevel > deepestLevel) {
					deepestLevel = nodeLevel;
				}
			}
		}
		
		this.graphProperties.setNodeColor(this.getTreeClustered());
		TreeTraversal.y = 0;
		TreeTraversal.postOrder(this.getTreeClustered().getRootNode(), shape, getTreeClustered().getRootNode().getLeaves().size(), deepestLevel);
        TreeTraversal.preOrder(this.getTreeClustered().getRootNode(), getTreeClustered().getEdges(), shape);
        for(WAbstractArea area:this.getAreas()) {
        	this.removeArea(area);
        }
        draw.paint(graphWebApplication, this, graphics, getWidth().getValue(), getHeight().getValue(), paintDevice.getWidth().getValue(), paintDevice.getHeight().getValue());
	}
	
	// TODO: Fix the Nexus Exporter
//	public String writeTree() {
//		return NexusExporter.export(this.getTree(), this.getTreeClustered());
//	}
	
	public void setTree(int id) {
//		Tree tempTree = kMedoidsToPhylo.getGraph(this.fullTree, node, nrClusters);
		String nodeId = Integer.toString(id);
		Tree tempTree = preRendering.getTreeFromXML(nodeId);
		this.setTreeClustered(tempTree);
		WApplication.getInstance().setInternalPath("/root/" + nodeId);
		update();
	}
}
