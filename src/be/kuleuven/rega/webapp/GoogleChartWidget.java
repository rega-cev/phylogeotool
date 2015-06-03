package be.kuleuven.rega.webapp;

import java.util.EnumSet;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.webtoolkit.jwt.JSignal1;
import eu.webtoolkit.jwt.RenderFlag;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.utils.EnumUtils;

public class GoogleChartWidget extends WCompositeWidget {

	private static Logger logger = LoggerFactory.getLogger(GoogleChartWidget.class);
	private HashMap<String, Integer> countries = null;
	private String region;
	private static final HashMap<String, String> regionToDigit;
	static {
		regionToDigit = new HashMap<String, String>();
		regionToDigit.put("Europe", "150");
		regionToDigit.put("Africa", "002");
		regionToDigit.put("Americas", "019");
		regionToDigit.put("Northern America", "021");
		regionToDigit.put("Caribbean", "029");
		regionToDigit.put("Central America", "013");
		regionToDigit.put("South America", "005");
		regionToDigit.put("Asia", "142");
		
		regionToDigit.put("Northern Africa", "015");
		regionToDigit.put("Western Africa", "011");
		regionToDigit.put("Middle Africa", "017");
		regionToDigit.put("Eastern Africa", "014");
		regionToDigit.put("Southern Africa", "018");
		regionToDigit.put("Northern Europe", "154");
		regionToDigit.put("Western Europe", "155");
		regionToDigit.put("Eastern Europe", "151");
		regionToDigit.put("Southern Europe", "039");
		regionToDigit.put("Caribbean", "029");
		regionToDigit.put("Central Asia", "143");
		regionToDigit.put("Eastern Asia", "030");
		regionToDigit.put("Southern Asia", "034");
		regionToDigit.put("South-Eastern Asia", "035");
		regionToDigit.put("Western Asia", "145");
		regionToDigit.put("Oceania", "009");
		regionToDigit.put("Australia and New Zealand", "053");
		regionToDigit.put("Melanesia", "054");
		regionToDigit.put("Micronesia", "057");
		regionToDigit.put("Polynesia", "061");
		
	}
	
	public GoogleChartWidget(WContainerWidget parent) {
		super();
		this.selected_ = new JSignal1<String>(this, "select") {};
		this.setImplementation(new WContainerWidget());
		if (parent != null) {
			parent.addWidget(this);
		}
	}
	public GoogleChartWidget(HashMap<String, Integer> countries, String region) {
		this((WContainerWidget) null);
		this.countries = countries;
		this.region = region;
	}
	public GoogleChartWidget() {
		this((WContainerWidget) null);
	}
	
	/**
	 * The click event.
	 * <p>
	 * This event is fired when the user selects a country on the google chart.
	 */
	public JSignal1<String> selected() {
		return this.selected_;
	}
	
	/**
	 * Sets the options to draw
	 */
	public void setOptions(String region) {
		this.region = region;
		StringBuilder strm = new StringBuilder();
		strm.append(buildChart(region));
		strm.append(this.getJsRef()).append(".chart.draw(");
		strm.append("data, options);");
		this.doGmJavaScript(strm.toString());
	}
	
	private String buildChart(String region) {
		StringBuilder strm = new StringBuilder();
		String regionToView = "";
		if(regionToDigit.containsKey(region)) { regionToView = regionToDigit.get(region); } else { regionToView = "world"; }
		strm.append("var options = {explorer: {maxZoomOut:2,keepInBounds: true}};");
		strm.append("options['region'] = '" +  regionToView  + "';");
		strm.append("var data = google.visualization.arrayToDataTable([");
		strm.append("['Country', 'Cohort size'],");
		if(countries != null) {
			for(String country:countries.keySet()) {
				strm.append("[\"" + country + "\", " + countries.get(country) + "],");
			}
			strm.deleteCharAt(strm.length() - 1);
		}
		strm.append("]);");
		return strm.toString();
	}
	
	/**
	 * Destructor.
	 */
	public void remove() {
		;
		super.remove();
	}
	
	static String WT_RESIZE_JS = "wtResize";
	private JSignal1<String> selected_;
	
	@Override
	protected void render(EnumSet<RenderFlag> flags) {
		if (!EnumUtils.mask(flags, RenderFlag.RenderFull).isEmpty()) {
			WApplication app = WApplication.getInstance();
			final String gmuri = "http://www.google.com/jsapi";
			app.require(gmuri, "google");
			
			String initFunction = app.getJavaScriptClass()
					+ ".init_google_chart_" + this.getId();
			StringBuilder strm = new StringBuilder();
			strm.append("{ ").append(initFunction).append(
					" = function() {var self = ").append(this.getJsRef())
					.append(";if (!self) { setTimeout(").append(initFunction)
					.append(", 0);return;}");
				strm.append("var chart = new google.visualization.GeoChart(self);");
				strm.append(buildChart(region));
				strm.append("chart.draw(data, options);");
			strm.append("self.chart = chart;");
			this.streamJSListener(this.selected_, "select", strm);
			strm
			.append("setTimeout(function(){ delete ")
			.append(initFunction)
			.append(";}, 0)};")
			.append("google.load('visualization', '1', {'packages': ['geochart'], callback: ")
			.append(initFunction).append("});").append("}");
			app.doJavaScript(strm.toString(), true);
		}
		super.render(flags);
	}
	
	private void streamJSListener(final JSignal1<String> signal,String signalName, final StringBuilder strm) {
			strm.append("google.visualization.events.addListener(chart, \"")
			.append(signalName)
			.append("\", function testPopup() { var selectedItem = chart.getSelection()[0];if(selectedItem) { ")
			.append(signal.createCall("data.getValue(selectedItem.row, 0)"))
			.append(";}});");
	}
	
	protected void doGmJavaScript(final String jscode) {
		if (this.isRendered()) {
			this.doJavaScript(jscode);
		} 
//		else {
//			this.additions_.add(jscode);
//		}
	}
	
	public HashMap<String, Integer> getCountries() {
		return countries;
	}
	public void setCountries(HashMap<String, Integer> countries) {
		this.countries = countries;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}
