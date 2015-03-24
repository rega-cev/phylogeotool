package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jebl.evolution.graphs.Node;
import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;
import be.kuleuven.rega.phylogeotool.tree.tools.JeblTools;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBorderLayout;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;

public class GraphWebApplication extends WApplication {

	private GraphWidget graphWidget;
	private File metaDataFile;
	private MyComboBox wComboBoxMetadata;
	private WPieChartMine wPieChartMine;
	private WGroupBox wGroupBoxGoogleMapWidget;
	private GoogleChartWidget googleChartWidget;
	private final WBorderLayout layout = new WBorderLayout(getRoot());
	private WComboBox wComboBoxRegions;

	public GraphWebApplication(WEnvironment env) {
		super(env);
		setTitle("PhyloGeoTool");

		try {
			metaDataFile = new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/one_viral_isolate_per_patient_subtyped_new.csv");
			WGroupBox wGroupBoxNorth = getNavigationWGroupBox();
			
			layout.addWidget(wGroupBoxNorth, WBorderLayout.Position.North);
			graphWidget = new GraphWidget<Node,String>(null, null, new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.tree"));
			graphWidget.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
				@Override
				public void trigger(WMouseEvent event) {
					GraphElementAccessor<Node,String> pickSupport = graphWidget.getVisualisationViewer().getPickSupport();
					Node node = (Node) pickSupport.getVertex(graphWidget.getVisualisationViewer().getGraphLayout(), event.getWidget().x, event.getWidget().y);
					if(node != null) {
						try {
							HashMap<String, Integer> hashMapTemp = CsvUtils.csvToHashMapStringInteger(metaDataFile, ',', JeblTools.getLeafNames(graphWidget.getJeblTree(), node), wComboBoxMetadata.getValueText());
							HashMap<String, Integer> countries = CsvUtils.csvToHashMapStringInteger(metaDataFile, ',', JeblTools.getLeafNames(graphWidget.getJeblTree(), node), "Country of origin");
							GraphWebApplication.this.googleChartWidget.setCountries(countries);
							GraphWebApplication.this.googleChartWidget.setRegion("");
							GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
							wPieChartMine.setData(hashMapTemp);
							//wPieChartMine.getWidget().up
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			graphWidget.doubleClicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
				@Override
				public void trigger(WMouseEvent event) {
					graphWidget.doubleClicked();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		WApplication.getInstance().useStyleSheet("/phylogeotool/PhyloGeoTool/style/Cssexample.css");
		graphWidget.setStyleClass("CSS-example");
		//graphWidget.resize(450, 350);
		layout.addWidget(graphWidget, WBorderLayout.Position.Center);
		
		//wGroupBoxPieChartWidget = getPieChartWGroupBox(null);
		//wGroupBoxPieChartWidget.hide();
		//layout.addWidget(wGroupBoxPieChartWidget, WBorderLayout.Position.East);
		
		wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(null,null,null);
		layout.addWidget(wGroupBoxGoogleMapWidget, WBorderLayout.Position.West);
		
		GraphWebApplication.this.googleChartWidget.setOptions("");
	}

	private WGroupBox getNavigationWGroupBox() throws IOException {
		WGroupBox wGroupBoxNorth = new WGroupBox();
		wComboBoxMetadata = new MyComboBox(metaDataFile);
		WPushButton wPushButton = new WPushButton("View Tree");
		wPushButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
		        showDialog();
		    }
		});

		wComboBoxRegions = new WComboBox();
		wComboBoxRegions.addItem("World");
		wComboBoxRegions.addItem("Europe");
		wComboBoxRegions.addItem("Africa");
		wComboBoxRegions.addItem("Northern America");
		wComboBoxRegions.addItem("South America");
		wComboBoxRegions.addItem("Asia");
		wComboBoxRegions.addItem("Oceania");
		wComboBoxRegions.addItem("Northern Africa");
		wComboBoxRegions.addItem("Western Africa");
		wComboBoxRegions.addItem("Middle Africa");
		wComboBoxRegions.addItem("Eastern Africa");
		wComboBoxRegions.addItem("Southern Africa");
		wComboBoxRegions.addItem("Central America");
		wComboBoxRegions.addItem("Northern Europe");
		wComboBoxRegions.addItem("Western Europe");
		wComboBoxRegions.addItem("Eastern Europe");
		wComboBoxRegions.addItem("Southern Europe");
		wComboBoxRegions.addItem("Caribbean");
		wComboBoxRegions.addItem("Central Asia");
		wComboBoxRegions.addItem("Eastern Asia");
		wComboBoxRegions.addItem("Southern Asia");
		wComboBoxRegions.addItem("South-Eastern Asia");
		wComboBoxRegions.addItem("Western Asia");
		wComboBoxRegions.addItem("Australia and New Zealand");
		wComboBoxRegions.addItem("Melanesia");
		wComboBoxRegions.addItem("Micronesia");
		wComboBoxRegions.addItem("Polynesia");
		
		wComboBoxRegions.changed().addListener(this, new Signal.Listener() {
			public void trigger() {
				GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
			}
		});
		wGroupBoxNorth.addWidget(wComboBoxMetadata);
		wGroupBoxNorth.addWidget(wComboBoxRegions);
		wGroupBoxNorth.addWidget(wPushButton);
		return wGroupBoxNorth;
	}

	private final void showDialog() {
	    final WDialog dialog = new WDialog("Tree");
	    WWidget image = new WImageTreeMine(graphWidget).getWidget();
	    dialog.getContents().addWidget(image);
	    WPushButton cancel = new WPushButton("Exit", dialog.getContents());
	    dialog.rejectWhenEscapePressed();
	    cancel.clicked().addListener(dialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                    dialog.reject();
	                }
	            });
	    dialog.show();
	}
	
	private WGroupBox getGoogleChartWGroupBox(final HashMap<String, Integer> countries, String region, HashMap<String, Integer> hashMapTemp) {
		WGroupBox wGroupBoxGoogleMapWidget = new WGroupBox();
		WVBoxLayout wvBoxLayout = new WVBoxLayout(wGroupBoxGoogleMapWidget);
		googleChartWidget = new GoogleChartWidget(countries, region);
		googleChartWidget.resize(new WLength(600), new WLength(500));
		wvBoxLayout.addWidget(googleChartWidget);
		wGroupBoxGoogleMapWidget.resize(new WLength(600), new WLength(530));
		wPieChartMine = new WPieChartMine(hashMapTemp);
		wvBoxLayout.addLayout(wPieChartMine.getWidget());
		return wGroupBoxGoogleMapWidget;
	}
}
