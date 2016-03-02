package be.kuleuven.rega.webapp.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WShadow;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class WBarChartMine {
	
	private Map<String, Integer> fullDataset;
	private WCartesianChart chart = null;
	private WStandardItemModel fullDatasetModel = null;
	private WHBoxLayout whBoxLayout = null;
//	private WVBoxLayout wvBoxLayout = null;
	
	public WBarChartMine(Map<String,Integer> fullDataset) {
		whBoxLayout = new WHBoxLayout();
//		wvBoxLayout = new WVBoxLayout();
		chart = new WCartesianChart();
		setData(fullDataset);
		chart.resize(new WLength(120), new WLength(120));
		chart.setMargin(new WLength(10), EnumSet.of(Side.Top, Side.Left));
		whBoxLayout.addWidget(chart);
//		whBoxLayout.addLayout(wvBoxLayout);
	}
	
	public WLayout getLayout() {
		return this.whBoxLayout;
	}
	
	public WCartesianChart getWidget() {
		//widget.setLayout(whBoxLayout);
		return this.chart;
	}
	
	public void setData(Map<String,Integer> fullDataSet) {
		int maxViews = 5;
		
		if(fullDataSet != null) {
			Map<String,Integer> sorted_map = sortMap(fullDataSet);
			this.fullDataset = sorted_map;
			
			WStandardItemModel model = new WStandardItemModel();
			model.insertColumns(model.getColumnCount(), 2);
			if(sorted_map.size() > maxViews) {
				model.insertRows(model.getRowCount(), (maxViews + 1));
			} else {
				model.insertRows(model.getRowCount(), fullDataSet.keySet().size());
			}
			model.setHeaderData(0, new WString("Property"));
			model.setHeaderData(1, new WString("Full Dataset"));
			
			int j = 0;
			int totalValues = 0;
			int totalValuesSet = 0;
			
			for(int value: fullDataSet.values()) {
				 totalValues += value;
			}
			
			for(String label:sorted_map.keySet()) {
				model.setData(j, 0, label);
				model.setData(j, 1, sorted_map.get(label));
				totalValuesSet += sorted_map.get(label);
				if(++j >= maxViews) {
					model.setData(j, 0, new WString("Other"));
					model.setData(j, 1, totalValues - totalValuesSet);
					break;
				}
			}
		
			this.fullDatasetModel = model;
			this.updateChart(model, chart);
		}
	}
	
	public void updateData(Map<String,Integer> node) {
		int maxViews = 5;
		
		if(node != null) {
			Map<String,Integer> sorted_map = sortMap(node);
			
			WStandardItemModel model = new WStandardItemModel();
			model.insertColumns(model.getColumnCount(), 3);
			if(this.fullDataset.size() > maxViews) {
				model.insertRows(model.getRowCount(), maxViews + 1);
			} else {
				model.insertRows(model.getRowCount(), this.fullDataset.keySet().size());
			}
			model.setHeaderData(0, new WString("Property"));
			model.setHeaderData(1, new WString("Full Dataset"));
			model.setHeaderData(2, new WString("Hovered Node"));
			
			int j = 0;
			int totalValues = 0;
			int totalValuesSet = 0;
			
			// Set full dataset
			for(int value: this.fullDataset.values()) {
				 totalValues += value;
			}
			
			for(String label:this.fullDataset.keySet()) {
				model.setData(j, 0, label);
				model.setData(j, 1, fullDataset.get(label));
				totalValuesSet += fullDataset.get(label);
				if(++j >= maxViews) {
					model.setData(j, 0, new WString("Other"));
					model.setData(j, 1, totalValues - totalValuesSet);
					break;
				}
			}
			
			totalValues = 0;
			totalValuesSet = 0;
			// set hovered node
			for(int value: sorted_map.values()) {
				 totalValues += value;
			}
			
			int i = 0;
			for(Map.Entry<String, Integer> entry:fullDataset.entrySet()) {
//				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//				System.out.println(model.getData(i, 0));
//				System.out.println(sorted_map.containsKey(model.getData(i, 0)));
				if(sorted_map.containsKey(model.getData(i, 0))) {
					model.setData(i, 2, sorted_map.get(model.getData(i, 0)));
					totalValuesSet += sorted_map.get(model.getData(i, 0));
				} else {
					model.setData(i, 2, 0);
				}
				
				if(++i >= maxViews) {
					model.setData(i, 0, new WString("Other"));
					model.setData(i, 2, totalValues - totalValuesSet);
					break;
				}
			}
		
			this.updateChart(model, chart);
		}
	}
	
	private void updateChart(WStandardItemModel model, WCartesianChart wCartesianChart) {
		wCartesianChart.setModel(model);
		wCartesianChart.setXSeriesColumn(0);
		wCartesianChart.setLegendEnabled(true);
//		wCartesianChart.setLegendLocation(LegendLocation.LegendOutside, Side.Bottom, AlignmentFlag.AlignBottom);
		wCartesianChart.setPlotAreaPadding(40, EnumSet.of(Side.Left));
		wCartesianChart.setPlotAreaPadding(120, EnumSet.of(Side.Right));
		for (int column = 1; column < model.getColumnCount(); ++column) {
			WDataSeries series = new WDataSeries(column, SeriesType.BarSeries);
			series.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
			wCartesianChart.addSeries(series);
		}
		wCartesianChart.setMargin(WLength.Auto, EnumSet.of(Side.Left, Side.Right));
//		wCartesianChart.resize(new WLength(120), new WLength(120));
	}
	
	public <K, V extends Comparable<? super V>> Map<K, V> sortMap(final Map<K, V> mapToSort) {
		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(mapToSort.size());
 
		entries.addAll(mapToSort.entrySet());
 
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
 
		Collections.reverse(entries);
		
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
