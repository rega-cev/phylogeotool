package be.kuleuven.rega.phylogeotool.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import be.kuleuven.rega.comparator.SequenceComparator;
import be.kuleuven.rega.phylogeotool.data.Sequence;

public class SequenceComparatorTest extends TestCase {

	private final SequenceComparator sortSequencePatientIdDatasetComparator = new SequenceComparator();
	
	@Test
    public void testDataset1EqualToDataset2() {
		Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected Dataset 1 and Dataset 2 to be equal", result == 0);
    }

    @Test
    public void testDataset1GreaterThanDataset2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "2", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected Dataset 1 to be greater than Dataset 2", result >= 1);
    }

    @Test
    public void testDataset1LessThanDataset2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "2", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected Dataset 1 to be less than Dataset 2", result <= -1);
    }
    
    @Test
    public void testPatientId1EqualToPatientId2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected PatientId 1 and PatientId 2 to be equal", result == 0);
    }
    
    @Test
    public void testPatientId1GreaterThanPatientId2() {
    	Sequence sequence1 = new Sequence("1", "2", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected PatientId 1 to be greater than PatientId 2", result >= 1);
    }

    @Test
    public void testPatientId1LessThanPatientId2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "2", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected PatientId 1 to be greater than PatientId 2", result <= -1);
    }
    
    @Test
    public void testSampleId1EqualToSampleId2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected SampleId 1 and SampleId 2 to be equal", result == 0);
    }
    
    @Test
    public void testSampleId1GreaterThanSampleId2() {
    	Sequence sequence1 = new Sequence("1", "1", "2", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected SampleId 1 to be greater than SampleId 2", result >= 1);
    }

    @Test
    public void testSampleId1LessThanSampleId2() {
    	Sequence sequence1 = new Sequence("1", "1", "1", new Date(), "1", "", "", "");
		Sequence sequence2 = new Sequence("1", "1", "2", new Date(), "1", "", "", "");
        int result = sortSequencePatientIdDatasetComparator.compare(sequence1, sequence2);
        assertTrue("Expected SampleId 1 to be greater than SampleId 2", result <= -1);
    }
    
}
