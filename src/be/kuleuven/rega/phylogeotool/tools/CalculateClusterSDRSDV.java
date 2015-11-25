package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.clustering.ClusterAlgos;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class CalculateClusterSDRSDV {

	private class ReturnObject {
		private double SDR;
		private double SDV;
		
		public ReturnObject(double SDR, double SDV) {
			this.SDR = SDR;
			this.SDV = SDV;
		}
		
		public double getSDR() {
			return this.SDR;
		}
		
		public double getSDV() {
			return this.SDV;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		String treeLocation = null;
		int nrClusters = 0;
		int minimumClusterSize = 2;
		
		if(args.length > 1) {
			treeLocation = args[0];
			nrClusters = new Integer(args[1]);
		} else {
			System.err.println("java -jar CalculateClustersSDRSDV.jar phylo.tree nrClusters");
			System.exit(0);
		}
		
		CalculateClusterSDRSDV calculateClusterSDRSDV = new CalculateClusterSDRSDV();
		Tree tree = calculateClusterSDRSDV.init(treeLocation);
		ClusterAlgos clusterAlgos = new ClusterAlgos();
		Tree tempTree = clusterAlgos.getCluster(tree, tree.getRootNode(), nrClusters, 2);
		ReturnObject returnObject = calculateClusterSDRSDV.calculateStatistics(tree, tempTree, minimumClusterSize);
		
		System.out.println("SDR: " + returnObject.getSDR());
		System.out.println("SDV: " + returnObject.getSDV());
	}
	
	private Tree init(String treeLocation) throws FileNotFoundException {
		jebl.evolution.trees.Tree jeblTree = ReadNexusTree.readNexusTree(new FileReader(treeLocation));
		
		Tree tree = ReadNexusTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
		return tree;
	}
	
	private ReturnObject calculateStatistics(Tree tree, List<Node> clusters) {
		double inter = tree.getInterClusterPairwiseDistance(clusters);
		
		List<Double> intraClusterDistances = new ArrayList<Double>();
		List<Double> intraOnInterClusterDistances = new ArrayList<Double>();
		
		for (int i = 0; i < clusters.size(); i++) {
			double intra = tree.getIntraPairwiseDistance(clusters.get(i));

			intraClusterDistances.add(intra);
			intraOnInterClusterDistances.add(intra/inter);
		}
		
		ReturnObject returnObject = new ReturnObject(this.calculateAverage(intraClusterDistances)/inter, getVariance(intraOnInterClusterDistances));
		
		return returnObject;
	}
	
	private ReturnObject calculateStatistics(Tree tree, Tree clusteredTree, int minimumClusterSize) {
		List<Node> clusters = clusteredTree.getAcceptableClusters(minimumClusterSize);
		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		System.out.println("Inter: " + inter);
		List<Double> intraClusterDistances = new ArrayList<Double>();
		List<Double> intraOnInterClusterDistances = new ArrayList<Double>();
		for (int i = 0; i < clusters.size(); i++) {
			double intra = tree.getIntraPairwiseDistance(clusters.get(i));
//			System.out.println("intra: " + intra);
			intraClusterDistances.add(intra);
			intraOnInterClusterDistances.add(intra/inter);
		}
		ReturnObject returnObject = new ReturnObject(this.calculateAverage(intraClusterDistances)/inter, getVariance(intraOnInterClusterDistances));
		
		return returnObject;
	}
	
	private double calculateAverage(List<Double> distances) {
		Double sum = 0.0D;
		if (!distances.isEmpty()) {
			for (Double distance : distances) {
				sum += distance;
			}
			return sum.doubleValue() / distances.size();
		}
		return sum;
	}
	
	private double getVariance(List<Double> values) {
		double d1 = calculateAverage(values);
		double d2 = 0.0D;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) != 0.0D) {
				d2 += Math.pow(values.get(i) - d1, 2.0D);
			}
		}
		return d2 / (values.size() - 1);
	}
	
}
