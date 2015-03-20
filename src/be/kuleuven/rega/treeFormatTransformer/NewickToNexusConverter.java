package be.kuleuven.rega.treeFormatTransformer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jebl.evolution.io.NewickImporter;

public class NewickToNexusConverter {

	private Writer writer = null;
	
	public NewickToNexusConverter() {

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

	public NexusExporterFigTree getNexusExporter(String location) {
		NexusExporterFigTree nexusExporterFigTree = null;
		try {
			writer = new FileWriter(new File(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
		nexusExporterFigTree = new NexusExporterFigTree(writer, null);
		return nexusExporterFigTree;
	}
	
	public void closeWriter() throws IOException {
		this.writer.close();
	}

}
