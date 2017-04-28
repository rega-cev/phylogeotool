package be.kuleuven.rega.webapp;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import be.kuleuven.rega.comparator.SortingOptions;
import be.kuleuven.rega.form.MyComboBox;
import be.kuleuven.rega.phylogeotool.core.Node;
import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.prerendering.FacadeRequestData;
import be.kuleuven.rega.prerendering.PreRendering;
import be.kuleuven.rega.url.UrlManipulator;
import be.kuleuven.rega.webapp.widgets.Chart;
import be.kuleuven.rega.webapp.widgets.GoogleChartWidget;
import be.kuleuven.rega.webapp.widgets.PPlacerForm;
import be.kuleuven.rega.webapp.widgets.SDRVisualizer;
import be.kuleuven.rega.webapp.widgets.WBarChartMine;
import be.kuleuven.rega.webapp.widgets.WComboBoxRegions;
import be.kuleuven.rega.webapp.widgets.WConfirmationDialog;
import be.kuleuven.rega.webapp.widgets.WDownloadResource;
import be.kuleuven.rega.webapp.widgets.WExportTreeForm;
import be.kuleuven.rega.webapp.widgets.WHistogramMine;
import be.kuleuven.rega.webapp.widgets.WImageTreeMine;
import be.kuleuven.rega.webapp.widgets.WTreeDownloaderForm;
import eu.webtoolkit.jwt.AnchorTarget;
import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WCheckBox;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WTableCell;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import figtree.application.GraphicFormat;

public class GraphWebApplication extends WApplication {

	private GraphWidget graphWidget;
	private File metaDataFile;
	private MyComboBox wComboBoxMetadata;
	private WBarChartMine wBarChartMine;
	private WHistogramMine wHistogramMine;
	private Chart chart;
	private WLayout wGroupBoxGoogleMapWidget;
	private GoogleChartWidget googleChartWidget;
	private WComboBox wComboBoxRegions;
	private char csvDelimitor = ';';
	private FacadeRequestData facadeRequestData;
	private PPlacer pplacer;
	
	// Elements that have to be dynamically shown and hidden
	private final WText orderLabel = new WText("Select ordering: ");
	private final WComboBox order = new WComboBox();
	private final WText showNALabel = new WText("Show NA: ");
	private final WCheckBox showNACheckbox = new WCheckBox();
	
//	private String treeRenderLocation = "";
//	private String clusterRenderLocation = "";
//	private String csvRenderLocation = "";
//	private String treeViewRenderLocation = "";
	private String basePath = "";
	private WLabel treeLevel;
	
	private boolean showNAData = false;
	
	private ServletContext servletContext_;

