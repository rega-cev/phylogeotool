package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.model.VirtualModel;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtilsMetadata;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.utils.Settings;
import be.kuleuven.rega.prerendering.PreRendering;
import eu.webtoolkit.jwt.SelectionMode;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAbstractItemView;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBorderLayout;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WMouseEvent.Button;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WStandardItem;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WTableView;
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
	private String leafIdsLocation = "";

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
		this.leafIdsLocation = settings.getLeafsIdsPath();
		this.metaDataFile = new File(settings.getMetaDataFile());
		
		try {
//			metaDataFile = new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/temp.csv");
//			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist/EUResist.metadata.csv");
//			metaDataFile = new File("/Users/ewout/Documents/phylogeo/EUResist_New/EUResist.metadata.csv");
//			metaDataFile = new File("C:\\Program files\\rega_phylogeotool\\EUResist.metadata.csv");
//			WGroupBox wGroupBoxNorth = getNavigationWGroupBox();
			
//			layout.addWidget(wGroupBoxNorth, WBorderLayout.Position.North);
			preRendering = new PreRendering(clusterRenderLocation, csvRenderLocation, treeRenderLocation, leafIdsLocation);
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
		WPushButton exportSequencesButton = new WPushButton("Export sequences");
		exportSequencesButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
		       showSequenceData(preRendering.getLeafIdFromXML(WApplication.getInstance().getInternalPath().split("/")[2]));
			}
		});
		WHBoxLayout wHBoxLayout = new WHBoxLayout();
		wHBoxLayout.addWidget(wPushButton);
		wHBoxLayout.addWidget(exportSequencesButton);
		wVBoxLayoutGraphWidget.addLayout(wHBoxLayout);
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

		graphWidget.resize((int)graphWidth, (int)graphHeigth);
		wGroupBoxGoogleMapWidget.resize((int)mapWidth, 450);
	}

	public void clicked(WMouseEvent wMouseEvent, Node node) {
		if(node != null) {
			if(wMouseEvent.getButton().equals(Button.LeftButton)) {
				HashMap<String, Integer> hashMapTemp = preRendering.readCsv(node.getId(), wComboBoxMetadata.getValueText(), settings.getShowNAData());
	//			System.out.println(hashMapTemp.keySet().size());
				HashMap<String, Integer> countries = preRendering.readCsv(node.getId(), "COUNTRY_OF_ORIGIN_ISO", settings.getShowNAData());
				GraphWebApplication.this.googleChartWidget.setCountries(countries);
				GraphWebApplication.this.googleChartWidget.setRegion("");
				GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
				wPieChartMine.setData(hashMapTemp);
			} else if(wMouseEvent.getButton().equals(Button.RightButton)) {
				System.out.println("Right mouse has been clicked on the node.");
			}
		}
	}
	
	public void doubleClicked(Node node) {
//		Node tmpNode = graphWidget.getTree().getNodeById(node.getId());
		graphWidget.addPreviousRootId(graphWidget.getTreeClustered().getRootNode().getId());
		graphWidget.setTree(node.getId());
	}
	
	public void rightMouseClick(Node node) {
		System.out.println(node.getId() + " has been right mouse clicked.");
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
		WPushButton reset = new WPushButton("Reset");
		
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

		wGroupBoxNorth.addWidget(reset);
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
	
	private final void showSequenceData(List<String> ids) {
		final WDialog dialog = new WDialog("Sequences");
		WTableView tableView = new WTableView();
		List<String> metaDatas = null;
		try {
			metaDatas = CsvUtilsMetadata.getDataFromIds(ids, this.metaDataFile, ';');
		} catch(IOException e) {
			System.err.println(CsvUtilsMetadata.class + " Couldn't create fileReader on " + this.metaDataFile.getPath());
		}
		WStandardItemModel wStandardItemModel = new WStandardItemModel(metaDatas.size(),10);

		int i = 0;
		int j = 0;
		for(String line:metaDatas) {
			j = 0;
			String [] data = line.split(";");
			for(String dataItem:data) {
				wStandardItemModel.setItem(i, j++, new WStandardItem(dataItem));
			}
			i++;
		}
		
		wStandardItemModel.setHeaderData(0, "ID");
		wStandardItemModel.setHeaderData(1, "YEAR OF BIRTH");
		wStandardItemModel.setHeaderData(2, "GENDER");
		wStandardItemModel.setHeaderData(3, "COUNTRY OF ORIGIN");
		wStandardItemModel.setHeaderData(4, "COUNTRY OF ORIGIN");
		wStandardItemModel.setHeaderData(5, "COUNTRY OF INFECTION");
		wStandardItemModel.setHeaderData(6, "COUNTRY OF INFECTION");
		wStandardItemModel.setHeaderData(7, "ETHNIC GROUP");
		wStandardItemModel.setHeaderData(8, "RISK GROUP");
		wStandardItemModel.setHeaderData(9, "SUBTYPE");
		tableView.setModel(wStandardItemModel);
		tableView.setRowHeaderCount(1);
		tableView.setSortingEnabled(false);
		tableView.setAlternatingRowColors(true);
		tableView.setRowHeight(new WLength(28));
		tableView.setHeaderHeight(new WLength(28));
		tableView.setSelectionMode(SelectionMode.ExtendedSelection);
		tableView.setEditTriggers(EnumSet.of(WAbstractItemView.EditTrigger.NoEditTrigger));
		tableView.resize(new WLength(650), new WLength(400));
	    dialog.getContents().addWidget(tableView);
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
