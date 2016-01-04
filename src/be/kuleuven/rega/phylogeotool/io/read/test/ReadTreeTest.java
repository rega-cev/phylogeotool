package be.kuleuven.rega.phylogeotool.io.read.test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;

public class ReadTreeTest {

	private String standard_tree;
	private String pplaced_tree;
	private String pplaced_tree_3Seq;
	
	@Before
	public void init() {
		standard_tree = "(((1:0.7264115,2:0.1890329):3.0,((3:0.3115034,(4:0.3301704,(5:0.02515042,(6:0.5103078,7:0.5342037):0.481009):0.6893577):0.1339643):0.00997174,8:0.3722245):0.9042523):0.04831143,(((9:0.841895,10:0.5697972):0.1858073,((11:0.3322625,((12:0.8102205,(13:0.3534404,14:0.1435162):0.2430767):0.772287,(15:0.09886185,20:0.39403928):0.38372832):0.9000565):0.5827812,16:0.9382017):0.8880224):0.271917,((17:0.6410765,18:0.3540476):0.6862444,19:0.8806629):0.9612157):0.05155421);";
		pplaced_tree = "(((1:0.7264115,2:0.1890329):3.0,((3:0.3115034,(4:0.3301704,(TEST_1:0.4439269,(5:0.02515042,(6:0.5103078,7:0.5342037):0.481009):0.6847349):0.6893577):0.1339643):0.00997174,8:0.3722245):0.9042523):0.04831143,(((9:0.841895,10:0.5697972):0.1858073,((11:0.3322625,((12:0.8102205,(13:0.3534404,14:0.1435162):0.2430767):0.772287,(15:0.09886185,20:0.39403928):0.38372832):0.9000565):0.5827812,16:0.9382017):0.8880224):0.271917,((17:0.6410765,18:0.3540476):0.6862444,19:0.8806629):0.9612157):0.05155421);";
		pplaced_tree_3Seq = "(((1:0.7264115,2:0.1890329):3.0,((3:0.3115034,(4:0.3301704,(TEST_1:0.4439269,(5:0.02515042,(6:0.5103078,7:0.5342037):0.481009):0.6847349):0.6893577):0.1339643):0.00997174,8:0.3722245):0.9042523):0.04831143,((((9:0.841895,10:0.5697972):0.1858073,(((11:0.3322625,((12:0.8102205,(13:0.3534404,14:0.1435162):0.2430767):0.772287,(15:0.09886185,20:0.39403928):0.38372832):0.9000565):0.5827812,16:0.9382017),TEST_3:0.9):0.8880224):0.271917,((17:0.6410765,18:0.3540476):0.6862444,19:0.8806629):0.9612157),TEST_2:1.7):0.05155421);";
	}
	