	public GraphWebApplication(WEnvironment env, ServletContext servletContext) {
		super(env);
		setTitle("PhyloGeoTool");
//		this.treeRenderLocation = settings.getTreePath();
		this.basePath = Settings.getInstance().getBasePath();
//		this.clusterRenderLocation = settings.getClusterPath();
//		this.csvRenderLocation = settings.getXmlPath();
//		this.treeViewRenderLocation = settings.getTreeviewPath();
		this.metaDataFile = new File(Settings.getInstance().getMetaDataFile());
		this.showNAData = Settings.getInstance().getShowNAData();
		this.servletContext_ = servletContext;
		
		this.setCSS();
		
		facadeRequestData = new FacadeRequestData(new PreRendering(basePath));
		try {
			boolean isGeoGraphicDataISO3166 = facadeRequestData.isGeoGraphicDataISO3166();
			if(!isGeoGraphicDataISO3166) {
				showISO3166Warning();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//			jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
//			Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree) jeblTree, new ArrayList<String>());
//			facadeRequestData = new FacadeRequestData(tree, new File("/Users/ewout/Documents/TDRDetector/fullPortugal/allSequences_cleaned_ids.out2.csv"), new DistanceCalculateFromTree());
		
		WVBoxLayout rootLayout = new WVBoxLayout(this.getRoot());
		// Added for design
		rootLayout.setContentsMargins(2, 0, 2, 2);
		rootLayout.setSpacing(-5);
		WTemplate header = createHeader();
		header.setMargin(-2, EnumSet.of(Side.Right, Side.Left));
		rootLayout.addWidget(header);
		rootLayout.setStretchFactor(header, 0);
		
		WHBoxLayout bodyLayout = new WHBoxLayout();
		// Added for design
		bodyLayout.setSpacing(2);
		try {
			graphWidget = new GraphWidget(facadeRequestData);
			HashMap<String, Integer> countries = null;
			countries = facadeRequestData.readCsv(graphWidget.getCluster().getRoot().getId(), Settings.getInstance().getVisualizeGeography());
			wGroupBoxGoogleMapWidget = getGoogleChartWGroupBox(countries,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bodyLayout.addLayout(wGroupBoxGoogleMapWidget);
		rootLayout.addLayout(bodyLayout);
		rootLayout.setStretchFactor(bodyLayout, 1);
		
		WGroupBox wGroupBoxGraphWidget = new WGroupBox();
		wGroupBoxGraphWidget.setPadding(new WLength(0));
		wGroupBoxGraphWidget.setStyleClass("card");
		wGroupBoxGraphWidget.setMargin(0);
		WVBoxLayout wVBoxLayoutGraphWidget = new WVBoxLayout(wGroupBoxGraphWidget);
		bodyLayout.addWidget(wGroupBoxGraphWidget);
		
		WHBoxLayout wHBoxLayout = new WHBoxLayout();
		
		WPushButton wPushButton = new WPushButton("View Tree");
		wPushButton.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				showDialog();
			}
		});
		
		WPushButton pplaceButton = new WPushButton("Place your sequence");
		if(Settings.getInstance().getPPlacerSupport()) {
			pplaceButton.clicked().addListener(this, new Signal.Listener() {
				public void trigger() {
			        showPPlacer();
			    }
			});
		} else {
			pplaceButton.setDisabled(true);
		}
		
		WPushButton wPushButtonStatistics = new WPushButton("View statistics");
		if(Settings.getInstance().getStastisticsSupport()) {
			wPushButtonStatistics.clicked().addListener(this, new Signal.Listener() {
				public void trigger() {
					showDialogStatistics();
				}
			});
		} else {
			wPushButtonStatistics.setHidden(true);
		}
		
//		WHBoxLayout treeLevelHBox = new WHBoxLayout();
		WGroupBox wGroupBox = new WGroupBox();
		wGroupBox.setMaximumSize(new WLength(250), new WLength(25));
		treeLevel = new WLabel("Level: ");
		treeLevel.setText(treeLevel.getText() + String.valueOf(graphWidget.getBreadCrumb()));
		treeLevel.setMargin(7, Side.Right);
		
		final WImage wImage = new WImage(new WLink(getServletContext().getContextPath().concat("/images/question_mark.png")));
		wImage.setToolTip("Use the webbrowser back button to move up one level. Use the webbrowser forward button to move down again.");
		wImage.setMaximumSize(new WLength(15), new WLength(15));
		wImage.setMargin(7, Side.Right);
		wImage.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				WDialog wDialog = new WDialog("Help");
				wDialog.setClosable(true);
				wDialog.rejectWhenEscapePressed(true);
				WText explanation = new WText("Use the webbrowser back button to move up one level. <br />Use the webbrowser forward button to move down again.");
				wDialog.getContents().addWidget(explanation);
				wDialog.show();
			}
		});
		
//		final WImage wImageHome = new WImage(new WLink(getServletContext().getContextPath().concat("/images/home.png")));
//		wImageHome.setMaximumSize(new WLength(20), new WLength(20));
		final WPushButton wPushbuttonHome = new WPushButton("Back to level 0");
		wImage.setMaximumSize(new WLength(50), new WLength(15));
		wPushbuttonHome.clicked().addListener(this, new Signal.Listener() {
			public void trigger() {
				graphWidget.setBreadCrumb(0);
				graphWidget.setCluster(1, treeLevel, 0);
				setGoogleChart(1);
				setStatisticGraph(1, true);
			}
		});
		wGroupBox.addWidget(wImage);
		wGroupBox.addWidget(treeLevel);
		wGroupBox.addWidget(wPushbuttonHome);
		wGroupBox.setStyleClass("card");
//		wGroupBox.setLayout(treeLevelHBox);
		
//		graphWidget.setMaximumSize(new WLength(100.0, Unit.Percentage), new WLength(100.0, Unit.Percentage));
		graphWidget.setMinimumSize(new WLength(500.0, Unit.Pixel), new WLength(100.0, Unit.Percentage));
