package be.kuleuven.rega.webapp.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.rega.comparator.NaturalOrderComparator;
import be.kuleuven.rega.comparator.SortingOptions;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.chart.WCartesianChart;

public class WBarChartMine extends Chart {
	
	private Map<String, Integer> fullDataset;
	
	private int maxValue = 0;
	private SortingOptions sortingOption;
	
	private final int MAX_LABEL_LENGTH = 21;
	private final int maxViews = 5;
	private final String remainingValuesColumnName = "Remaining";
	
	public WBarChartMine(WCartesianChart wCartesianChart) {
		super(wCartesianChart);
	}
	
	// We do not want to reread the model if it is caused by for example hovering out of a node
	@Override
	public void setData(Map<String,Integer> fullDataSet, boolean reread) {
		if(reread) {
			maxValue = 0;
			if(model != null) {
				model.clear();
				model.insertColumns(model.getColumnCount(), 2);
				model.setHeaderData(0, new WString("Property"));
				model.setHeaderData(1, new WString("Full Dataset"));
			}
			
			if(fullDataSet != null) {
				Map<String,Integer> sorted_map = getSortedMap(fullDataSet);
				Map<String, Integer> reduced_map = reduceMap(sorted_map, maxViews);
				
				this.fullDataset = reduced_map;
				
				model.insertRows(model.getRowCount(), reduced_map.keySet().size());
				
				int j = 0;
				
				for(String label:reduced_map.keySet()) {
					if(label.length() >= MAX_LABEL_LENGTH)
						model.setData(j, 0, label.substring(0, MAX_LABEL_LENGTH));
					else
						model.setData(j, 0, label);
					model.setData(j, 1, reduced_map.get(label));
					
					if(reduced_map.get(label) > maxValue) {
						maxValue = reduced_map.get(label);
					}
					j++;
				}
			}
		}
		this.updateChart(model, maxValue);
	}
	
	@Override
	public void updateData(Map<String,Integer> node, WColor wColor) {
		if(node != null) {
			Map<String,Integer> sorted_map = getSortedMap(node);
			Map<String, Integer> reduced_map = reduceMap(sorted_map, maxViews);
			
			if(hoveredModel != null) {
				hoveredModel.clear();
				hoveredModel.insertColumns(hoveredModel.getColumnCount(), 3);
				hoveredModel.setHeaderData(0, new WString("Property"));
				hoveredModel.setHeaderData(1, new WString("Full Dataset"));
				hoveredModel.setHeaderData(2, new WString("Hovered Node"));
			}
			
			hoveredModel.insertRows(hoveredModel.getRowCount(), fullDataset.keySet().size());
			
			int j = 0;
			
			// Set full dataset
			for(String label:this.fullDataset.keySet()) {
				if(label.length() >= MAX_LABEL_LENGTH)
					hoveredModel.setData(j, 0, label.substring(0, MAX_LABEL_LENGTH));
				else
					hoveredModel.setData(j, 0, label);
				
				if(reduced_map.containsKey(label)) {
					// Because we need to recalculate "full dataset" part
					hoveredModel.setData(j, 1, (fullDataset.get(label) - reduced_map.get(label)));
					if(fullDataset.get(label) > maxValue) {
						maxValue = fullDataset.get(label);
					}
				} else {
					hoveredModel.setData(j, 1, fullDataset.get(label));
				}
				j++;
			}
			
			// set hovered node
			int i = 0;
			for(Map.Entry<String, Integer> entry:fullDataset.entrySet()) {
//				System.out.println("Value: " + model.getData(i, 0));
				if(reduced_map.containsKey(hoveredModel.getData(i, 0))) {
					hoveredModel.setData(i, 2, reduced_map.get(hoveredModel.getData(i, 0)));
					reduced_map.remove(hoveredModel.getData(i, 0));
				} else {
					hoveredModel.setData(i, 2, 0);
				}
				i++;
			}
			
			// In case that the hovered node has data such as [HIV-1 D => 258, HIV-1 C => 1]
			// And full dataset doesn't contain HIV-1 D, it should be added to the others bar
			int columnNr = maxViews;
			if(fullDataset.keySet().size() < maxViews) {
				columnNr = fullDataset.keySet().size();
			}
			for(Map.Entry<String, Integer> entry:reduced_map.entrySet()) {
				if(fullDataset.containsKey("NA")) {
					hoveredModel.setData(columnNr - 1, 2, (Integer)hoveredModel.getData(columnNr - 1, 2) + entry.getValue());
					// Because of flipping the graphs, we also need to lower the max height of the full dataset chart here
					hoveredModel.setData(columnNr - 1, 1, (Integer)hoveredModel.getData(columnNr - 1, 1) - entry.getValue());
				} else {
					hoveredModel.setData(columnNr, 2, (Integer)hoveredModel.getData(columnNr, 2) + entry.getValue());
					// Because of flipping the graphs, we also need to lower the max height of the full dataset chart here
					hoveredModel.setData(columnNr, 1, (Integer)hoveredModel.getData(columnNr, 1) - entry.getValue());
				}
			}
			this.setSecondBarColor(wColor);
			this.updateChart(hoveredModel, maxValue);
		}
	}
	
