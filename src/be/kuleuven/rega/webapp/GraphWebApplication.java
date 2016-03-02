package be.kuleuven.rega.webapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.prerendering.FacadeRequestData;
import be.kuleuven.rega.prerendering.PreRendering;
import be.kuleuven.rega.url.UrlManipulator;
import be.kuleuven.rega.webapp.widgets.GoogleChartWidget;
import be.kuleuven.rega.webapp.widgets.WBarChartMine;
import be.kuleuven.rega.webapp.widgets.WComboBoxRegions;
import be.kuleuven.rega.webapp.widgets.WDownloadResource;
import be.kuleuven.rega.webapp.widgets.WImageSDRMine;
import be.kuleuven.rega.webapp.widgets.WImageTreeMine;
import be.kuleuven.rega.webapp.widgets.WTreeDownloaderForm;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WCssTextRule;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WResource.DispositionType;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;
import figtree.application.GraphicFormat;

public class GraphWebApplication extends WApplication {

	private GraphWidget graphWidget;
	private File metaDataFile;
	private MyComboBox wComboBoxMetadata;
	private WBarChartMine wBarChartMine;
	private WLayout wGroupBoxGoogleMapWidget;
	private GoogleChartWidget googleChartWidget;
	private WComboBox wComboBoxRegions;
	private char csvDelimitor = ';';
	private FacadeRequestData facadeRequestData;
	private PPlacer pplacer;
	
	private String treeRenderLocation = "";
	private String clusterRenderLocation = "";
	private String csvRenderLocation = "";
	private String treeViewRenderLocation = "";
	private String sdrLocation = "";
	
	private boolean showNAData = false;

	private Settings settings;
	
	public GraphWebApplication(WEnvironment env) {
		super(env);
		setTitle("PhyloGeoTool");
		this.settings = Settings.getInstance(null);
		this.treeRenderLocation = settings.getTreePath();
		this.clusterRenderLocation = settings.getClusterPath();
		this.csvRenderLocation = settings.getXmlPath();
		this.treeViewRenderLocation = settings.getTreeviewPath();
		this.metaDataFile = new File(settings.getMetaDataFile());
		this.sdrLocation = settings.getRGraphsPath();
		this.showNAData = settings.getShowNAData();
		
		this.setCSS();
		
		facadeRequestData = new FacadeRequestData(new PreRendering(treeRenderLocation, clusterRenderLocation, csvRenderLocation, treeViewRenderLocation));
//			jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
//			Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
//			facadeRequestData = new FacadeRequestData(tree, new File("/Users/ewout/Documents/TDRDetector/fullPortugal/allSequences_cleaned_ids.out2.csv"), new DistanceCalculateFromTree());
		
		WVBoxLayout rootLayout = new WVBoxLayout(this.getRoot());
		WTemplate header = createHeader();
		rootLayout.addWidget(header);
		rootLayout.setStretchFactor(header, 0);
		
		WHBoxLayout bodyLayout = new WHBoxLayout();

		try {
			graphWidget = new GraphWidget(facadeRequestData);
			HashMap<String, Integer> countries = null;
			countries = facadeRequestData.readCsv(graphWidget.getCluster().getRoot().getId(), "COUNTRY_OF_ORIGIN", showNAData);
			wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(countries,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bodyLayout.addLayout(wGroupBoxGoogleMapWidget);
		rootLayout.addLayout(bodyLayout);
		rootLayout.setStretchFactor(bodyLayout, 1);
		
		WGroupBox wGroupBoxGraphWidget = new WGroupBox();
		wGroupBoxGraphWidget.setStyleClass("card");
		WVBoxLayout wVBoxLayoutGraphWidget = new WVBoxLayout(wGroupBoxGraphWidget);
		bodyLayout.addWidget(wGroupBoxGraphWidget);
		
		WHBoxLayout wHBoxLayout = new WHBoxLayout();
		
		WPushButton wPushButton = new WPushButton("View Tree");
		wPushButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				showDialog();
			}
		});
		
		WPushButton sdrButton = new WPushButton("SDR");
		sdrButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
		        showSDR();
		    }
		});
		sdrButton.setDisabled(true);
		
