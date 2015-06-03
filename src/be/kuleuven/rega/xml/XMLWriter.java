package be.kuleuven.rega.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

import com.opencsv.CSVReader;

public class XMLWriter {
	
	private String folderLocation = "/Users/ewout/Documents/phylogeo/EUResist_New/xml";
	
	public XMLWriter(int clusterId, List<String> ids) {
		CSVReader csvReader;
		try {
			csvReader = new CSVReader(new FileReader(new File(folderLocation)), ';');
			String[] header = csvReader.readNext();
			FileWriter fileWriter = new FileWriter(new File(folderLocation + clusterId + ".xml"));
			fileWriter.write("<xml>" + "\n");
			
			for(String key:header) {
				fileWriter.write("<" + key + ">" + "\n");
				HashMap<String,Integer> tempHashMap = CsvUtils.csvToHashMapStringInteger(new File(folderLocation), ';', ids, key);
				for(String hashMapKey:tempHashMap.keySet()) {
					fileWriter.write("<" + key + " value=\"" + tempHashMap.get(key) + "\"/>" + "\n");
				}
				fileWriter.write("<" + key + "/>" + "\n");
			}
			fileWriter.write("</xml>");
		} catch (FileNotFoundException e) {
			System.err.println(XMLWriter.class + ": " + "File could not be found.");
		} catch (IOException e) {
			System.err.println(XMLWriter.class + ": " + "IOException in the CSVReader.");
		}
	}
}