	/**
	 * Method to determine in which way the dataset should be sorted. It makes use of the SortingOptions enum under be.kuleuven.rega.comparator
	 * @param fullDataSet: Map with keys being x-values of the barchart, values being the y-values of the barchart
	 * @return sorted map
	 */
	private Map<String,Integer> getSortedMap(Map<String,Integer> fullDataSet) {
		if(sortingOption != null) {
			switch(sortingOption) {
				case ALPHABETICAL_ASCENDING:
					return sortMapAlphabetically(fullDataSet, true);
				case ALPHABETICAL_DESCENDING:
					return sortMapAlphabetically(fullDataSet, false);
				case FREQUENCY_ASCENDING:
					return sortMap(fullDataSet, true);
				case FREQUENCY_DESCENDING:
					return sortMap(fullDataSet, false);
				default:
					return sortMap(fullDataSet, false);
			}
		} else {
			return sortMap(fullDataSet, false);
		}
	}
	
	/**
	 * Method to sort the dataset based on frequency. How many times a value occurs.
	 * @param mapToSort: Map containing keys (x-values of the barchart) and values (y-values of the barchart)
	 * @param ascending: : Boolean indicating if should be sorted ascending or descending.
	 * @return LinkedHashMap with all the entries that were in the list such that we keep the ordered structure of the dataset
	 */
	public <K, V extends Comparable<? super V>> Map<K, V> sortMap(final Map<K, V> mapToSort, boolean ascending) {
		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(mapToSort.size());
 
		entries.addAll(mapToSort.entrySet());
 
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
		
		return listToSortedMap(entries, ascending);
	}
	
	/**
	 * Method to sort the dataset based on alphabetical order of the keys (x-values of the bar chart)
	 * @param mapToSort: Map containing keys (x-values of the barchart) and values (y-values of the barchart)
	 * @param ascending: : Boolean indicating if should be sorted ascending or descending.
	 * @return LinkedHashMap with all the entries that were in the list such that we keep the ordered structure of the dataset
	 */
	public <K, V extends Comparable<? super V>> Map<K, V> sortMapAlphabetically(final Map<K, V> mapToSort, boolean ascending) {
		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(mapToSort.size());
 
		entries.addAll(mapToSort.entrySet());
		final NaturalOrderComparator naturalOrderComparator = new NaturalOrderComparator();
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			//TODO: Check if this function works properly if we have a mixture of strings and ints
			@Override
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {
					return naturalOrderComparator.compare(entry1.getKey(), entry2.getKey());
			}
		});
 
		return listToSortedMap(entries, ascending);
	}
	
	/**
	 * We transform the list with sorted elements to a map.
	 * On top of that we check if the dataset contains "NA" or "Others" values as they have to be last in the ordering
	 * @param entries: List of sorted entries which we want to store in a map
	 * @param ascending: Boolean indicating if should be sorted ascending or descending. Currently sorted ascending
	 * @return LinkedHashMap with all the entries that were in the list such that we keep the ordered structure of the dataset
	 */
	private <K, V extends Comparable<? super V>> Map<K, V> listToSortedMap(List<Map.Entry<K, V>> entries, boolean ascending) {
		if(!ascending)
			Collections.reverse(entries);
		
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		Map.Entry<K, V> entryNA = null;
//		Map.Entry<K, V> entryOthers = null;
		for (Map.Entry<K, V> entry : entries) {
			if(entry.getKey().equals("NA")){
				entryNA = entry;
//			} else if(entry.getKey().equals("Other")) {
//				entryOthers = entry;
			} else {
				sortedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		// Add the 'NA' as last element to the map
		if(entryNA != null)
			sortedMap.put(entryNA.getKey(), entryNA.getValue());
		
		// Add the 'Others' as last element to the map
//		if(entryOthers != null)
//			sortedMap.put(entryOthers.getKey(), entryOthers.getValue());

		return sortedMap;
	}
	
	private Map<String,Integer> reduceMap(Map<String,Integer> mapToReduce, int maxViews) {
		Map<String, Integer> mapToReturn = new LinkedHashMap<String, Integer>();
		int i = 0;
		int totalOtherValue = 0;
		boolean showNA = mapToReduce.containsKey("NA");
		for(Map.Entry<String, Integer> entry:mapToReduce.entrySet()) {
			if(!showNA && ++i <= maxViews) {
				if(!entry.getKey().equals("Other")) {
					mapToReturn.put(entry.getKey(), entry.getValue());
				} else {
					totalOtherValue += entry.getValue();
				}
			} else if (showNA && ++i < maxViews) {
				if(!entry.getKey().equals("Other")) {
					mapToReturn.put(entry.getKey(), entry.getValue());
				} else {
					totalOtherValue += entry.getValue();
				}
			} else {
				// This should be the last element of the array
				if(entry.getKey().equals("NA")) {
					mapToReturn.put(remainingValuesColumnName, totalOtherValue);
					mapToReturn.put("NA", entry.getValue());
					return mapToReturn;
				} else {
					totalOtherValue += entry.getValue();
				}
			}
		}
		mapToReturn.put(remainingValuesColumnName, totalOtherValue);
		
		return mapToReturn;
	}
	
	public void setSortingOption(SortingOptions sortingOption) {
		this.sortingOption = sortingOption;
	}
}