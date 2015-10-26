package be.kuleuven.rega.phylogeotool.distance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class KMedoids {
	/* Distance measure to measure the distance between instances */
	private SimpleLinkage simpleLinkage;

	/* Number of IClusters to generate */
	private int numberOfIClusters;

	/* Random generator for selection of candidate medoids */
	private Random rg;

	/* The maximum number of iterations the algorithm is allowed to run. */
	private int maxIterations;

	private Map<ICluster, List<ICluster>> associationMap = new HashMap<ICluster, List<ICluster>>();
	private List<ICluster> medoids;
	private List<ICluster> tempMedoids;
	private List<ICluster> datapoints = null;
	
	/**
	 * Creates a new instance of the k-medoids algorithm with the specified
	 * parameters.
	 * 
	 * @param numberOfIClusters
	 *            the number of IClusters to generate
	 * @param maxIterations
	 *            the maximum number of iteration the algorithm is allowed to
	 *            run
	 */
	public KMedoids(int numberOfIClusters, int maxIterations, Tree tree) {
		this.maxIterations = maxIterations;
		this.simpleLinkage = new SimpleLinkage(tree);
		
		if(tree.getLeaves().size() >= numberOfIClusters) {
			for(Node node:tree.getNodes()) {
				datapoints.add(node);
			}
			this.numberOfIClusters = numberOfIClusters;
		} else {
			throw new RuntimeException("The number of IClusters cannot be bigger than the amount of datapoints.");
		}
		
		rg = new Random(System.currentTimeMillis());
		
	}

	public Map<ICluster, List<ICluster>> calculate(boolean randomMedoids) {
		medoids = init(datapoints, randomMedoids);
		associationMap = associateDatapoints(medoids,datapoints);
		tempMedoids = determineNewMedoids(associationMap);
		while(!medoids.containsAll(tempMedoids)) {
			medoids = tempMedoids;
			associationMap = associateDatapoints(medoids,datapoints);
			tempMedoids = determineNewMedoids(associationMap);
		}
		return associationMap;
	}
	
	private List<ICluster> init(List<ICluster> datapoints, boolean randomMedoids) {
		Set<ICluster> medoids = new HashSet<ICluster>(numberOfIClusters);
		int step = (int) Math.ceil(datapoints.size()/(double)numberOfIClusters);
		//int step = (int) Math.round(datapoints.size()/(long)numberOfIClusters);
		
		int i = 0;
		if(!randomMedoids) {
			while(medoids.size() < numberOfIClusters) {
				medoids.add(datapoints.get(i));
				i += step;
			}
		} else {
			while(medoids.size() < numberOfIClusters) {
				int random = rg.nextInt(datapoints.size());
				medoids.add(datapoints.get(random));
			}
		}

		List<ICluster> listToReturn = new ArrayList<ICluster>(numberOfIClusters);
		listToReturn.addAll(medoids);
		return listToReturn;
	}
	
	public Map<ICluster,List<ICluster>> associateDatapoints(List<ICluster> medoids, List<ICluster> datapoints) {
		// TODO This is efficient?
		Map<ICluster, List<ICluster>> associationMap = new HashMap<ICluster, List<ICluster>>();
		for(ICluster medoid:medoids) {
			associationMap.put(medoid, new ArrayList<ICluster>());
		}
		
		for(ICluster datapoint:datapoints) {
			if(!medoids.contains(datapoint)) {
				associationMap.get(associateDatapoint(medoids, datapoint)).add(datapoint);
			}
		}
		
		return associationMap;
	}

	public List<ICluster> determineNewMedoids(Map<ICluster, List<ICluster>> associationMap) {
		List<ICluster> newMedoids = new ArrayList<ICluster>(numberOfIClusters);
		// Analysed ICluster index
		int i = 0;
		for(ICluster ICluster:associationMap.keySet()) {
			List<ICluster> datapointsInICluster = associationMap.get(ICluster);
			datapointsInICluster.add(0,ICluster);
			double tempDistance = Double.MAX_VALUE;
			ICluster bestMedoid = null;
			for(ICluster datapointInICluster:datapointsInICluster) {
				double distance = determineCostICluster(datapointInICluster, datapointsInICluster);
				if(distance < tempDistance) {
					bestMedoid = datapointInICluster;
					tempDistance = distance;
				}
			}
			newMedoids.add(bestMedoid);
			i++;
		}
		return newMedoids;
	}
	
	// Calculate the cost to get from each point in the ICluster to the medoid
	private double determineCostICluster(ICluster medoid, List<ICluster> datapoints) {
		double totalDistance = 0.0;
		for(ICluster datapoint:datapoints) {
			totalDistance += simpleLinkage.measure(medoid, datapoint);
		}
		return totalDistance;
	}
	
	private ICluster associateDatapoint(List<ICluster> medoids, ICluster datapoint) {
		double shortestDistance = Double.MAX_VALUE;
		ICluster associatedICluster = null;
		for(int i = 0; i < medoids.size(); i++) {
			double distance = this.simpleLinkage.measure(medoids.get(i), datapoint);
			if(distance < shortestDistance) {
				shortestDistance = distance;
				associatedICluster = medoids.get(i);
			}
		}
		return associatedICluster;
		
	}
	
	/* Methods and code to find the best K for the datapoints */
	
//	public void Wk(ICluster medoids, List<ICluster> clusters) {
//		K = len(mu)
//	    return sum([np.linalg.norm(mu[i]-c)**2/(2*len(c)) \
//	               for i in range(K) for c in clusters[i]])
//	}
}