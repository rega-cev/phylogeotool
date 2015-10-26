package be.kuleuven.rega.phylogeotool.distance.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.runner.RunWith;

import be.kuleuven.rega.phylogeotool.distance.KMedoids;
import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.test.ExtendedRunner;
import be.kuleuven.rega.phylogeotool.tree.Edge;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

@RunWith(ExtendedRunner.class)
public class KMedoidsTest {

	private Node t1 = new Node("t1");
	private Node t2 = new Node("t2");
	private Node t3 = new Node("t3");
	private Node t4 = new Node("t4");
	private Node t5 = new Node("t5");
	private Node t6 = new Node("t6");
	private Node t7 = new Node("t7");
	private Node t8 = new Node("t8");
	private Node t9 = new Node("t9");
	private Node t10 = new Node("t10");
	private Node t11 = new Node("t11");
	private Node t12 = new Node("t12");
	private Node t13 = new Node("t13");
	private Node t14 = new Node("t14");
	private Node t15 = new Node("t15");
	private Node t16 = new Node("t16");
	private Node t17 = new Node("t17");
	private Node t18 = new Node("t18");
	private Node t19 = new Node("t19");
	
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
	
	@Before
	public void before() {
		
		t19.addChild(t13);
		t19.addChild(t18);
		t13.setParent(t19);
		t18.setParent(t19);
		
		t13.addChild(t11);
		t13.addChild(t8);
		t11.setParent(t13);
		t8.setParent(t13);
		
		t18.addChild(t14);
		t18.addChild(t17);
		t14.setParent(t18);
		t17.setParent(t18);
		
		t11.addChild(t3);
		t11.addChild(t12);
		t3.setParent(t11);
		t12.setParent(t11);
		
		t17.addChild(t16);
		t17.addChild(t5);
		t16.setParent(t17);
		t5.setParent(t17);
		
		t14.addChild(t4);
		t14.addChild(t9);
		t4.setParent(t14);
		t9.setParent(t14);
		
		t12.addChild(t1);
		t12.addChild(t10);
		t1.setParent(t12);
		t10.setParent(t12);
		
		t16.addChild(t15);
		t16.addChild(t2);
		t15.setParent(t16);
		t2.setParent(t16);
		
		t15.addChild(t6);
		t15.addChild(t7);
		t6.setParent(t15);
		t7.setParent(t15);
		
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
		
		Tree tree = new Tree(nodes, edges, t19);
		
		kMedoids = new KMedoids(3, 100, tree);
		medoids = new ArrayList<ICluster>();
		datapoints = new ArrayList<ICluster>();
	}
	
