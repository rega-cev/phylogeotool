package be.kuleuven.rega.phylogeotool.tools;

import java.util.Comparator;

public class SequencePatientIdComparator implements Comparator<Sequence> {

	@Override
	public int compare(Sequence sequence1, Sequence sequence2) {
		int patientId1 = Integer.parseInt(sequence1.getPatientId());
		int patientId2 = Integer.parseInt(sequence2.getPatientId());
		if(patientId1 > patientId2) {
			return 1;
		} else if(patientId1 < patientId2) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
