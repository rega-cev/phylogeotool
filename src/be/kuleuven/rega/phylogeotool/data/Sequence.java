package be.kuleuven.rega.phylogeotool.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sequence {
	String id;
	String patientId;
	String viralIsolateId;
	Date sampleDate;
	String dataset;
	String nucleotides;
	// TODO: make an attribute-map out of this
	String countryOfOrigin;

	public Sequence(String id, String patientId, String viralIsolateId, Date sampleDate, String dataset, String nucleotides, String countryOfOrigin) {
		this.id = id;
		this.patientId = patientId;
		this.viralIsolateId = viralIsolateId;
		this.sampleDate = sampleDate;
		this.dataset = dataset;
		this.nucleotides = nucleotides;
		this.countryOfOrigin = countryOfOrigin;
	}

	public String getId() {
		return id;
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

	public String asCsv() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return this.getId() + "," + this.getPatientId() + "," + this.getViralIsolateId() + "," + this.getDataset() + "," + this.getNucleotides() + ","
				+ format1.format(this.getSampleDate()) + "," + this.getCountryOfOrigin();
	}
}
