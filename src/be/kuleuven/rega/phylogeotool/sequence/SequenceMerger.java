package be.kuleuven.rega.phylogeotool.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.rega.comparator.SequenceComparator;
import be.kuleuven.rega.phylogeotool.data.Sequence;
import be.kuleuven.rega.phylogeotool.data.SequenceUtils;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class SequenceMerger {

	public static void main(String[] args) {
		File csvFile = new File(args[0]);
		File outputFile = new File(args[1]);
		List<Sequence> sequences = null;
		try {
			sequences = CsvUtils.readCsv(csvFile, ',', "dd/MM/yy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		performQualityCheck(sequences);
		Collections.sort(sequences, new SequenceComparator());

		List<Sequence> result = mergeSequences(sequences);

		try {
			CsvUtils.writeSequencesToCSV(outputFile, result, ',');
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Result: " + result.size() + ".");
	}

	/**
	 * Check the length of the sequences (They should be of equal lengths)
	 * 
	 * @param sequences
	 */
	public static void performQualityCheck(List<Sequence> sequences) {
		int sequenceLength;
		if (sequences.size() > 0) {
			sequenceLength = sequences.get(0).getNucleotides().length();
			for (Sequence sequence : sequences) {
				if (!(sequence.getNucleotides().length() == sequenceLength)) {
					System.out.println(sequence.getPatientId());
					throw new RuntimeException("Sequences should be aligned and thus of equal lengths before they can be submitted.");
				}
			}
		}

	}

	public static void checkEqualSequenceLength(String sequence1, String sequence2) {
		if (sequence1.length() != sequence2.length()) {
			throw new RuntimeException("Sequence 1 doesn't have the same length as sequence 2. Are the sequences aligned?");
		}
	}

	public static List<Sequence> mergeSequences(List<Sequence> sequences) {
		List<Sequence> mergedSequences = new ArrayList<Sequence>();

		Sequence previous = null;
		List<Sequence> group = new ArrayList<Sequence>();

		for (Sequence current : sequences) {
			if (previous != null && !previous.getViralIsolateId().equals(current.getViralIsolateId())) {
				if (group.size() > 1) {
					mergedSequences.add(mergeSequence(group));
				} else {
					mergedSequences.addAll(group);
				}

				group.clear();
			}

			group.add(current);
			previous = current;
		}
		return mergedSequences;
	}

	public static Sequence mergeSequence(List<Sequence> group) {
		char[] merged = new char[group.get(0).getNucleotides().length()];
		java.util.Arrays.fill(merged, '-');

		for (Sequence sequence : group) {
			for (int i = 0; i < sequence.getNucleotides().length(); i++) {
				if (merged[i] == '-') {
					merged[i] = sequence.getNucleotides().charAt(i);
				} else if (SequenceUtils.isGeneralization(sequence.getNucleotides().charAt(i), merged[i]) == 1) {
					merged[i] = sequence.getNucleotides().charAt(i);
				} else if (!(sequence.getNucleotides().charAt(i) == '-')
						&& SequenceUtils.isGeneralization(merged[i], sequence.getNucleotides().charAt(i)) == -1) {
					System.out.println("PatientId:" + sequence.getPatientId());
					throw new RuntimeException("Incorrect compare with char: " + sequence.getNucleotides().charAt(i) + " and " + merged[i] + " on position: " + i);
				}
			}
		}
		
		return new Sequence(group.get(0).getId(), group.get(0).getPatientId(), group.get(0).getViralIsolateId(), group.get(0).getSampleDate(), group.get(0)
				.getDataset(), new String(merged), group.get(0).getCountryOfOriginEn());
	}
}
