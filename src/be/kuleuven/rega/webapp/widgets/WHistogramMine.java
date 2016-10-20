package be.kuleuven.rega.webapp.widgets;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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

public class WHistogramMine extends Chart {

	private int maxValue = 0;
	private double min = 0;
	private double max = 0;

	private int nrBins = 0;
	private double binWidth = 0;
	
	private int[] fullDatasetHistogram;
	
	public WHistogramMine(WCartesianChart wCartesianChart) {
		super(wCartesianChart);
	}
	
	public static int[] calcHistogram(double[] data, double min, double max, int numBins) {
		final int[] result = new int[numBins];
		final double binSize = (max - min) / numBins;

		for (double d : data) {
			int bin = (int) ((d - min) / binSize);
			if (bin < 0) { /* this data is smaller than min */
			} else if (bin >= numBins) { /* this data point is bigger than max */
			} else {
				result[bin] += 1;
			}
		}
		return result;
	}
	
	/**
	 * @param data: sorted array
	 * @return binwidth for a histogram
	 */
	public static double calculateBinWidth(double[] data, double min, double max) {
		double range_data = max - min;
		double binwidth = range_data / (2 * calculateIQR(data) / Math.pow(data.length,(1D/3)));
		return binwidth;
	}

	public static double calculateIQR(double[] data) {
		DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(data);
		double iqr = descriptiveStatistics.getPercentile(75) - descriptiveStatistics.getPercentile(25);
		return iqr;
	}
	
	@Override
	public void setData(Map<String, Integer> readCsv, boolean reread) {
		if(reread) {
			maxValue = 0;
			if(model != null) {
				model.clear();
				model.insertColumns(model.getColumnCount(), 2);
				model.setHeaderData(0, new WString("X-label"));
				model.setHeaderData(1, new WString("Frequency"));
			}
			
			SpecialReturnValue specialReturnValue = mapToArrayFrequencies(readCsv);
			double[] sortedData = specialReturnValue.getSortedArray();
			boolean arrayContainsDoubles = specialReturnValue.isDoubleValues();
			
			min = sortedData[0];
			max = sortedData[sortedData.length - 1];

			nrBins = (int)calculateBinWidth(sortedData, min, max);
			binWidth = ((max - min) / nrBins);
			
			fullDatasetHistogram = calcHistogram(sortedData, min, max, nrBins);
			
			model.insertRows(model.getRowCount(), fullDatasetHistogram.length);
			
			int i = 0;
			for(int item:fullDatasetHistogram) {
				if(!arrayContainsDoubles) {
					model.setData(i, 0, Integer.toString((int)(min + (binWidth * i))));
				} else {
					model.setData(i, 0, min + (binWidth * i));
				}
				model.setData(i, 1, item);
				if(item > maxValue) {
					maxValue = item;
				}
				i++;
			}
		}
		this.updateChart(model, maxValue);
	}

