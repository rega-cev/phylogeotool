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
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WShadow;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;
import eu.webtoolkit.jwt.chart.Axis;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class WBarChartMine {
	
	private WCartesianChart chart = null;
	private WHBoxLayout whBoxLayout = null;
	private WVBoxLayout wvBoxLayout = null;
	WWidget legend0 = null;
	WWidget legend1 = null;
	WWidget legend2 = null;
	WWidget legend3 = null;
	WWidget legend4 = null;
	WWidget legend5 = null;
	WWidget legend6 = null;
	
	public WBarChartMine(Map<String,Integer> map) {
		whBoxLayout = new WHBoxLayout();
		wvBoxLayout = new WVBoxLayout();
		chart = new WCartesianChart();
		setData(map);
		chart.resize(new WLength(360), new WLength(360));
		chart.setMargin(new WLength(10), EnumSet.of(Side.Top, Side.Left));
		whBoxLayout.addWidget(chart);
		whBoxLayout.addLayout(wvBoxLayout);
	}
	
	public WCartesianChart getWCartesianChart() {
		return this.chart;
	}
	
	public WHBoxLayout getWidget() {
		//widget.setLayout(whBoxLayout);
		return this.whBoxLayout;
	}
	
	public void setData(Map<String,Integer> map) {
		WStandardItemModel model = new WStandardItemModel();
		model.insertColumns(model.getColumnCount(), 2);
		model.insertRows(model.getRowCount(), 7);
		model.setHeaderData(0, new WString("Label"));
		model.setHeaderData(1, new WString("Values"));
		int row = 0;
		int totalValues = 0;
		int totalValuesSet = 0;

		if(map != null) {
			Map<String,Integer> sorted_map = sortMap(map);
			for(int value: map.values()) {
				 totalValues += value;
			}
			for(String label:sorted_map.keySet()) {
				model.setData(row, 0, new WString(label));
				model.setData(row, 1, sorted_map.get(label));
				totalValuesSet += sorted_map.get(label);
				if(++row > 5) {
					model.setData(row, 0, new WString("Other"));
					model.setData(row, 1, totalValues - totalValuesSet);
					break;
				}
				
				WDataSeries s = new WDataSeries(row, SeriesType.BarSeries);
				s.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
				chart.addSeries(s);
			}
		}
		System.out.println(model.getData(0, 0));
		System.out.println(model.getData(0, 1));
		
		chart.setModel(model); // set the model
		chart.setXSeriesColumn(0); // set the column that holds the categories
		chart.setLegendEnabled(true); // enable the legend
		chart.getAxis(Axis.YAxis).setLabelFormat("%.0f");
		chart.setMargin(10, Side.Top, Side.Bottom); // add margin vertically
	    chart.setMargin(WLength.Auto, Side.Left, Side.Right); // center
	    
//        chart.setAutoLayoutEnabled();
		//chart.setLabelsColumn(0);
		//chart.setDataColumn(1);
//		if(legend0 != null) {
//			wvBoxLayout.removeWidget(legend0);
//		}
//		if(legend1 != null) {
//			wvBoxLayout.removeWidget(legend1);
//		}
//		if(legend2 != null) {
//			wvBoxLayout.removeWidget(legend2);
//		}
//		if(legend3 != null) {
//			wvBoxLayout.removeWidget(legend3);
//		}
//		if(legend4 != null) {
//			wvBoxLayout.removeWidget(legend4);
//		}
//		if(legend5 != null) {
//			wvBoxLayout.removeWidget(legend5);
//		}
//		if(legend6 != null) {
//			wvBoxLayout.removeWidget(legend6);
//		}
//		
//		int legendWidth = 150;
//		legend0 = chart.createLegendItemWidget(0);
//		legend0.setWidth(new WLength(legendWidth));
//		legend1 = chart.createLegendItemWidget(1);
//		legend1.setWidth(new WLength(legendWidth));
//		legend2 = chart.createLegendItemWidget(2);
//		legend2.setWidth(new WLength(legendWidth));
//		legend3 = chart.createLegendItemWidget(3);
//		legend3.setWidth(new WLength(legendWidth));
//		legend4 = chart.createLegendItemWidget(4);
//		legend4.setWidth(new WLength(legendWidth));
//		legend5 = chart.createLegendItemWidget(5);
//		legend5.setWidth(new WLength(legendWidth));
//		legend6 = chart.createLegendItemWidget(6);
//		legend6.setWidth(new WLength(legendWidth));
//		wvBoxLayout.addWidget(legend0);
//		wvBoxLayout.addWidget(legend1);
//		wvBoxLayout.addWidget(legend2);
//		wvBoxLayout.addWidget(legend3);
//		wvBoxLayout.addWidget(legend4);
//		wvBoxLayout.addWidget(legend5);
//		wvBoxLayout.addWidget(legend6);
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
