package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.tree.SimpleRootedTree;

public class CalculateSDRSDV {

//	TODO: Update this class
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
//	
//	public static void main(String[] args) throws FileNotFoundException {
//		
//		String treeLocation = null;
//		
//		if(args.length > 0) {
//			treeLocation = args[0];
//		} else {
//			System.err.println("java -jar CalculateSDRSDV phylo.tree");
//			System.exit(0);
//		}
//		
//		CalculateSDRSDV calculateSDRSDV = new CalculateSDRSDV();
//		Tree tree = calculateSDRSDV.init(treeLocation);
//		List<Node> clusters = new ArrayList<Node>();
//		for(Node node:tree.getNodes()) {
//			if(node.getAttribute() != null && node.getAttribute().contains("genotype")) {
//				clusters.add(node);
//			}
//		}
//		ReturnObject returnObject = calculateSDRSDV.calculateStatistics(tree, clusters);
//		
//		System.out.println("SDR: " + returnObject.getSDR());
//		System.out.println("SDV: " + returnObject.getSDV());
//	}
//	
//	private Tree init(String treeLocation) throws FileNotFoundException {
//		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
//		
//		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>(), null);
//		return tree;
//	}
//	
//	private ReturnObject calculateStatistics(Tree tree, List<Node> clusters) {
//		double inter = tree.getInterClusterPairwiseDistance(clusters);
//		
//		List<Double> intraClusterDistances = new ArrayList<Double>();
//		List<Double> intraOnInterClusterDistances = new ArrayList<Double>();
//		
//		for (int i = 0; i < clusters.size(); i++) {
//			double intra = tree.getIntraPairwiseDistance(clusters.get(i));
//
//			intraClusterDistances.add(intra);
//			intraOnInterClusterDistances.add(intra/inter);
//		}
//		
//		ReturnObject returnObject = new ReturnObject(this.calculateAverage(intraClusterDistances)/inter, getVariance(intraOnInterClusterDistances));
//		
//		return returnObject;
//	}
//	
//	private double calculateStatistics(Cluster cluster) {
//		List<Node> clusters = cluster.getAcceptableClusters(2);
//		double inter = cluster.getTree().getInterClusterPairwiseDistance(clusters);
////		System.out.println("Inter: " + inter);
//		List<Double> intraClusterDistances = new ArrayList<Double>();
//		for (int i = 0; i < clusters.size(); i++) {
//			double intra = cluster.getTree().getIntraPairwiseDistance(clusters.get(i));
////			System.out.println("intra: " + intra);
//			intraClusterDistances.add(intra);
//		}
//		return this.calculateAverage(intraClusterDistances)/inter;
//	}
//	
//	private double calculateAverage(List<Double> distances) {
//		Double sum = 0.0D;
//		if (!distances.isEmpty()) {
//			for (Double distance : distances) {
//				sum += distance;
//			}
//			return sum.doubleValue() / distances.size();
//		}
//		return sum;
//	}
//	
//	private double getVariance(List<Double> values) {
//		double d1 = calculateAverage(values);
//		double d2 = 0.0D;
//		for (int i = 0; i < values.size(); i++) {
//			if (values.get(i) != 0.0D) {
//				d2 += Math.pow(values.get(i) - d1, 2.0D);
//			}
//		}
//		return d2 / (values.size() - 1);
//	}
//	
}
