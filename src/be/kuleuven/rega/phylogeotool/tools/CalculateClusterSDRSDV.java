package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import jebl.evolution.trees.SimpleRootedTree;
import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceCalculateFromTree;
import be.kuleuven.rega.phylogeotool.tree.distance.TreeStatistics;

public class CalculateClusterSDRSDV {

//	private class ReturnObject {
//		private double SDR;
//		private double SDV;
//		
//		public ReturnObject(double SDR, double SDV) {
//			this.SDR = SDR;
//			this.SDV = SDV;
//		}
//		
//		public double getSDR() {
//			return this.SDR;
//		}
//		
//		public double getSDV() {
//			return this.SDV;
//		}
//	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		String treeLocation = null;
		int nrClusters = 0;
		
		if(args.length > 1) {
			treeLocation = args[0];
			nrClusters = new Integer(args[1]);
		} else {
			System.err.println("java -jar CalculateClustersSDRSDV.jar phylo.tree nrClusters");
			System.exit(0);
		}
		
		CalculateClusterSDRSDV calculateClusterSDRSDV = new CalculateClusterSDRSDV();
		Tree tree = calculateClusterSDRSDV.init(treeLocation);
		Cluster cluster = MidRootCluster.calculate(tree, tree.getRootNode(), null, new ClusterSizeComparator(tree), 2, nrClusters);
		double sdr = calculateClusterSDRSDV.calculateStatistics(cluster);
		
		System.out.println("SDR: " + sdr);
//		System.out.println("SDV: " + returnObject.getSDV());
	}
	
	private Tree init(String treeLocation) throws FileNotFoundException {
		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		return tree;
	}
	
	private Double calculateStatistics(Cluster cluster) {
		return TreeStatistics.calculateStatistics(cluster, new DistanceCalculateFromTree(), 2);
	}
}
