package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.prerendering.PreRendering;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBorderLayout;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WVBoxLayout;

public class GraphWebApplication extends WApplication {

	private GraphWidget graphWidget;
	private File metaDataFile;
	private MyComboBox wComboBoxMetadata;
	private WPieChartMine wPieChartMine;
	private WGroupBox wGroupBoxGoogleMapWidget;
	private GoogleChartWidget googleChartWidget;
	private final WBorderLayout layout = new WBorderLayout(getRoot());
	private WComboBox wComboBoxRegions;
	private char csvDelimitor = ';';
	private PreRendering preRendering;
//	private String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist/besttree.midpoint.tree";
//	private String treeLocation = "/Users/ewout/Documents/phylogeo/EUResist_New/tree/besttree.midpoint.newick";
//	private String treeLocation = "/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.tree";
//	private String treeLocation = "C:\\Program files\\rega_phylogeotool\\tree.newick";
	
	private String clusterRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/clusters";
	private String csvRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/xml";
	private String treeRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/treeview";

	public GraphWebApplication(WEnvironment env) {
		super(env);
		setTitle("PhyloGeoTool");

		try {
//			metaDataFile = new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/temp.csv");
			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist/EUResist.metadata.csv");
//			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist_New/EUResist.metadata.csv");
//			metaDataFile = new File("C:\\Program files\\rega_phylogeotool\\EUResist.metadata.csv");
			WGroupBox wGroupBoxNorth = getNavigationWGroupBox();
			
			layout.addWidget(wGroupBoxNorth, WBorderLayout.Position.North);
			preRendering = new PreRendering(clusterRenderLocation, csvRenderLocation, treeRenderLocation);
//			preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/portugal/clusters", "/Users/ewout/Documents/phylogeo/portugal/xml", "/Users/ewout/Documents/phylogeo/portugal/treeview");
			graphWidget = new GraphWidget(this, null, null, preRendering);
//			graphWidget = new GraphWidget(this, null, null, new File("/Users/ewout/example.tree"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//WApplication.getInstance().useStyleSheet("/phylogeotool/PhyloGeoTool/style/Cssexample.css");
		//graphWidget.setStyleClass("CSS-example");
		//graphWidget.resize(450, 350);
		layout.addWidget(graphWidget, WBorderLayout.Position.Center);
		
		//wGroupBoxPieChartWidget = getPieChartWGroupBox(null);
		//wGroupBoxPieChartWidget.hide();
		//layout.addWidget(wGroupBoxPieChartWidget, WBorderLayout.Position.East);
		
		wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(null,null,null);
		layout.addWidget(wGroupBoxGoogleMapWidget, WBorderLayout.Position.West);
		
		GraphWebApplication.this.googleChartWidget.setOptions("");
	}

	public void clicked(Node node) {
		if(node != null) {
			HashMap<String, Integer> hashMapTemp = preRendering.readCsv(node.getId(), wComboBoxMetadata.getValueText());
			System.out.println(hashMapTemp.keySet().size());
			HashMap<String, Integer> countries = preRendering.readCsv(node.getId(), "COUNTRY_OF_ORIGIN_ISO");
			GraphWebApplication.this.googleChartWidget.setCountries(countries);
			GraphWebApplication.this.googleChartWidget.setRegion("");
			GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
			wPieChartMine.setData(hashMapTemp);
		}
	}
	
	public void doubleClicked(Node node) {
//		Node tmpNode = graphWidget.getTree().getNodeById(node.getId());
		graphWidget.addPreviousRootId(graphWidget.getTreeClustered().getRootNode().getId());
		graphWidget.setTree(node.getId());
	}
	
	private WGroupBox getNavigationWGroupBox() throws IOException {
		WGroupBox wGroupBoxNorth = new WGroupBox();
		wComboBoxMetadata = new MyComboBox(metaDataFile, csvDelimitor);
		WPushButton wPushButton = new WPushButton("View Tree");
		WPushButton wPushButtonBack = new WPushButton("Back");
		WPushButton reset = new WPushButton("Reset");
		wPushButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
		        showDialog();
		    }
		});
		
		// Todo: Add functionality to the back button. Tree should be redrawn
		wPushButtonBack.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				if (WApplication.getInstance().getInternalPath().contains("/root")) {
					Integer previousRootId = graphWidget.getPreviousRootId();
					graphWidget.setTree(previousRootId);
				}
		    }
		});
		
		reset.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
//				WApplication.getInstance().setInternalPath("/root/1");
				// TODO: Fix the root representation. Shouldn't stay the integer 1
				graphWidget.setTree(1);
				GraphWebApplication.this.googleChartWidget.setCountries(new HashMap<String, Integer>());
				GraphWebApplication.this.googleChartWidget.setRegion("");
				GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
				wPieChartMine.setData(new HashMap<String, Integer>());
				graphWidget.emptyPreviousRootId();
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
		wGroupBoxNorth.addWidget(wPushButtonBack);
		wGroupBoxNorth.addWidget(reset);
		//wGroupBoxNorth.addWidget(slider);
		return wGroupBoxNorth;
	}

	private final void showDialog() {
	    final WDialog dialog = new WDialog("Tree");
	    WImageTreeMine wImageTreeMine = null;
	    if (WApplication.getInstance().getInternalPath().contains("/root")) {
	    	wImageTreeMine = new WImageTreeMine(treeRenderLocation, WApplication.getInstance().getInternalPath().split("/")[2]);
		} else {
			// TODO: Change value of clusterId 
			wImageTreeMine = new WImageTreeMine(treeRenderLocation, "1");
		}
	    dialog.getContents().addWidget(wImageTreeMine.getWidget());
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
