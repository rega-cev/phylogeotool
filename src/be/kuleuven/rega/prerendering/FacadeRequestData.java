package be.kuleuven.rega.prerendering;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tree.distance.DistanceInterface;

public class FacadeRequestData {

	private PreRendering preRendering;
	private File csvFile;
	private DistanceInterface distanceInterface;
	private int minimumClusterSize;
	private boolean showNAData = false;
	
	public FacadeRequestData(PreRendering preRendering) {
		this.preRendering = preRendering;
		ReadTree.setJeblTree(Settings.getInstance().getPhyloTree());
		ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
	}
	
	//TODO: Needs a jeblTree
//	public FacadeRequestData(Tree tree, File csvFile, DistanceInterface distanceInterface) {
//		this.tree = tree;
//		this.csvFile = csvFile;
//		this.distanceInterface = distanceInterface;
//	}
	
	public Cluster getCluster(String clusterId) {
		if(preRendering != null) {
			if(ReadTree.getTreeDrawTree() != null) {
				return preRendering.getClusterFromXML(ReadTree.getTreeDrawTree(), clusterId);
			} else {
				return preRendering.getClusterFromXML(clusterId);
			}
		} else {
			// TODO: Change the nr 12 and add SDRCalculations
//			return MidRootCluster.calculate(tree, tree.getNodeById(Integer.parseInt(clusterId)), new ClusterSizeComparator(tree), minimumClusterSize, 12);
			return BestClusterMultiThread.getBestCluster(Paths.get(Settings.getInstance().getRBinary()), Paths.get(Settings.getInstance().getScriptFolder()), Paths.get(Settings.getInstance().getBasePath()), minimumClusterSize, 50, 2, ReadTree.getTreeDrawTree(), ReadTree.getTreeDrawTree().getNodeById(Integer.parseInt(clusterId)), distanceInterface);
		}
	}
	
	// TODO: Implement this method
	public HashMap<String, Integer> readCsv(int nodeid, String key) {
		if(preRendering != null) {
			return preRendering.readCsv(nodeid, key, showNAData);
		} else {
			List<String> ids = new ArrayList<String>();
			
			for(Node node:ReadTree.getTreeDrawTree().getLeaves(ReadTree.getTreeDrawTree().getNodeById(nodeid))) {
				ids.add(node.getLabel());
			}
			
			return CsvUtils.csvToHashMapStringInteger(csvFile, ';', ids, key, showNAData);
		}
	}
	
	public void setShowNAData(boolean showNAData) {
		this.showNAData = showNAData;
	}
	
	public boolean isShowNAData() {
		return this.showNAData;
	}
}
