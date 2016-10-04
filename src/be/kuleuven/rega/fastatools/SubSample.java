package be.kuleuven.rega.fastatools;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SubSample {

	public static void subSample(String fastaFileLocation, String subsetLocation, int nrSequences) {
		int countNrSequences = 0;
		try {
			File fastaFile = new File(fastaFileLocation);
			File subset = new File(subsetLocation);
			FASTAReader fastaReader = FASTAReader.getInstance(fastaFile);
			FASTAEntry fastaEntry;
			while((fastaEntry = fastaReader.readNext()) != null) {
				countNrSequences++;
			}
			fastaReader.close();
			
			Set<Integer> lines = new HashSet<Integer>();
			int n;
			while(lines.size() <= nrSequences) {
				Random rand = new Random();
				n = rand.nextInt(countNrSequences) + 1;
				lines.add(n);
			}
			
			int lineNr = 0;
			fastaReader = FASTAReader.getInstance(fastaFile);
			PrintWriter subsetFileWriter = new PrintWriter(subset);
			while((fastaEntry = fastaReader.readNext()) != null) {
				if(lines.contains(lineNr++)) {
					subsetFileWriter.println(fastaEntry.getHeaderLine());
					subsetFileWriter.println(fastaEntry.getSequence());
				}
			}
			subsetFileWriter.close();
			fastaReader.close();
			
			System.out.println("File written");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SubSample.subSample("/Users/ewout/Documents/phylogeo/EUResist_POL/treebuilding_attempt1/arevir.pure_same.fasta_changedheadershort.fasta", "/var/folders/0q/jfc96hd17xzfy6ck_ghw9grh0000gn/T/pplacer.QNhxlOBV/alignment.java.short.fasta", 200);
	}
}
