package be.kuleuven.rega.phylogeotool.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.prerendering.PreRendering;

public class AddCsvInfo {
	private String treeLocation;
	
	public List<String> listFilesForFolder(final File folder) {
		List<String> clusterIds = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	    	if(FilenameUtils.getExtension(fileEntry.getName()).equals("xml")) {
	    		clusterIds.add(FilenameUtils.getBaseName(fileEntry.getName()));
	    	}
	    }
	    return clusterIds;
	}
	
	public void run() {
		treeLocation = "/Users/ewout/Documents/phylogeo/EUResist_POL/treebuilding_attempt1/besttree.arevir.pure.solved.newick";
		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Configs/euresist_pure");
		ReadTree.setJeblTree(treeLocation);
		ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
		
		final File folder = new File("/Users/ewout/Documents/phylogeo/Configs/euresist_pure/clusters");
		List<String> clusterIds = listFilesForFolder(folder);
		for(String clusterId:clusterIds) {
			Cluster cluster = preRendering.getClusterFromXML(clusterId);
			preRendering.prepareCSV(cluster.getRootId(), ReadTree.getTreeDrawTree().getLeaves(cluster.getRoot()), null, "/Users/ewout/Documents/phylogeo/TDR/attributes.final.merged.csv", false);
			System.err.println("Cluster: " + clusterId + ", done");
		}
	}
	
	public void run(String specificClusterId) {
		treeLocation = "/Users/ewout/Documents/phylogeo/EUResist_POL/treebuilding_attempt1/besttree.arevir.pure.solved.newick";
		PreRendering preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/Configs/euresist_pure");
		ReadTree.setJeblTree(treeLocation);
		ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
		
//		final File folder = new File("/Users/ewout/Documents/phylogeo/Configs/euresist_pure/clusters");
//		List<String> clusterIds = listFilesForFolder(folder);
		List<String> clusterIds = new ArrayList<String>();
		clusterIds.add(specificClusterId);
		for(String clusterId:clusterIds) {
			Cluster cluster = preRendering.getClusterFromXML(clusterId);
			preRendering.prepareCSV(cluster.getRootId(), ReadTree.getTreeDrawTree().getLeaves(cluster.getRoot()), null, "/Users/ewout/Documents/phylogeo/TDR/attributes.final.merged.csv", false);
			System.err.println("Cluster: " + clusterId + ", done");
		}
	}
	
	public void run(String treeLocation, String baseFolder, String csv) {
		PreRendering preRendering = new PreRendering(baseFolder);
		ReadTree.setJeblTree(treeLocation);
		ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
		
		final File folder = new File(baseFolder + "/clusters");
		List<String> clusterIds = listFilesForFolder(folder);
		for(String clusterId:clusterIds) {
			Cluster cluster = preRendering.getClusterFromXML(clusterId);
			preRendering.prepareCSV(cluster.getRootId(), ReadTree.getTreeDrawTree().getLeaves(cluster.getRoot()), null, csv, false);
			System.err.println("Cluster: " + clusterId + ", done");
		}
	}
	
	public static void main(String[] args) {
		String treeLocation = null;
		String baseFolder = null;
		String csv = null;
		
		if(args.length > 3) {
			treeLocation = args[0];
			baseFolder = args[1];
			csv = args[2];
		} else {
			System.err.println("java -jar CreateCsv.jar phylo.tree baseFolder csvFile");
			System.exit(0);
		}
		
		AddCsvInfo addCsvInfo = new AddCsvInfo();
		addCsvInfo.run(treeLocation, baseFolder, csv);
	}
}