//		wPushButton.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(15));
//		pplaceButton.setMaximumSize(new WLength(50.0, Unit.Percentage), new WLength(15));
//		treeLevel.setMaximumSize(new WLength(1.0, Unit.Pixel), new WLength(15));
//		treeLevel.setWidth(new WLength(30.0));
		//TODO: Change this exact value
		wHBoxLayout.addWidget(wPushButton);
//		wHBoxLayout.addWidget(treeLevel);
		wHBoxLayout.addWidget(pplaceButton);
		wHBoxLayout.addWidget(wPushButtonStatistics);
//		wHBoxLayout.addWidget(exportSequencesButton);
		wVBoxLayoutGraphWidget.addLayout(wHBoxLayout);
		wVBoxLayoutGraphWidget.addWidget(graphWidget);
		wVBoxLayoutGraphWidget.addWidget(wGroupBox);
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
	
	public ServletContext getServletContext() {
		return this.servletContext_;
	}

	public GraphWebApplication(WEnvironment wEnvironment, ServletContext servletContext, String pplacerId) {
		this(wEnvironment, servletContext);
//		String tmpDir = System.getProperty("java.io.tmpdir");
//		if(Files.notExists(FileSystems.getDefault().getPath(tmpDir), LinkOption.NOFOLLOW_LINKS)) {
//			System.err.println("Tmp directory does not exist! Please set the TEMP environment variable.");
//		} else if(pplacerId != null) {
//			String location = tmpDir + File.separator + "pplacer." + pplacerId;
//			List<String> pplacedIds = PPlacer.getPPlacerIds(location);
//			this.pplacer = new PPlacer(location + File.separator + "sequences.tog.tre", pplacedIds);
//			graphWidget.setPPlacer(pplacer);
//		}
		
		if(pplacerId != null && !pplacerId.equals("")) {
			if(Settings.getInstance().getPPlacerSupport()) {
//				if(Files.notExists(FileSystems.getDefault().getPath(Settings.getInstance().getScriptFolder() + File.separator + "init.sh"), LinkOption.NOFOLLOW_LINKS)) {
//					System.err.println("File init.sh does not exists at: " + Settings.getInstance().getScriptFolder() + File.separator + "init.sh");
//				} else {
//					String args[] = {Settings.getInstance().getScriptFolder() + File.separator + "init.sh", pplacerId};
//					StreamGobbler streamGobbler = StreamGobbler.runProcess(args);
//					System.out.println("PPlacer tree loaded from: " + streamGobbler.getLastLine());
//					String location = streamGobbler.getLastLine();
					
					String location = System.getProperty("java.io.tmpdir") + File.separator + "pplacer" + pplacerId;
					if(Files.notExists(FileSystems.getDefault().getPath(location + File.separator + "sequences.tog.tre"), LinkOption.NOFOLLOW_LINKS)) {
						System.err.println("PPlaced tree with id: " + pplacerId + " could not be found at " + location + File.separator + "sequences.tog.tre");
					} else {
						List<String> pplacedIds = PPlacer.getPPlacerIds(location);
						this.pplacer = new PPlacer(location + File.separator + "sequences.tog.tre", pplacedIds);
						graphWidget.setPPlacer(pplacer);
					}
//				}
			} else {
				System.err.println("PPlacer not supported. If you want to support PPlacer, please toggle PPlacer support on in your global-conf.xml file");
			}
		}
	}
	
	private WTemplate createHeader() {
		WTemplate wTemplate = new WTemplate(WString.tr("euresist"));
		WXmlLocalizedStrings resources = new WXmlLocalizedStrings();
		resources.use("/be/kuleuven/rega/webapp/resources");
		this.setLocalizedStrings(resources);
		this.useStyleSheet(new WLink(getServletContext().getContextPath().concat("/style/euresist/euresist.css")));
//		wTemplate.bindString("server", "http://engine.euresist.org");
		wTemplate.bindString("euresist_server", new WLink("http://engine.euresist.org").getUrl());
		wTemplate.bindString("server", new WLink(getServletContext().getContextPath().concat("/style/euresist")).getUrl());
		return wTemplate;
	}

	public void clicked(WMouseEvent wMouseEvent, Node node, WCircleNode wCircleNode) {
		graphWidget.setCluster(node.getId(), treeLevel, -1);
		mouseWentOut(null, wCircleNode);
		
		this.setGoogleChart(wCircleNode.getNode().getId());
		this.setStatisticGraph(wCircleNode.getNode().getId(), true);
	}
	
	
	public void mouseWentOver(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		this.setGoogleChart(wCircleNode.getNode().getId());
		this.updateStatisticGraph(wCircleNode.getNode().getId(), wCircleNode.getColor());
	}

	public void mouseWentOut(WMouseEvent wMouseEvent, WCircleNode wCircleNode) {
		int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
		this.setGoogleChart(id);
		this.setStatisticGraph(id, false);
	}
	
	public void pathChanged() {
		int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));

		/*
		 *  deeper == -1 -> User went deeper in the tree (further away from root)
		 *  deeper == 0  -> No level change (refreshed page? ...)
		 *  deeper == 1  -> User went higher in the tree (more towards root)
		 */
		int deeper = 0;
		// User went up in the tree
		if (id < graphWidget.getPreviousClusterID()) {
			deeper = 1;
		// User stayed at the same level
		} else if(id == graphWidget.getPreviousClusterID()) {
			deeper = 0;
		// User went down in the tree
		} else {
			deeper = -1;
		}
		graphWidget.setCluster(id, treeLevel, deeper);
		setGoogleChart(id);
		setStatisticGraph(id, true);
	}
	
	private void setGoogleChart(int nodeId) {
		HashMap<String, Integer> countries = facadeRequestData.readCsv(nodeId, Settings.getInstance().getVisualizeGeography());
		this.googleChartWidget.setCountries(countries);
		this.googleChartWidget.setRegion("");
		this.googleChartWidget.setOptions(wComboBoxRegions.getCurrentText().getValue());
	}
	
	private void setStatisticGraph(int nodeId, boolean reread) {
		if (wComboBoxMetadata != null) {
			if(Settings.getInstance().getCsvColumnRepresentation().containsKey(wComboBoxMetadata.getValueText().toLowerCase())) {
				if(Settings.getInstance().getCsvColumnRepresentation().get(wComboBoxMetadata.getValueText().toLowerCase()).equals("number")) {
					this.chart = wHistogramMine;
					this.hideHistogramElements();
				} else {
					this.chart = wBarChartMine;
					this.showHistogramElements();
				}
				
				this.chart.setSecondBarColor(WColor.white);
				if (reread) {
					this.chart.setData(facadeRequestData.readCsv(nodeId, wComboBoxMetadata.getValueText()), reread);
				} else {
					this.chart.setData(null, reread);
				}
			} else {
				System.err.println("Please update your global-conf.xml file. <property name=\"csvFieldRepresentation\"> not found");
			}
		}
	}
	
	private void updateStatisticGraph(int nodeId, Color clusterColor) {
		if(wComboBoxMetadata != null)
			chart.updateData(facadeRequestData.readCsv(nodeId, wComboBoxMetadata.getValueText()), new WColor(clusterColor.getRed(), clusterColor.getGreen(), clusterColor.getBlue()));
	}
	
	private final void showDialog() {
	    final WDialog dialog = new WDialog("Visualize Tree", this);
//	    WFigTreeMine wFigTreeMine = new WFigTreeMine(facadeRequestData.getJeblTree(), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())));
	    WImageTreeMine wImageTreeMine = new WImageTreeMine(Settings.getInstance().getTreeviewPath(), UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
//	    dialog.getContents().addWidget(wImageTreeMine.getWidget());
	    WPushButton cancel = new WPushButton("Exit");
	    
	    WPushButton buttonCallDialog = new WPushButton("Export", dialog.getContents());

	    dialog.getContents().addWidget(wImageTreeMine.getWidget());
	    WTreeDownloaderForm wTreeDownloader = new WTreeDownloaderForm(dialog, buttonCallDialog, cancel);
	    dialog.getContents().addWidget(wTreeDownloader.getWidget());
	    
	    dialog.rejectWhenEscapePressed();
	    dialog.setClosable(true);
	    
	    buttonCallDialog.clicked().addListener(dialog, 
	    		new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent e1) {
            	showExportTreeDialog(dialog);
            }
        });
	    
	    cancel.clicked().addListener(dialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                    dialog.reject();
	                }
	            });
	    dialog.show();
	}
	
	private void showExportTreeDialog(final WDialog parent) {
		final WDialog dialog = new WDialog("Export Tree", parent);
		
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		ColorClusters.prepareFullTreeView(null, Settings.getInstance().getPhyloTree(), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())), GraphicFormat.PDF, byteArrayOutputStream, 2, true, true);
//		WDownloadResource wDownloadResource = new WDownloadResource(parent, "cluster_1", GraphicFormat.PDF, byteArrayOutputStream);
//		WFileResource wFileResource = new WFileResource("image/pdf", fileName);
//		System.out.println(wFileResource.generateUrl());
		