	private KMedoids kMedoids = null;
	private List<ICluster> medoids = null;
	private List<ICluster> datapoints = null;
	
//	@Before
//	public void init() throws IOException {
//		NodeIndexProvider nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testNodeIndexProvider.csv"));
//		DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testDistances.csv"));
//		node1 = new Node("Node1", nodeIndexProvider);
//		node2 = new Node("Node2", nodeIndexProvider);
//		node3 = new Node("Node3", nodeIndexProvider);
//		node4 = new Node("Node4", nodeIndexProvider);
//		node5 = new Node("Node5", nodeIndexProvider);
//		node6 = new Node("Node6", nodeIndexProvider);
//		node7 = new Node("Node7", nodeIndexProvider);
//		node8 = new Node("Node8", nodeIndexProvider);
//		
//		node1 = new Node("Node1", 1);
//		node2 = new Node("Node2", 2);
//		node3 = new Node("Node3", 3);
//		node4 = new Node("Node4", 4);
//		node5 = new Node("Node5", 5);
//		node6 = new Node("Node6", 6);
//		node7 = new Node("Node7", 7);
//		node8 = new Node("Node8", 8);
//		
//		tree1 = new Tree(node1, distanceProvider);
//		tree2 = new Tree(node2, distanceProvider);
//		tree3 = new Tree(node3, distanceProvider);
//		tree4 = new Tree(node4, distanceProvider);
//		tree5 = new Tree(node5, distanceProvider);
//		tree6 = new Tree(node6, distanceProvider);
//		tree7 = new Tree(node7, distanceProvider);
//		tree8 = new Tree(node8, distanceProvider);
//		allData = new ArrayList<ICluster>();
//		allData.add(tree1);
//		allData.add(tree2);
//		allData.add(tree3);
//		allData.add(tree4);
//		allData.add(tree5);
//		allData.add(tree6);
//		allData.add(tree7);
//		allData.add(tree8);
//		
//		Tree tree = new Tree();
//		
//		kMedoids = new KMedoids(3, 100, allData);
//		medoids = new ArrayList<ICluster>();
//		datapoints = new ArrayList<ICluster>();
//	}
//
//	@Test
//	public void testAssociateDataPointsCornered() throws IOException {
//		medoids.add(tree1);
//		medoids.add(tree2);
//		medoids.add(tree3);
//		
//		datapoints.add(tree1);
//		datapoints.add(tree2);
//		datapoints.add(tree3);
//		datapoints.add(tree4);
//		datapoints.add(tree5);
//		datapoints.add(tree6);
//		datapoints.add(tree7);
//		datapoints.add(tree8);
//		Map<ICluster,List<ICluster>> associationMap = kMedoids.associateDatapoints(medoids, datapoints);
//		assertTrue(associationMap.keySet().contains(tree1));
//		assertTrue(associationMap.keySet().contains(tree2));
//		assertTrue(associationMap.keySet().contains(tree3));
//		
//		assertEquals(0,associationMap.get(tree1).size(),0.0);
//		assertEquals(0,associationMap.get(tree2).size(),0.0);
//		assertEquals(5,associationMap.get(tree3).size(),0.0);
//	}
//
//	@Test
//	public void testAssociateDataPointsNormal() throws IOException {
//		medoids.add(tree2);
//		medoids.add(tree4);
//		medoids.add(tree7);
//		datapoints.add(tree1);
//		datapoints.add(tree2);
//		datapoints.add(tree3);
//		datapoints.add(tree4);
//		datapoints.add(tree5);
//		datapoints.add(tree6);
//		datapoints.add(tree7);
//		datapoints.add(tree8);
//		Map<ICluster,List<ICluster>> associationMap = kMedoids.associateDatapoints(medoids, datapoints);
//		assertTrue(associationMap.keySet().contains(tree2));
//		assertTrue(associationMap.keySet().contains(tree4));
//		assertTrue(associationMap.keySet().contains(tree7));
//		
//		assertEquals(2,associationMap.get(tree2).size(),0.0);
//		assertEquals(1,associationMap.get(tree4).size(),0.0);
//		assertEquals(2,associationMap.get(tree7).size(),0.0);
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsOneCluster() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree2);
//		value1.add(tree3);
//		value1.add(tree4);
//		value1.add(tree5);
//		value1.add(tree7);
//		value1.add(tree8);
//		associationMap.put(tree1, value1);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(1,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree3));
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsTwoClusters() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree2);
//		value1.add(tree3);
//		value1.add(tree4);
//		List<ICluster> value2 = new ArrayList<ICluster>();
//		value2.add(tree5);
//		value2.add(tree6);
//		value2.add(tree8);
//		associationMap.put(tree1, value1);
//		associationMap.put(tree7, value2);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(2,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree3));
//		assertTrue(newMedoids.contains(tree8));
//		
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsThreeClusters() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree2);
//		value1.add(tree3);
//		List<ICluster> value2 = new ArrayList<ICluster>();
//		value2.add(tree5);
//		List<ICluster> value3 = new ArrayList<ICluster>();
//		value3.add(tree7);
//		value3.add(tree8);
//		associationMap.put(tree1, value1);
//		associationMap.put(tree4, value2);
//		associationMap.put(tree6, value3);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(3,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree2));
//		assertTrue(newMedoids.contains(tree4));
//		assertTrue(newMedoids.contains(tree7));
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsBigCluster() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree2);
//		List<ICluster> value2 = new ArrayList<ICluster>();
//		value2.add(tree6);
//		value2.add(tree7);
//		List<ICluster> value3 = new ArrayList<ICluster>();
//		value3.add(tree5);
//		value3.add(tree8);
//		associationMap.put(tree1, value1);
//		associationMap.put(tree3, value2);
//		associationMap.put(tree4, value3);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(3,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree1));
//		assertTrue(newMedoids.contains(tree5));
//		assertTrue(newMedoids.contains(tree6));
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsOneClusterEmpty() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree8);
//		List<ICluster> value2 = new ArrayList<ICluster>();
//		value2.add(tree1);
//		value2.add(tree2);
//		value2.add(tree4);
//		value2.add(tree5);
//		List<ICluster> value3 = new ArrayList<ICluster>();
//		associationMap.put(tree7, value1);
//		associationMap.put(tree3, value2);
//		associationMap.put(tree6, value3);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(3,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree3));
//		assertTrue(newMedoids.contains(tree6));
//		assertTrue(newMedoids.contains(tree7));
//	}
//	
//	@Test
//	public void testDetermineNewMedoidsAlmostEnd() throws IOException {
//		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
//		List<ICluster> value1 = new ArrayList<ICluster>();
//		value1.add(tree1);
//		value1.add(tree3);
//		List<ICluster> value2 = new ArrayList<ICluster>();
//		value2.add(tree5);
//		List<ICluster> value3 = new ArrayList<ICluster>();
//		value3.add(tree7);
//		value3.add(tree8);
//		associationMap.put(tree2, value1);
//		associationMap.put(tree4, value2);
//		associationMap.put(tree6, value3);
//		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
//		
//		assertEquals(3,newMedoids.size(),0.0);
//		assertTrue(newMedoids.contains(tree2));
//		assertTrue(newMedoids.contains(tree4));
//		assertTrue(newMedoids.contains(tree7));
//	}
//	
//	@Test
//	@Repeat(100)
//	public void testKMedoids() {
//		Map<ICluster,List<ICluster>> associationMap = kMedoids.calculate(false);
//		assertTrue(associationMap.keySet().contains(tree2));
//		assertTrue(associationMap.keySet().contains(tree4) || associationMap.keySet().contains(tree5));
//		assertTrue(associationMap.keySet().contains(tree7));
//	}
}
