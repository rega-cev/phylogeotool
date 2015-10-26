package be.kuleuven.rega.phylogeotool.tree.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;

//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
public class NodeTest {

	private Node node1 = new Node("Node1", 1);
	private Node node2 = new Node("Node2", 2);
	private Node node3 = new Node("Node3", 3);
	private Node node4 = new Node("Node4", 4);
	private Node node5 = new Node("Node5", 5);
	private Node node6 = new Node("Node6", 6);
	private Node node7 = new Node("Node7", 7);
	private Node node8 = new Node("Node8", 8);
	private Node node9 = new Node("Node9", 9);
	private Node node10 = new Node("Node10", 10);
	private Node node11 = new Node("Node11", 11);
	private Node node12 = new Node("Node12", 12);
	private Node node13 = new Node("Node13", 13);
	private Node node14 = new Node("Node14", 14);
	private Node node15 = new Node("Node15", 15);
	
	private List<Node> nodes = new ArrayList<Node>();
	
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
	
	private List<Edge> edges = new ArrayList<Edge>();

	@Before
	public void init() {
		node1.addChild(node2);
		node2.setParent(node1);
		node1.addChild(node3);
		node3.setParent(node1);

		edge1 = new Edge("Edge1", node1, node2, 1.0);
		edge2 = new Edge("Edge2", node1, node3, 1.0);
		
		node2.addChild(node4);
		node4.setParent(node2);
		node2.addChild(node5);
		node5.setParent(node2);
		
		edge3 = new Edge("Edge3", node2, node4, 1.0);
		edge4 = new Edge("Edge4", node2, node5, 1.0);

		node3.addChild(node6);
		node6.setParent(node3);
		node3.addChild(node7);
		node7.setParent(node3);

		edge5 = new Edge("Edge5", node3, node6, 1.0);
		edge6 = new Edge("Edge6", node3, node7, 1.0);
		
		node4.addChild(node8);
		node8.setParent(node4);
		node4.addChild(node9);
		node9.setParent(node4);
		
		edge7 = new Edge("Edge7", node4, node8, 1.0);
		edge8 = new Edge("Edge8", node4, node9, 1.0);

		node5.addChild(node10);
		node10.setParent(node5);
		node5.addChild(node11);
		node11.setParent(node5);
		
		edge9 = new Edge("Edge9", node5, node10, 1.0);
		edge10 = new Edge("Edge10", node5, node11, 1.0);

		node8.addChild(node12);
		node12.setParent(node8);
		node8.addChild(node13);
		node13.setParent(node8);

		edge11 = new Edge("Edge11", node8, node12, 1.0);
		edge12 = new Edge("Edge12", node8, node13, 1.0);
		
		node9.addChild(node14);
		node14.setParent(node9);
		node9.addChild(node15);
		node15.setParent(node9);
		
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
	}
	
	@Test
	public void testGetParents() {
		assertEquals("", 0, node1.getAllParents().size(), 0.0);
		assertEquals("", 1, node2.getAllParents().size(), 0.0);
		assertEquals("", 1, node3.getAllParents().size(), 0.0);
		assertEquals("", 2, node4.getAllParents().size(), 0.0);
		assertEquals("", 2, node5.getAllParents().size(), 0.0);
		assertEquals("", 3, node8.getAllParents().size(), 0.0);
		assertEquals("", 3, node9.getAllParents().size(), 0.0);
		assertEquals("", 4, node14.getAllParents().size(), 0.0);
		assertEquals("", 4, node15.getAllParents().size(), 0.0);
	}
	
	@Test
	public void testGetAllChildren() {
		assertEquals("", 14, node1.getAllChildren().size(), 0.0);
		assertEquals("", 2, node3.getAllChildren().size(), 0.0);
		assertEquals("", 0, node10.getAllChildren().size(), 0.0);
		assertEquals("", 10, node2.getAllChildren().size(), 0.0);
	}
//
//	@Test
//	public void testgetLeaves() {
//		Assert.assertEquals(8, node1.getLeaves().size());
//	}
//
//	@Test
//	public void testNodeBiggestCluster() {
//		List<Node> temp = node1.getChildrenBiggestFirst();
//		Assert.assertEquals(node2, temp.get(0));
//		Assert.assertEquals(node3, temp.get(1));
//
//		temp = node2.getChildrenBiggestFirst();
//		Assert.assertEquals(node4, temp.get(0));
//		Assert.assertEquals(node5, temp.get(1));
//
//	}

}
