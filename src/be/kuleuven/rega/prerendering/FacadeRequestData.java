package be.kuleuven.rega.prerendering;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.clustering.MidRootCluster;
import be.kuleuven.rega.comparator.ClusterSizeComparator;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.core.Tree;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;

public class FacadeRequestData {

	private PreRendering preRendering;
	private Tree tree;
	private File csvFile;
	private DistanceInterface distanceInterface;
	private int minimumClusterSize;
	
	public FacadeRequestData(PreRendering preRendering) {
		this.preRendering = preRendering;
		this.tree = preRendering.getTreeFromXML("1");
	}
	
	public FacadeRequestData(Tree tree, File csvFile, DistanceInterface distanceInterface) {
		this.tree = tree;
		this.csvFile = csvFile;
		this.distanceInterface = distanceInterface;
	}
	
	public Cluster getCluster(String clusterId) {
		if(preRendering != null) {
			if(tree != null) {
				return preRendering.getClusterFromXML(tree, clusterId);
			} else {
				return preRendering.getClusterFromXML(clusterId);
			}
		} else {
			// TODO: Change the nr 12 and add SDRCalculations
			return MidRootCluster.calculate(tree, tree.getNodeById(Integer.parseInt(clusterId)), new ClusterSizeComparator(tree), minimumClusterSize, 12);
		}
	}
	
	// TODO: Implement this method
	public HashMap<String, Integer> readCsv(int nodeid, String key, boolean readNA) {
		if(preRendering != null) {
			return preRendering.readCsv(nodeid, key, readNA);
		} else {
			List<String> ids = new ArrayList<String>();
			
			for(Node node:tree.getLeaves(tree.getNodeById(nodeid))) {
				ids.add(node.getLabel());
			}
			
			return CsvUtils.csvToHashMapStringInteger(csvFile, ';', ids, key, readNA);
		}
	}
}
