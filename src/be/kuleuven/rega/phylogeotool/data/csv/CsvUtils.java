package be.kuleuven.rega.phylogeotool.data.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import be.kuleuven.rega.phylogeotool.data.Sequence;

public class CsvUtils {
	private final static String ID = "id";
	private final static String PATIENT_ID = "patient_id";
	private final static String SAMPLE_ID = "sample_id";
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

					Sequence s = new Sequence(row[indexOf(header, ID)], row[indexOf(header, PATIENT_ID)], row[indexOf(header, VIRAL_ISOLATE_ID)], d,
							row[indexOf(header, DATASET)], row[indexOf(header, NUCLEOTIDES)], null);
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

	public static void writeSequencesToCSV(File outputFile, List<Sequence> sequences) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFile);
			fw.write(ID.toString() + ',' + PATIENT_ID.toString() + ',' + VIRAL_ISOLATE_ID.toString() + ',' + DATASET.toString() + ',' + NUCLEOTIDES.toString()
					+ ',' + SAMPLE_DATE.toString() + ',' + COUNTRY_OF_ORIGIN.toString());
			fw.write('\n');
			for (Sequence s : sequences) {
				fw.write(s.asCsv());
				fw.write('\n');
			}
			fw.close();
		} finally {
			if (fw != null)
				fw.close();
		}
	}

	public static double[][] textToMatrix(File csv) throws IOException {
		CSVParser parser = new CSVParser();

		BufferedReader br = new BufferedReader(new FileReader(csv));
		String line = br.readLine();
		double[][] distanceMatrix = null;

		if(line != null) {
			String[] header = parser.parseLine(line);
			distanceMatrix = new double[header.length][header.length];
			
			int i = 0;
			line = br.readLine();
			while (line != null) {
				String[] row = parser.parseLine(line);
				
				for (int j = 0; j <= i; j++) {
//					System.out.println("i: " + i + " j: " + j);
//					System.out.println(line);
					if (j == i) {
						distanceMatrix[i][j] = 0.0;
					} else {
						distanceMatrix[i][j] = Double.parseDouble(row[j]);
						distanceMatrix[j][i] = Double.parseDouble(row[j]);
					}
				}
				line = br.readLine();
				i++;
			}
		} else {
			System.err.println(CsvUtils.class.getName() + "No data found in inputfile");
		}

		br.close();
		return distanceMatrix;
	}
	
	public static HashMap<String,Integer> getNodeLabelToIndexMapping(File csv) throws IOException {
		CSVParser parser = new CSVParser();
		HashMap<String,Integer> labelToIndexMap = new HashMap<String, Integer>();
		
		BufferedReader br = new BufferedReader(new FileReader(csv));
		String line = br.readLine();
		String[] header = parser.parseLine(line);

		line = br.readLine();
		while (line != null) {
			String[] row = parser.parseLine(line);
			labelToIndexMap.put(row[indexOf(header, SAMPLE_ID)], Integer.parseInt(row[indexOf(header, ID)]));
			line = br.readLine();
		}
		return labelToIndexMap;
	}
}
