package be.kuleuven.rega.phylogeotool.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class CsvCleaner {

	public static void main(String[] args) {
		File csvFile = new File(args[0]);
		File outputFile = new File(args[1]);
		char delimitor = ';';
		List<Sequence> sequences = null;
		try {
			sequences = CsvUtils.readCsv(csvFile, delimitor, "yyyy-MM-dd");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(sequences, new SequencePatientIdComparator());

		Sequence previous =sequences.get(0);
		List<Sequence> uniqueSeqPerPatient = new ArrayList<Sequence>();
		for (Sequence sequence : sequences) {
			if (sequence.getPatientId().equals(previous.getPatientId())) {
				if(previous.getSampleDate() != null) {
					if(sequence.getSampleDate() != null) {
						if (sequence.getSampleDate().before(previous.getSampleDate())) {
							previous = sequence;
						}
					}
				}
			} else {
				uniqueSeqPerPatient.add(previous);
				previous = sequence;
			}
		}

		try {
			CsvUtils.writeSequencesToCSV(outputFile, uniqueSeqPerPatient, delimitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sequences.size() + " sequences brought back to " + uniqueSeqPerPatient.size());
		
//		for (Sequence sequence : uniqueSeqPerPatient) {
//			System.out.println(sequence.getPatientId());
//			System.out.println(sequence.getSampleDate());
//		}
	}

}
