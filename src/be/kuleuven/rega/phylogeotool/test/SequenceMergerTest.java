package be.kuleuven.rega.phylogeotool.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import be.kuleuven.rega.phylogeotool.data.Sequence;
import be.kuleuven.rega.phylogeotool.sequence.SequenceMerger;

public class SequenceMergerTest {

	@Test
	public void testMergeSimple() {
		List<Sequence> sequences = new ArrayList<Sequence>();
		sequences.add(new Sequence("1","1","1",new Date(),"1","ATG---CGA",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","---CGT---",""));
		Sequence result = SequenceMerger.mergeSequence(sequences);
		assertEquals("ATGCGTCGA", result.getNucleotides());
	}
	
	@Test
	public void testMergeGeneralNucleotides() {
		List<Sequence> sequences = new ArrayList<Sequence>();
		sequences.add(new Sequence("1","1","1",new Date(),"1","ATG---CVA",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","---CGT---",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","W------G-",""));
		Sequence result = SequenceMerger.mergeSequence(sequences);
		assertEquals("WTGCGTCVA", result.getNucleotides());
	}
	
	@Test(expected = RuntimeException.class)
	public void testMergeIncorrectNucleotides() {
		List<Sequence> sequences = new ArrayList<Sequence>();
		sequences.add(new Sequence("1","1","1",new Date(),"1","ATG---CVA",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","---CGT---",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","S------G-",""));
		Sequence result = SequenceMerger.mergeSequence(sequences);
		assertEquals("WTGCGTCVA", result.getNucleotides());
	}
	
	@Test
	public void testMergeDifferentSequenceLength() {
		List<Sequence> sequences = new ArrayList<Sequence>();
		sequences.add(new Sequence("1","1","1",new Date(),"1","ATG---",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","---CGT---",""));
		sequences.add(new Sequence("1","1","1",new Date(),"1","W------G-",""));
		Sequence result = SequenceMerger.mergeSequence(sequences);
		assertEquals("WTGCGTCVA", result.getNucleotides());
	}

}
