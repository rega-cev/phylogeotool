package be.kuleuven.rega.phylogeotool.data.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;

public class CsvUtilsMetadata {

	private File csvFile = null;
	private CSVReader csvReader = null;
	private char delimitor;
	
	public CsvUtilsMetadata(File csvFile, char delimitor) throws FileNotFoundException {
		this.csvFile = csvFile;
		this.delimitor = delimitor;
		csvReader = new CSVReader(new FileReader(csvFile), delimitor);
	}
	
	public String[] getHeader() throws IOException {
		csvReader.close();
		csvReader = new CSVReader(new FileReader(csvFile), delimitor);
		return csvReader.readNext();
	}
	
	public String[] getDataFromId(int id) throws IOException {
		csvReader.close();
		csvReader = new CSVReader(new FileReader(csvFile), delimitor, ' ', id);
		return csvReader.readNext();
	}
	
	public static List<String> getDataFromIds(List<String> ids, File csvFile, char delimitor) throws IOException {
		List<String> csvData = new ArrayList<String>();
		CSVReader csvReader = new CSVReader(new FileReader(csvFile), delimitor);
		Iterator<String[]> iterator = csvReader.iterator();
		String line[] = null;
		// Skip the header if there is any
		if(iterator.hasNext()) {
			line = iterator.next();
			try {
				Integer.parseInt(line[0]);
			} catch(NumberFormatException e) {
				line = iterator.next();
			}
		}
		while(iterator.hasNext()) {
			if(ids.contains(line[0])) {
				csvData.add(StringUtils.join(line, ";"));
			}
			line = iterator.next();
		}
		csvReader.close();
		return csvData;
	}
	
	public static List<String> getDataFromIds(List<String> ids, List<String> headersToShow, File csvFile, char delimitor) throws Exception {
		List<String> csvData = new ArrayList<String>();
		CSVReader csvReader = new CSVReader(new FileReader(csvFile), delimitor);
		Iterator<String[]> iterator = csvReader.iterator();
		String line[] = null;
		List<Integer> idsOfAllowedHeaders = new ArrayList<Integer>();
		if(iterator.hasNext()) {
			line = iterator.next();
			if(line[0].equals("ID")) {
				for(int i = 0; i < line.length; i++) {
					if(headersToShow.contains(line[i])) {
						idsOfAllowedHeaders.add(i);
					}
				}
				iterator.next();
			} else {
				csvReader.close();
				throw new Exception("The first column of your csv should be a column with header name \"ID\"");
			}
		}
		while(iterator.hasNext()) {
			if(ids.contains(line[0])) {
				StringBuilder stringBuilder = new StringBuilder();
				for(int i:idsOfAllowedHeaders) {
					stringBuilder.append(line[i] + delimitor);
				}
				csvData.add(stringBuilder.substring(0, stringBuilder.length() - 2));
			}
			line = iterator.next();
		}
		csvReader.close();
		return csvData;
	}
	
	public String[] getDataFromIdWithBuffer(int id, int buffer) throws IOException {
		csvReader.close();
		csvReader = new CSVReader(new FileReader(csvFile), delimitor, '"', id - buffer);
		Iterator<String[]> iterator = csvReader.iterator();
		String line[] = null;
		if(iterator.hasNext()) {
			line = iterator.next();
			try {
				Integer.parseInt(line[0]);
			} catch(NumberFormatException e) {
				line = iterator.next();
			}
		}
		while(iterator.hasNext()) {
			if(Integer.parseInt(line[0]) == id) {
				return line;
			}
			line = iterator.next();
		}
		return null;
	}
}
