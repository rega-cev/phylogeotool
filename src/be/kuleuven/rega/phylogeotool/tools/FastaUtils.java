package be.kuleuven.rega.phylogeotool.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FastaUtils {
	
	public static String toFastaEntry(Sequence sequence) {
		String output = "";
		output += ">";
		output += sequence.getId();
		output += "\n";
		output += sequence.getNucleotides();
		return output;
	}
	
	public static void fastaToCsv(File fastaFile, File csvFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fastaFile));
		PrintWriter writer = new PrintWriter(csvFile);
		
		String line;
		String id;
		String sequence = "";
		id = br.readLine();
		
		writer.print("id,nucleotides\n");
		
		while ((line = br.readLine()) != null) {
			if(line.charAt(0) == '>') {
				writer.print(id.substring(1, id.length() - 1) + "," + sequence + "\n");
				id = line;
				sequence = "";
			} else {
				sequence += line;
			}
		}
		br.close();
		writer.close();
	}
}
