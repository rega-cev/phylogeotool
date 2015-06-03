package be.kuleuven.rega.phylogeotool.data.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

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
	
//	public String[] getDataFromSequenceId(int id) throws IOException {
//		csvReader.close();
//		csvReader = new CSVReader(new FileReader(csvFile), delimitor, ' ');
//		return csvReader.readNext();
//	}

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
