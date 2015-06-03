package be.kuleuven.rega.phylogeotool.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.rega.phylogeotool.data.Sequence;
import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class RemoveShortSequences {
	
	public static void main(String[] args) {
		File csvFile = new File(args[0]);
		File outputFile = new File(args[1]);
		int sequenceLength = Integer.parseInt(args[2]);
		
		List<Sequence> sequences = null;
		try {
			sequences = CsvUtils.readCsv(csvFile, ';', "dd/MM/yy");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Sequence> result = checkSequenceLength(sequences, sequenceLength);

		try {
			CsvUtils.writeSequencesToCSV(outputFile, result, ';');
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Result: " + result.size() + ".");
	}
	
	public static List<Sequence> checkSequenceLength(List<Sequence> sequences, int length) {
		
		List<Sequence> toReturn = new ArrayList<Sequence>();
		for(Sequence sequence:sequences) {
			if(sequence.getNucleotides().length() > length) {
				toReturn.add(sequence);
			}
		}
		return toReturn;
	}
}
