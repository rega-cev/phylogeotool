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
import eu.webtoolkit.jwt.WFont;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WShadow;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.chart.Axis;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class WBarChartMine {
	
	private Map<String, Integer> fullDataset;
	private WCartesianChart chart = null;
	private WStandardItemModel fullDatasetModel = null;
	private WStandardPaletteMine wStandardPaletteMine = null;
	private int maxValue = 0;
	private WStandardItemModel model;
	
	private final int MAX_LABEL_LENGTH = 11;
	
	public WBarChartMine(Map<String,Integer> fullDataset) {
		chart = new WCartesianChart();
		setData(fullDataset);
		wStandardPaletteMine = new WStandardPaletteMine();
		chart.setPalette(wStandardPaletteMine);
		chart.setMinimumSize(new WLength(120), new WLength(140));
		chart.setPlotAreaPadding(0, EnumSet.of(Side.Top));
		chart.setPlotAreaPadding(10, EnumSet.of(Side.Left));
		model = new WStandardItemModel();
	}
	
	public WCartesianChart getWidget() {
		return this.chart;
	}
	
	public void setData(Map<String,Integer> fullDataSet) {
		int maxViews = 5;
		maxValue = 0;
		
		if(model != null) {
			model.clear();
			model.insertColumns(model.getColumnCount(), 2);
			model.setHeaderData(0, new WString("Property"));
			model.setHeaderData(1, new WString("Full Dataset"));
		}
		
		if(fullDataSet != null) {
			Map<String,Integer> sorted_map = sortMap(fullDataSet);
			this.fullDataset = sorted_map;
			
			if(sorted_map.size() > maxViews) {
				model.insertRows(model.getRowCount(), (maxViews + 1));
			} else {
				model.insertRows(model.getRowCount(), fullDataSet.keySet().size());
			}
			
			int j = 0;
			int totalValues = 0;
			int totalValuesSet = 0;
			
			for(int value: fullDataSet.values()) {
				 totalValues += value;
			}
			
			for(String label:sorted_map.keySet()) {
				if(label.length() >= MAX_LABEL_LENGTH)
					model.setData(j, 0, label.substring(0, MAX_LABEL_LENGTH));
				else
					model.setData(j, 0, label);
				model.setData(j, 1, sorted_map.get(label));
				
				if(sorted_map.get(label) > maxValue) {
					maxValue = sorted_map.get(label);
				}
				
				totalValuesSet += sorted_map.get(label);
				if(++j >= maxViews) {
					model.setData(j, 0, new WString("Other"));
					model.setData(j, 1, totalValues - totalValuesSet);
					
					if(totalValues - totalValuesSet > maxValue) {
						maxValue = totalValues - totalValuesSet;
					}
					
					break;
				}
			}
		
			this.fullDatasetModel = model;
			this.updateChart(model, chart);
		}
	}
	
	public void updateData(Map<String,Integer> node, WColor wColor) {
		int maxViews = 5;
		
		if(node != null) {
			Map<String,Integer> sorted_map = sortMap(node);
			
			if(model != null) {
				model.clear();
				model.insertColumns(model.getColumnCount(), 3);
				model.setHeaderData(0, new WString("Property"));
				model.setHeaderData(1, new WString("Full Dataset"));
				model.setHeaderData(2, new WString("Hovered Node"));
			}
			
			if(this.fullDataset.size() > maxViews) {
				model.insertRows(model.getRowCount(), maxViews + 1);
			} else {
				model.insertRows(model.getRowCount(), this.fullDataset.keySet().size());
			}
			
			int j = 0;
			int totalValues = 0;
			int totalValuesSet = 0;
			
			// Set full dataset
			for(int value: this.fullDataset.values()) {
				 totalValues += value;
			}
			
			for(String label:this.fullDataset.keySet()) {
				if(label.length() >= MAX_LABEL_LENGTH)
					model.setData(j, 0, label.substring(0, MAX_LABEL_LENGTH));
				else
					model.setData(j, 0, label);
				
				if(sorted_map.containsKey(label)) {
					// Because we need to recalculate "full dataset" part
					model.setData(j, 1, (fullDataset.get(label) - sorted_map.get(label)));
					if(fullDataset.get(label) > maxValue) {
						maxValue = fullDataset.get(label);
					}
				} else {
					model.setData(j, 1, fullDataset.get(label));
				}
				totalValuesSet += fullDataset.get(label);
				if(++j >= maxViews) {
					model.setData(j, 0, new WString("Other"));
					model.setData(j, 1, totalValues - totalValuesSet);
					if((totalValues - totalValuesSet) > maxValue) {
						maxValue = totalValues - totalValuesSet;
					}
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
//				System.out.println("Value: " + model.getData(i, 0));
				if(sorted_map.containsKey(model.getData(i, 0))) {
					model.setData(i, 2, sorted_map.get(model.getData(i, 0)));
					totalValuesSet += sorted_map.get(model.getData(i, 0));
				} else {
					model.setData(i, 2, 0);
				}
				
				if(++i >= maxViews) {
					model.setData(i, 0, new WString("Other"));
					// Because we need to recalculate "other" part
					model.setData(i, 1, (Integer)model.getData(i, 1) - (totalValues - totalValuesSet));
					if((Integer)model.getData(i, 1) - (totalValues - totalValuesSet) > maxValue) {
						maxValue = (Integer)model.getData(i, 1) - (totalValues - totalValuesSet);
					}
					model.setData(i, 2, totalValues - totalValuesSet);
					break;
				}
			}
			this.setSecondBarColor(wColor);
			this.updateChart(model, chart);
		}
	}
	
	private void updateChart(WStandardItemModel model, WCartesianChart wCartesianChart) {
		wCartesianChart.setModel(model);
		wCartesianChart.setXSeriesColumn(0);
		wCartesianChart.setLegendEnabled(true);
//		wCartesianChart.getAxis(Axis.XAxis).setLabelAngle(315);
//		wCartesianChart.getAxis(Axis.XAxis).setLabelAngle(15);
		WFont wFont = wCartesianChart.getAxis(Axis.XAxis).getLabelFont();
		wFont.setSize(new WLength(12));
		wCartesianChart.getAxis(Axis.XAxis).setLabelFont(wFont);
//		wCartesianChart.getAxis(Axis.YAxis).setMaximum(maxValue);
		wCartesianChart.getAxis(Axis.YAxis).setLabelFormat("%.5g");
//		wCartesianChart.setAxis(wCartesianChart.getAxis(Axis.YAxis), Axis.YAxis);
		wCartesianChart.getAxis(Axis.YAxis).setRange(0.0, maxValue);
//		wCartesianChart.setLegendLocation(LegendLocation.LegendOutside, Side.Bottom, AlignmentFlag.AlignBottom);
		wCartesianChart.setPlotAreaPadding(50, EnumSet.of(Side.Left));
		wCartesianChart.setPlotAreaPadding(150, EnumSet.of(Side.Right));
		WFont legendFont = wCartesianChart.getLegendFont();
		legendFont.setSize(new WLength(15));
		wCartesianChart.setLegendStyle(legendFont, wCartesianChart.getLegendBorder(), wCartesianChart.getLegendBackground());
		for (int column = model.getColumnCount() - 1; column > 0; --column) {
			WDataSeries series = new WDataSeries(column, SeriesType.BarSeries);
			series.setStacked(true);
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
	
	public void setSecondBarColor(WColor wColor) {
		this.wStandardPaletteMine.setSecondColor(wColor);
	}
}