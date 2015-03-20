package be.kuleuven.rega.webapp;

import jam.controlpalettes.BasicControlPalette;
import jam.controlpalettes.ControlPalette;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.Tree;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WWidget;
import figtree.application.FigTreeNexusImporter;
import figtree.application.FigTreePanel;
import figtree.panel.SimpleLabelPainter;
import figtree.treeviewer.ExtendedTreeViewer;
import figtree.treeviewer.TreePane;
import figtree.treeviewer.treelayouts.RadialTreeLayout;

public class WImageTreeMine<V,E> {
	
	private WImage wImage = null;
	public final static int CONTROL_PALETTE_WIDTH = 200;
	
	public WImageTreeMine(GraphWidget<V,E> graphWidget) {
		try {
			graphWidget.writeTree();
	        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.nxs")));
	        String line = bufferedReader.readLine();
	        while (line != null && line.length() == 0) {
	            line = bufferedReader.readLine();
	        }

	        bufferedReader.close();

	        boolean isNexus = (line != null && line.toUpperCase().contains("#NEXUS"));

	        Reader reader = new FileReader(new File("/Users/ewout/git/phylogeotool/lib/EwoutTrees/test.nxs"));

	        Map<String, Object> settings = new HashMap<String, Object>();

	        ExtendedTreeViewer treeViewer = new ExtendedTreeViewer();
	        ControlPalette controlPalette = new BasicControlPalette(CONTROL_PALETTE_WIDTH, BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
	        FigTreePanel figTreePanel = new FigTreePanel(null, treeViewer, controlPalette);

	        // First of all, fully populate the settings map so that
	        // all the settings have defaults
	        controlPalette.getSettings(settings);

	        List<Tree> trees = new ArrayList<Tree>();

	        if (isNexus) {
	            FigTreeNexusImporter importer = new FigTreeNexusImporter(reader);
	            trees.add(importer.importNextTree());

	            // Try to find a figtree block and if found, parse the settings
	            while (true) {
	                try {
	                    importer.findNextBlock();
	                    if (importer.getNextBlockName().equalsIgnoreCase("FIGTREE")) {
	                        importer.parseFigTreeBlock(settings);
	                    }
	                } catch (EOFException ex) {
	                    break;
	                }
	            }
	        } else {
	            NewickImporter importer = new NewickImporter(reader, true);
	            trees.add(importer.importNextTree());
	        }

	        if (trees.size() == 0) {
	            throw new ImportException("This file contained no trees.");
	        }

	        treeViewer.setTrees(trees);
	        controlPalette.setSettings(settings);
	        
	        int width = 500;
	        int height = 500;
	        treeViewer.getContentPane().setSize(width, height);

	        JComponent comp = treeViewer.getContentPane();
	        ((TreePane)comp).setTreeLayout(new RadialTreeLayout());
	        SimpleLabelPainter simpleLabelPainter = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
	        simpleLabelPainter.setVisible(false);
	        ((TreePane)comp).setTipLabelPainter(simpleLabelPainter);
	        
	        ((TreePane)comp).setBranchStroke(new BasicStroke(2.0f));
	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics g = bi.createGraphics();
	        comp.paint(g);
	        g.dispose();
	        
	        WMemoryResource memoryResource = new WMemoryResource("PNG");
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(bi, "PNG", baos);
	        memoryResource.setData(baos.toByteArray());
	        wImage = new WImage(new WLink(memoryResource));
	        wImage.resize(width, height);
	    } catch(ImportException ie) {
	        throw new RuntimeException("Error writing graphic file: " + ie);
	    } catch(IOException ioe) {
	        throw new RuntimeException("Error writing graphic file: " + ioe);
	    }
	}
	
	public WWidget getWidget() {
		return wImage;
	}
}
