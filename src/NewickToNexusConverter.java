import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jebl.evolution.io.NewickImporter;
import jebl.evolution.io.NexusExporter;

public class NewickToNexusConverter {

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

	public NexusExporter getNexusExporter(String location) {
		Writer writer = null;
		NexusExporter nexusExporter = null;
		try {
			writer = new FileWriter(new File(location));
			nexusExporter = new NexusExporter(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nexusExporter;
	}

}
