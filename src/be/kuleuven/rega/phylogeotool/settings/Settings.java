package be.kuleuven.rega.phylogeotool.settings;

/*
 * Copyright (C) 2008 Rega Institute for Medical Research, KULeuven
 * 
 * See the LICENSE file for terms of use.
 */
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.Main;

/**
 * Singleton class which contains the application settings, parses them from the xml configuration file.
 * 
 * @author Ewout Vanden Eynden
 *
 */
public class Settings {
//	public final static String defaultStyleSheet = "../style/genotype.css";
	
	public Settings(File f) {
		System.err.println("Loading config file: " + f.getAbsolutePath());
		if (!f.exists())
			throw new RuntimeException("Config file could not be found!");
		parseConfFile(f);
	}
	
	public String getBasePath() {
		return basePath;
	}
	
	public static String getXmlPath(String basePath) {
		return basePath + File.separator + "xml";
	}
	
	public String getXmlPath() {
		return basePath + File.separator + "xml";
	}

	public static String getClusterPath(String basePath) {
		return basePath + File.separator + "clusters";
	}
	
	public String getClusterPath() {
		return basePath + File.separator + "clusters";
	}

	public static String getTreeviewPath(String basePath) {
		return basePath + File.separator + "treeview";
	}
	
	public String getTreeviewPath() {
		return basePath + File.separator + "treeview";
	}
	
	public String getMetaDataFile() {
		return metaDataFile;
	}
	
	/**
	 * Location of the phylogenetic tree used in this tool
	 * @return Location of the phylogenetic tree
	 */
	public String getPhyloTree() {
		return phyloTreeFile;
	}
	
	/**
	 * Location of the alignment file that was used to build the phylogenetic tree used in this tool
	 * @return Location of the alignment file
	 */
	public String getAlignmentLocation() {
		return alignmentLocation;
	}
	
	/**
	 * Location of the logfile file that was returned by the tree building program to build the phylogenetic tree used in this tool
	 * @return Location of the log file
	 */
	public String getLogfileLocation() {
		return logfileLocation;
	}
	
	/**
	 * Location of the shell scripts that are used to to initiate the PPlacer
	 * This folder contains init.sh, place.sh
	 * @return Location of the shell scripts folder
	 */
	public String getScriptFolder() {
		return scriptFolder;
	}

	public String getDatalessRegionColor() {
		return datalessRegionColor;
	}

	public String getBackgroundcolor() {
		return backgroundcolor;
	}

	public String[] getColorAxis() {
		return colorAxis;
	}
	
	public boolean getShowNAData() {
		return showNAData;
	}
	
	public boolean getPPlacerSupport() {
		return pplacerSupport;
	}
	
	public String getVisualizeGeography() {
		return visualizeGeography;
	}
	
	public String getRBinary() {
		return rBinary;
	}
	
	public List<String> getColumnsToExport() {
		return this.columnsToExport;
	}
	
	public Map<String, String> getCsvColumnRepresentation() {
		return this.csvColumnRepresentation;
	}
	
	private String basePath;
	private String metaDataFile;
	private String phyloTreeFile;
	private String alignmentLocation;
	private String logfileLocation;
	private String scriptFolder;
	private String rBinary;
	private boolean showNAData;
	private String visualizeGeography;
	private String datalessRegionColor;
	private String backgroundcolor;
	private String[] colorAxis;
	private boolean pplacerSupport;
	private List<String> columnsToExport;
	private Map<String, String> csvColumnRepresentation;
	
