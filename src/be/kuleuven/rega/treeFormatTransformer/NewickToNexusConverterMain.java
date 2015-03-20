package be.kuleuven.rega.treeFormatTransformer;
import java.io.IOException;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.io.NexusExporter;
import jebl.evolution.trees.Tree;

public class NewickToNexusConverterMain {

	public static void main(String[] args) {
		NewickToNexusConverter newickToNexusConverter = new NewickToNexusConverter();
		NewickImporter newickImporter = newickToNexusConverter.getNewickImporter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.tre");
		NexusExporterFigTree nexusExporterFigTree = newickToNexusConverter.getNexusExporter("/Users/ewout/git/phylogeotool/lib/EwoutTrees/MEDIUMsize_newicktree.nxs");

		try {
			while (newickImporter.hasTree()) {
				Tree tree = newickImporter.importNextTree();
				nexusExporterFigTree.exportTree(tree);
				newickToNexusConverter.closeWriter();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
	}

}
