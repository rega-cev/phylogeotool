package be.kuleuven.rega.webapp;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
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
import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;
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
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class GraphWidget extends WPaintedWidget {

	private Cluster cluster;
	private Stack<Integer> previousRootId = new Stack<Integer>();
//	private PreRendering preRendering = null;
	private FacadeRequestData facadeRequestData;
	private PPlacer pplacer;
	private int width;
	private int height;
	
	private int previousClusterID = 0;
	private int breadCrumb = 0;
	
	// TODO: We need to get rid of the reference to GraphWebApplication
	public GraphWidget(FacadeRequestData facadeRequestData) throws IOException {
		//TODO : Check if the trees have been prerendered
		this.facadeRequestData = facadeRequestData;
		this.loadGraph();
//		this.setLayoutSizeAware(true);
	}

	@Override
	protected void layoutSizeChanged(int w, int h) {
		super.layoutSizeChanged(w,h);
//		System.out.println("Width: " + w + " height: " + h);
		this.width = w;
		this.height = h;
	}
	
	public void loadGraph() throws IOException {
//		kMedoidsToPhylo = new ClusterAlgos(nodeIndexProvider, locationDistances);
//		fullTree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader(treeLocation)));
		String rootNodeId = UrlManipulator.getId(WApplication.getInstance().getInternalPath());
		this.addPreviousRootId(Integer.parseInt(rootNodeId));
//		this.setTreeClustered(kMedoidsToPhylo.getGraph(fullTree, rootNode, nrClusters));
		this.setCluster(facadeRequestData.getCluster(rootNodeId));
		this.setBreadCrumb();
			
//		preRendering.writeTreeToXML(this.getTreeClustered());
	}

	public void setBreadCrumb() {
		breadCrumb = 0;
		if(!cluster.getRoot().equals(cluster.getTree().getRootNode())) {
			Cluster tempCluster = cluster;
			while(tempCluster.getRoot() != cluster.getTree().getRootNode()) {
				breadCrumb++;
				tempCluster = facadeRequestData.getCluster(Integer.toString(tempCluster.getParentalClusterRootId()));
			}
		} else {
			breadCrumb = 0;
		}
		System.out.println("Breadcrumb: " + breadCrumb);
	}
	
	public void setBreadCrumb(int breadCrumb) {
		this.breadCrumb = breadCrumb;
	}
	
	public int getBreadCrumb() {
		return this.breadCrumb;
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
	
	public int getPreviousClusterID() {
		return previousClusterID;
	}

	public void setPreviousClusterID(int previousClusterID) {
		this.previousClusterID = previousClusterID;
	}

	//TODO: Method is called twice due to the addition of the view tree button on top
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
		
		List<Node> clustersWithPPlacerSeq = new ArrayList<Node>();
		for(Node node:cluster.getBoundaries()) {
			if(pplacer != null && pplacer.clusterContainsPPlacerSequence(facadeRequestData.getCluster(Integer.toString(node.getId())))) {
				clustersWithPPlacerSeq.add(node);
			}
		}
		
		if(shape.equals(Shape.RECTANGULAR_CLADOGRAM) || shape.equals(Shape.RECTANGULAR_PHYLOGRAM)) {
			draw = new DrawRectangular(this.getCluster(), shape);
		} else {
			draw = new DrawCircular(this.getCluster(), shape, clustersWithPPlacerSeq);
		}
		for(Node node:cluster.getBoundaries()) {
			int nodeLevel = TreeTraversal.nodeLevel(cluster.getRoot(), node, 0);
			if(nodeLevel > deepestLevel) {
				deepestLevel = nodeLevel;
			}
		}
		
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
//			if(wCircle.getCenterY() <= height/2) {
////				int strWidth = graphics.getFontMetrics().stringWidth(Integer.toString(facadeRequestData.getTree().getLeaves(wCircle.getNode()).size()));
//				graphics.drawString(Integer.toString(facadeRequestData.getTree().getLeaves(wCircle.getNode()).size()), wCircle.getCenterX() - 10, wCircle.getCenterY() - wCircle.getRadius());
//			} else {
//				graphics.drawString(Integer.toString(facadeRequestData.getTree().getLeaves(wCircle.getNode()).size()), wCircle.getCenterX() - 10, wCircle.getCenterY() + 2*wCircle.getRadius() + 5);
//			}
//			System.out.println("Size: " + facadeRequestData.getTree().getLeaves(wCircle.getNode()).size() + " X: " + wCircle.getCenterX());
//			System.out.println(wCircle.getCenterX() + wCircle.getRadius() + 5);
			
//			WImage wImage = new WImage(new WLink("http://www.google.com/mapfiles/marker.png"));
//			System.out.println(pplacer);
//			if(pplacer != null && cluster.getTree().getLeaves(wCircle.getNode()).size() > 3) {
//				System.out.println(pplacer.clusterContainsPPlacerSequence(facadeRequestData.getCluster(Integer.toString(wCircle.getNode().getId()))));
//			}
//			System.out.println(facadeRequestData.getCluster(Integer.toString(wCircle.getNode().getId())));
//			if(pplacer != null && pplacer.clusterContainsPPlacerSequence(facadeRequestData.getCluster(Integer.toString(wCircle.getNode().getId())))) {
//				System.out.println("Here: " + Integer.toString(wCircle.getNode().getId()));
//				graphics.drawString("pplaced", wCircle.getCenterX() + wCircle.getRadius() + 5, wCircle.getCenterY());
//				graphics.drawRect(wCircle.getCenterX() - wCircle.getRadius(), wCircle.getCenterY() - wCircle.getRadius(), wCircle.getRadius()*2, wCircle.getRadius()*2);
//				graphics.draw3DRect(wCircle.getCenterX() - wCircle.getRadius(), wCircle.getCenterY() - wCircle.getRadius(), wCircle.getRadius()*2, wCircle.getRadius()*2, true);
//				graphics.setStroke(new BasicStroke(2));
//				graphics.drawOval(wCircle.getCenterX() - wCircle.getRadius() - 7, wCircle.getCenterY() - wCircle.getRadius() - 7, wCircle.getRadius()*2 + 14, wCircle.getRadius()*2 + 14);
			
			
//				graphics.setStroke(new BasicStroke(1));
//				int x = wCircle.getCenterX();
//				int y = wCircle.getCenterY();
				
//				graphics.setColor(Color.LIGHT_GRAY);
//				graphics.fillOval(x+18, y-35, 20, 20);
//				RadialGradientPaint rgp = new RadialGradientPaint(new Point(),wCircle.getRadius() ,new float[]{0f, 1f},new Color[]{Color.RED, Color.YELLOW});
//				graphics.setPaint(rgp);
				
//				graphics.setColor(Color.LIGHT_GRAY.darker());
//				graphics.drawOval(x+18, y-35, 20, 20);
//				
//
//				graphics.setColor(Color.black);
//				graphics.drawLine(x, y, x+20, y-20);
				
//				x = 0;
//				y = 50;
//				graphics.drawLine(x, y, x+15, y-15);
				
//				graphics.drawRoundRect(100, 100, 20, 30, 20, 30);
//				graphics.drawArc(50, 60, 30, 20, 230, 160);
				
//				System.out.println("Here");
//				java.awt.Shape shapeTemp = null;
//		        
//		        float origAlpha = 1.0f;
//		        Composite origComposite = graphics.getComposite();
//		        if (origComposite instanceof AlphaComposite) {
//		            AlphaComposite origAlphaComposite = (AlphaComposite)origComposite;
//		            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
//		                origAlpha = origAlphaComposite.getAlpha();
//		            }
//		        }
		        
//		        java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();
//
//		        transformations.offer(graphics.getTransform());
//		        graphics.transform(new AffineTransform(0.5f, 0, 0, 0.5f, 0.1f, 0));
//		        graphics.translate(wCircle.getCenterX()*2, wCircle.getCenterY()*2 - 80);
//		        
//		        shapeTemp = new GeneralPath();
//		        ((GeneralPath) shapeTemp).moveTo(31.399, 53.387);
//		        ((GeneralPath) shapeTemp).lineTo(26.031, 58.872);
//		        ((GeneralPath) shapeTemp).lineTo(15.294, 69.612);
//		        ((GeneralPath) shapeTemp).lineTo(9.807, 74.979);
//		        ((GeneralPath) shapeTemp).lineTo(7.184, 83.091);
//		        ((GeneralPath) shapeTemp).lineTo(15.294, 80.467);
//		        ((GeneralPath) shapeTemp).lineTo(20.663, 74.979);
//		        ((GeneralPath) shapeTemp).lineTo(31.399, 64.242);
//		        ((GeneralPath) shapeTemp).lineTo(36.886, 58.872);
//		        ((GeneralPath) shapeTemp).lineTo(34.141, 56.129);
//		        ((GeneralPath) shapeTemp).closePath();
//
//		        graphics.setPaint(Color.BLACK);
//		        graphics.fill(shapeTemp);
//		        
//		        shapeTemp = new GeneralPath();
//		        ((GeneralPath) shapeTemp).moveTo(69.1, 21.441);
//		        ((GeneralPath) shapeTemp).curveTo(68.899, 21.225, 67.493, 19.857, 66.816, 19.156);
//		        ((GeneralPath) shapeTemp).curveTo(64.895004, 17.235, 54.187004, 6.526, 54.187004, 6.526);
//		        ((GeneralPath) shapeTemp).lineTo(53.113003, 9.885);
//		        ((GeneralPath) shapeTemp).curveTo(52.180004, 12.683001, 51.863003, 15.302, 51.765003, 17.814);
//		        ((GeneralPath) shapeTemp).lineTo(34.837006, 34.744);
//		        ((GeneralPath) shapeTemp).curveTo(31.191006, 34.328, 27.234005, 34.412, 23.416006, 35.55);
//		        ((GeneralPath) shapeTemp).lineTo(19.924006, 36.626);
//		        ((GeneralPath) shapeTemp).lineTo(53.783005, 70.486);
//		        ((GeneralPath) shapeTemp).lineTo(54.857006, 66.99);
//		        ((GeneralPath) shapeTemp).curveTo(55.981007, 63.219997, 56.053005, 59.442997, 55.664005, 55.839996);
//		        ((GeneralPath) shapeTemp).lineTo(72.592, 38.774994);
//		        ((GeneralPath) shapeTemp).curveTo(75.148, 38.695995, 77.811005, 38.244995, 80.65401, 37.296993);
//		        ((GeneralPath) shapeTemp).lineTo(83.880005, 36.222992);
//		        ((GeneralPath) shapeTemp).lineTo(69.1, 21.441);
//		        ((GeneralPath) shapeTemp).closePath();
//		        ((GeneralPath) shapeTemp).moveTo(71.651, 34.608);
//		        ((GeneralPath) shapeTemp).lineTo(70.711, 34.608);
//		        ((GeneralPath) shapeTemp).lineTo(51.361, 53.956);
//		        ((GeneralPath) shapeTemp).lineTo(51.632, 55.030003);
//		        ((GeneralPath) shapeTemp).curveTo(51.982, 57.055004, 51.383, 59.38, 51.095, 61.614002);
//		        ((GeneralPath) shapeTemp).lineTo(28.925001, 39.445);
//		        ((GeneralPath) shapeTemp).curveTo(31.114, 39.174, 33.39, 38.568, 35.374, 38.908);
//		        ((GeneralPath) shapeTemp).lineTo(36.45, 39.043);
//		        ((GeneralPath) shapeTemp).lineTo(55.798, 19.692999);
//		        ((GeneralPath) shapeTemp).lineTo(55.798, 18.755);
//		        ((GeneralPath) shapeTemp).curveTo(55.783, 17.619999, 56.371002, 16.273998, 56.602, 14.991999);
//		        ((GeneralPath) shapeTemp).curveTo(59.838, 18.227999, 64.397, 22.785, 64.397, 22.785);
//		        ((GeneralPath) shapeTemp).curveTo(64.963005, 23.372, 65.79301, 24.167, 66.008, 24.396);
//		        ((GeneralPath) shapeTemp).lineTo(75.551, 33.936);
//		        ((GeneralPath) shapeTemp).curveTo(74.246, 34.178, 72.805, 34.625, 71.651, 34.608);
//		        ((GeneralPath) shapeTemp).closePath();
//
//		        graphics.fill(shapeTemp);
//		        graphics.setTransform(transformations.poll());
//			}
        }
			
	}
	
	// TODO: Fix the Nexus Exporter
