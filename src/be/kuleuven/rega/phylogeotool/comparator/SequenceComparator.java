package be.kuleuven.rega.phylogeotool.comparator;

import java.util.Comparator;

import be.kuleuven.rega.phylogeotool.data.Sequence;

public class SequenceComparator implements Comparator<Sequence> {

	@Override
	public int compare(Sequence sequence1, Sequence sequence2) {
		int datasetCompare = sequence1.getDataset().compareTo(sequence2.getDataset());
		
		// The two patients are part of the same dataset
		if(datasetCompare == 0) {
			int patientCompare = sequence1.getPatientId().compareTo(sequence2.getPatientId());
			if(patientCompare == 0) {
				return sequence1.getViralIsolateId().compareTo(sequence2.getViralIsolateId());
			}
			return patientCompare;
		}
		
		return datasetCompare;
	}
	
}
