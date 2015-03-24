package be.kuleuven.rega.phylogeotool.distance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.kuleuven.rega.phylogeotool.distance.KMedoids;
import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.test.ExtendedRunner;
import be.kuleuven.rega.phylogeotool.test.Repeat;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;
import be.kuleuven.rega.phylogeotool.tree.tools.DistanceProvider;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;

@RunWith(ExtendedRunner.class)
public class KMedoidsTest {

	private Node node1 = null;
	private Node node2 = null;
	private Node node3 = null;
	private Node node4 = null;
	private Node node5 = null;
	private Node node6 = null;
	private Node node7 = null;
	private Node node8 = null;
	private Tree tree1 = null;
	private Tree tree2 = null;
	private Tree tree3 = null;
	private Tree tree4 = null;
	private Tree tree5 = null;
	private Tree tree6 = null;
	private Tree tree7 = null;
	private Tree tree8 = null;
	private List<ICluster> allData = null;
	private KMedoids kMedoids = null;
	private List<ICluster> medoids = null;
	private List<ICluster> datapoints = null;
	
	@Before
	public void init() throws IOException {
		NodeIndexProvider nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testNodeIndexProvider.csv"));
		DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testDistances.csv"));
		node1 = new Node("Node1", nodeIndexProvider);
		node2 = new Node("Node2", nodeIndexProvider);
		node3 = new Node("Node3", nodeIndexProvider);
		node4 = new Node("Node4", nodeIndexProvider);
		node5 = new Node("Node5", nodeIndexProvider);
		node6 = new Node("Node6", nodeIndexProvider);
		node7 = new Node("Node7", nodeIndexProvider);
		node8 = new Node("Node8", nodeIndexProvider);
		tree1 = new Tree(node1, distanceProvider);
		tree2 = new Tree(node2, distanceProvider);
		tree3 = new Tree(node3, distanceProvider);
		tree4 = new Tree(node4, distanceProvider);
		tree5 = new Tree(node5, distanceProvider);
		tree6 = new Tree(node6, distanceProvider);
		tree7 = new Tree(node7, distanceProvider);
		tree8 = new Tree(node8, distanceProvider);
		allData = new ArrayList<ICluster>();
		allData.add(tree1);
		allData.add(tree2);
		allData.add(tree3);
		allData.add(tree4);
		allData.add(tree5);
		allData.add(tree6);
		allData.add(tree7);
		allData.add(tree8);
		kMedoids = new KMedoids(3, 100, allData);
		medoids = new ArrayList<ICluster>();
		datapoints = new ArrayList<ICluster>();
	}

	@Test
	public void testAssociateDataPointsCornered() throws IOException {
		medoids.add(tree1);
		medoids.add(tree2);
		medoids.add(tree3);
		
		datapoints.add(tree1);
		datapoints.add(tree2);
		datapoints.add(tree3);
		datapoints.add(tree4);
		datapoints.add(tree5);
		datapoints.add(tree6);
		datapoints.add(tree7);
		datapoints.add(tree8);
		Map<ICluster,List<ICluster>> associationMap = kMedoids.associateDatapoints(medoids, datapoints);
		assertTrue(associationMap.keySet().contains(tree1));
		assertTrue(associationMap.keySet().contains(tree2));
		assertTrue(associationMap.keySet().contains(tree3));
		
		assertEquals(0,associationMap.get(tree1).size(),0.0);
		assertEquals(0,associationMap.get(tree2).size(),0.0);
		assertEquals(5,associationMap.get(tree3).size(),0.0);
	}

	@Test
	public void testAssociateDataPointsNormal() throws IOException {
		medoids.add(tree2);
		medoids.add(tree4);
		medoids.add(tree7);
		datapoints.add(tree1);
		datapoints.add(tree2);
		datapoints.add(tree3);
		datapoints.add(tree4);
		datapoints.add(tree5);
		datapoints.add(tree6);
		datapoints.add(tree7);
		datapoints.add(tree8);
		Map<ICluster,List<ICluster>> associationMap = kMedoids.associateDatapoints(medoids, datapoints);
		assertTrue(associationMap.keySet().contains(tree2));
		assertTrue(associationMap.keySet().contains(tree4));
		assertTrue(associationMap.keySet().contains(tree7));
		
		assertEquals(2,associationMap.get(tree2).size(),0.0);
		assertEquals(1,associationMap.get(tree4).size(),0.0);
		assertEquals(2,associationMap.get(tree7).size(),0.0);
	}
	
