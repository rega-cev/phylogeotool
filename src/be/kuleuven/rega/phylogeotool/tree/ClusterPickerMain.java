package be.kuleuven.rega.phylogeotool.tree;

import java.io.StringReader;

import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.SimpleTree;
import jebl.evolution.trees.Tree;
import clusterPicking.BasicSequence;
import clusterPicking.ClusterPicker;
import edu.uci.ics.jung.algorithms.util.KMeansClusterer;


public class ClusterPickerMain {
	
	public static void main(String[] args) {
		ClusterPickerMain clusterPickerMain = new ClusterPickerMain();
		clusterPickerMain.run(args);
	}
	
	public void run(String[] args) {
//		String sequencesName 	= "/home/ewout_kul/Documents/fasttree/thesis/outputSubGRemovedHighMutRegion.fa";		//A_outgroup_refSeq.fas";
//		String treeName 		= "/home/ewout_kul/Documents/fasttree/thesis/newickTree.nh";			//ARefFT5.9.newick";
//		
//		double initialThres 	= 0.9;
//		double supportThres		= 0.9;
//		double geneticThres		= 4.5/100;
//		int	   largeClusterThres = 10;
//		
//		BasicSequence.setDifferenceType("gap");
//		
//		ClusterPicker cp = new ClusterPicker();
//		cp.setInitialSupportThres(initialThres);
//		cp.setSupportThres(supportThres);
//		cp.setGeneticThres(geneticThres);
//		cp.setLargeClusterThreshold(largeClusterThres);
//		cp.readSequences(sequencesName);
//		cp.readTree(treeName);
//		cp.processData();
//		System.out.println(cp.getFinalTree().trLine());
//		
//		for(clusterPicking.Tree tree:cp.getGoodTrees()) {
//			tree.trLine();
//		}
//		NewickImporter newickImporter = new NewickImporter(new StringReader(cp.getFinalTree().trLine()), false);
//		if(newickImporter.hasTree()) {
//			Tree tree = newickImporter.importNextTree();
//		}
//		KMeansClusterer kMeansClusterer = new KMeansClusterer<>();
//		// Create 20 clusters
//		kMeansClusterer.cluster(arg0, 20);
//		cp.writeResults();
		
//		DistanceMeasure distanceMeasure = new Distance
//		KMedoids kMedoids = new KMedoids(20, 1000, DistanceMeasure);
		
	}
	
}
