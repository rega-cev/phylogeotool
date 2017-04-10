package be.kuleuven.rega.phylogeotool.settings;

/*
 * Copyright (C) 2008 Rega Institute for Medical Research, KULeuven
 * 
 * See the LICENSE file for terms of use.
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Singleton class which contains the application settings, parses them from the xml configuration file.
 * 
 * @author Ewout Vanden Eynden
 *
 */
public class Settings {
//	public final static String defaultStyleSheet = "../style/genotype.css";
	
	private static Settings settingsInstance = null;
	
	protected Settings(File f) {
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
	
	public static String getStatisticalImagesPath(String basePath) {
		return basePath + File.separator + "r";
	}
	
	public String getStatisticalImagesPath() {
		return basePath + File.separator + "r";
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
	
	public boolean getStastisticsSupport() {
		return statisticsSupport;
	}
	
	public String getFull_url() {
		return full_url;
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
	private boolean statisticsSupport;
	private String full_url;
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
            } else if(name.equals("statistics_support")) {
            	statisticsSupport = Boolean.parseBoolean(e.getValue().trim());
            } else if(name.equals("full_url")) {
            	full_url = e.getValue().trim();
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
		if(settingsInstance != null)
			return settingsInstance;
		else {
			return getInstance(null);
		}
	}
	
//	public static Settings getInstanceByPath(String configLocation) {
//		return new Settings(new File(configLocation + File.separatorChar + "global-conf.xml"));
//	}

	public static Settings getInstance(ServletContext servletContext) {
		String configFile = null;
		String confDir = null;
        if(settingsInstance == null) {
			confDir = servletContext.getInitParameter("conf-dir");
			if(confDir != null & confDir != "") {
				configFile = confDir + File.separatorChar + "global-conf.xml";
	        	settingsInstance = new Settings(new File(configFile));
	        	return settingsInstance;
	        } 
	        
	//        if (configFile == null) {
	//            System.err.println("REGA_GENOTYPE_CONF_DIR"+":"+System.getenv("REGA_GENOTYPE_CONF_DIR"));
	//        	configFile = System.getenv("REGA_GENOTYPE_CONF_DIR");
	//        }
	        
	        if (confDir == null) {
	            String osName = System.getProperty("os.name");
	            osName = osName.toLowerCase();
	            if (osName.startsWith("windows"))
	            	confDir = "C:\\Program files\\phylogeotool\\";
	            else
	            	confDir = "/etc/phylogeotool/";
	        }
	
	       	configFile = confDir + File.separatorChar + "global-conf.xml";
	        
	        settingsInstance = new Settings(new File(configFile));
        }
        return settingsInstance;
	}
}