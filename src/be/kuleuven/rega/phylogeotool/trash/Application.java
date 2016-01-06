package be.kuleuven.rega.phylogeotool.trash;
//package be.kuleuven.rega.treedraw.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.Shape;
import be.kuleuven.rega.treedraw.DrawData;
import be.kuleuven.rega.treedraw.TreeTraversal;
import be.kuleuven.rega.webapp.GraphProperties;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WVBoxLayout;

public class Application extends WApplication {
	
	private Node t19 = new Node(19, "t19", null, null);
	private Node t13 = new Node(13, "t13", t19, null);
	private Node t8 = new Node(8, "t8", t13, null);
	private Node t11 = new Node(11, "t11", t13, null);
	private Node t3 = new Node(3, "t3", t11, null);
	private Node t12 = new Node(12, "t12", t11, null);
	private Node t1 = new Node(1, "t1", t12, null);
	private Node t10 = new Node(10, "t10", t12, null);
	private Node t18 = new Node(18, "t18", t19, null);
	private Node t14 = new Node(14, "t14", t18, null);
	private Node t4 = new Node(4, "t4", t14, null);
	private Node t9 = new Node(9, "t9", t14, null);
	private Node t17 = new Node(17, "t17", t18, null);
	private Node t5 = new Node(5, "t5", t17, null);
	private Node t16 = new Node(16, "t16", t17, null);
	private Node t2 = new Node(2, "t2", t16, null);
	private Node t15 = new Node(15, "t15", t16, null);
	private Node t6 = new Node(6, "t6", t15, null);
	private Node t7 = new Node(7, "t7", t15, null);
	
	private Edge t19t18 = new Edge("t19t18", t19, t18, 0.9063808941);
	private Edge t13t8 = new Edge("t13t8", t13, t8, 0.4848527387);
	private Edge t13t11 = new Edge("t13t11", t13, t11, 0.5995814849);
	private Edge t19t13 = new Edge("t19t13", t19, t13, 0.5236769456);
	private Edge t18t17 = new Edge("t18t17", t18, t17, 0.6159315344);
	private Edge t18t14 = new Edge("t18t14", t18, t14, 0.914607449);
	private Edge t14t4 = new Edge("t14t4", t14, t4, 0.2882589418);
	private Edge t14t9 = new Edge("t14t9", t14, t9, 0.6715812988);
	private Edge t17t5 = new Edge("t17t5", t17, t5, 0.8888230831);
	private Edge t17t16 = new Edge("t17t16", t17, t16, 0.4394014708);
	private Edge t11t3 = new Edge("t11t3", t11, t3, 0.5569783391);
	private Edge t11t12 = new Edge("t11t12", t11, t12, 0.7155418394);
	private Edge t12t10 = new Edge("t12t10", t12, t10, 0.7785333549);
	private Edge t12t1 = new Edge("t12t1", t12, t1, 0.6787646217);
	private Edge t16t2 = new Edge("t16t2", t16, t2, 0.07695768401);
	private Edge t16t15 = new Edge("t16t15", t16, t15, 0.1205861182);
	private Edge t15t7 = new Edge("t15t7", t15, t7, 0.7195842071);
	private Edge t15t6 = new Edge("t15t6", t15, t6, 0.9620062774);
	
//	private Edge t19t18 = new Edge("t19t18", t19, t18, 1);
//	private Edge t13t8 = new Edge("t13t8", t13, t8, 1);
//	private Edge t13t11 = new Edge("t13t11", t13, t11, 1);
//	private Edge t19t13 = new Edge("t19t13", t19, t13, 1);
//	private Edge t18t17 = new Edge("t18t17", t18, t17, 1);
//	private Edge t18t14 = new Edge("t18t14", t18, t14, 1);
//	private Edge t14t4 = new Edge("t14t4", t14, t4, 1);
//	private Edge t14t9 = new Edge("t14t9", t14, t9, 1);
//	private Edge t17t5 = new Edge("t17t5", t17, t5, 1);
//	private Edge t17t16 = new Edge("t17t16", t17, t16, 1);
//	private Edge t11t3 = new Edge("t11t3", t11, t3, 1);
//	private Edge t11t12 = new Edge("t11t12", t11, t12, 1);
//	private Edge t12t10 = new Edge("t12t10", t12, t10, 1);
//	private Edge t12t1 = new Edge("t12t1", t12, t1, 1);
//	private Edge t16t2 = new Edge("t16t2", t16, t2, 1);
//	private Edge t16t15 = new Edge("t16t15", t16, t15, 1);
//	private Edge t15t7 = new Edge("t15t7", t15, t7, 1);
//	private Edge t15t6 = new Edge("t15t6", t15, t6, 1);

