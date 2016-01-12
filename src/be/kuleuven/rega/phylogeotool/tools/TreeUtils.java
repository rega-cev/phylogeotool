package be.kuleuven.rega.phylogeotool.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jebl.evolution.trees.Tree;
import be.kuleuven.rega.phylogeotool.io.read.NewickImporterAdapted;
import be.kuleuven.rega.phylogeotool.io.read.NexusImporter;

public class TreeUtils {
	static Tree readNexus(Reader reader) {
		try {
			NexusImporter nexusImporter = new NexusImporter(reader);
			if (nexusImporter.hasTree()) {
				return nexusImporter.importNextTree();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static Tree readNewick(Reader reader) {
		try {
			NewickImporterAdapted newickImporter = new NewickImporterAdapted(reader, false);
			if (newickImporter.hasTree()) {
				return newickImporter.importNextTree();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 static Tree readTree(File tree) throws FileNotFoundException {
		Reader reader = null;
		try {
			reader = new FileReader(tree.getAbsolutePath());
	
			if (tree.getName().endsWith(".nexus")) {
				return readNexus(reader);
			} else if (tree.getName().endsWith(".newick")) {
				return readNewick(reader);
			} else {
				throw new RuntimeException("Unknown tree format");
			}
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
