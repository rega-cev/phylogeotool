package be.kuleuven.rega.clustering;

import java.util.List;

import jebl.evolution.graphs.Node;
import be.kuleuven.rega.phylogeotool.interfaces.ICluster;

public class GraphVertex {

	private String name;
	private ICluster iCluster;
	private List<ICluster> datapoints;
	
	public GraphVertex(ICluster iCluster, String name) {
		setName(name);
		setICluster(iCluster);
	}
	
	public GraphVertex(ICluster iCluster, List<ICluster> datapoints, String name) {
		setName(name);
		setICluster(iCluster);
		setDatapoints(datapoints);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return getDatapoints().size();
	}

	public ICluster getICluster() {
		return iCluster;
	}

	public void setICluster(ICluster iCluster) {
		this.iCluster = iCluster;
	}

	public List<ICluster> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<ICluster> datapoints) {
		this.datapoints = datapoints;
	}
	
}
