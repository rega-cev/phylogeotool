package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtilsMetadata;
import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.phylogeotool.utils.Settings;
import be.kuleuven.rega.prerendering.PreRendering;
import be.kuleuven.rega.webapp.widgets.GoogleChartWidget;
import be.kuleuven.rega.webapp.widgets.WComboBoxRegions;
import be.kuleuven.rega.webapp.widgets.WImageTreeMine;
import be.kuleuven.rega.webapp.widgets.WPieChartMine;
import eu.webtoolkit.jwt.SelectionMode;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAbstractItemView;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBorderLayout;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WCssTextRule;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WStandardItem;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTableView;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;

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
	
	//TODO: Put back in the method
	private WContainerWidget wContainerWidgetNorth;
	private WPieChartMine wPieChartMineFloat;
	
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
		//TODO: Implement the way how the report should be written. Currently disabled, should be updated later on.
		WPushButton exportSequencesButton = new WPushButton("Report");
		exportSequencesButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				if (WApplication.getInstance().getInternalPath().contains("/root")) {
					showReport(preRendering.getLeafIdFromXML(WApplication.getInstance().getInternalPath().split("/")[2]), settings.getColumnsToExport());
				} else {
					// TODO: Change value of clusterId 
					showReport(preRendering.getLeafIdFromXML("1"), settings.getColumnsToExport());
				}
			}
		});
		WHBoxLayout wHBoxLayout = new WHBoxLayout();
		wPushButton.setMaximumSize(new WLength(graphWidth / 2), new WLength(25));
		wHBoxLayout.addWidget(wPushButton);
		exportSequencesButton.setMaximumSize(new WLength(graphWidth / 2), new WLength(25));
