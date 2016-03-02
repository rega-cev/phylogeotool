package be.kuleuven.rega.phylogeotool.pplacer.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.pplacer.JobScheduler;
import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;

public class PPlacerTest {

	private PPlacer pplacer;
	
	@Before
	public void before() {
		List<String> introducedSequences = new ArrayList<String>();
		introducedSequences.add("B.JP.-.KI_811.AB874130");
		introducedSequences.add("C.ZA.1998.TV001_patent.AX455917");
		introducedSequences.add("G.ES.2014.ARP1201.KT276261");
		JobScheduler jobScheduler = new JobScheduler();
		String tempDir = jobScheduler.createTempDirs();
		String pplacedTree = jobScheduler.doPPlacing(tempDir, "/Users/ewout/git/phylogeotool/test/phylo.tree", 
				"/Users/ewout/git/phylogeotool/test/alignment.fasta", "/Users/ewout/git/phylogeotool/test/sequences.fasta", 
				"/Users/ewout/git/phylogeotool/test/logfile.log");
		pplacer = new PPlacer(pplacedTree, introducedSequences);
	}
	
	@Test
	public void testSequenceNeigbour() {
		for(Entry<Node, Node> entry:pplacer.getNeighbourSeqs().entrySet()) {
			if(entry.getKey().getLabel().equals("B.JP.-.KI_811.AB874130")) {
				assertEquals("B.JP.2004.04JPDR6075B.AB221125", entry.getValue().getLabel());
				assertEquals(20, entry.getValue().getId());
			} else if(entry.getKey().getLabel().equals("C.ZA.1998.TV001_patent.AX455917")) {
				assertEquals("C.ZM.2002.02ZM108.AB254141", entry.getValue().getLabel());
				assertEquals(28, entry.getValue().getId());
			} else if(entry.getKey().getLabel().equals("G.ES.2014.ARP1201.KT276261")) {
				assertEquals("", entry.getValue().getLabel());
				assertEquals(2, entry.getValue().getId());
			}
		}
		
	}
	
	@Test
	public void testClusterContainsSequence() {
		Cluster cluster = new Cluster(pplacer.getTree(), pplacer.getTree().getRootNode(), pplacer.getTree().getLeaves());
		assertEquals(true, pplacer.clusterContainsPPlacerSequence(cluster));
	}
}
