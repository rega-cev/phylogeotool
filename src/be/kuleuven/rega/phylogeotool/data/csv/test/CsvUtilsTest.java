package be.kuleuven.rega.phylogeotool.data.csv.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class CsvUtilsTest {
	@Test
	public void testReadHeader() throws IOException {
		CsvUtils.RAxMLDistanceMatrixToStandard(new File("/Users/ewout/Documents/phylogeo/RAxML_distances.distance_matrix.sorted"), new File("/Users/ewout/Documents/phylogeo/outfile.csv"));
	}
}
