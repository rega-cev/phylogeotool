package be.kuleuven.rega.treeFormatTransformer;
import java.io.IOException;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.io.PHYLIPExporter;


public class NewickToPhylipConverterMain {
	
	public static void main(String[] args) {
		NewickToPhylipConverter newickToPhylipConverter = new NewickToPhylipConverter();
		NewickImporter newickImporter = newickToPhylipConverter.getNewickImporter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.tre");
		PHYLIPExporter phylipExporter = newickToPhylipConverter.getPhylipExporter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.phy");
		
		try {
			while (newickImporter.hasTree()) {
				phylipExporter.exportTree(newickImporter.importNextTree());
				newickToPhylipConverter.closeWriter();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
	}
	
}
