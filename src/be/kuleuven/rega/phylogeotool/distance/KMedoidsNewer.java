//package be.kuleuven.rega.phylogeotool.distance;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//
//import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
//
//public class KMedoidsNewer {
//	/* Distance measure to measure the distance between instances */
//	private SimpleLinkage simpleLinkage;
//
//	/* Number of IClusters to generate */
//	private int numberOfIClusters;
//
//	/* Random generator for selection of candidate medoids */
//	private Random rg;
//
//	/* The maximum number of iterations the algorithm is allowed to run. */
//	private int maxIterations;
//
//	private Map<ICluster, List<ICluster>> associationMap = new HashMap<ICluster, List<ICluster>>();
//	private List<ICluster> medoids;
//	private List<ICluster> tempMedoids;
//	private List<ICluster> datapoints = null;
//	
//	/**
//	 * Creates a new instance of the k-medoids algorithm with the specified
//	 * parameters.
//	 * 
//	 * @param numberOfIClusters
//	 *            the number of IClusters to generate
//	 * @param maxIterations
//	 *            the maximum number of iteration the algorithm is allowed to
//	 *            run
//	 */
//	public KMedoidsNewer(int numberOfIClusters, int maxIterations, List<ICluster> data) {
//		this.maxIterations = maxIterations;
//		this.simpleLinkage = new SimpleLinkage();
//		
//		if(data.size() >= numberOfIClusters) {
//			datapoints = data;
//			this.numberOfIClusters = numberOfIClusters;
//		} else {
//			throw new RuntimeException("The number of IClusters cannot be bigger than the amount of datapoints.");
//		}
//		
//		rg = new Random(System.currentTimeMillis());
//		
//	}
//
//	public Map<ICluster, List<ICluster>> calculate() {
//		medoids = init(datapoints);
//		associationMap = associateDatapoints(medoids,datapoints);
//		tempMedoids = determineNewMedoids(associationMap);
//		while(!medoids.containsAll(tempMedoids)) {
//			medoids = tempMedoids;
//			associationMap = associateDatapoints(medoids,datapoints);
//			tempMedoids = determineNewMedoids(associationMap);
//		}
//		return associationMap;
//	}
//	
//	private List<ICluster> init(List<ICluster> datapoints) {
//		Set<ICluster> medoids = new HashSet<>(numberOfIClusters);
//		while(medoids.size() < numberOfIClusters) {
//			int random = rg.nextInt(datapoints.size());
//			medoids.add(datapoints.get(random));
//		}
//
//		List<ICluster> listToReturn = new ArrayList<>(numberOfIClusters);
//		listToReturn.addAll(medoids);
//		return listToReturn;
//	}
//	
//	public Map<ICluster,List<ICluster>> associateDatapoints(Collection<ICluster> medoids, List<ICluster> datapoints) {
//		// TODO This is efficient?
//		Map<ICluster, List<ICluster>> associationMap = new HashMap<ICluster, List<ICluster>>();
//		for(ICluster medoid:medoids) {
//			associationMap.put(medoid, new ArrayList<ICluster>());
//		}
//		
//		for(ICluster datapoint:datapoints) {
//			if(!medoids.contains(datapoint)) {
//				associationMap.get(associateDatapoint(medoids, datapoint)).add(datapoint);
//			}
//		}
//		
//		return associationMap;
//	}
//
//	public List<ICluster> determineNewMedoids(Map<ICluster, List<ICluster>> associationMap) {
//		List<ICluster> newMedoids = new ArrayList<>(numberOfIClusters);
//		// Analysed ICluster index
//		int i = 0;
//		double tempDistance = 0;
//		for(ICluster ICluster:associationMap.keySet()) {
//			determineCostICluster(ICluster);
//		}
//		return newMedoids;
//	}
//	
//	// Calculate the cost to get from each point in the ICluster to the medoid
//	private Collection<ICluster> determineCostICluster(Set<ICluster> medoids) {
//		int item = new Random().nextInt(datapoints.size());
//		Set<ICluster> tempMedoids = new HashSet<ICluster>(medoids);
//		List<ICluster> tempDatapoints = new ArrayList<ICluster>(datapoints);
//		ICluster datapoint = tempDatapoints.get(item);
//		boolean changed = false;
//		
//		ICluster iCluster = null;
//		for(ICluster medoid:medoids) {
//			tempMedoids.remove(medoid);
//			tempMedoids.add(tempDatapoints.get(item));
//			tempDatapoints.remove(item);
//			tempDatapoints.add(medoid);
//			
//			Map<ICluster,List<ICluster>> association = associateDatapoints(tempMedoids, tempDatapoints);
//			int cost = 0;
//			
//			for(ICluster tempMedoid:tempMedoids) {
//				cost += determineCostICluster(medoid, association.get(medoid));
//			}
//			
//			if(cost >= lowestCost) {
//				tempMedoids.add(medoid);
//				tempMedoids.remove(tempDatapoints.get(item));
//				tempDatapoints.add(datapoint);
//				tempDatapoints.remove(medoid);
//			} else {
//				changed = true;
//			}
//		}
//		
//		
//		return tempMedoids;
//	}
//	
//	// Calculate the cost to get from each point in the ICluster to the medoid
//	private double determineCostICluster(ICluster medoid, Collection<ICluster> datapoints) {
//		double totalDistance = 0.0;
//		for(ICluster datapoint:datapoints) {
//			totalDistance += simpleLinkage.measure(medoid, datapoint);
//		}
//		return totalDistance;
//	}
//	
//	private ICluster associateDatapoint(Collection<ICluster> medoids, ICluster datapoint) {
//		double shortestDistance = Double.MAX_VALUE;
//		ICluster associatedICluster = null;
//		for(ICluster iCluster:medoids) {
//			double distance = this.simpleLinkage.measure(iCluster, datapoint);
//			if(distance < shortestDistance) {
//				shortestDistance = distance;
//				associatedICluster = iCluster;
//			}
//		}
//		return associatedICluster;
//		
//	}
//}