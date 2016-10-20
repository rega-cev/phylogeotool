package be.kuleuven.rega.webapp.widgets;

import java.util.EnumSet;
import java.util.Map;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WFont;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WShadow;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.chart.Axis;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public abstract class Chart {

	protected WCartesianChart chart = null;
	protected WStandardItemModel model;
	protected WStandardItemModel hoveredModel;
	protected WStandardPaletteMine wStandardPaletteMine = null;
	
	public Chart(WCartesianChart wCartesianChart) {
		chart = wCartesianChart;
		model = new WStandardItemModel();
		hoveredModel = new WStandardItemModel();
		chart.setMinimumSize(new WLength(120), new WLength(140));
		chart.setPlotAreaPadding(0, EnumSet.of(Side.Top));
		chart.setPlotAreaPadding(10, EnumSet.of(Side.Left));
		wStandardPaletteMine = new WStandardPaletteMine();
		chart.setPalette(wStandardPaletteMine);
	}
	
	public void setSecondBarColor(WColor wColor) {
		((WStandardPaletteMine)chart.getPalette()).setSecondColor(wColor);
	}

	public abstract void setData(Map<String, Integer> readCsv, boolean reread);

	public abstract void updateData(Map<String, Integer> readCsv, WColor wColor);

	public WCartesianChart getWidget() {
		return this.chart;
	}
	
	protected void updateChart(WStandardItemModel model, int maxValue) {
		chart.setModel(model);
		chart.setXSeriesColumn(0);
//		wCartesianChart.setLegendEnabled(true);
//		wCartesianChart.getAxis(Axis.XAxis).setLabelAngle(315);
//		wCartesianChart.getAxis(Axis.XAxis).setLabelAngle(15);
		WFont wFont = chart.getAxis(Axis.XAxis).getLabelFont();
		wFont.setSize(new WLength(12));
		chart.getAxis(Axis.XAxis).setLabelFont(wFont);
//		wCartesianChart.getAxis(Axis.YAxis).setMaximum(maxValue);
		chart.getAxis(Axis.YAxis).setLabelFormat("%.5g");
//		wCartesianChart.setAxis(wCartesianChart.getAxis(Axis.YAxis), Axis.YAxis);
		chart.getAxis(Axis.YAxis).setRange(0.0, maxValue);
//		wCartesianChart.setLegendLocation(LegendLocation.LegendOutside, Side.Bottom, AlignmentFlag.AlignBottom);
		chart.setPlotAreaPadding(50, EnumSet.of(Side.Left));
		chart.setToolTip("White charts show the distribution of all values contained in the clusters on the right. Colored charts show the distribution of all values contained in the cluster that's been hovered over.");
//		wCartesianChart.setPlotAreaPadding(150, EnumSet.of(Side.Right));
//		WFont legendFont = wCartesianChart.getLegendFont();
//		legendFont.setSize(new WLength(15));
//		wCartesianChart.setLegendStyle(legendFont, wCartesianChart.getLegendBorder(), wCartesianChart.getLegendBackground());
		for (int column = model.getColumnCount() - 1; column > 0; --column) {
			WDataSeries series = new WDataSeries(column, SeriesType.BarSeries);
			series.setStacked(true);
			series.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
			chart.addSeries(series);
		}
		chart.setMargin(WLength.Auto, EnumSet.of(Side.Left, Side.Right));
		chart.setOffsets(0);
//		wCartesianChart.resize(new WLength(120), new WLength(120));
	}
	
}