//	public String writeTree() {
//		return NexusExporter.export(this.getTree(), this.getTreeClustered());
//	}
	
	/**
	 * @param id
	 * @param deeper
	 *  deeper == -1 -> User went deeper in the tree (further away from root)
	 *  deeper == 0  -> No level change (refreshed page? ...)
	 *  deeper == 1  -> User went higher in the tree (more towards root)
	 */
	public void setCluster(int id, WLabel treeLevel, int deeper) {
//		Tree tempTree = kMedoidsToPhylo.getGraph(this.fullTree, node, nrClusters);
		String nodeId = Integer.toString(id);
//		Tree tempTree = preRendering.getTreeFromXML(nodeId);
		Cluster tempCluster = facadeRequestData.getCluster(nodeId);
		this.setCluster(tempCluster);
		//TODO: check if pplacer is part of link
		WApplication.getInstance().setInternalPath("/root_" + nodeId);
//		WApplication.getInstance().getInternalPath();
		switch(deeper) {
			case -1: setBreadCrumb(++breadCrumb);break;
			case 0:  break;
			case 1:  setBreadCrumb(--breadCrumb);break;
		}
		treeLevel.setText("Level: " + breadCrumb);
		this.setPreviousClusterID(id);
		update();
	}
	
	public void setPPlacer(PPlacer pplacer) {
		this.pplacer = pplacer;
	}
}