    @SuppressWarnings("unchecked")
	private void parseConfFile(File confFile) {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(confFile);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element root = doc.getRootElement();

        List<Element> children = root.getChildren("property");
        String name;
        for (Element e : children) {
            name = e.getAttributeValue("name");
            if (name.equals("basePath")) {
            	basePath = e.getValue().trim();
            } else if(name.equals("metadataFile")) {
            	metaDataFile = e.getValue().trim();
            } else if(name.equals("phyloTreeFile")) {
            	phyloTreeFile = e.getValue().trim();
            } else if(name.equals("alignmentFile")) {
            	alignmentLocation = e.getValue().trim();
            } else if(name.equals("logFile")) {
            	logfileLocation = e.getValue().trim();
            } else if(name.equals("scriptFolder")) {
            	scriptFolder = e.getValue().trim();
            } else if(name.equals("rBinary")) {
            	rBinary = e.getValue().trim();
            } else if(name.equals("visualizeGeography")) {
            	visualizeGeography = e.getValue().trim();
            } else if(name.equals("showNAData")) {
            	showNAData = Boolean.parseBoolean(e.getValue().trim());
            } else if(name.equals("pplacer_support")) {
            	pplacerSupport = Boolean.parseBoolean(e.getValue().trim());
            } else if(name.equals("colorCodes")) {
            	List<Element> colorProperties = e.getChildren();
            	String colorPropertyName;
            	for (Element child : colorProperties) {
            		colorPropertyName = child.getAttributeValue("name");
            		if(colorPropertyName.equals("datalessregion")) {
            			datalessRegionColor = child.getValue().trim();
            		} else if(colorPropertyName.equals("backgroundcolor")) { 
            			backgroundcolor = child.getValue().trim();
            		} else if(colorPropertyName.equals("colorAxis")) { 
            			String temp = child.getValue().trim();
            			colorAxis = temp.split(",");
            		}
            	}
            } else if(name.equals("exportFields")) {
            	columnsToExport = new ArrayList<String>();
            	List<Element> headersToExport = e.getChildren();
            	for (Element child : headersToExport) {
            		columnsToExport.add(child.getValue().trim());
            	}
            } else if(name.equals("csvFieldRepresentation")) {
            	csvColumnRepresentation = new HashMap<String,String>();
            	List<Element> headersToExport = e.getChildren();
            	for (Element child : headersToExport) {
//            		System.out.println("Adding: " + child.getValue().trim() + ", " + child.getAttribute("type").getValue().trim());
            		csvColumnRepresentation.put(child.getValue().toLowerCase().trim(), child.getAttribute("type").getValue().trim());
            	}
            }
        }
    }
    
//	public static void initSettings(Settings s) {
//		PhyloClusterAnalysis.paupCommand = s.getPaupCmd();
//		SequenceAlign.clustalWPath = s.getClustalWCmd();
//		GenotypeTool.setXmlBasePath(s.getXmlPath().getAbsolutePath() + File.separatorChar);
//		BlastAnalysis.blastPath = s.getBlastPath().getAbsolutePath() + File.separatorChar;
//		PhyloClusterAnalysis.puzzleCommand = s.getTreePuzzleCmd();
//		treeGraphCommand = s.getTreeGraphCmd();
//	}

	public static Settings getInstance() {
		GraphWebApplication app = Main.getApp();
		if (app == null)
			return getInstance(null);
		else
			return app.getSettings();
	}
	
	public static Settings getInstanceByPath(String configLocation) {
		return new Settings(new File(configLocation + File.separatorChar + "global-conf.xml"));
	}

	public static Settings getInstance(ServletConfig config) {
        String configFile = null;
        
        if (config != null) {
        	configFile = config.getInitParameter("configFile");
        	if (configFile != null)
        		return new Settings(new File(configFile));
        } 
        
//        if (configFile == null) {
//            System.err.println("REGA_GENOTYPE_CONF_DIR"+":"+System.getenv("REGA_GENOTYPE_CONF_DIR"));
//        	configFile = System.getenv("REGA_GENOTYPE_CONF_DIR");
//        }
        
        if (configFile == null) {
            String osName = System.getProperty("os.name");
            osName = osName.toLowerCase();
            if (osName.startsWith("windows"))
                configFile = "C:\\Program files\\phylogeotool\\";
            else
                configFile = "/etc/phylogeotool/";
        }

       	configFile += File.separatorChar + "global-conf.xml";
        
        return new Settings(new File(configFile));
	}
}