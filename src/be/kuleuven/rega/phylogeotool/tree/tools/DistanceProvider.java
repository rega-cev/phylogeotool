package be.kuleuven.rega.phylogeotool.tree.tools;

import java.io.File;
import java.io.IOException;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.interfaces.IDistanceProvider;
import be.kuleuven.rega.phylogeotool.tree.Node;

public class DistanceProvider implements IDistanceProvider {

	private double[][] distanceMatrix = null;
	
	public DistanceProvider(File csv) throws IOException {
		distanceMatrix = CsvUtils.textToMatrix(csv);
	}

	//TODO: Mapping
	public double getDistance(Node node1, Node node2) {
		if(node1.getIndex() <= distanceMatrix.length && node2.getIndex() <= distanceMatrix.length) {
			return distanceMatrix[node1.getIndex()-1][node2.getIndex()-1];
		} else {
			throw new RuntimeException(DistanceProvider.class.getName() + " labelSample1 and labelSample2 in getDistance has to be in range of the matrix size.");
		}
	}
	
	public double getDistance(int node1Index, int node2Index) {
		if(node1Index < distanceMatrix.length && node2Index < distanceMatrix.length) {
			return distanceMatrix[node1Index][node2Index];
		} else {
			throw new RuntimeException(DistanceProvider.class.getName() + " labelSample1 and labelSample2 in getDistance has to be in range of the matrix size.");
		}
	}
	
}
