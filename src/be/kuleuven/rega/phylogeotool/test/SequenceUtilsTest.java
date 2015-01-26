package be.kuleuven.rega.phylogeotool.test;

import static org.junit.Assert.*;

import org.junit.Test;

import be.kuleuven.rega.phylogeotool.data.SequenceUtils;

public class SequenceUtilsTest {

	@Test
    public void testIsGeneralization() {
		assertEquals(0, SequenceUtils.isGeneralization('a', 'a'));
		assertEquals(-1, SequenceUtils.isGeneralization('a', 'c'));
		assertEquals(1, SequenceUtils.isGeneralization('w', 'a'));
		assertEquals(-1, SequenceUtils.isGeneralization('w', 'c'));
		assertEquals(0, SequenceUtils.isGeneralization('w', 'w'));
		assertEquals(1, SequenceUtils.isGeneralization('v', 'a'));
		assertEquals(-1, SequenceUtils.isGeneralization('a', 'v'));
		assertEquals(1, SequenceUtils.isGeneralization('v', 's'));
		assertEquals(-1, SequenceUtils.isGeneralization('s', 'v'));
		assertEquals(1, SequenceUtils.isGeneralization('n', 'a'));
		assertEquals(1, SequenceUtils.isGeneralization('n', 'w'));
		assertEquals(1, SequenceUtils.isGeneralization('n', 'v'));
		assertEquals(0, SequenceUtils.isGeneralization('-', '-'));
	}

}
