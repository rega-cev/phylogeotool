package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletContext;

import be.kuleuven.rega.phylogeotool.settings.Settings;

public class GeneratePPlacerScript {
	
	/**
	 * Check if the folder already has a script called place.sh
	 * @return 	true: Folder contains script
	 * 			false: Folder doesn't contain script
	 */
	private static boolean folderContainsScript() {
		return new File(Settings.getInstance().getScriptFolder(), "place.sh").exists();
	}
	
	/**
	 * Method to check if the global-conf.xml file contains the required links
	 * @return 	true: The global-conf.xml file contains all the required links
	 * 			false: The global-conf.xml file doesn't contain all the required links
	 */
	private static boolean isConfigFileComplete() {
		boolean configIsComplete = true;
		if(Settings.getInstance().getMafftBinary() == null || Settings.getInstance().getMafftBinary().equals("")) {
			configIsComplete = false;
		}
		
		if(Settings.getInstance().getTaxitBinary() == null || Settings.getInstance().getTaxitBinary().equals("")) {
			configIsComplete = false;
		}
		
		if(Settings.getInstance().getPPlacerBinary() == null || Settings.getInstance().getPPlacerBinary().equals("")) {
			configIsComplete = false;
		}
		
		if(Settings.getInstance().getGuppyBinary() == null || Settings.getInstance().getGuppyBinary().equals("")) {
			configIsComplete = false;
		}
		
		if(Settings.getInstance().getScriptFolder() == null || Settings.getInstance().getScriptFolder().equals("")) {
			configIsComplete = false;
		}
		return configIsComplete;
	}
	
	public void generateScript(ServletContext servletContext) {
		if(!folderContainsScript()) {
			if(isConfigFileComplete()) {
				URL url = getClass().getResource("PPlacerTemplate.txt");
				File file = new File(url.getPath());
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					byte[] data = new byte[(int) file.length()];
					fis.read(data);
					fis.close();
					String fileContent = new String(data, "UTF-8");
					fileContent = fileContent.replace("${mafftBinary}", Settings.getInstance().getMafftBinary());
					fileContent = fileContent.replace("${taxitBinary}", Settings.getInstance().getTaxitBinary());
					fileContent = fileContent.replace("${pplacerBinary}", Settings.getInstance().getPPlacerBinary());
					fileContent = fileContent.replace("${guppyBinary}", Settings.getInstance().getGuppyBinary());
					FileOutputStream fos = new FileOutputStream(new File(Settings.getInstance().getScriptFolder() + File.separator + "place.sh"));
					fos.write(fileContent.getBytes());
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("Your global-conf.xml is missing some required parameters. Please check the PhyloGeoTool install manual.");
			}
		}
	}
}
