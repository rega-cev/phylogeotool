package be.kuleuven.rega.phylogeotool.pplacer.test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;

public class PPlacerTest {

	private PPlacer pplacer;
//	private int[] initialClusters = {4742,4606,4416,4091,3898,3289,3012,2889,2886,2877,2876,2705,2704,2697,2690,2687,2662,2597,2584,2555,2496,1795,1785,1762,1736,1726,1723,990,986,913,839,603,32,21,6,2,7547,6828,6795,6439,6341,5610,5545,5419};
//	private String[] initialClusters = {"6438", "6381", "5544", "4605", "3008", "2889", "838", "31", "21", "6", "2", "7547"};
	
	@Before
	public void before() {
//		List<String> introducedSequences = new ArrayList<String>();
//		introducedSequences.add("TEST_1");
//		introducedSequences.add("TEST_2");
//		introducedSequences.add("TEST_3");
//		this.pplacer = new PPlacer("/Users/ewout/Documents/phylogeo/portugal/nodeIds", "/Users/ewout/Documents/phylogeo/portugal/pplacerIds/", "1.xml");
//		this.pplacer = new PPlacer("/Users/ewout/Documents/phylogeo/EUResist/nodeIds", "/Users/ewout/Documents/phylogeo/EUResist/pplacerIds", "1");
	}
	
/*	@Test
	public void testSequenceNeigbour() {
//		Node sequence1;
//		try {
//			sequence1 = pplacer.getTree().getLeafByLabel("1");
//		} catch (Exception e) {
//			System.out.println("Here");
//			System.err.println("Sequence 1 not found.");
//			sequence1 = null;
//		}
//		for(int clusterId:initialClusters) {
//			System.out.println("ClusterId: " + clusterId + ", " + pplacer.clusterContainsPPlacerSequence(sequence1, clusterId, "/Users/ewout/Documents/phylogeo/portugal/nodeIds"));
//		}
		
		Map<String, Boolean> clustersReturned = pplacer.clustersContainPPlacerSequence(Arrays.asList(initialClusters));
		
		for(String clusterId:clustersReturned.keySet()) {
			System.out.println("ClusterId: " + clusterId + " " + clustersReturned.get(clusterId));
		}
	} */
	
	@Test
	public void write() {
		List<String> introducedSequences = new ArrayList<String>();
		introducedSequences.add("TEST_1");
		introducedSequences.add("TEST_2");
		introducedSequences.add("TEST_3");
		try {
			//"/Users/ewout/Documents/phylogeo/portugal/nodeIds",
//			PPlacer.write("/Users/ewout/Documents/phylogeo/portugal/sequences.tog.tre", "/Users/ewout/Documents/phylogeo/portugal/pplacer.xml", introducedSequences);
			PPlacer.write("/Users/ewout/Documents/pplacer/sequences.tog.solved.newick", "/Users/ewout/Documents/phylogeo/Configs/Portugal/pplacerIds/pplacer.xml", introducedSequences);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
//	@Test
//	public void read() {
//		//"/Users/ewout/Documents/phylogeo/portugal/nodeIds",
//		PPlacer.read("/Users/ewout/Documents/phylogeo/portugal/pplacer.xml");
//	}
}