//		wHBoxLayout.addWidget(exportSequencesButton);
		wContainerWidgetNorth = new WContainerWidget();
		wContainerWidgetNorth.setMaximumSize(new WLength(1, Unit.Pixel), new WLength(1, Unit.Pixel));
		wVBoxLayoutGraphWidget.addLayout(wHBoxLayout);
		wVBoxLayoutGraphWidget.addWidget(wContainerWidgetNorth);
		wVBoxLayoutGraphWidget.addWidget(graphWidget);
		wVBoxLayoutGraphWidget.setSpacing(0);
		
		layout.addWidget(this.createHeader(), WBorderLayout.Position.North);
		layout.addWidget(wGroupBoxGraphWidget, WBorderLayout.Position.East);
		
		try {
			HashMap<String, Integer> countries = null;
			if (WApplication.getInstance().getInternalPath().contains("/root")) {
		    	countries = preRendering.readCsv(Integer.parseInt(WApplication.getInstance().getInternalPath().split("/")[2]), "COUNTRY_OF_ORIGIN_ISO", settings.getShowNAData());
			} else {
				// TODO: Change value of clusterId 
				countries = preRendering.readCsv(1, "COUNTRY_OF_ORIGIN_ISO", settings.getShowNAData());
			}
			
			wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(countries,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		layout.addWidget(wGroupBoxGoogleMapWidget, WBorderLayout.Position.West);
//		layout.addWidget(wContainerWidgetNorth,  WBorderLayout.Position.North);
		GraphWebApplication.this.googleChartWidget.setOptions("");
		this.internalPathChanged().addListener(this,new Signal.Listener() {
			@Override
			public void trigger() {
				pathChanged();
			}
		});

		graphWidget.resize((int)graphWidth, (int)graphHeigth);
		wGroupBoxGoogleMapWidget.resize((int)mapWidth, 450);
		this.getStyleSheet().addRule(new WCssTextRule(".CSS-example", "background: blue;position: fixed; width: 100%;top: 0px; left: 0px;z-index: 1"));
		wContainerWidgetNorth.setStyleClass("CSS-example");
	}
	
	private WTemplate createHeader() {
		WTemplate wTemplate = new WTemplate(WString.tr("euresist"));
		WXmlLocalizedStrings resources = new WXmlLocalizedStrings();
		resources.use("/be/kuleuven/rega/webapp/resources");
		this.setLocalizedStrings(resources);
		this.useStyleSheet(new WLink("style/euresist/euresist.css"));
		wTemplate.bindString("server", "http://engine.euresist.org");
//		this.getRoot().addWidget(wTemplate);
		return wTemplate;
	}

	public void clicked(WMouseEvent wMouseEvent, Node node, WCircleNode wCircleNode) {
		graphWidget.setTree(node.getId());
		mouseWentOut(null, wCircleNode);
		
		this.setStatisticGraph(wPieChartMine, wCircleNode.getNode().getId());
		this.setGoogleChart(wCircleNode.getNode().getId());
//		GraphWebApplication.this.googleChartWidget.setCountries(null);
//		GraphWebApplication.this.googleChartWidget.setRegion("");
//		GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
	}
	
	public void mouseWentOver(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		this.setGoogleChart(wCircleNode.getNode().getId());
		double xValue = wCircleNode.getCenterDrawing().getX();
		double yValue = wCircleNode.getCenterDrawing().getY();
		
		this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .div", "visibility: visible;position: absolute;z-index: 2;left: " + Double.toString(xValue - ((this.mapWidth * 0.25)/2) - 8) + "px; top: " + Double.toString(yValue - ((this.getEnvironment().getScreenHeight() * 0.25)/2) - 2) + "px;width: 100px; height: 80px"));
		
		wPieChartMineFloat = new WPieChartMine(null, wContainerWidgetNorth);
		wPieChartMineFloat.getWPieChart().setStyleClass("div");
		wPieChartMineFloat.getWPieChart().setInline(false);
		this.setStatisticGraph(wPieChartMineFloat, wCircleNode.getNode().getId());
		
		wPieChartMineFloat.getWPieChart().setWidth(new WLength(this.mapWidth * 0.25));
		wPieChartMineFloat.getWPieChart().setHeight(new WLength(this.getEnvironment().getScreenHeight() * 0.25));
		
		wContainerWidgetNorth.addWidget(wPieChartMineFloat.getWPieChart());
		
		/**
		 * We don't want to show all of the legend items if that is not necessary.
		 */
		this.setFloatingLegend();
	}

	public void mouseWentOut(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getWPieChart());
		if(wPieChartMineFloat.getLegend0() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend0());
		if(wPieChartMineFloat.getLegend1() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend1());
		if(wPieChartMineFloat.getLegend2() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend2());
		if(wPieChartMineFloat.getLegend3() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend3());
		if(wPieChartMineFloat.getLegend4() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend4());
		if(wPieChartMineFloat.getLegend5() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend5());
		if(wPieChartMineFloat.getLegend6() != null)
			wContainerWidgetNorth.removeWidget(wPieChartMineFloat.getLegend6());
	}
	
	public void pathChanged() {
		int id;
		if (WApplication.getInstance().getInternalPath().contains("/root")) {
			id = Integer.parseInt(WApplication.getInstance().getInternalPath().split("/")[2]);
		} else {
			// TODO: Change value of clusterId 
			id = 1;
		}
		graphWidget.setTree(id);
		setGoogleChart(id);
		setStatisticGraph(this.wPieChartMine, id);
	}
	
	private void setGoogleChart(int nodeId) {
		HashMap<String, Integer> countries = preRendering.readCsv(nodeId, "COUNTRY_OF_ORIGIN_ISO", settings.getShowNAData());
		this.googleChartWidget.setCountries(countries);
		this.googleChartWidget.setRegion("");
		this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
	}
	
	private void setStatisticGraph(WPieChartMine wPieChartMine, int id) {
		wPieChartMine.setData(preRendering.readCsv(id, wComboBoxMetadata.getValueText(), settings.getShowNAData()));
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
	
	private void setFloatingLegend() {
		if(wPieChartMineFloat.getLegend0() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText0", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 10 + "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend0().addStyleClass("wText0");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend0());
		}
		if(wPieChartMineFloat.getLegend1() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText1", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 30+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend1().addStyleClass("wText1");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend1());
		}
		if(wPieChartMineFloat.getLegend2() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText2", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 50+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend2().addStyleClass("wText2");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend2());
		}
		if(wPieChartMineFloat.getLegend3() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText3", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 70+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend3().addStyleClass("wText3");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend3());
		}
		if(wPieChartMineFloat.getLegend4() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText4", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 90+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend4().addStyleClass("wText4");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend4());
		}
		if(wPieChartMineFloat.getLegend5() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText5", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 110+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend5().addStyleClass("wText5");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend5());
		}
		if(wPieChartMineFloat.getLegend6() != null) {
			this.getStyleSheet().addRule(new WCssTextRule(".CSS-example .wText6", "background: white;visibility: visible;position: absolute;z-index: 2;left: " + 10 + "px; top: " + 130+ "px;width: 70px; height: 20px"));
			wPieChartMineFloat.getLegend6().addStyleClass("wText6");
			wContainerWidgetNorth.addWidget(wPieChartMineFloat.getLegend6());
		}
	}
	
	private final void showReport(List<String> ids, List<String> headersToShow) {
		final WDialog dialog = new WDialog("Sequences");
		WTableView tableView = new WTableView();
		List<String> metaDatas = null;
		try {
			metaDatas = CsvUtilsMetadata.getDataFromIds(ids, headersToShow, this.metaDataFile, ';');
		} catch(IOException e) {
			System.err.println(CsvUtilsMetadata.class + " Couldn't create fileReader on " + this.metaDataFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
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
		
		int k = 0;
		for(String header:headersToShow) {
			wStandardItemModel.setHeaderData(k++, header);
		}
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
		final WComboBoxRegions wComboBoxRegions = new WComboBoxRegions();
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
		wComboBoxMetadata.changed().addListener(this, new Signal.Listener() {
			public void trigger() {
				int id;
				if (WApplication.getInstance().getInternalPath().contains("/root")) {
					id = Integer.parseInt(WApplication.getInstance().getInternalPath().split("/")[2]);
				} else {
					// TODO: Change value of clusterId
					id = 1;
				}
				setStatisticGraph(wPieChartMine, id);
			}
		});
		wComboBoxMetadata.changed().trigger();
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