	@Override
	public void updateData(Map<String, Integer> readCsv, WColor wColor) {
		if(readCsv != null) {
			if(hoveredModel != null) {
				hoveredModel.clear();
				hoveredModel.insertColumns(hoveredModel.getColumnCount(), 3);
				hoveredModel.setHeaderData(0, new WString("X-label"));
				hoveredModel.setHeaderData(1, new WString("Frequency Full Dataset"));
				hoveredModel.setHeaderData(2, new WString("Frequency Hovered Node"));
			}
			
			SpecialReturnValue specialReturnValue = mapToArrayFrequencies(readCsv);
			double[] sortedData = specialReturnValue.getSortedArray();
			boolean arrayContainsDoubles = specialReturnValue.isDoubleValues();
			
			int[] histogram = calcHistogram(sortedData, min, max, nrBins);
//			System.out.println("Histogram: " + histogram.length);
			hoveredModel.insertRows(hoveredModel.getRowCount(), fullDatasetHistogram.length);
			
			// Set full dataset
			int i = 0;
			for(int item:fullDatasetHistogram) {
				if(!arrayContainsDoubles) {
					hoveredModel.setData(i, 0, Integer.toString((int)(min + (binWidth * i))));
				} else {
					hoveredModel.setData(i, 0, min + (binWidth * i));
				}
				hoveredModel.setData(i, 1, item - histogram[i]);
				if(item > maxValue) {
					maxValue = item;
				}
				i++;
			}
			
			// Set hovered node
			int j = 0;
			for(int item:histogram) {
				hoveredModel.setData(j, 2, item);
				if(item > maxValue) {
					maxValue = item;
				}
				j++;
			}
			
			this.setSecondBarColor(wColor);
			this.updateChart(hoveredModel, maxValue);
		}
		
	}

//	private void updateChart(WStandardItemModel model) {
//		chart.setModel(model);
//		chart.setXSeriesColumn(0);
//		WFont wFont = chart.getAxis(Axis.XAxis).getLabelFont();
//		wFont.setSize(new WLength(12));
//		chart.getAxis(Axis.XAxis).setLabelFont(wFont);
//		chart.getAxis(Axis.YAxis).setLabelFormat("%.5g");
//		chart.getAxis(Axis.YAxis).setRange(0.0, maxValue);
////		wCartesianChart.getAxis(Axis.XAxis).setLabelInterval(1 % 0.3711340206185567);
//		chart.setPlotAreaPadding(50, EnumSet.of(Side.Left));
////		for (int column = 1; column < model.getColumnCount(); ++column) {
////			WDataSeries series = new WDataSeries(column, SeriesType.BarSeries);
////			series.setStacked(true);
////			series.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
////			chart.addSeries(series);
////		}
//		
//		for (int column = model.getColumnCount() - 1; column > 0; --column) {
//			WDataSeries series = new WDataSeries(column, SeriesType.BarSeries);
//			series.setStacked(true);
//			series.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
//			chart.addSeries(series);
//		}
//		
//		chart.setMargin(WLength.Auto, EnumSet.of(Side.Left, Side.Right));
//		chart.setOffsets(0);
//	}
	
	/**
	 * This method takes a hashmap of the form:
	 * 
	 * map {
	 * 	1991 => 4
	 *  1990 => 3
	 *  ...
	 *  2010 => 3
	 *  }
	 *  
	 *  and transforms it into the following sorted array
	 *  
	 *  [1990, 1990, 1990, 1991, 1991, 1991, 1991, ..., 2010, 2010, 2010]
	 * 
	 * @param mapToTransform: Pre condition: The String variable should be castable to text.
	 * We are expecting to work only with continuous data here
	 * @return sorted array
	 */
	public SpecialReturnValue mapToArrayFrequencies(Map<String, Integer> mapToTransform) {
		int totalSize = 0;
		boolean doubleValues = false;
		for(Integer size:mapToTransform.values()) {
			totalSize += size;
		}
		double[] values = new double[totalSize];
		
		int i = 0;
		for(String valueString:mapToTransform.keySet()) {
			Double value = null;
			try {
				value = Double.parseDouble(valueString);
				if (Integer.parseInt(valueString) != Math.floor(value)) {
					doubleValues = true;
				}
				for(int j = 0; j < mapToTransform.get(valueString); j++) {
					values[i++] = value;
				}
			} catch(NumberFormatException e) {
				System.err.println("Cannot convert " + valueString + " to double.");
			}
		}
		Arrays.sort(values);
		return new SpecialReturnValue(values, doubleValues);
	}
	
	public class SpecialReturnValue {
		private double[] array;
		private boolean doubleValues;
		
		public SpecialReturnValue(double[] array, boolean doubleValues) {
			this.array = array;
			this.doubleValues = doubleValues;
		}
		
		public double[] getSortedArray() {
			return this.array;
		}
		
		public boolean isDoubleValues() {
			return doubleValues;
		}
	}
}