//		WAnchor wAnchor = new WAnchor(wDownloadResource, "Download File");
//		dialog.getContents().addWidget(wAnchor);
		
		final WPushButton button = new WPushButton("Export");
		WPushButton cancel = new WPushButton("Cancel");
		cancel.clicked().addListener(dialog,
				new Signal1.Listener<WMouseEvent>() {
			public void trigger(WMouseEvent e1) {
				dialog.reject();
			}
		});
		final WExportTreeForm wExportTreeForm = new WExportTreeForm(dialog, button, cancel);
		dialog.rejectWhenEscapePressed();
		dialog.getContents().addWidget(wExportTreeForm.getWidget());
		
		dialog.show();
//		final WDownloadResource wDownloadResource = new WDownloadResource(dialog.getContents(), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())), );
//		wDownloadResource.setDispositionType(DispositionType.Attachment);
		
//		button.setLink(new WLink(wDownloadResource));
		final GraphWebApplication graphWebApplication = this;
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		button.clicked().addListener(dialog, new Signal1.Listener<WMouseEvent>() {
			public void trigger(WMouseEvent e1) {
				byteArrayOutputStream.reset();
				button.disable();
				
				final WConfirmationDialog wConfirmationDialog = new WConfirmationDialog("Confirmation", "Your file is being prepared for download. Your download link will appear shortly. Please be patient.");
				wConfirmationDialog.getOkButton().clicked().addListener(parent, new Signal1.Listener<WMouseEvent>() {
					public void trigger(WMouseEvent e1) {
						button.enable();
						wConfirmationDialog.reject();
					}
				});
				wConfirmationDialog.show();
				
				graphWebApplication.enableUpdates(true);
				Main.getTreeExportJobScheduler().addExportTreeJob(graphWebApplication, wConfirmationDialog, dialog.getContents(), "cluster_" + UrlManipulator.getId(WApplication.getInstance().getInternalPath()), facadeRequestData.getCluster(UrlManipulator.getId(WApplication.getInstance().getInternalPath())), wExportTreeForm.getwMyComboBoxExportFormat().getGraphicFormat(), byteArrayOutputStream, 2, wExportTreeForm.getwMyButtonGroupColorTree().isTreeColored(), wExportTreeForm.getwMyButtonGroupNodeTips().getShowTips());
			}
		});
	}
	
	private final void showPPlacer() {
		final WDialog dialog = new WDialog("Phylogenetic placement of your sequence");
	    final PPlacerForm PPlacerForm = new PPlacerForm(dialog);
		
//		WGroupBox wGroupBox = new WGroupBox();
//		wGroupBox.setStyleClass("nostyle");
//		WHBoxLayout whBoxLayout = new WHBoxLayout();
		
	    WPushButton cancel = new WPushButton("Exit");
	    dialog.rejectWhenEscapePressed();
	    dialog.setClosable(true);
	    cancel.clicked().addListener(dialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                    dialog.reject();
	                }
	            });
	    dialog.show();
	    WPushButton pplace = new WPushButton("Start");
	    pplace.clicked().addListener(dialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                	if(Settings.getInstance().getPPlacerSupport()) {
	                		if(PPlacerForm.isFormValid()) {
	            				Path path = null;
	            				File pplacerFile = null;
								try {
									path = Files.createTempDirectory("pplacer");
									pplacerFile = new File(path + File.separator + "sequences.fasta");
									FileWriter fileWriter = new FileWriter(pplacerFile);
									System.out.println(PPlacerForm.getFastaSequence());
									fileWriter.write(PPlacerForm.getFastaSequence());
									fileWriter.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
	            				
	                			System.out.println(pplacerFile.getAbsolutePath());
	                			Main.getJobScheduler().addPPlacerJob(path.toString(), Settings.getInstance().getScriptFolder(), Settings.getInstance().getPhyloTree(), Settings.getInstance().getAlignmentLocation(),
	                					pplacerFile.getAbsolutePath(), Settings.getInstance().getLogfileLocation(), PPlacerForm.getEmail());
	                			dialog.reject();
	                		}
	                	} else {
	                		System.err.println("PPlacer not supported. If you want to support PPlacer, please toggle PPlacer support on in your global-conf.xml file");
	                	}
	                }
	            });

	    PPlacerForm.addStartButton(pplace);
	    PPlacerForm.addCancelButton(cancel);
	    dialog.getContents().addWidget(PPlacerForm.getWidget());
	    
