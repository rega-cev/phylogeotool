package be.kuleuven.rega.phylogeotool.trash;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarOutputStream;

import be.kuleuven.rega.phylogeotool.pplacer.PPlacer;

public class Test {

	public static void main(String[] args) throws IOException {
		// Output file stream
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		// Create a TarOutputStream
		TarOutputStream out = new TarOutputStream(new GZIPOutputStream(new BufferedOutputStream(dest)));

		// Files to tar
		/*
		 * sequences_subtypeB.align_No_SDRM = alignment.fasta
		 * subtypeB.log = logfile.log
		 * Portugal.final_tree = phylo.tree
		 * sequences.noSDRM.fasta = sequences.fasta
		 */
		File[] filesToTar = new File[4];
//		filesToTar[0] = new File("/Users/ewout/Documents/TDRDetector/temp/sequences_subtypeB.align_No_SDRM.fa");
//		filesToTar[1] = new File("/Users/ewout/Documents/TDRDetector/temp/subtypeB.log");
//		filesToTar[2] = new File("/Users/ewout/Documents/TDRDetector/temp/Portugal.final_tree");
//		filesToTar[3] = new File("/Users/ewout/Documents/TDRDetector/temp/sequences.noSDRM.fasta");
		
		filesToTar[0] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/alignment.fasta");
		filesToTar[1] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/logfile.log");
		filesToTar[2] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/phylo.tree");
		filesToTar[3] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/sequences.fasta");
		
//		filesToTar[0] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/EUResist.HXB2PRRT.fasta");
//		filesToTar[1] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/EUResist.HXB2PRRT.log");
//		filesToTar[2] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/EUResist.final_tree");
//		filesToTar[3] = new File("/Users/ewout/Documents/phylogeo/EUResist/data/temp/sequences.fasta");

		for (File f : filesToTar) {
			out.putNextEntry(new TarEntry(f, f.getName()));
			BufferedInputStream origin = new BufferedInputStream(new FileInputStream(f));

			int count;
			byte data[] = new byte[2048];
			while ((count = origin.read(data)) != -1) {
				out.write(data, 0, count);
			}

			out.flush();
			origin.close();
		}

		out.close();
		
		PhyloGeoService phyloGeoService = new PhyloGeoService();
		String newTree = phyloGeoService.getPPlacedTree(dest);
		System.out.println(newTree);
		
		PrintWriter writer = new PrintWriter("/Users/ewout/Documents/phylogeo/EUResist/data/temp/sequences.tog.tre", "UTF-8");
		writer.write(newTree);
		writer.close();
		List<String> sequences = new ArrayList<String>();
		sequences.add("TEST_1");
		sequences.add("TEST_2");
		sequences.add("TEST_3");
		PPlacer.write("/Users/ewout/Documents/phylogeo/EUResist/data/temp/sequences.tog.tre", "/Users/ewout/Documents/phylogeo/EUResist/pplacerIds/3.xml", sequences);
	}
}