//		graphWidget.setMaximumSize(new WLength(100.0, Unit.Percentage), new WLength(100.0, Unit.Percentage));
		graphWidget.setMinimumSize(new WLength(300.0, Unit.Pixel), new WLength(100.0, Unit.Percentage));
		wPushButton.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(15));
		sdrButton.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(15));
		//TODO: Change this exact value
		wHBoxLayout.addWidget(wPushButton);
		wHBoxLayout.addWidget(sdrButton);
//		wHBoxLayout.addWidget(exportSequencesButton);
		wVBoxLayoutGraphWidget.addLayout(wHBoxLayout);
		wVBoxLayoutGraphWidget.addWidget(graphWidget);
		wVBoxLayoutGraphWidget.setStretchFactor(wHBoxLayout, 0);
		wVBoxLayoutGraphWidget.setStretchFactor(graphWidget, 1);
		
		//TODO: Implement the way how the report should be written. Currently disabled, should be updated later on.
//		WPushButton exportSequencesButton = new WPushButton("Report");
//		exportSequencesButton.clicked().addListener(this, new Signal.Listener() {
//			public void trigger() {
//				if (WApplication.getInstance().getInternalPath().contains("/root")) {
//					showReport(PreRendering.getLeafIdFromXML(leafIdsLocation, WApplication.getInstance().getInternalPath().split("/")[2]), settings.getColumnsToExport());
//				} else {
//					// TODO: Change value of clusterId 
//					showReport(PreRendering.getLeafIdFromXML(leafIdsLocation, "1"), settings.getColumnsToExport());
//				}
//			}
//		});
		
		GraphWebApplication.this.googleChartWidget.setOptions("");
		this.internalPathChanged().addListener(this,new Signal.Listener() {
			@Override
			public void trigger() {
				pathChanged();
			}
		});
	}
	
	public GraphWebApplication(WEnvironment wEnvironment, String pplacerId) {
		this(wEnvironment);
		String tmpDir = System.getProperty("java.io.tmpdir");
		if(Files.notExists(FileSystems.getDefault().getPath(tmpDir), LinkOption.NOFOLLOW_LINKS)) {
			System.err.println("Tmp directory does not exist! Please set the TEMP environment variable.");
		} else if(pplacerId != null) {
			String location = tmpDir + File.separator + "pplacer." + pplacerId;
			List<String> pplacedIds = PPlacer.getPPlacerIds(location);
			this.pplacer = new PPlacer(location + File.separator + "sequences.tog.tre", pplacedIds);
			graphWidget.setPPlacer(pplacer);
		}
	}
	
	private WTemplate createHeader() {
		WTemplate wTemplate = new WTemplate(WString.tr("euresist"));
		WXmlLocalizedStrings resources = new WXmlLocalizedStrings();
		resources.use("/be/kuleuven/rega/webapp/resources");
		this.setLocalizedStrings(resources);
		this.useStyleSheet(new WLink("style/euresist/euresist.css"));
		wTemplate.bindString("server", "http://engine.euresist.org");
		return wTemplate;
	}

	public void clicked(WMouseEvent wMouseEvent, Node node, WCircleNode wCircleNode) {
		graphWidget.setCluster(node.getId());
		mouseWentOut(null, wCircleNode);
		
		this.setGoogleChart(wCircleNode.getNode().getId());
		this.setStatisticGraph(wBarChartMine, wCircleNode.getNode().getId());
	}
	
	
	public void mouseWentOver(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		this.setGoogleChart(wCircleNode.getNode().getId());
		this.updateStatisticGraph(wBarChartMine, wCircleNode.getNode().getId());
	}

	public void mouseWentOut(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
		this.setGoogleChart(id);
		this.setStatisticGraph(wBarChartMine, id);
	}
	
	public void pathChanged() {
		int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
		graphWidget.setCluster(id);
		setGoogleChart(id);
		setStatisticGraph(this.wBarChartMine, id);
	}
	
	private void setGoogleChart(int nodeId) {
		HashMap<String, Integer> countries = facadeRequestData.readCsv(nodeId, "COUNTRY_OF_ORIGIN", showNAData);
		this.googleChartWidget.setCountries(countries);
		this.googleChartWidget.setRegion("");
		this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
	}
	
	private void setStatisticGraph(WBarChartMine wBarChartMine, int nodeId) {
		if(wComboBoxMetadata != null)
			wBarChartMine.setData(facadeRequestData.readCsv(nodeId, wComboBoxMetadata.getValueText(), showNAData));
	}
	
	private void updateStatisticGraph(WBarChartMine wBarChartMine, int nodeId) {
		if(wComboBoxMetadata != null)
			wBarChartMine.updateData(facadeRequestData.readCsv(nodeId, wComboBoxMetadata.getValueText(), showNAData));
	}
	
	private final void showDialog() {
	    final WDialog dialog = new WDialog("Tree");
//	    WFigTreeMine wFigTreeMine = new WFigTreeMine(facadeRequestData.getJeblTree(), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())));
	    WImageTreeMine wImageTreeMine = new WImageTreeMine(treeViewRenderLocation, UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
//	    dialog.getContents().addWidget(wImageTreeMine.getWidget());
	    WPushButton cancel = new WPushButton("Exit");
	    
	    WPushButton button = new WPushButton("Create", dialog.getContents());

	    final WDownloadResource wDownloadResource = new WDownloadResource(dialog.getContents(), "cluster_" + UrlManipulator.getId(WApplication.getInstance().getInternalPath()), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())), GraphicFormat.PDF);
	    wDownloadResource.setDispositionType(DispositionType.Attachment);
	    button.setLink(new WLink(wDownloadResource));

	    dialog.getContents().addWidget(wImageTreeMine.getWidget());
	    WTreeDownloaderForm wTreeDownloader = new WTreeDownloaderForm(dialog, wDownloadResource, button, cancel);
	    dialog.getContents().addWidget(wTreeDownloader.getWidget());
	    
	    dialog.rejectWhenEscapePressed();
	    
	    
	    cancel.clicked().addListener(dialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                    dialog.reject();
	                }
	            });
	    dialog.show();
	}
	
	private final void showSDR() {
		final WDialog dialog = new WDialog("SDR - Second Derivative");
		WImageSDRMine wImageSDRMine = new WImageSDRMine(sdrLocation, UrlManipulator.getId(WApplication.getInstance().getInternalPath()), true);
		WImageSDRMine wImageSDRMine2 = new WImageSDRMine(sdrLocation, UrlManipulator.getId(WApplication.getInstance().getInternalPath()), false);
	    dialog.getContents().addWidget(wImageSDRMine.getWidget());
	    dialog.getContents().addWidget(wImageSDRMine2.getWidget());
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
	
	private void setCSS() {
		this.getStyleSheet().addRule(new WCssTextRule("body", "background-color: #BEBEBE"));
		this.getStyleSheet().addRule(new WCssTextRule(".label", "background: #FFFFFF;margin-left: 0px; margin-top: 0px;border: 0px;"));
		this.getStyleSheet().addRule(new WCssTextRule(".card", "background: #FFFFFF;margin-left: 0px; margin-top: 0px; border: 0px;box-shadow: 5px 0px 15px #444444;"));
	}
	
//	private final void showReport(List<String> ids, List<String> headersToShow) {
//		final WDialog dialog = new WDialog("Sequences");
//		WTableView tableView = new WTableView();
//		List<String> metaDatas = null;
//		try {
//			metaDatas = CsvUtilsMetadata.getDataFromIds(ids, headersToShow, this.metaDataFile, ';');
//		} catch(IOException e) {
//			System.err.println(CsvUtilsMetadata.class + " Couldn't create fileReader on " + this.metaDataFile.getPath());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		WStandardItemModel wStandardItemModel = new WStandardItemModel(metaDatas.size(),10);
//
//		int i = 0;
//		int j = 0;
//		for(String line:metaDatas) {
//			j = 0;
//			String [] data = line.split(";");
//			for(String dataItem:data) {
//				wStandardItemModel.setItem(i, j++, new WStandardItem(dataItem));
//			}
//			i++;
//		}
//		
//		int k = 0;
//		for(String header:headersToShow) {
//			wStandardItemModel.setHeaderData(k++, header);
//		}
//		tableView.setModel(wStandardItemModel);
//		tableView.setRowHeaderCount(1);
//		tableView.setSortingEnabled(false);
//		tableView.setAlternatingRowColors(true);
//		tableView.setRowHeight(new WLength(28));
//		tableView.setHeaderHeight(new WLength(28));
//		tableView.setSelectionMode(SelectionMode.ExtendedSelection);
//		tableView.setEditTriggers(EnumSet.of(WAbstractItemView.EditTrigger.NoEditTrigger));
//		tableView.resize(new WLength(650), new WLength(400));
//	    dialog.getContents().addWidget(tableView);
//	    WPushButton cancel = new WPushButton("Exit", dialog.getContents());
//	    dialog.rejectWhenEscapePressed();
//	    cancel.clicked().addListener(dialog,
//	            new Signal1.Listener<WMouseEvent>() {
//	                public void trigger(WMouseEvent e1) {
//	                    dialog.reject();
//	                }
//	            });
//	    dialog.show();
//	}
	
	private WLayout getGoogleChartWGroupBox(final HashMap<String, Integer> countries, String region, HashMap<String, Integer> hashMapTemp) throws IOException {
		WVBoxLayout wvBoxLayout = new WVBoxLayout();
		wComboBoxRegions = new WComboBoxRegions();
		wComboBoxRegions.changed().addListener(this, new Signal.Listener() {
			public void trigger() {
				GraphWebApplication.this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
			}
		});

		/* TOP PART */
		
		WGroupBox wGroupBoxTop = new WGroupBox();
		wGroupBoxTop.setStyleClass("card");
		wGroupBoxTop.setMargin(0);
		wGroupBoxTop.setOffsets(0);
		wGroupBoxTop.setPadding(new WLength(0));
		
		wvBoxLayout.addWidget(wGroupBoxTop);
		WVBoxLayout wvBoxLayoutMap = new WVBoxLayout(wGroupBoxTop);
		
		WGroupBox wgroupboxRegion = new WGroupBox();
		wgroupboxRegion.setStyleClass("label");
		WText regionLabel = new WText("Select region: ");
		wgroupboxRegion.addWidget(regionLabel);
		wgroupboxRegion.addWidget(wComboBoxRegions);
		
		googleChartWidget = new GoogleChartWidget(countries, region, settings.getDatalessRegionColor(), settings.getBackgroundcolor(), settings.getColorAxis());
		googleChartWidget.setMinimumSize(new WLength(30.0, Unit.Percentage), new WLength(300.0, Unit.Pixel));

		wvBoxLayoutMap.addWidget(wgroupboxRegion);
		wvBoxLayoutMap.setStretchFactor(wgroupboxRegion, 0);
		wvBoxLayoutMap.addWidget(googleChartWidget);
		wvBoxLayoutMap.setStretchFactor(googleChartWidget, 1);
		
		/* BOTTOM PART */
		
		WGroupBox wGroupBoxBottom = new WGroupBox();
		wGroupBoxBottom.setStyleClass("card");
		wGroupBoxBottom.setMargin(0);
		wGroupBoxBottom.setOffsets(0);
		wGroupBoxBottom.setPadding(new WLength(0));
		wvBoxLayout.addWidget(wGroupBoxBottom);
		WVBoxLayout wvBoxLayoutChart = new WVBoxLayout(wGroupBoxBottom);
		
		wBarChartMine = new WBarChartMine(hashMapTemp);
		
		if(metaDataFile != null && metaDataFile.exists()) {
			wComboBoxMetadata = new MyComboBox(metaDataFile, csvDelimitor);
			wComboBoxMetadata.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
					setStatisticGraph(wBarChartMine, id);
				}
			});
			wComboBoxMetadata.changed().trigger();
			wComboBoxMetadata.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(20));
			WGroupBox wgroupboxMetadata = new WGroupBox();
			wgroupboxMetadata.setStyleClass("label");
			WText metadataLabel = new WText("Select attribute: ");
			wgroupboxMetadata.addWidget(metadataLabel);
			metadataLabel.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(20));
			wgroupboxMetadata.addWidget(wComboBoxMetadata);
			wvBoxLayoutChart.addWidget(wgroupboxMetadata);
			wvBoxLayoutChart.setStretchFactor(wgroupboxMetadata, 0);
		}
		
		
		wvBoxLayoutChart.addLayout(wBarChartMine.getLayout());
		wvBoxLayoutChart.setStretchFactor(wBarChartMine.getLayout(), 1);
		return wvBoxLayout;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public PPlacer getPPlacer() {
		return this.pplacer;
	}
}
