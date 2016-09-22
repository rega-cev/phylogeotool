package be.kuleuven.rega.phylogeotool.tools;

import jam.controlpalettes.BasicControlPalette;
import jam.controlpalettes.ControlPalette;

import java.awt.BasicStroke;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import jebl.evolution.trees.Tree;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.io.write.NexusExporter;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.prerendering.PreRendering;

import com.itextpdf.text.DocumentException;

import figtree.application.FigTreeApplication;
import figtree.application.FigTreeFrame;
import figtree.application.FigTreePanel;
import figtree.application.GraphicFormat;
import figtree.panel.SimpleLabelPainter;
import figtree.treeviewer.ExtendedTreeViewer;
import figtree.treeviewer.TreePane;
import figtree.treeviewer.decorators.AttributableDecorator;
import figtree.treeviewer.treelayouts.RadialTreeLayout;

public class ColorClusters {

	public static void main(String[] args) {

		String treeLocation = null;
		String basePath = null;
		int minimumClusterSize = 2;

		if (args.length > 2) {
			treeLocation = args[0];
			basePath = args[1];
			
			ReadTree.setJeblTree(treeLocation);
			ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
		} else {
			System.err.println("java -jar ColorClusters.jar phylo.tree basePath");
			System.exit(0);
		}

		ColorClusters colorClusters = new ColorClusters();
		// colorClusters.colorCluster(treeLocation, clusterLocation,
		// treeviewLocation, minimumClusterSize);
		colorClusters.colorClusterExportPNG(treeLocation, basePath, minimumClusterSize);
		// colorClusters.colorClusterExportGIF(treeLocation, treeviewLocation);
	}

