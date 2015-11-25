package be.kuleuven.rega.phylogeotool.distance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import be.kuleuven.rega.phylogeotool.tree.Node;

public class DistanceMatrixDistance implements DistanceInterface {

	private double[][] distanceMatrix = null;
	private HashMap<String, Integer> translatedNodeNames;

	public DistanceMatrixDistance(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public DistanceMatrixDistance(String translatedNodeNamesLocation, String distanceMatrixLocation) {
		// TODO: Create this constructor
//		this.translatedNodeNames = ;
//		this.distanceMatrix = this.readDistanceMatrix(translatedNodeNames, distanceMatrixLocation);
	}
	
	public DistanceMatrixDistance(HashMap<String, Integer> translatedNodeNames, String distanceMatrixLocation) throws IOException {
		this.translatedNodeNames = translatedNodeNames;
		this.distanceMatrix = this.readDistanceMatrix(translatedNodeNames, distanceMatrixLocation);
	}


	@Override
	public double getDistance(Node node1, Node node2) {
		int indexNode1 = this.translatedNodeNames.get(node1.getLabel());
		int indexNode2 = this.translatedNodeNames.get(node2.getLabel());
		if(indexNode1 < indexNode2) {
			return distanceMatrix[indexNode1][indexNode2];
		} else {
			return distanceMatrix[indexNode2][indexNode1];
		}
	}

	private double[][] readDistanceMatrix(HashMap<String, Integer> translatedNodeNames, String fileName) {
		BufferedReader bufferedReader = null;
		double[][] distances = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
			distances = new double[translatedNodeNames.keySet().size()][translatedNodeNames.keySet().size()];

			String line;
			int i = 0;
			int j = 0;
			while ((line = bufferedReader.readLine()) != null) {
				j = 0;
				String[] distancesLine = line.split(";");
				for (String distance : distancesLine) {
					if(distance.equals("")) {
						distances[i][j++] = 0.0d;
					} else {
						distances[i][j++] = new Double(distance);
					}
				}
				i++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distances;
	}
	
//	private void printDistanceMatrix(double [][] distances) throws IOException {
//		
//		for (int i = 0; i < distances.length; i++) {
//		    for (int j = 0; j < distances[0].length; j++) {
//		    	System.out.print(distances[i][j] + ";");
//		    }
//		    System.out.print("\n");
//		}
//	}

}
