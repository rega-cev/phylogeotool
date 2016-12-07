package be.kuleuven.rega.prerendering;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

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
	
	/**
	 * TODO: Check if HashSet<String> acceptedCountryCodes could be loaded only once
	 * @return 	true if the dataset used to represent the countries in the Google Chart is built in the ISO 3166-1 alpha-2 format
	 * (https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
	 * 			false if the dataset used is not in the correct format
	 * @throws FileNotFoundException when the file 1.xml is not found. It is required to check the ISO structure of the countries.
	 */
	public boolean isGeoGraphicDataISO3166() throws FileNotFoundException {
		boolean xmlFileExist = new File(Settings.getInstance().getXmlPath() + File.separator + "1.xml").exists();
		
		if(xmlFileExist) {
			HashMap<String, Integer> countries = this.readCsv(1, Settings.getInstance().getVisualizeGeography());
			HashSet<String> acceptedCountryCodes = new HashSet<String>();
			for (CountryCode countryCode : CountryCode.values()) {
			      acceptedCountryCodes.add(countryCode.name());
			}
			
			for(String key:countries.keySet()) {
				if(!acceptedCountryCodes.contains(key)) {
					System.err.println("Could not find country " + key + " in the ISO3166 list");
					return false;
				}
			}
			return true;
		} else {
			throw new FileNotFoundException("File " + Settings.getInstance().getXmlPath() + File.separator + "1.xml");
		}
	}
}
