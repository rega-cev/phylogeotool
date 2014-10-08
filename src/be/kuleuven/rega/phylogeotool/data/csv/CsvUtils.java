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
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import be.kuleuven.rega.phylogeotool.data.Sequence;

public class CsvUtils {
	private final static String PATIENT_ID = "patient_id";
	private final static String VIRAL_ISOLATE_ID = "sample_id";
	private final static String SAMPLE_DATE = "sample_date";
	private final static String DATASET = "dataset";
	private final static String NUCLEOTIDES = "nucleotides";
	private final static String COUNTRY_OF_ORIGIN = "Country of origin";
	
	public static List<Sequence> readCsv(File csv) throws IOException {
		List<Sequence> sequences = new ArrayList<Sequence>();
		
		CSVParser parser = new CSVParser();
		
		BufferedReader br = new BufferedReader(new FileReader(csv));
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
