package be.kuleuven.rega.phylogeotool.data.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVParser;
import be.kuleuven.rega.phylogeotool.data.Sequence;

public class CsvUtils {
	private final static String PATIENT_ID = "patient_id";
	private final static String VIRAL_ISOLATE_ID = "sample_id";
	private final static String SAMPLE_DATE = "sample_date";
	private final static String DATASET = "dataset";
	private final static String NUCLEOTIDES = "nucleotides";
	private final static String COUNTRY_OF_ORIGIN = "Country of origin";
	
	public static List<Sequence> readCsv(File viralIsolatesCsv, File attributesCsv) throws IOException {
		List<Sequence> sequences = new ArrayList<Sequence>();
		
		CSVParser parser = new CSVParser();
		
		BufferedReader br = new BufferedReader(new FileReader(viralIsolatesCsv));
	    try {
	        String line = br.readLine();
	        String[] header = parser.parseLine(line);
	        
	        line = br.readLine();
	        while (line != null) {
	        	String[] row = parser.parseLine(line);
	        		        	
	        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        	
	        	try {
	        		Date d = null;
	        		String sampleDate = row[indexOf(header, SAMPLE_DATE)];
	        		if (!sampleDate.equals(""))
	        			d = df.parse(sampleDate);
	        		
					Sequence s = new Sequence(
										row[indexOf(header, PATIENT_ID)],
										row[indexOf(header, VIRAL_ISOLATE_ID)],
										d,
										row[indexOf(header, DATASET)],
										row[indexOf(header, NUCLEOTIDES)],
										null
										);
					sequences.add(s);
					
					line = br.readLine();
				} catch (ParseException e) {
					e.printStackTrace();
				}
	        }
	    } finally {
	        br.close();
	    }
	    
	    return sequences;
	}
	
	private Map<Integer, Map<String, String>> readAttributes(File attributesCsv) throws IOException {
		Map<Integer, Map<String, String>> attributes = new HashMap<Integer, Map<String, String>>();

        CSVParser parser = new CSVParser();
		
		BufferedReader br = new BufferedReader(new FileReader(attributesCsv));
	    try {
	        String line = br.readLine();
	        String[] header = parser.parseLine(line);
	        
	        int patientIdIndex = indexOf(header, PATIENT_ID);
	        
	        while (line != null) {
	        	line = br.readLine();
	        	String[] row = parser.parseLine(line);
	        	
	        	Map<String, String> attributeValues = new HashMap<String, String>();
	            for (int i = 0; i < header.length; ++i) {
	            	if (i != patientIdIndex) {
	            		attributeValues.put(header[i], row[i]);
	            	}
	            }
	            
	            attributes.put(Integer.parseInt(row[patientIdIndex]), attributeValues);
	        }
	    } finally {
	        br.close();
	    }
	    
	    return attributes;
	}
	
	private static int indexOf(String[] row, String r) {
		int index = -1;
        for (int i = 0; i < row.length; ++i) {
        	if (row[i].equals(r)) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
}
