package be.kuleuven.rega.treeFormatTransformer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jebl.evolution.io.NewickImporter;
import jebl.evolution.io.PHYLIPExporter;


public class NewickToPhylipConverter {
	private Writer writer = null;
	
	public NewickToPhylipConverter() {
		
	}

	public NewickImporter getNewickImporter(String location) {
		Reader reader = null;
		NewickImporter newickImporter = null;
		try {
			reader = new FileReader(new File(location));
			newickImporter = new NewickImporter(reader, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return newickImporter;
	}

	public PHYLIPExporter getPhylipExporter(String location) {
		PHYLIPExporter phylipExporter = null;
		try {
			writer = new FileWriter(new File(location));
			phylipExporter = new PHYLIPExporter(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return phylipExporter;
	}
	
	public void closeWriter() throws IOException {
		this.writer.close();
	}

}
