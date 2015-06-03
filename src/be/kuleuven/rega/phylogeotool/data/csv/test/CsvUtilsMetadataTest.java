package be.kuleuven.rega.phylogeotool.data.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtilsMetadata;

public class CsvUtilsMetadataTest {
	
	CsvUtilsMetadata csvUtilsMetadata = null;
	
	@Before
	public void init() throws IOException {
		csvUtilsMetadata = new CsvUtilsMetadata(new File("/Users/ewout/git/phylogeotool/lib/testMeta.csv"), ',');
	}
	
	@Test
	public void testReadHeader() throws IOException {
		assertEquals(4,csvUtilsMetadata.getHeader().length);
		assertArrayEquals(new String[] {"id","Country of origin","Age","Subtype"}, csvUtilsMetadata.getHeader());
	}
	
	@Test
	public void testDataFromId() throws IOException {
		assertArrayEquals(new String[] {"1","Portugal","43","G"}, csvUtilsMetadata.getDataFromId(1));
		assertArrayEquals(new String[] {"3","Brasil","23","B"}, csvUtilsMetadata.getDataFromId(3));
	}
	
	@Test
	public void testDataFromIdWithBuffer() throws IOException {
		assertArrayEquals(new String[] {"1","Portugal","43","G"}, csvUtilsMetadata.getDataFromIdWithBuffer(1,2));
		assertArrayEquals(new String[] {"3","Brasil","23","B"}, csvUtilsMetadata.getDataFromIdWithBuffer(3,100));
	}

}
