package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.utils.Settings;
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
	
//	private String clusterRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/clusters";
//	private String csvRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/xml";
//	private String treeRenderLocation = "/Users/ewout/Documents/phylogeo/EUResist/treeview";
	
	private String clusterRenderLocation = "";
	private String csvRenderLocation = "";
	private String treeRenderLocation = "";

	private double mapWidth = this.getEnvironment().getScreenWidth() * 0.45;
	private double mapHeigth = this.getEnvironment().getScreenHeight() * 0.60;
	private double graphWidth = this.getEnvironment().getScreenWidth() * 0.55;
	private double graphHeigth = this.getEnvironment().getScreenHeight() * 0.95;
	
	private Settings settings;
	
	public GraphWebApplication(WEnvironment env) {
		super(env);
		setTitle("PhyloGeoTool");

		this.settings = Settings.getInstance(null);
		this.clusterRenderLocation = settings.getClusterPath();
		this.csvRenderLocation = settings.getXmlPath();
		this.treeRenderLocation = settings.getTreeviewPath();
		this.metaDataFile = new File(settings.getMetaDataFile());
		
		try {
//			metaDataFile = new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/temp.csv");
//			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist/EUResist.metadata.csv");
//			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist_New/EUResist.metadata.csv");
//			metaDataFile = new File("C:\\Program files\\rega_phylogeotool\\EUResist.metadata.csv");
//			WGroupBox wGroupBoxNorth = getNavigationWGroupBox();
			
//			layout.addWidget(wGroupBoxNorth, WBorderLayout.Position.North);
			preRendering = new PreRendering(clusterRenderLocation, csvRenderLocation, treeRenderLocation);
//			preRendering = new PreRendering("/Users/ewout/Documents/phylogeo/portugal/clusters", "/Users/ewout/Documents/phylogeo/portugal/xml", "/Users/ewout/Documents/phylogeo/portugal/treeview");
			graphWidget = new GraphWidget(this, null, null, preRendering);
//			graphWidget = new GraphWidget(this, null, null, new File("/Users/ewout/example.tree"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WGroupBox wGroupBoxGraphWidget = new WGroupBox();
		WVBoxLayout wVBoxLayoutGraphWidget = new WVBoxLayout(wGroupBoxGraphWidget);
		WPushButton wPushButton = new WPushButton("View Tree");
		wPushButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
		        showDialog();
		    }
		});
//		wPushButton.resize(30, 30);
		wVBoxLayoutGraphWidget.addWidget(wPushButton);
		wVBoxLayoutGraphWidget.addWidget(graphWidget);
		//WApplication.getInstance().useStyleSheet("/phylogeotool/PhyloGeoTool/style/Cssexample.css");
		//graphWidget.setStyleClass("CSS-example");
		//layout.addWidget(graphWidget, WBorderLayout.Position.Center);
		
		layout.addWidget(wGroupBoxGraphWidget, WBorderLayout.Position.East);
		
		//wGroupBoxPieChartWidget = getPieChartWGroupBox(null);
		//wGroupBoxPieChartWidget.hide();
		//layout.addWidget(wGroupBoxPieChartWidget, WBorderLayout.Position.East);
		try {
			wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(null,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		layout.addWidget(wGroupBoxGoogleMapWidget, WBorderLayout.Position.West);
		
		GraphWebApplication.this.googleChartWidget.setOptions("");
		this.internalPathChanged().addListener(this,new Signal.Listener() {
			@Override
			public void trigger() {
				pathChanged();
			}
		});

//		wPushButton.resize(10, 10);
		graphWidget.resize((int)graphWidth, (int)graphHeigth);
//		wVBoxLayoutGraphWidget.resize((int)graphWidth - 100, 450);
		wGroupBoxGoogleMapWidget.resize((int)mapWidth, 450);
	}

	public void clicked(Node node) {
		if(node != null) {
			HashMap<String, Integer> hashMapTemp = preRendering.readCsv(node.getId(), wComboBoxMetadata.getValueText(), settings.getShowNAData());
//			System.out.println(hashMapTemp.keySet().size());
			HashMap<String, Integer> countries = preRendering.readCsv(node.getId(), "COUNTRY_OF_ORIGIN_ISO", settings.getShowNAData());
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
	
	public void pathChanged() {
		if (WApplication.getInstance().getInternalPath().contains("/root")) {
			graphWidget.setTree(Integer.parseInt(WApplication.getInstance().getInternalPath().split("/")[2]));
		} else {
			// TODO: Change value of clusterId 
			graphWidget.setTree(1);
		}
	}
	
	private WGroupBox getNavigationWGroupBox() {
		WGroupBox wGroupBoxNorth = new WGroupBox();
//		WPushButton wPushButtonBack = new WPushButton("Back");
		WPushButton reset = new WPushButton("Reset");
		
		// Todo: remove the back button
//		wPushButtonBack.clicked().addListener(this, new Signal.Listener() {
//			public void trigger() {
//				if (WApplication.getInstance().getInternalPath().contains("/root")) {
//					Integer previousRootId = graphWidget.getPreviousRootId();
//					graphWidget.setTree(previousRootId);
//				}
//		    }
//		});
		
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

		
//		wGroupBoxNorth.addWidget(wComboBoxMetadata);
//		wGroupBoxNorth.addWidget(wComboBoxRegions);
//		wGroupBoxNorth.addWidget(wPushButton);
//		wGroupBoxNorth.addWidget(wPushButtonBack);
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
	
	private WGroupBox getGoogleChartWGroupBox(final HashMap<String, Integer> countries, String region, HashMap<String, Integer> hashMapTemp) throws IOException {
		WGroupBox wGroupBoxGoogleMapWidget = new WGroupBox();
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
		WVBoxLayout wvBoxLayout = new WVBoxLayout(wGroupBoxGoogleMapWidget);
		wvBoxLayout.addWidget(wComboBoxRegions);
		googleChartWidget = new GoogleChartWidget(countries, region, settings.getDatalessRegionColor(), settings.getBackgroundcolor(), settings.getColorAxis());
		googleChartWidget.resize((int)this.mapWidth, (int)(mapHeigth));
		wvBoxLayout.addWidget(googleChartWidget);
		wPieChartMine = new WPieChartMine(hashMapTemp);
		wPieChartMine.getWPieChart().setWidth(new WLength(this.mapWidth * 0.25));
		wPieChartMine.getWPieChart().setHeight(new WLength(this.getEnvironment().getScreenHeight() * 0.25));
		wComboBoxMetadata = new MyComboBox(metaDataFile, csvDelimitor);
		wvBoxLayout.addWidget(wComboBoxMetadata);
		wvBoxLayout.addLayout(wPieChartMine.getWidget());
		return wGroupBoxGoogleMapWidget;
	}
	
	public double getGraphWidth() {
		return this.graphWidth;
	}
	
	public double getGraphHeight() {
		return this.graphHeigth;
	}
	
	public Settings getSettings() {
		return settings;
	}
}