	private void colorClusterExportNexus(String treeLocation, String basePath, int minimumClusterSize) {
		try {
			Tree jeblTree = ReadTree.readTree(new FileReader(treeLocation));
			PreRendering preRendering = new PreRendering(basePath);
			File clusters = new File(Settings.getClusterPath(basePath));
			File[] files = clusters.listFiles();
			Cluster currentCluster = null;
			for (int i = 0; i <= 1; i++) {
				if (files[i].getName().contains(".xml")) {
					currentCluster = preRendering.getClusterFromXML(files[i].getName().split(".")[0]);
					NexusExporter.export(currentCluster, jeblTree, new FileWriter(new File(basePath + File.separator + "treeview" + File.separator + files[i].getName().split(".")[0]
							+ ".nexus")), minimumClusterSize, true);
				} else {
					System.err.println(files[i].getName() + " is not a nexus file.");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void colorClusterExportPNG(String treeLocation, String configPath, int minimumClusterSize) {
		PreRendering preRendering = new PreRendering(configPath);
		File clusters = new File(Settings.getClusterPath(configPath));
		File[] files = clusters.listFiles();
		Cluster currentCluster = null;

		// Generate Default Drawing
		currentCluster = preRendering.getClusterFromXML("3363");
		try {
			prepareFullTreeView(treeLocation, currentCluster, GraphicFormat.PDF, new FileOutputStream(new File(Settings.getTreeviewPath(configPath) + File.separator + "default")),
					minimumClusterSize, false, false);

//			for (int i = 0; i < files.length; i++) {
//				if (files[i].getName().contains(".xml")) {
//					System.err.println("File: " + files[i].getName());
//					currentCluster = preRendering.getClusterFromXML(files[i].getName().split("\\.")[0]);
//					if (currentCluster.getTree().getAllParents(currentCluster.getRoot()).size() < 20) {
//						prepareFullTreeView(treeLocation, currentCluster, GraphicFormat.PNG, new FileOutputStream(new File(treeviewLocation + File.separator
//								+ files[i].getName().split("\\.")[0] + ".png")), minimumClusterSize, true, false);
			prepareFullTreeView(treeLocation, currentCluster, GraphicFormat.PDF, new FileOutputStream(new File(Settings.getTreeviewPath(configPath) + File.separator
					+ "3363.pdf")), minimumClusterSize, true, false);
//					} else {
//						System.err.println("Cluster too far away from root to color");
//					}
//				} else {
//					System.err.println(files[i].getName() + " is not a nexus file.");
//				}
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void colorClusterExportGIF(String treeLocation, String basePath) {
		// jeblTree = ReadTree.readTree(new FileReader(treeLocation));
		File nexusTrees = new File(Settings.getTreeviewPath(basePath));
		File[] files = nexusTrees.listFiles();
		for (int i = 0; i <= 1; i++) {
			if (files[i].getName().contains(".nexus")) {
				String args[] = new String[8];
				args[0] = "-graphic";
				args[1] = "SVG";
				args[2] = "-width";
				args[3] = "320";
				args[4] = "-height";
				args[5] = "320";
				args[6] = treeLocation;
				// System.out.println(files[i].getName().split("\\.")[0]);
				args[7] = Settings.getTreeviewPath(basePath) + File.separator + files[i].getName().split("\\.")[0] + ".svg";
				FigTreeApplication.main(args);
			} else {
				System.err.println(files[i].getName() + " is not a nexus file.");
			}
		}
	}

	public static void prepareFullTreeView(String treeLocation, Cluster cluster, GraphicFormat graphicsFormat, OutputStream output, int minimumClusterSize, boolean colorTree, boolean showTips) {
		int CONTROL_PALETTE_WIDTH = 200;
		Tree tree;
		// We read a new instance of jebltree to prevent overlapping coloring events to occur
		try {
			tree = ReadTree.readTree(new FileReader(new File(treeLocation)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			tree = null;
		}
		try {
			if (graphicsFormat == null) {
				NexusExporter.export(cluster, tree, new OutputStreamWriter(output), minimumClusterSize, colorTree);
			} else {
				Tree coloredTree = NexusExporter.export(cluster, tree, null, minimumClusterSize, colorTree);

				Map<String, Object> settings = new HashMap<String, Object>();

				ExtendedTreeViewer treeViewer = new ExtendedTreeViewer();
				ControlPalette controlPalette = new BasicControlPalette(CONTROL_PALETTE_WIDTH, BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
				// This is important for the coloring.
				new FigTreePanel(null, treeViewer, controlPalette);
				// First of all, fully populate the settings map so that
				// all the settings have defaults
				controlPalette.getSettings(settings);

				List<jebl.evolution.trees.Tree> trees = new ArrayList<jebl.evolution.trees.Tree>();
				trees.add(coloredTree);
				treeViewer.setTrees(trees);
				controlPalette.setSettings(settings);

				int width = 650;
				int height = 650;
				treeViewer.getContentPane().setSize(width, height);

				JComponent comp = treeViewer.getContentPane();
				((TreePane) comp).setTreeLayout(new RadialTreeLayout());
				SimpleLabelPainter simpleLabelPainter = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
				simpleLabelPainter.setVisible(false);
				((TreePane) comp).setTipLabelPainter(simpleLabelPainter);

				((TreePane) comp).setBranchStroke(new BasicStroke(2.0f));

				final AttributableDecorator branchDecorator = new AttributableDecorator();
				branchDecorator.setPaintAttributeName("!color");
				((TreePane) comp).setBranchDecorator(branchDecorator, false);

				if (showTips) {
					SimpleLabelPainter simpleLabelPainterTip = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
					simpleLabelPainterTip.setNumberFormat(new DecimalFormat("#.####"));
					simpleLabelPainterTip.setFont(new Font("sansserif", Font.PLAIN, 8));
					simpleLabelPainterTip.setTextDecorator(branchDecorator);
					((TreePane) comp).setTipLabelPainter(simpleLabelPainterTip);
				}
//			try {
//				be.kuleuven.rega.phylogeotool.io.write.NexusExporter.export(cluster, ReadTree.getJeblTree(), new FileWriter(new File("/Users/ewout/Desktop/test.nexus")), 2, true);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
				
				try {
					FigTreeFrame.exportGraphics(graphicsFormat, ((TreePane) comp), output);
				} catch (DocumentException e) {
					e.printStackTrace();
				}

				// BufferedImage bi = new BufferedImage(width, height,
				// BufferedImage.TYPE_INT_ARGB);
				// Graphics g = bi.createGraphics();
				// comp.paint(g);
				// g.dispose();
				//
				// File file = new File(treeviewLocation + File.separator +
				// fileName + ".png");
				// ImageIO.write(bi, "PNG", file);
			}
		} catch (IOException ioe) {
			throw new RuntimeException("Error writing graphic file: " + ioe);
		}
	}

}
