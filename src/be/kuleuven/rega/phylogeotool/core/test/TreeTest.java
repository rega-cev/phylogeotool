package be.kuleuven.rega.phylogeotool.core.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.core.Edge;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;

public class TreeTest {

	private Tree tree;
	private Node node1;
	private Node node2;
	private Node node3;
	private Node node4;
	private Node node5;
	private Node node6;
	private Node node7;
	private Node node8;
	private Node node9;
	private Node node10;
	private Node node11;
	private Node node12;
	private Node node13;
	private Node node14;
	private Node node15;
	
	private Set<Node> nodes = new HashSet<Node>();
	
	private Edge edge1;
	private Edge edge2;
	private Edge edge3;
	private Edge edge4;
	private Edge edge5;
	private Edge edge6;
	private Edge edge7;
	private Edge edge8;
	private Edge edge9;
	private Edge edge10;
	private Edge edge11;
	private Edge edge12;
	private Edge edge13;
	private Edge edge14;
	
	private Set<Edge> edges = new HashSet<Edge>();

	@Before
	public void init() {
		
		node1 = new Node(1, "Node1", null, "");
		node2 = new Node(2, "Node2", node1, "");
		node3 = new Node(3, "Node3", node1, "");
		node4 = new Node(4, "Node4", node2, "");
		node5 = new Node(5, "Node5", node2, "");
		node6 = new Node(6, "Node6", node3, "");
		node7 = new Node(7, "Node7", node3, "");
		node8 = new Node(8, "Node8", node4, "");
		node9 = new Node(9, "Node9", node4, "");
		node10 = new Node(10, "Node10", node5, "");
		node11 = new Node(11, "Node11", node5, "");
		node12 = new Node(12, "Node12", node8, "");
		node13 = new Node(13, "Node13", node8, "");
		node14 = new Node(14, "Node14", node9, "");
		node15 = new Node(15, "Node15", node9, "");
		
		edge1 = new Edge("Edge1", node1, node2, 1.0);
		edge2 = new Edge("Edge2", node1, node3, 1.0);
		edge3 = new Edge("Edge3", node2, node4, 1.0);
		edge4 = new Edge("Edge4", node2, node5, 1.0);
		edge5 = new Edge("Edge5", node3, node6, 1.0);
		edge6 = new Edge("Edge6", node3, node7, 1.0);
		edge7 = new Edge("Edge7", node4, node8, 1.0);
		edge8 = new Edge("Edge8", node4, node9, 1.0);
		edge9 = new Edge("Edge9", node5, node10, 1.0);
		edge10 = new Edge("Edge10", node5, node11, 1.0);
		edge11 = new Edge("Edge11", node8, node12, 1.0);
		edge12 = new Edge("Edge12", node8, node13, 1.0);
		edge13 = new Edge("Edge13", node9, node14, 1.0);
		edge14 = new Edge("Edge14", node9, node15, 1.0);
		
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		nodes.add(node6);
		nodes.add(node7);
		nodes.add(node8);
		nodes.add(node9);
		nodes.add(node10);
		nodes.add(node11);
		nodes.add(node12);
		nodes.add(node13);
		nodes.add(node14);
		nodes.add(node15);

		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);
		edges.add(edge5);
		edges.add(edge6);
		edges.add(edge7);
		edges.add(edge8);
		edges.add(edge9);
		edges.add(edge10);
		edges.add(edge11);
		edges.add(edge12);
		edges.add(edge13);
		edges.add(edge14);

		tree = new Tree(nodes,edges,node1);
	}
	
//	@Test
//	public void testGetAllChildren() {
//		assertEquals("", 14, node1.getAllChildren().size(), 0.0);
//		assertEquals("", 2, node3.getAllChildren().size(), 0.0);
//		assertEquals("", 0, node10.getAllChildren().size(), 0.0);
//		assertEquals("", 10, node2.getAllChildren().size(), 0.0);
//	}
	
	@Test
	public void testGetLeaves() {
		assertEquals("", 8, tree.getLeaves().size(), 0.0);
		
		assertEquals("", 8, tree.getLeaves(node1).size(), 0.0);
		assertEquals("", 6, tree.getLeaves(node2).size(), 0.0);
		assertEquals("", 2, tree.getLeaves(node3).size(), 0.0);
	}
	
	@Test
	public void testGetAllChildren() {
		assertEquals("", 15, tree.getAllNodes(node1).size(), 0.0);
		assertEquals("", 11, tree.getAllNodes(node2).size(), 0.0);
		assertEquals("", 3, tree.getAllNodes(node3).size(), 0.0);
	}
	
	@Test
	public void testGetParents() {
		assertEquals("", 0, tree.getAllParents(node1).size(), 0.0);
		assertEquals("", 1, tree.getAllParents(node2).size(), 0.0);
		assertEquals("", 1, tree.getAllParents(node3).size(), 0.0);
		assertEquals("", 2, tree.getAllParents(node4).size(), 0.0);
		assertEquals("", 2, tree.getAllParents(node5).size(), 0.0);
		assertEquals("", 3, tree.getAllParents(node8).size(), 0.0);
		assertEquals("", 3, tree.getAllParents(node9).size(), 0.0);
		assertEquals("", 4, tree.getAllParents(node14).size(), 0.0);
		assertEquals("", 4, tree.getAllParents(node15).size(), 0.0);
	}
	
	@Test
	public void testGetLeastCommonAncestor() {
		assertEquals("", node1, tree.getLeastCommonAncestor(node2, node15));
		assertEquals("", node1, tree.getLeastCommonAncestor(node7, node15));
		assertEquals("", node4, tree.getLeastCommonAncestor(node13, node9));
		assertEquals("", node1, tree.getLeastCommonAncestor(node1, node3));
		assertEquals("", node1, tree.getLeastCommonAncestor(node3, node1));
	}
	
}
