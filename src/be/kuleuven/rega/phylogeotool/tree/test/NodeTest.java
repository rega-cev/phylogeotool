//package be.kuleuven.rega.phylogeotool.tree.test;
//
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import be.kuleuven.rega.phylogeotool.tree.Node;
//
//public class NodeTest {
//
//	private Node node1 = new Node();
//	private Node node2 = new Node();
//	private Node node3 = new Node();
//	private Node node4 = new Node();
//	private Node node5 = new Node();
//	private Node node6 = new Node();
//	private Node node7 = new Node();
//	private Node node8 = new Node();
//	private Node node9 = new Node();
//	private Node node10 = new Node();
//	private Node node11 = new Node();
//	private Node node12 = new Node();
//	private Node node13 = new Node();
//	private Node node14 = new Node();
//	private Node node15 = new Node();
//
//	@Before
//	public void init() {
//		node1.setLabel("Node1");
//		node2.setLabel("Node2");
//		node3.setLabel("Node3");
//		node4.setLabel("Node4");
//		node5.setLabel("Node5");
//		node6.setLabel("Node6");
//		node7.setLabel("Node7");
//		node8.setLabel("Node8");
//		node9.setLabel("Node9");
//		node10.setLabel("Node10");
//		node11.setLabel("Node11");
//		node12.setLabel("Node12");
//		node13.setLabel("Node13");
//		node14.setLabel("Node14");
//		node15.setLabel("Node15");
//		
//		node1.addChild(node2);
//		node1.addChild(node3);
//
//		node2.addChild(node4);
//		node2.addChild(node5);
//
//		node3.addChild(node6);
//		node3.addChild(node7);
//
//		node4.addChild(node8);
//		node4.addChild(node9);
//
//		node5.addChild(node10);
//		node5.addChild(node11);
//
//		node8.addChild(node12);
//		node8.addChild(node13);
//
//		node9.addChild(node14);
//		node9.addChild(node15);
//
//	}
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
//
//}