	@Test
	public void testDetermineNewMedoidsOneCluster() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree2);
		value1.add(tree3);
		value1.add(tree4);
		value1.add(tree5);
		value1.add(tree7);
		value1.add(tree8);
		associationMap.put(tree1, value1);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(1,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree3));
	}
	
	@Test
	public void testDetermineNewMedoidsTwoClusters() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree2);
		value1.add(tree3);
		value1.add(tree4);
		List<ICluster> value2 = new ArrayList<ICluster>();
		value2.add(tree5);
		value2.add(tree6);
		value2.add(tree8);
		associationMap.put(tree1, value1);
		associationMap.put(tree7, value2);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(2,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree3));
		assertTrue(newMedoids.contains(tree8));
		
	}
	
	@Test
	public void testDetermineNewMedoidsThreeClusters() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree2);
		value1.add(tree3);
		List<ICluster> value2 = new ArrayList<ICluster>();
		value2.add(tree5);
		List<ICluster> value3 = new ArrayList<ICluster>();
		value3.add(tree7);
		value3.add(tree8);
		associationMap.put(tree1, value1);
		associationMap.put(tree4, value2);
		associationMap.put(tree6, value3);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(3,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree2));
		assertTrue(newMedoids.contains(tree4));
		assertTrue(newMedoids.contains(tree7));
	}
	
	@Test
	public void testDetermineNewMedoidsBigCluster() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree2);
		List<ICluster> value2 = new ArrayList<ICluster>();
		value2.add(tree6);
		value2.add(tree7);
		List<ICluster> value3 = new ArrayList<ICluster>();
		value3.add(tree5);
		value3.add(tree8);
		associationMap.put(tree1, value1);
		associationMap.put(tree3, value2);
		associationMap.put(tree4, value3);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(3,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree1));
		assertTrue(newMedoids.contains(tree5));
		assertTrue(newMedoids.contains(tree6));
	}
	
	@Test
	public void testDetermineNewMedoidsOneClusterEmpty() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree8);
		List<ICluster> value2 = new ArrayList<ICluster>();
		value2.add(tree1);
		value2.add(tree2);
		value2.add(tree4);
		value2.add(tree5);
		List<ICluster> value3 = new ArrayList<ICluster>();
		associationMap.put(tree7, value1);
		associationMap.put(tree3, value2);
		associationMap.put(tree6, value3);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(3,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree3));
		assertTrue(newMedoids.contains(tree6));
		assertTrue(newMedoids.contains(tree7));
	}
	
	@Test
	public void testDetermineNewMedoidsAlmostEnd() throws IOException {
		Map<ICluster,List<ICluster>> associationMap = new HashMap<ICluster,List<ICluster>>();
		List<ICluster> value1 = new ArrayList<ICluster>();
		value1.add(tree1);
		value1.add(tree3);
		List<ICluster> value2 = new ArrayList<ICluster>();
		value2.add(tree5);
		List<ICluster> value3 = new ArrayList<ICluster>();
		value3.add(tree7);
		value3.add(tree8);
		associationMap.put(tree2, value1);
		associationMap.put(tree4, value2);
		associationMap.put(tree6, value3);
		List<ICluster> newMedoids = kMedoids.determineNewMedoids(associationMap);
		
		assertEquals(3,newMedoids.size(),0.0);
		assertTrue(newMedoids.contains(tree2));
		assertTrue(newMedoids.contains(tree4));
		assertTrue(newMedoids.contains(tree7));
	}
	
	@Test
	@Repeat(100)
	public void testKMedoids() {
		Map<ICluster,List<ICluster>> associationMap = kMedoids.calculate(false);
		assertTrue(associationMap.keySet().contains(tree2));
		assertTrue(associationMap.keySet().contains(tree4) || associationMap.keySet().contains(tree5));
		assertTrue(associationMap.keySet().contains(tree7));
	}
}
