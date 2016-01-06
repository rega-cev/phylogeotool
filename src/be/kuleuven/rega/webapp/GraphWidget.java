package be.kuleuven.rega.webapp;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import be.kuleuven.rega.listeners.WCircleMouseWentOutListener;
import be.kuleuven.rega.listeners.WCircleMouseWentOverListener;
import be.kuleuven.rega.listeners.WCircleNodeClickListener;
import be.kuleuven.rega.listeners.WCircleNodeDoubleClickListener;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.prerendering.FacadeRequestData;
import be.kuleuven.rega.treedraw.Draw;
import be.kuleuven.rega.treedraw.DrawCircular;
import be.kuleuven.rega.treedraw.DrawData;
import be.kuleuven.rega.treedraw.DrawRectangular;
import be.kuleuven.rega.treedraw.TreeTraversal;
import be.kuleuven.rega.url.UrlManipulator;
import eu.webtoolkit.jwt.WAbstractArea;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class GraphWidget extends WPaintedWidget {

	private Cluster cluster;
	private Stack<Integer> previousRootId = new Stack<Integer>();
//	private PreRendering preRendering = null;
	private FacadeRequestData facadeRequestData;
	
	// TODO: We need to get rid of the reference to GraphWebApplication
	public GraphWidget(FacadeRequestData facadeRequestData) throws IOException {
		//TODO : Check if the trees have been prerendered
		this.facadeRequestData = facadeRequestData;
		this.loadGraph();
	}

	public void loadGraph() throws IOException {
//		kMedoidsToPhylo = new ClusterAlgos(nodeIndexProvider, locationDistances);
//		fullTree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader(treeLocation)));
		String rootNodeId = UrlManipulator.getId(WApplication.getInstance().getInternalPath());
		this.addPreviousRootId(Integer.parseInt(rootNodeId));
//		this.setTreeClustered(kMedoidsToPhylo.getGraph(fullTree, rootNode, nrClusters));
		this.setCluster(facadeRequestData.getCluster(rootNodeId));
			
//		preRendering.writeTreeToXML(this.getTreeClustered());
	}

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
	
	public Cluster getCluster() {
		return this.cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	//TODO: Check why this method is called 2x
	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		WebGraphics2DMine graphics = new WebGraphics2DMine(painter);
		int deepestLevel = 0;
		//TODO : See which representation we need.
		Shape shape = Shape.CIRCULAR_CLADOGRAM;
//		Shape shape = Shape.CIRCULAR_PHYLOGRAM;
//		Shape shape = Shape.RECTANGULAR_CLADOGRAM;
//		Shape shape = Shape.RECTANGULAR_PHYLOGRAM;
//		Shape shape = Shape.RADIAL;
		Draw draw = null;
		if(shape.equals(Shape.RECTANGULAR_CLADOGRAM) || shape.equals(Shape.RECTANGULAR_PHYLOGRAM)) {
			draw = new DrawRectangular(this.getCluster(), shape);
		} else {
			draw = new DrawCircular(this.getCluster(), shape);
		}
		for(Node node:cluster.getBoundaries()) {
			int nodeLevel = TreeTraversal.nodeLevel(cluster.getRoot(), node, 0);
			if(nodeLevel > deepestLevel) {
				deepestLevel = nodeLevel;
			}
		}
		
//		this.graphProperties.getClusterColor(cluster, 2);
		TreeTraversal.y = 0;
		Map<Node, DrawData> map = new HashMap<Node, DrawData>();
		Map<Node, Color> nodeToColor = new HashMap<Node, Color>();
		// TODO: Set minimumclustersize
		nodeToColor = GraphProperties.getClusterColor(cluster, 2);
		TreeTraversal.postOrder(cluster, cluster.getRoot(), map, shape, cluster.getBoundaries().size(), deepestLevel);
        TreeTraversal.preOrder(cluster, cluster.getRoot(), map, cluster.getEdges(), shape);
        for(WAbstractArea area:this.getAreas()) {
        	this.removeArea(area);
        }

        List<WCircleNode> circleNodes = draw.paint(graphics, map, nodeToColor, paintDevice.getWidth().getValue(), paintDevice.getHeight().getValue());
        for(WCircleNode wCircle:circleNodes) {
        	if(cluster.getTree().getLeaves(wCircle.getNode()).size() > 1) {
        		wCircle.clicked().addListener(this, new WCircleNodeClickListener(wCircle, (GraphWebApplication)WApplication.getInstance()));
        	}
			wCircle.doubleClicked().addListener(this, new WCircleNodeDoubleClickListener(wCircle, (GraphWebApplication)WApplication.getInstance()));
			wCircle.mouseWentOver().addListener(this, new WCircleMouseWentOverListener(wCircle, (GraphWebApplication)WApplication.getInstance()));
			wCircle.mouseWentOut().addListener(this, new WCircleMouseWentOutListener(wCircle, (GraphWebApplication)WApplication.getInstance()));
			this.addArea(wCircle);
		}
			
	}
	
	// TODO: Fix the Nexus Exporter
//	public String writeTree() {
//		return NexusExporter.export(this.getTree(), this.getTreeClustered());
//	}
	
	public void setCluster(int id) {
//		Tree tempTree = kMedoidsToPhylo.getGraph(this.fullTree, node, nrClusters);
		String nodeId = Integer.toString(id);
//		Tree tempTree = preRendering.getTreeFromXML(nodeId);
		Cluster tempCluster = facadeRequestData.getCluster(nodeId);
		this.setCluster(tempCluster);
		//TODO: check if pplacer is part of link
		WApplication.getInstance().setInternalPath("/root_" + nodeId);
//		WApplication.getInstance().getInternalPath();
		update();
	}
}
