package be.kuleuven.rega.phylogeotool.tools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.Tree;
import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.treeFormatTransformer.NexusExporterFigTree;

import com.opencsv.CSVParser;

public class CsvColorTree {
	public static void main(String [] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Usage: CsvColorTree tree.nexus colors.csv");
			System.exit(0);
		}
		
		File nexus = new File(args[0]);
		File csvColor = new File(args[1]);
		
		try {
			Tree tree = readTree(nexus);
			List<Sequence> sequences = parseCSV(csvColor);
			
			Map<String, String> taxonNameToColor = new HashMap<String,String>();
			for (Sequence s : sequences) {
				System.err.println(s.label);
				
				List<Taxon> taxa = findLabel(tree, s.label);
				for(Taxon t : taxa) {
					for (int i = 0; i < s.label.length()/2; ++i) {
						System.err.print(" ");
					}
					System.err.println("=> " + t.getName());
				}
				
				if (taxa.size() == 0) {
					throw new RuntimeException("Could not find node with label \"" + s.label + "\"");
				} else if (taxa.size() >= 1) {
					for(Taxon t : taxa) {
						taxonNameToColor.put(t.getName(), colorToAttribute(s.color));
					}
				} else {
					//taxonNameToColor.put(s.label, colorToAttribute(Color.black));				
				}
			}
		
			System.out.print(exportTree(tree, taxonNameToColor));			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static String colorToAttribute(Color c) {
		return "#" + String.format("%06x", c.getRGB() & 0x00FFFFFF);
	}
	
	private static String exportTree(Tree tree, Map<String, String> taxonNameToColor) {
		StringWriter writer = new StringWriter();
		NexusExporterFigTree nexusExporterFigTree = null;
		try {
			nexusExporterFigTree = new NexusExporterFigTree(writer, taxonNameToColor);
			nexusExporterFigTree.exportTree(tree);
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static List<Taxon> findLabel(Tree tree, String label) {
		List<Taxon> taxa = new ArrayList<Taxon>();
		
		for (Taxon t : tree.getTaxa()) {
			if (t.getName().contains(label)) {
				taxa.add(t);
			}
		}
		
		return taxa;
	}
	
	private static Color toColor(String colorName) {
		try {
		    Field field = Class.forName("java.awt.Color").getField(colorName);
		    return (Color)field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static int indexOf(String [] haystack, String needle) {
		for (int i = 0; i < haystack.length; ++i) {
			if (needle.equals(haystack[i])) {
				return i;
			}
		}
		return -1;
	}
	
	private static class Sequence {
		public Sequence(Color color, String label) {
			this.color = color;
			this.label = label;
		}
		Color color;
		String label;
	}
	private static List<Sequence> parseCSV(File csv) throws IOException {
		List<Sequence> sequences = new ArrayList<Sequence>();
		
		CSVParser parser = new CSVParser(',');

		BufferedReader br = new BufferedReader(new FileReader(csv));
		try {
			String line = br.readLine();
			String[] header = parser.parseLine(line);

			line = br.readLine();
			while (line != null) {
				String[] row = parser.parseLine(line);
				Color c = toColor(row[indexOf(header, "color")]);
				String l = row[indexOf(header, "label")];
				
				sequences.add(new Sequence(c, l));
				
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		
		return sequences;
	}
	
	private static Tree readTree(File nexus) throws FileNotFoundException {
		jebl.evolution.trees.Tree tree = ReadTree.readTree(new FileReader(nexus.getAbsolutePath()));
		
		return tree;
	}
}
