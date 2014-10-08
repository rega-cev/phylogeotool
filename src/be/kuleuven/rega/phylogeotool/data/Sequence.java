package be.kuleuven.rega.phylogeotool.data;

import java.util.Date;

public class Sequence {
	String patientId;
	String viralIsolateId;
	Date sampleDate;
	String dataset;
	String nucleotides;
	//TODO: make an attribute-map out of this
	String countryOfOrigin;
	
	public Sequence(String patientId, String viralIsolateId, 
			Date sampleDate, String dataset,
			String nucleotides, String countryOfOrigin) {
		this.patientId = patientId;
		this.viralIsolateId = viralIsolateId;
		this.sampleDate = sampleDate;
		this.dataset = dataset;
		this.nucleotides = nucleotides;
		this.countryOfOrigin = countryOfOrigin;
	}
	public String getPatientId() {
		return patientId;
	}
	public String getViralIsolateId() {
		return viralIsolateId;
	}
	public Date getSampleDate() {
		return sampleDate;
	}
	public String getDataset() {
		return dataset;
	}
	public String getNucleotides() {
		return nucleotides;
	}
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}
}