	private Set<Edge> edges;
	private Set<Node> nodes;

	
	public Application(WEnvironment env) {
		super(env);
        
//		t5.setSize(10);
//		t5.setColor(Color.GREEN);
//		t2.setSize(15);
//		t2.setColor(Color.RED);
//		t7.setSize(25);
//		t7.setColor(Color.YELLOW);
//		t6.setSize(20);
//		t6.setColor(Color.ORANGE);
//		t9.setSize(35);
//		t9.setColor(Color.PINK);
//		t4.setSize(30);
//		t4.setColor(Color.CYAN);
//		t8.setSize(35);
//		t8.setColor(Color.MAGENTA);
//		t10.setSize(40);
//		t10.setColor(Color.BLACK);
//		t1.setSize(45);
//		t1.setColor(Color.YELLOW);
//		t3.setSize(45);
//		t3.setColor(Color.RED);

		edges = new HashSet<Edge>();
		edges.add(t19t18);
		edges.add(t13t8);
		edges.add(t13t11);
		edges.add(t19t13);
		edges.add(t18t17);
		edges.add(t18t14);
		edges.add(t14t4);
		edges.add(t14t9);
		edges.add(t17t5);
		edges.add(t17t16);
		edges.add(t11t3);
		edges.add(t11t12);
		edges.add(t12t10);
		edges.add(t12t1);
		edges.add(t16t2);
		edges.add(t16t15);
		edges.add(t15t7);
		edges.add(t15t6);
		
		nodes = new HashSet<Node>();
		nodes.add(t1);
		nodes.add(t2);
		nodes.add(t3);
		nodes.add(t4);
		nodes.add(t5);
		nodes.add(t6);
		nodes.add(t7);
		nodes.add(t8);
		nodes.add(t9);
		nodes.add(t10);
		nodes.add(t11);
		nodes.add(t12);
		nodes.add(t13);
		nodes.add(t14);
		nodes.add(t15);
		nodes.add(t16);
		nodes.add(t17);
		nodes.add(t18);
		nodes.add(t19);
        setTitle("Tree draw");
        
		jebl.evolution.trees.Tree jeblTree = null;
		try {
			jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
        Tree tree = new Tree(nodes, edges, t19);
//		Cluster cluster = MidRootCluster.calculate(tree, tree.getRootNode(), new ClusterSizeComparator(tree), 2, 10);
        List<Node> boundaries = new ArrayList<Node>();
        boundaries.addAll(tree.getLeaves());
        Cluster cluster = new Cluster(tree, t19, boundaries);
//        Shape shape = Shape.RECTANGULAR_CLADOGRAM;
		Shape shape = Shape.RECTANGULAR_PHYLOGRAM;
//        Shape shape = Shape.CIRCULAR_CLADOGRAM;
//		Shape shape = Shape.CIRCULAR_PHYLOGRAM;
//		Shape shape = Shape.RADIAL;
        Map<Node, DrawData> nodeToData = new HashMap<Node, DrawData>();
        
        int deepestLevel = 0;
        for(Node node:cluster.getBoundaries()) {
			int nodeLevel = TreeTraversal.nodeLevel(cluster.getRoot(), node, 0);
			if(nodeLevel > deepestLevel) {
				deepestLevel = nodeLevel;
			}
		}
        
        TreeTraversal.postOrder(cluster, cluster.getRoot(), nodeToData, shape, cluster.getBoundaries().size(), deepestLevel);
        TreeTraversal.preOrder(cluster, cluster.getRoot(), nodeToData, cluster.getEdges(), shape);
       
        Map<Node, Color> nodeToColor = GraphProperties.getClusterColor(cluster, 2);
        
        WVBoxLayout wvBoxLayout = new WVBoxLayout(getRoot());
    	wvBoxLayout.addWidget(new WPaintedWidgetTemp(nodeToData, nodeToColor, cluster, shape));
	}
}