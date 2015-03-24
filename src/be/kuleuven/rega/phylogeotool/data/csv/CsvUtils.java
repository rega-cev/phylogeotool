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
import be.kuleuven.rega.phylogeotool.data.Sequence;

import com.opencsv.CSVParser;

public class CsvUtils {
	private final static String ID = "id";
	private final static String PATIENT_ID = "patient_id";
	private final static String SAMPLE_ID = "sample_id";
	private final static String VIRAL_ISOLATE_ID = "sample_id";
	private final static String SAMPLE_DATE = "sample_date";
	private final static String DATASET = "dataset";
	private final static String NUCLEOTIDES = "nucleotides";
	private final static String COUNTRY_OF_ORIGIN = "Country of origin";

	public static List<Sequence> readCsv(File csv, char delimitor, String dateFormat) throws IOException {
		List<Sequence> sequences = new ArrayList<Sequence>();

		CSVParser parser = new CSVParser(delimitor);

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
							row[indexOf(header, DATASET)], row[indexOf(header, NUCLEOTIDES)], row[indexOf(header, COUNTRY_OF_ORIGIN)]);
					sequences.add(s);

					line = br.readLine();
				} catch (ParseException e) {
					e.printStackTrace();
					System.out.println(row[indexOf(header, PATIENT_ID)]);
					System.exit(0);
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

		if (line != null) {
			String[] header = parser.parseLine(line);
			distanceMatrix = new double[header.length][header.length];

			int i = 0;
			line = br.readLine();
			while (line != null) {
				String[] row = parser.parseLine(line);

				for (int j = 0; j <= i; j++) {
					// System.out.println("i: " + i + " j: " + j);
					// System.out.println(line);
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

	public static HashMap<String, Integer> getNodeLabelToIndexMapping(File csv) throws IOException {
		CSVParser parser = new CSVParser();
		HashMap<String, Integer> labelToIndexMap = new HashMap<String, Integer>();

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

	/**
	 * Format: 1 2 0.066763 1 3 0.056302 1 4 0.062535 1 5 0.157657
	 * 
	 * @param csv
	 * @throws IOException
	 */

	public static void RAxMLDistanceMatrixToStandard(File csv, File outFile) throws IOException {
		// CSVParser spaceParser = new CSVParser(' ');
		CSVParser parser = new CSVParser('\t');

		BufferedReader br = new BufferedReader(new FileReader(csv));
		String line = br.readLine();
		double[][] distanceMatrix = new double[6000][6000];

		while (line != null) {
			String[] lineParsed = parser.parseLine(line);
			int sequence1 = Integer.parseInt(lineParsed[0].split(" ")[0]);
			int sequence2 = Integer.parseInt(lineParsed[0].split(" ")[1]);
			double distance = Double.parseDouble(lineParsed[1].trim());
			if (sequence1 > sequence2) {
				distanceMatrix[sequence1][sequence2] = distance;
			} else {
				distanceMatrix[sequence2][sequence1] = distance;
			}
			line = br.readLine();
		}

		try {
			FileWriter writer = new FileWriter(outFile);
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 1; i <= 6000; i++) {
				stringBuilder.append(i + ",");
			}
			stringBuilder.append('\n');
			for (int i = 0; i < distanceMatrix.length - 1; i++) {
				for (int j = 0; j < distanceMatrix.length - 1; j++) {
					stringBuilder.append(Double.toString(distanceMatrix[i + 1][j + 1]));
					stringBuilder.append(',');
				}
				stringBuilder.append('\n');
			}
			writer.write(stringBuilder.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Integer> csvToHashMapStringInteger(File csvFile, char delimitor, List<String> ids, String key) throws IOException {
		CSVParser parser = new CSVParser(delimitor);
		HashMap<String, Integer> hashMap= new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		try {
			String line = br.readLine();
			String[] header = parser.parseLine(line);

			line = br.readLine();
			while (line != null) {
				String[] row = parser.parseLine(line);
				String id = row[indexOf(header, "id")];
				if(ids.contains(id)) {
					String value = row[indexOf(header, key)];
					if(hashMap.containsKey(value)) {
						hashMap.put(value, hashMap.get(value) + 1);
					} else {
						hashMap.put(value, 1);
					}
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return hashMap;
	}
}
