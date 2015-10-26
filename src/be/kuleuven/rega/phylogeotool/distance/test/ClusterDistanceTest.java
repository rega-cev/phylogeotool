package be.kuleuven.rega.phylogeotool.distance.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.comparator.ClusterDistanceComparator;
import be.kuleuven.rega.phylogeotool.distance.MidRootCluster;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class ClusterDistanceTest {
	
	private Node t1 = new Node("t1", 1);
	private Node t2 = new Node("t2", 2);
	private Node t3 = new Node("t3", 3);
	private Node t4 = new Node("t4", 4);
	private Node t5 = new Node("t5", 5);
	private Node t6 = new Node("t6", 6);
	private Node t7 = new Node("t7", 7);
	private Node t8 = new Node("t8", 8);
	private Node t9 = new Node("t9", 9);
	private Node t10 = new Node("t10", 10);
	private Node t11 = new Node("t11", 11);
	private Node t12 = new Node("t12", 12);
	private Node t13 = new Node("t13", 13);
	private Node t14 = new Node("t14", 14);
	private Node t15 = new Node("t15", 15);
	private Node t16 = new Node("t16", 16);
	private Node t17 = new Node("t17", 17);
	private Node t18 = new Node("t18", 18);
	private Node t19 = new Node("t19", 19);
	
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

	private Set<Edge> edges;
	private Set<Node> nodes;
	
	private Tree tree;
	
	@Before
	public void before() {
		
		t19.addChild(t13);
		t19.addChild(t18);
		t13.setParent(t19);
		t18.setParent(t19);
		t19.setSize(10);
		
		t13.addChild(t11);
		t13.addChild(t8);
		t11.setParent(t13);
		t8.setParent(t13);
		t13.setSize(4);
		
		t18.addChild(t14);
		t18.addChild(t17);
		t14.setParent(t18);
		t17.setParent(t18);
		t18.setSize(6);
		
		t11.addChild(t3);
		t11.addChild(t12);
		t3.setParent(t11);
		t12.setParent(t11);
		t11.setSize(3);
		
		t17.addChild(t16);
		t17.addChild(t5);
		t16.setParent(t17);
		t5.setParent(t17);
		t17.setSize(4);
		
		t14.addChild(t4);
		t14.addChild(t9);
		t4.setParent(t14);
		t9.setParent(t14);
		t14.setSize(2);
		
		t12.addChild(t1);
		t12.addChild(t10);
		t1.setParent(t12);
		t10.setParent(t12);
		t12.setSize(2);
		
		t16.addChild(t15);
		t16.addChild(t2);
		t15.setParent(t16);
		t2.setParent(t16);
		t16.setSize(3);
		
		t15.addChild(t6);
		t15.addChild(t7);
		t6.setParent(t15);
		t7.setParent(t15);
		t15.setSize(2);
		
		t5.setSize(1);
		t2.setSize(1);
		t7.setSize(1);
		t6.setSize(1);
		t9.setSize(1);
		t4.setSize(1);
		t8.setSize(1);
		t10.setSize(1);
		t1.setSize(1);
		t3.setSize(1);
		
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
		nodes.add(t19);
		nodes.add(t18);
		nodes.add(t17);
		nodes.add(t16);
		nodes.add(t15);
		nodes.add(t14);
		nodes.add(t13);
		nodes.add(t12);
		nodes.add(t11);
		nodes.add(t10);
		nodes.add(t9);
		nodes.add(t8);
		nodes.add(t7);
		nodes.add(t6);
		nodes.add(t5);
		nodes.add(t4);
		nodes.add(t3);
		nodes.add(t2);
		nodes.add(t1);
	
		tree = new Tree(nodes, edges, t19);
	}
	
	@Test
	public void testClusterSizes() {
		MidRootCluster midRootCluster = new MidRootCluster(1);
		assertEquals("", 1, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getNodes().size(), 0.0);
		midRootCluster.setNumberOfIClusters(2);
		assertEquals("", 2, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(3);
		assertEquals("", 3, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(4);
		assertEquals("", 4, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(5);
		assertEquals("", 5, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(6);
		assertEquals("", 6, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(7);
		assertEquals("", 7, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
		midRootCluster.setNumberOfIClusters(8);
		assertEquals("", 8, midRootCluster.calculate(tree, tree.getRootNode(), new ClusterDistanceComparator(tree)).getLeaves().size(), 0.0);
	}
}