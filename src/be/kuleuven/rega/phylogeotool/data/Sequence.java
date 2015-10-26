package be.kuleuven.rega.phylogeotool.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sequence {
	String id;
	String patientId;
	// Need to get this one from EUResist!
	String viralIsolateId;
	// Not important in case of EUResist
	String dataset;
	// TODO: make an attribute-map out of this
	String countryOfOriginIso;
	String countryOfOriginEn;
	String yearOfBirth;
	String countryOfInfectionIso;
	String countryOfInfectionEn;
	String gender;
	String ethnic_group;
	String risk_group;
	Date sampleDate;
	String nucleotides;
	String gene_region;

	public Sequence(String id, String patientId, String viralIsolateId, Date sampleDate, String dataset, String nucleotides, String countryOfOriginEn, String gene_region) {
		this.id = id;
		this.patientId = patientId;
		this.viralIsolateId = viralIsolateId;
		this.sampleDate = sampleDate;
		this.dataset = dataset;
		this.nucleotides = nucleotides;
		this.countryOfOriginEn = countryOfOriginEn;
		this.gene_region = gene_region;
	}

	// EUResist
	public Sequence(String id, String patientId, Date sampleDate, String nucleotides, 
			String countryOfOriginIso, String countryOfOriginEn, String yearOfBirth, 
			String countryOfSamplingIso, String countryOfSamplingEn, String gender, 
			String ethnic_group, String risk_group, String gene_region) {
		this.id = id;
		this.patientId = patientId;
		this.sampleDate = sampleDate;
		this.nucleotides = nucleotides;
		this.countryOfOriginIso = countryOfOriginIso;
		this.countryOfOriginEn = countryOfOriginEn;
		this.yearOfBirth = yearOfBirth;
		this.countryOfInfectionIso = countryOfSamplingIso;
		this.countryOfInfectionEn = countryOfSamplingEn;
		this.gender = gender;
		this.ethnic_group = ethnic_group;
		this.risk_group = risk_group;
		this.gene_region = gene_region;
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

	public String getCountryOfOriginIso() {
		return countryOfOriginIso;
	}
	
	public String getCountryOfOriginEn() {
		return countryOfOriginEn;
	}

//	public String asCsv() {
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//		return this.getId() + "," + this.getPatientId() + "," + this.getViralIsolateId() + "," + this.getDataset() + "," + this.getNucleotides() + ","
//				+ format1.format(this.getSampleDate()) + "," + this.getCountryOfOrigin();
//	}
	
	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public String getCountryOfInfectionIso() {
		return countryOfInfectionIso;
	}
	
	public String getCountryOfInfectionEn() {
		return countryOfInfectionEn;
	}

	public String getGender() {
		return gender;
	}

	public String getEthnic_group() {
		return ethnic_group;
	}

	public String getRisk_group() {
		return risk_group;
	}
	
	public String getGene_region() {
		return gene_region;
	}

	public String asCsv() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String toReturn = "";
		toReturn += this.getId() + "," + this.getPatientId() + "," + this.getViralIsolateId() + "," + this.getNucleotides() + ",";
		if(this.getSampleDate() != null) {
			toReturn += format1.format(this.getSampleDate());
		} else {
			toReturn += "";
		}
		return toReturn;
	}
	
	public String asCsvEUResist() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String toReturn = "";
		toReturn += this.getPatientId() + ";" + this.getYearOfBirth() + ";" + this.getGender() + ";"
				+ this.getCountryOfOriginIso() + ";" + this.getCountryOfOriginEn() +";" + this.getCountryOfInfectionIso() + ";"
				+ this.getCountryOfInfectionEn() + ";" +this.getEthnic_group() + ";"
				+ this.getRisk_group() + ";";
		if(this.getSampleDate() != null) {
			toReturn += format1.format(this.getSampleDate());
		} else {
			toReturn += "";
		}
		toReturn += ";" + this.getGene_region() + ";" + this.getNucleotides();
		return toReturn;
	}
}
