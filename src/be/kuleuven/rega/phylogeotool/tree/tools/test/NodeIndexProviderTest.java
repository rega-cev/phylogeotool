package be.kuleuven.rega.phylogeotool.tree.tools.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;

public class NodeIndexProviderTest {

	@Test
	public void testGetIndexEasy() {
		NodeIndexProvider nodeIndexProvider;
		try {
			nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/outputSubGRemovedHighMutRegion.csv"));
			Node node1 = new Node("10030_66215_2002", nodeIndexProvider);
			Node node2 = new Node("10071_92658_2003", nodeIndexProvider);
			assertEquals(2, nodeIndexProvider.getIndex(node1.getLabel()));
			assertEquals(3, nodeIndexProvider.getIndex(node2.getLabel()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetIndex() {
		NodeIndexProvider nodeIndexProvider;
		try {
			nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/outputSubGRemovedHighMutRegion.csv"));
			Node node1 = new Node("48533_153627_2007", nodeIndexProvider);
			Node node2 = new Node("759671_BM78428_2013", nodeIndexProvider);
			assertEquals(2304, nodeIndexProvider.getIndex(node1.getLabel()));
			assertEquals(3135, nodeIndexProvider.getIndex(node2.getLabel()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
