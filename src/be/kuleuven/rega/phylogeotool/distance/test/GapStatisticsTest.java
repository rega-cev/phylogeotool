package be.kuleuven.rega.phylogeotool.distance.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.tools.ReadNewickTree;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;

import com.opencsv.CSVReader;

public class GapStatisticsTest {

	private Tree tree;
	
	@Before
	public void init() {
		try {
			tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick")), new ArrayList<String>());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
//	
//	@Before
//	public void init() {
//		try {
//			tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/git/phylogeotool/lib/EwoutTrees/example.tree")));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} 
//	}
//	
//	private Node t1 = new Node("t1", 1);
//	private Node t2 = new Node("t2", 2);
//	private Node t3 = new Node("t3", 3);
//	private Node t4 = new Node("t4", 4);
//	private Node t5 = new Node("t5", 5);
//	private Node t6 = new Node("t6", 6);
//	private Node t7 = new Node("t7", 7);
//	private Node t8 = new Node("t8", 8);
//	private Node t9 = new Node("t9", 9);
//	private Node t10 = new Node("t10", 10);
//	private Node t11 = new Node("t11", 11);
//	private Node t12 = new Node("t12", 12);
//	private Node t13 = new Node("t13", 13);
//	private Node t14 = new Node("t14", 14);
//	private Node t15 = new Node("t15", 15);
//	private Node t16 = new Node("t16", 16);
//	private Node t17 = new Node("t17", 17);
//	private Node t18 = new Node("t18", 18);
//	private Node t19 = new Node("t19", 19);
//	
//	private Edge t19t18 = new Edge("t19t18", t19, t18, 0.9063808941);
//	private Edge t13t8 = new Edge("t13t8", t13, t8, 0.4848527387);
//	private Edge t13t11 = new Edge("t13t11", t13, t11, 0.5995814849);
//	private Edge t19t13 = new Edge("t19t13", t19, t13, 0.5236769456);
//	private Edge t18t17 = new Edge("t18t17", t18, t17, 0.6159315344);
//	private Edge t18t14 = new Edge("t18t14", t18, t14, 0.914607449);
//	private Edge t14t4 = new Edge("t14t4", t14, t4, 0.2882589418);
//	private Edge t14t9 = new Edge("t14t9", t14, t9, 0.6715812988);
//	private Edge t17t5 = new Edge("t17t5", t17, t5, 0.8888230831);
//	private Edge t17t16 = new Edge("t17t16", t17, t16, 0.4394014708);
//	private Edge t11t3 = new Edge("t11t3", t11, t3, 0.5569783391);
//	private Edge t11t12 = new Edge("t11t12", t11, t12, 0.7155418394);
//	private Edge t12t10 = new Edge("t12t10", t12, t10, 0.7785333549);
//	private Edge t12t1 = new Edge("t12t1", t12, t1, 0.6787646217);
//	private Edge t16t2 = new Edge("t16t2", t16, t2, 0.07695768401);
//	private Edge t16t15 = new Edge("t16t15", t16, t15, 0.1205861182);
//	private Edge t15t7 = new Edge("t15t7", t15, t7, 0.7195842071);
//	private Edge t15t6 = new Edge("t15t6", t15, t6, 0.9620062774);
//
//	private Set<Edge> edges;
//	private Set<Node> nodes;
//	
//	@Before
//	public void before() {
//		
//		t19.addChild(t13);
//		t19.addChild(t18);
//		t13.setParent(t19);
//		t18.setParent(t19);
//		t19.setSize(10);
//		
//		t13.addChild(t11);
//		t13.addChild(t8);
//		t11.setParent(t13);
//		t8.setParent(t13);
//		t13.setSize(4);
//		
//		t18.addChild(t14);
//		t18.addChild(t17);
//		t14.setParent(t18);
//		t17.setParent(t18);
//		t18.setSize(6);
//		
//		t11.addChild(t3);
//		t11.addChild(t12);
//		t3.setParent(t11);
//		t12.setParent(t11);
//		t11.setSize(3);
//		
//		t17.addChild(t16);
//		t17.addChild(t5);
//		t16.setParent(t17);
//		t5.setParent(t17);
//		t17.setSize(4);
//		
//		t14.addChild(t4);
//		t14.addChild(t9);
//		t4.setParent(t14);
//		t9.setParent(t14);
//		t14.setSize(2);
//		
//		t12.addChild(t1);
//		t12.addChild(t10);
//		t1.setParent(t12);
//		t10.setParent(t12);
//		t12.setSize(2);
//		
//		t16.addChild(t15);
//		t16.addChild(t2);
//		t15.setParent(t16);
//		t2.setParent(t16);
//		t16.setSize(3);
//		
//		t15.addChild(t6);
//		t15.addChild(t7);
//		t6.setParent(t15);
//		t7.setParent(t15);
//		t15.setSize(2);
//		
//		t5.setSize(1);
//		t2.setSize(1);
//		t7.setSize(1);
//		t6.setSize(1);
//		t9.setSize(1);
//		t4.setSize(1);
//		t8.setSize(1);
//		t10.setSize(1);
//		t1.setSize(1);
//		t3.setSize(1);
//		
//		edges = new HashSet<Edge>();
//		edges.add(t19t18);
//		edges.add(t13t8);
//		edges.add(t13t11);
//		edges.add(t19t13);
//		edges.add(t18t17);
//		edges.add(t18t14);
//		edges.add(t14t4);
//		edges.add(t14t9);
//		edges.add(t17t5);
//		edges.add(t17t16);
//		edges.add(t11t3);
//		edges.add(t11t12);
//		edges.add(t12t10);
//		edges.add(t12t1);
//		edges.add(t16t2);
//		edges.add(t16t15);
//		edges.add(t15t7);
//		edges.add(t15t6);
//
//		nodes = new HashSet<Node>();
//		nodes.add(t19);
//		nodes.add(t18);
//		nodes.add(t17);
//		nodes.add(t16);
//		nodes.add(t15);
//		nodes.add(t14);
//		nodes.add(t13);
//		nodes.add(t12);
//		nodes.add(t11);
//		nodes.add(t10);
//		nodes.add(t9);
//		nodes.add(t8);
//		nodes.add(t7);
//		nodes.add(t6);
//		nodes.add(t5);
//		nodes.add(t4);
//		nodes.add(t3);
//		nodes.add(t2);
//		nodes.add(t1);
//	
//		tree = new Tree(nodes, edges, t19);
//	}
//	
//	@Before
//	public void init() throws FileNotFoundException {
//		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.midpoint.tre"));
//		tree = ReadNewickTree.jeblToTreeDraw((SimpleRootedTree)jeblTree);
//	}
//	
//	@Test
//	public void gapStatisticsTest() {
//		GapStatistic gapStatistic = new GapStatistic();
//		MidRootCluster midRootCluster = new MidRootCluster(1);
//		Tree tempTree = tree;
//		for(int i = 1; i <= 10; i++) {
//			midRootCluster.setNumberOfIClusters(i);
//			tempTree = midRootCluster.calculate(tree, tree.getRootNode());
//			System.out.println("i: " + i + " Wk: " + gapStatistic.Wk(tree, tempTree, i));
//		}
//	}
//	
//	@Test
//	public void gapStatisticsTestR() {
//		System.out.print("plot(c(");
//		int k = 30;
//		for(int i = 1; i < k; i++) {
//			System.out.print(i + ",");
//		}
//		System.out.print(k + "), c(");
//		
//		GapStatistic gapStatistic = new GapStatistic();
//		MidRootCluster midRootCluster = new MidRootCluster(1);
//		Tree tempTree = tree;
//		for(int i = 1; i <= k; i++) {
//			midRootCluster.setNumberOfIClusters(i);
//			tempTree = midRootCluster.calculate(tree, tree.getRootNode());
//			System.out.print(gapStatistic.Wk(tree, tempTree, i));
//			System.out.print(",");
//		}
//		System.out.print("), type='l')");
////		plot(c(1,2,3,4,5,6,7,8,9,10),c(227.39184071478502, 9.614277118896428, 5.1708677137283345, 3.5930955928008337, 2.3063945179120835, 1.1678247861495834, 0.6831214502833334, 0.5231480768500001, 0.2802650807500001, 0.0), type='l')
//	}
//	
//	@Test
//	public void printDistances() {
//		double minDistance = Double.MAX_VALUE;
//		double maxDistance = Double.MIN_VALUE;
//		
//		HashMap<Double, Integer> values = new HashMap<Double, Integer>();
//		List<Double> valuesList = new ArrayList<Double>();
//		
//		for(Node node:tree.getLeaves()) {
//			Iterator<Node> nodesIterator = tree.getLeaves().iterator();
//			boolean foundNode = false;
//			while(nodesIterator.hasNext()) {
//				Node currentNode = nodesIterator.next();
//				if(currentNode.equals(node)) {
//					// Do distance calculations from here to the end.
//					foundNode = true;
//				}
//				
//				if(foundNode && !currentNode.equals(node)) {
////					System.out.println("Distance from: " + node.getLabel() + " to: " + currentNode.getLabel());
//					double distance = round(tree.getDistance(node, currentNode), 2);
////					System.out.println("Distance: " + distance);
//					if(values.containsKey(distance)) {
//						values.put(distance, values.get(distance) + 1);
//					} else {
//						values.put(distance, 1);
//					}
//					valuesList.add(distance);
//					
//					if(distance < minDistance) {
//						minDistance = distance;
//					}
//					
//					if(distance > maxDistance) {
//						maxDistance = distance;
//					}
//				}
//			}
//		}
//		
//		System.out.println("Min distance: " + minDistance);
//		System.out.println("Max distance: " + maxDistance);
//		int total = 0;
//		System.out.print("hist(c(");
////		for(double distance:values.keySet()) {
////			total += values.get(distance);
////			System.out.println("Distance: " + distance + " Value: " + values.get(distance));
////		}
//		for(double distance:valuesList) {
//			System.out.print(distance + ",");
//		}
//		System.out.print("))");
//	}
//	
//	@Test
//	public void printDistances() {
//		System.out.print("c(");
//		for(Node node:tree.getLeaves()) {
//			System.out.print(node.getLabel());
//			System.out.print(",");
//		}
//		System.out.print(")");
//	}
//	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
//	
//	@Test
//	public void testReadNodeList() {
//		List<Node> nodes = readNodeList(new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/sample1.csv"));
//		assertEquals("" ,1000, nodes.size(), 0.0);
//		assertEquals("23459", nodes.get(nodes.size() -1).getLabel());
//		assertEquals("22702", nodes.get(0).getLabel());
//	}
	
	public List<Node> readNodeList(File csvFile) {
		List<Node> nodes = new ArrayList<Node>();
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(csvFile),',');
			String labels[] = csvReader.readNext();
			
			for(String label:labels) {
				try {
					nodes.add(tree.getLeafByLabel(label));
				} catch (Exception e) {
					e.getMessage();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		};
		return nodes;
	}
	
	@Test
	public void printDistances() {
		
		Random random = new Random();
		HashMap<Integer, Node> leaves = tree.getLeavesMap();
		System.out.println("First Key: " + leaves.keySet().iterator().next());
		System.out.println("Value: " + leaves.get(7));
		PrintWriter distanceWriter = null;
		PrintWriter leafNameWriter = null;
		try {
			distanceWriter = new PrintWriter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/Statistics/distances.txt", "UTF-8");
			leafNameWriter = new PrintWriter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/Statistics/leafs.txt", "UTF-8");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i < 100000; i++) {
			Node node1 = null;
			Node node2 = null;
			try {
				int label1 = -1;
				int label2 = -1;
				
				do {
					label1 = random.nextInt(tree.getLeaves().size() - 1);
					label2 = random.nextInt(tree.getLeaves().size() - 1);
				} while (!leaves.containsKey(label1) || !leaves.containsKey(label2));
				
				node1 = leaves.get(label1);
//				System.out.println("Label 1: " + label1);
				node2 = leaves.get(label2);
//				System.out.println("Label 2: " + label2);
				
				double distance = tree.getDistance(node1, node2);
				distanceWriter.print(distance + ",");
				leafNameWriter.print(node1.getLabel() + "-" + node2.getLabel() + ",");
				
			} catch (Exception e) {
				System.out.println("Cannot find leaf with id: " + node1.getId() + " or id: " + node2.getId());
				// We need to find an extra one
				--i;
			}
		}
		
		distanceWriter.close();
		leafNameWriter.close();
	}
	
	@Test
	public void createDistanceMatrix() {
		double min = 0.018;
		double max = 0.808;
		Random random = new Random();
		PrintWriter printWriter = null;
		
		try {
			printWriter = new PrintWriter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/Statistics/distanceMatrix.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < tree.getLeaves().size(); i++) {
			for(int j = 0; j <= i; j++) {
				if(i == j) {
					printWriter.write(0 + "\n");
				} else {
					double randomValue = min + (max - min) * random.nextDouble();
					printWriter.write(randomValue + ";");
				}
			}
		}
		printWriter.close();
	}
}