//	    whBoxLayout.addWidget(pplace);
//	    whBoxLayout.addWidget(cancel);
//		wGroupBox.setLayout(whBoxLayout);
//		dialog.getContents().addWidget(wGroupBox);
	}
	
	private final void showDialogStatistics() {
	    final WDialog dialog = new WDialog("Visualize Statistics");
	    new SDRVisualizer(dialog, UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
	}
	
	private final void showISO3166Warning() {
		final WDialog wDialog = new WDialog("Warning");
		wDialog.setWidth(new WLength(500));
		wDialog.setClosable(true);
		WImage wImage = new WImage(new WLink(getServletContext().getContextPath().concat("/images/warning.png")));
		wImage.setWidth(new WLength(50));
		wImage.setHeight(new WLength(50));
		
		WText wText = new WText("The countries provided under '" + Settings.getInstance().getVisualizeGeography() + "'<br /> are not (all) formatted in the ISO 3166-1 alpha-2 standard.");
		wText.setMargin(8, Side.Top);
		
		WPushButton exit = new WPushButton("Exit");
		exit.clicked().addListener(wDialog,
	            new Signal1.Listener<WMouseEvent>() {
	                public void trigger(WMouseEvent e1) {
	                    wDialog.reject();
	                }
	            });
		exit.setMaximumSize(new WLength(50), new WLength(30));
		// TODO: Improve this part to put the Exit button in the middle by not using margin
		exit.setMargin(200, Side.Left);
		
		WHBoxLayout whBoxLayout = new WHBoxLayout();
		whBoxLayout.addWidget(wImage);
		whBoxLayout.addWidget(wText);
		
		WVBoxLayout wvBoxLayout = new WVBoxLayout();
		wvBoxLayout.addItem(whBoxLayout);
		wvBoxLayout.addWidget(exit);
		
		wDialog.getContents().setLayout(wvBoxLayout);
		wDialog.rejectWhenEscapePressed();
		wDialog.show();
	}
	
	private void setCSS() {
//		this.getStyleSheet().addRule(new WCssTextRule("body", "background-color: #BEBEBE"));
//		this.getStyleSheet().addRule(new WCssTextRule("button", "font-size: 14px;"));
//		this.getStyleSheet().addRule(new WCssTextRule(".label", "font-size: 18px;background: #FFFFFF;margin-left: 0px; margin-top: 0px;border: 0px;"));
//		this.getStyleSheet().addRule(new WCssTextRule(".card", "background: #FFFFFF;margin-left: 0px; margin-top: 0px; border: 0px"));
//		this.getStyleSheet().addRule(new WCssTextRule(".nostyle", "margin-left: 0px; margin-top: 0px; border: 0px"));
//		this.getStyleSheet().addRule(new WCssTextRule(".tableDialog", "margin-top: -15px; margin-left: -15px; border-spacing: 10px 10px; border-collapse: separate"));
		this.useStyleSheet(new WLink(getServletContext().getContextPath().concat("/style/stylesheet.css")));
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
		// Added for design
		wvBoxLayout.setSpacing(2);
		wComboBoxRegions = new WComboBoxRegions();
		wComboBoxRegions.setMargin(7, Side.Right);
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
		wgroupboxRegion.addWidget(new WText("Visualized attribute: " + Settings.getInstance().getVisualizeGeography()));
		
		googleChartWidget = new GoogleChartWidget(countries, region, Settings.getInstance().getDatalessRegionColor(), Settings.getInstance().getBackgroundcolor(), Settings.getInstance().getColorAxis());
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
		
		WCartesianChart wCartesianChart = new WCartesianChart();
		wBarChartMine = new WBarChartMine(wCartesianChart);
		wHistogramMine = new WHistogramMine(wCartesianChart);
		
		if(metaDataFile != null && metaDataFile.exists()) {
			wComboBoxMetadata = new MyComboBox(metaDataFile, csvDelimitor);
			wComboBoxMetadata.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
					setStatisticGraph(id, true);
				}
			});
			wComboBoxMetadata.changed().trigger();
			wComboBoxMetadata.setMaximumSize(new WLength(25.0, Unit.Percentage), new WLength(25));
			WGroupBox wgroupboxMetadata = new WGroupBox();
			wgroupboxMetadata.setStyleClass("label");
			WText metadataLabel = new WText("Select attribute: ");
			wgroupboxMetadata.addWidget(metadataLabel);
			wgroupboxMetadata.setMaximumSize(new WLength(100.0, Unit.Percentage), new WLength(25));
			wgroupboxMetadata.addWidget(wComboBoxMetadata);
			
			orderLabel.setMargin(20, Side.Left);
			orderLabel.setMaximumSize(new WLength(25.0, Unit.Percentage), new WLength(25));
			
			for(SortingOptions sortingOption:SortingOptions.values()) {
				order.addItem(sortingOption.getDescription());
			}
			order.setValueText(SortingOptions.FREQUENCY_DESCENDING.getDescription());
			order.setMaximumSize(new WLength(25.0, Unit.Percentage), new WLength(25));
			order.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					// We should only be able to sort the barchart, not the histogram
					if(chart instanceof WBarChartMine) {
						((WBarChartMine)chart).setSortingOption(SortingOptions.getOptionByDescription(order.getValueText()));
						int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
						setStatisticGraph(id, true);
					} else {
						System.err.println("Trying to cast a chart to WBarChart when it isn't");
					}
				}
			});
			
			wgroupboxMetadata.addWidget(orderLabel);
			wgroupboxMetadata.addWidget(order);
			
			showNALabel.setMargin(20, Side.Left);
