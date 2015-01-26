import java.io.IOException;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.io.NexusExporter;

public class NewickToNexusConverterMain {

	public static void main(String[] args) {
		NewickToNexusConverter newickToNexusConverter = new NewickToNexusConverter();
		NewickImporter newickImporter = newickToNexusConverter.getNewickImporter("/home/ewout_kul/Documents/fasttree/results/input.nwk");
		NexusExporter nexusExporter = newickToNexusConverter.getNexusExporter("/home/ewout_kul/Documents/fasttree/results/output.nxs");

		try {
			while (newickImporter.hasTree()) {
				nexusExporter.exportTree(newickImporter.importNextTree());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
	}

}