	@Test
	public void testStandardTree() {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new StringReader(standard_tree));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, null);
		
		assertEquals("", 1, tree.getRootNode().getId(), 0.0);
		// 1
		assertEquals("", 2, tree.getNodeById(1).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 25, tree.getNodeById(1).getImmediateChildren().get(1).getId(), 0.0);
		// 2
		assertEquals("", 3, tree.getNodeById(2).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 8, tree.getNodeById(2).getImmediateChildren().get(1).getId(), 0.0);
		// 3
		assertEquals("", 4, tree.getNodeById(3).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 5, tree.getNodeById(3).getImmediateChildren().get(1).getId(), 0.0);
		// 5
		assertEquals("", 6, tree.getNodeById(5).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 7, tree.getNodeById(5).getImmediateChildren().get(1).getId(), 0.0);
		// 8
		assertEquals("", 9, tree.getNodeById(8).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 22, tree.getNodeById(8).getImmediateChildren().get(1).getId(), 0.0);
		// 9
		assertEquals("", 10, tree.getNodeById(9).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 11, tree.getNodeById(9).getImmediateChildren().get(1).getId(), 0.0);
		// 11
		assertEquals("", 12, tree.getNodeById(11).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 21, tree.getNodeById(11).getImmediateChildren().get(1).getId(), 0.0);
		// 12
		assertEquals("", 13, tree.getNodeById(12).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 16, tree.getNodeById(12).getImmediateChildren().get(1).getId(), 0.0);
		// 13
		assertEquals("", 14, tree.getNodeById(13).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 15, tree.getNodeById(13).getImmediateChildren().get(1).getId(), 0.0);
		// 16
		assertEquals("", 17, tree.getNodeById(16).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 20, tree.getNodeById(16).getImmediateChildren().get(1).getId(), 0.0);
		// 17
		assertEquals("", 18, tree.getNodeById(17).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 19, tree.getNodeById(17).getImmediateChildren().get(1).getId(), 0.0);
		// 25
		assertEquals("", 26, tree.getNodeById(25).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 37, tree.getNodeById(25).getImmediateChildren().get(1).getId(), 0.0);
		// 26
		assertEquals("", 27, tree.getNodeById(26).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 28, tree.getNodeById(26).getImmediateChildren().get(1).getId(), 0.0);
		// 28
		assertEquals("", 29, tree.getNodeById(28).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 36, tree.getNodeById(28).getImmediateChildren().get(1).getId(), 0.0);
		// 29
		assertEquals("", 30, tree.getNodeById(29).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 35, tree.getNodeById(29).getImmediateChildren().get(1).getId(), 0.0);
		// 30
		assertEquals("", 31, tree.getNodeById(30).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 34, tree.getNodeById(30).getImmediateChildren().get(1).getId(), 0.0);
		// 31
		assertEquals("", 32, tree.getNodeById(31).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 33, tree.getNodeById(31).getImmediateChildren().get(1).getId(), 0.0);
		// 37
		assertEquals("", 38, tree.getNodeById(37).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 39, tree.getNodeById(37).getImmediateChildren().get(1).getId(), 0.0);
	}
	
	@Test
	public void testPPlacedTree_noList() {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new StringReader(pplaced_tree));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, null);
	
		assertEquals("", 1, tree.getRootNode().getId(), 0.0);
		// 1
		assertEquals("", 2, tree.getNodeById(1).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 25, tree.getNodeById(1).getImmediateChildren().get(1).getId(), 0.0);
		// 2
		assertEquals("", 3, tree.getNodeById(2).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 8, tree.getNodeById(2).getImmediateChildren().get(1).getId(), 0.0);
		// 3
		assertEquals("", 4, tree.getNodeById(3).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 5, tree.getNodeById(3).getImmediateChildren().get(1).getId(), 0.0);
		// 5
		assertEquals("", 6, tree.getNodeById(5).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 7, tree.getNodeById(5).getImmediateChildren().get(1).getId(), 0.0);
		// 8
		assertEquals("", 9, tree.getNodeById(8).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 22, tree.getNodeById(8).getImmediateChildren().get(1).getId(), 0.0);
		// 9
		assertEquals("", 10, tree.getNodeById(9).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 11, tree.getNodeById(9).getImmediateChildren().get(1).getId(), 0.0);
		// 11
		assertEquals("", 12, tree.getNodeById(11).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 21, tree.getNodeById(11).getImmediateChildren().get(1).getId(), 0.0);
		// 12
		assertEquals("", 13, tree.getNodeById(12).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 16, tree.getNodeById(12).getImmediateChildren().get(1).getId(), 0.0);
		// 13
		assertEquals("", 14, tree.getNodeById(13).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 15, tree.getNodeById(13).getImmediateChildren().get(1).getId(), 0.0);
		// 16
		assertEquals("", 17, tree.getNodeById(16).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 20, tree.getNodeById(16).getImmediateChildren().get(1).getId(), 0.0);
		// 17
		assertEquals("", 18, tree.getNodeById(17).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 19, tree.getNodeById(17).getImmediateChildren().get(1).getId(), 0.0);
		// 25
		assertEquals("", 26, tree.getNodeById(25).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 39, tree.getNodeById(25).getImmediateChildren().get(1).getId(), 0.0);
		// 26
		assertEquals("", 27, tree.getNodeById(26).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 28, tree.getNodeById(26).getImmediateChildren().get(1).getId(), 0.0);
		// 28
		assertEquals("", 29, tree.getNodeById(28).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 38, tree.getNodeById(28).getImmediateChildren().get(1).getId(), 0.0);
		// 29
		assertEquals("", 30, tree.getNodeById(29).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 37, tree.getNodeById(29).getImmediateChildren().get(1).getId(), 0.0);
		// 30
		assertEquals("", 31, tree.getNodeById(30).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 36, tree.getNodeById(30).getImmediateChildren().get(1).getId(), 0.0);
		// 31
		assertEquals("", 32, tree.getNodeById(31).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 35, tree.getNodeById(31).getImmediateChildren().get(1).getId(), 0.0);
		// 32
		assertEquals("", 33, tree.getNodeById(32).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 34, tree.getNodeById(32).getImmediateChildren().get(1).getId(), 0.0);
		// 39
		assertEquals("", 40, tree.getNodeById(39).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 41, tree.getNodeById(39).getImmediateChildren().get(1).getId(), 0.0);
	}
	
	@Test
	public void testPPlacedTree_withList() {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new StringReader(pplaced_tree));
		List<String> pplacedNodes = new ArrayList<String>();
		pplacedNodes.add("TEST_1");
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, pplacedNodes);
	
		assertEquals("", 1, tree.getRootNode().getId(), 0.0);
		// 1
		assertEquals("", 2, tree.getNodeById(1).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 25, tree.getNodeById(1).getImmediateChildren().get(1).getId(), 0.0);
		// 2
		assertEquals("", 3, tree.getNodeById(2).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 8, tree.getNodeById(2).getImmediateChildren().get(1).getId(), 0.0);
		// 3
		assertEquals("", 4, tree.getNodeById(3).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 5, tree.getNodeById(3).getImmediateChildren().get(1).getId(), 0.0);
		// 5
		assertEquals("", 6, tree.getNodeById(5).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 7, tree.getNodeById(5).getImmediateChildren().get(1).getId(), 0.0);
		// 8
		assertEquals("", 9, tree.getNodeById(8).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 22, tree.getNodeById(8).getImmediateChildren().get(1).getId(), 0.0);
		// 9
		assertEquals("", 10, tree.getNodeById(9).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 11, tree.getNodeById(9).getImmediateChildren().get(1).getId(), 0.0);
		// 11
		assertEquals("", 12, tree.getNodeById(11).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 21, tree.getNodeById(11).getImmediateChildren().get(1).getId(), 0.0);
		// 12
		assertEquals("", 13, tree.getNodeById(12).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 16, tree.getNodeById(12).getImmediateChildren().get(1).getId(), 0.0);
		// 13
		assertEquals("", 14, tree.getNodeById(13).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 15, tree.getNodeById(13).getImmediateChildren().get(1).getId(), 0.0);
		// 16
		assertEquals("", 17, tree.getNodeById(16).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 20, tree.getNodeById(16).getImmediateChildren().get(1).getId(), 0.0);
		// 17
		assertEquals("", 18, tree.getNodeById(17).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 19, tree.getNodeById(17).getImmediateChildren().get(1).getId(), 0.0);
		// 25
		assertEquals("", 26, tree.getNodeById(25).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 37, tree.getNodeById(25).getImmediateChildren().get(1).getId(), 0.0);
		// 26
		assertEquals("", 27, tree.getNodeById(26).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 28, tree.getNodeById(26).getImmediateChildren().get(1).getId(), 0.0);
		// 28
		assertEquals("", 29, tree.getNodeById(28).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 36, tree.getNodeById(28).getImmediateChildren().get(1).getId(), 0.0);
		// 29
		assertEquals("", 40, tree.getNodeById(29).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 35, tree.getNodeById(29).getImmediateChildren().get(1).getId(), 0.0);
		// 30
		assertEquals("", 31, tree.getNodeById(30).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 34, tree.getNodeById(30).getImmediateChildren().get(1).getId(), 0.0);
		// 31
		assertEquals("", 32, tree.getNodeById(31).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 33, tree.getNodeById(31).getImmediateChildren().get(1).getId(), 0.0);
		// 37
		assertEquals("", 38, tree.getNodeById(37).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 39, tree.getNodeById(37).getImmediateChildren().get(1).getId(), 0.0);
		// 40
		assertEquals("", 30, tree.getNodeById(40).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 41, tree.getNodeById(40).getImmediateChildren().get(1).getId(), 0.0);
	}
	
	@Test
	public void testPPlacedTree_withBigList() {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new StringReader(pplaced_tree_3Seq));
		List<String> pplacedNodes = new ArrayList<String>();
		pplacedNodes.add("TEST_1");
		pplacedNodes.add("TEST_2");
		pplacedNodes.add("TEST_3");
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, pplacedNodes);
	
		assertEquals("", 1, tree.getRootNode().getId(), 0.0);
		// 1
		assertEquals("", 40, tree.getNodeById(1).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 25, tree.getNodeById(1).getImmediateChildren().get(1).getId(), 0.0);
		// 2
		assertEquals("", 3, tree.getNodeById(2).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 8, tree.getNodeById(2).getImmediateChildren().get(1).getId(), 0.0);
		// 3
		assertEquals("", 4, tree.getNodeById(3).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 5, tree.getNodeById(3).getImmediateChildren().get(1).getId(), 0.0);
		// 5
		assertEquals("", 6, tree.getNodeById(5).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 7, tree.getNodeById(5).getImmediateChildren().get(1).getId(), 0.0);
		// 8
		assertEquals("", 42, tree.getNodeById(8).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 22, tree.getNodeById(8).getImmediateChildren().get(1).getId(), 0.0);
		// 9
		assertEquals("", 10, tree.getNodeById(9).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 11, tree.getNodeById(9).getImmediateChildren().get(1).getId(), 0.0);
		// 11
		assertEquals("", 12, tree.getNodeById(11).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 21, tree.getNodeById(11).getImmediateChildren().get(1).getId(), 0.0);
		// 12
		assertEquals("", 13, tree.getNodeById(12).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 16, tree.getNodeById(12).getImmediateChildren().get(1).getId(), 0.0);
		// 13
		assertEquals("", 14, tree.getNodeById(13).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 15, tree.getNodeById(13).getImmediateChildren().get(1).getId(), 0.0);
		// 16
		assertEquals("", 17, tree.getNodeById(16).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 20, tree.getNodeById(16).getImmediateChildren().get(1).getId(), 0.0);
		// 17
		assertEquals("", 18, tree.getNodeById(17).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 19, tree.getNodeById(17).getImmediateChildren().get(1).getId(), 0.0);
		// 25
		assertEquals("", 26, tree.getNodeById(25).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 37, tree.getNodeById(25).getImmediateChildren().get(1).getId(), 0.0);
		// 26
		assertEquals("", 27, tree.getNodeById(26).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 28, tree.getNodeById(26).getImmediateChildren().get(1).getId(), 0.0);
		// 28
		assertEquals("", 29, tree.getNodeById(28).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 36, tree.getNodeById(28).getImmediateChildren().get(1).getId(), 0.0);
		// 29
		assertEquals("", 44, tree.getNodeById(29).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 35, tree.getNodeById(29).getImmediateChildren().get(1).getId(), 0.0);
		// 30
		assertEquals("", 31, tree.getNodeById(30).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 34, tree.getNodeById(30).getImmediateChildren().get(1).getId(), 0.0);
		// 31
		assertEquals("", 32, tree.getNodeById(31).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 33, tree.getNodeById(31).getImmediateChildren().get(1).getId(), 0.0);
		// 37
		assertEquals("", 38, tree.getNodeById(37).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 39, tree.getNodeById(37).getImmediateChildren().get(1).getId(), 0.0);
		// 40
		assertEquals("", 41, tree.getNodeById(40).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 2, tree.getNodeById(40).getImmediateChildren().get(1).getId(), 0.0);
		// 42
		assertEquals("", 43, tree.getNodeById(42).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 9, tree.getNodeById(42).getImmediateChildren().get(1).getId(), 0.0);
		// 44
		assertEquals("", 30, tree.getNodeById(44).getImmediateChildren().get(0).getId(), 0.0);
		assertEquals("", 45, tree.getNodeById(44).getImmediateChildren().get(1).getId(), 0.0);
	}
}