//			showNALabel.setMaximumSize(new WLength(25.0, Unit.Percentage), new WLength(25));
			
			showNACheckbox.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					facadeRequestData.setShowNAData(showNACheckbox.isChecked());
					int id = Integer.parseInt(UrlManipulator.getId(WApplication.getInstance().getInternalPath()));
					setStatisticGraph(id, true);
				}
			});
			
			wgroupboxMetadata.addWidget(showNALabel);
			wgroupboxMetadata.addWidget(showNACheckbox);
			
			wvBoxLayoutChart.addWidget(wgroupboxMetadata);
//			wvBoxLayoutChart.setStretchFactor(wgroupboxMetadata, 0);
		}
		
		wvBoxLayoutChart.addWidget(wCartesianChart);
//		wvBoxLayoutChart.setStretchFactor(wCartesianChart, 1);
		return wvBoxLayout;
	}
	
	private void hideHistogramElements() {
		if(orderLabel != null)
			orderLabel.hide();
		if(order != null)
			order.hide();
		if(showNALabel != null)
			showNALabel.hide();
		if(showNACheckbox != null)
			showNACheckbox.hide();
	}
	
	private void showHistogramElements() {
		if(orderLabel != null)
			orderLabel.show();
		if(order != null)
			order.show();
		if(showNALabel != null)
			showNALabel.show();
		if(showNACheckbox != null)
			showNACheckbox.show();
	}
	
	public PPlacer getPPlacer() {
		return this.pplacer;
	}
	
	public void treeExportFinished(final WObject parent, final WConfirmationDialog wConfirmationDialog, ByteArrayOutputStream byteArrayOutputStream, String fileName, GraphicFormat graphicFormat) {
		UpdateLock updateLock = this.getUpdateLock();
		WDownloadResource wDownloadResource = new WDownloadResource(parent, fileName, graphicFormat, byteArrayOutputStream);
		WAnchor wAnchor = new WAnchor(wDownloadResource, "Download File");
		wAnchor.setTarget(AnchorTarget.TargetDownload);
		
		wAnchor.clicked().addListener(wConfirmationDialog,
	            new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent e1) {
            	wConfirmationDialog.reject();
            	// TODO: Improve this code. Maybe it'll be easier to just pass the correct button here or use observer pattern.
            	WTableCell cell = ((WTable)((WContainerWidget)parent).getChildren().get(0)).getElementAt(4, 3);
            	for(WWidget wwWidget:cell.getChildren()) {
            		if(wwWidget instanceof WPushButton && ((WPushButton)wwWidget).isDisabled()) {
            			((WPushButton)wwWidget).enable();
            		}
            	}
            }
        });
		
		wConfirmationDialog.addWidget(wAnchor);
		wConfirmationDialog.getOkButton().show();
		this.triggerUpdate();
		this.enableUpdates(false);
		updateLock.release();
	}
}
