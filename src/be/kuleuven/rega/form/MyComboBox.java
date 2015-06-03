package be.kuleuven.rega.form;

import java.io.File;
import java.io.IOException;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtilsMetadata;
import eu.webtoolkit.jwt.WComboBox;

public class MyComboBox extends WComboBox {

	private CsvUtilsMetadata csvUtilsMetadata;
	
	public MyComboBox(File metaData, char delimitor) throws IOException {
		super();
		csvUtilsMetadata = new CsvUtilsMetadata(metaData, delimitor);
		String header[] = csvUtilsMetadata.getHeader();
		for(int i = 1; i<header.length;i++) {
			this.addItem(header[i]);
		}
	}
	
}
