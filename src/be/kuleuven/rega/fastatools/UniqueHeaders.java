package be.kuleuven.rega.fastatools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class UniqueHeaders {
	
	public static void main(String[] args) {
		FASTAReader fastaReader;
		FASTAEntry fastaEntry;
		File outputFasta = new File("/Users/ewout/Documents/Rega_Algorithm/RT.aligned.unique.fasta");
		File headerToHeader = new File("/Users/ewout/Documents/Rega_Algorithm/RT.oldHeaderToNewHeader.csv");
		int counter = 1;
		try {
			BufferedWriter outputFastaWriter = new BufferedWriter(new FileWriter(outputFasta));
			BufferedWriter headerToHeaderWriter = new BufferedWriter(new FileWriter(headerToHeader));
			fastaReader = FASTAReader.getInstance(new File("/Users/ewout/Documents/Rega_Algorithm/RT.aligned.fasta"));
			while((fastaEntry = fastaReader.readNext()) != null) {
				outputFastaWriter.write(">" + counter + "\n");
				outputFastaWriter.write(fastaEntry.getSequence() + "\n");
				headerToHeaderWriter.write(fastaEntry.getHeaderLine().replaceAll("\\s+","").substring(1) + "," + counter++ + "\n");
			}
			outputFastaWriter.close();
			headerToHeaderWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
