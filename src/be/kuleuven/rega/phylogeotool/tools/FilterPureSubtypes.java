package be.kuleuven.rega.phylogeotool.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAReader;

public class FilterPureSubtypes {

	static String acceptedSubtypes[] = {"HIV-1 Subtype A (A1)", "HIV-1 Subtype A (A2)", "HIV-1 Subtype B", "HIV-1 Subtype C", "HIV-1 Subtype D", "HIV-1 Subtype F (F1)",
			"HIV-1 Subtype F (F2)","HIV-1 Subtype G", "HIV-1 Subtype H", "HIV-1 Subtype J", "HIV-1 Subtype K"};
	
	static Map<String,String> subtypeToColor = new HashMap<String, String>();
	
	public static void main(String[] args) {
		subtypeToColor.put("HIV-1 Subtype A (A1)","#FF9900");
		subtypeToColor.put("HIV-1 Subtype A (A2)","#d3d3d3");
		subtypeToColor.put("HIV-1 Subtype B","#00ff00");
		subtypeToColor.put("HIV-1 Subtype C","#ff0000");
		subtypeToColor.put("HIV-1 Subtype D","#551a8b");
		subtypeToColor.put("HIV-1 Subtype F (F1)","#0000ff");
		subtypeToColor.put("HIV-1 Subtype F (F2)","#ffff00");
		subtypeToColor.put("HIV-1 Subtype G","#ff69b4");
		subtypeToColor.put("HIV-1 Subtype H","#00e5ee");
		subtypeToColor.put("HIV-1 Subtype J","#f4a460");
		subtypeToColor.put("HIV-1 Subtype K","#BC23FF");
		
		try {
//			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/ewout/Documents/phylogeo/EUResist_POL/arevir.pure.fasta"));
//			BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/ewout/Documents/phylogeo/EUResist_POL/subtyping_attempt2/result.cut.sorted.csv"));
//			String line = "";
//			Map<String,String> idToSubtype= new HashMap<String, String>();
//			while((line = bufferedReader.readLine()) != null) {
//				for(String subtype:acceptedSubtypes) {
//					if (line.split(",")[1].equals(subtype)) {
//						idToSubtype.put(line.split(",")[0], line.split(",")[1]);
//					}
//				}
//				
//			}
//			
//			BufferedReader fullCsvReader = new BufferedReader(new FileReader("/Users/ewout/Documents/phylogeo/EUResist_POL/EUResist_one_seq_per_pat_shortened.csv"));
//			BufferedWriter bufferedFullCsvWriter = new BufferedWriter(new FileWriter("/Users/ewout/Documents/phylogeo/EUResist_POL/arevir.pure.csv"));
//			String lineFullCsvReader = "";
//			while((lineFullCsvReader = fullCsvReader.readLine()) != null) {
//				if(idToSubtype.containsKey(lineFullCsvReader.split(";")[0])) {
//					bufferedFullCsvWriter.write(lineFullCsvReader + ";" + idToSubtype.get(lineFullCsvReader.split(";")[0]) + "\n");
//				}
//			}
//			FASTAReader fastaReader = FASTAReader.getInstance(new File("/Users/ewout/Documents/phylogeo/EUResist_POL/alignment.ninja.noSDRM.fasta"));
//			FASTAEntry fastaEntry = null;
//			while((fastaEntry = fastaReader.readNext()) != null) {
//				if(idToSubtype.containsKey(fastaEntry.getHeaderLine().replaceAll("\\s+","").substring(1))) {
//					bufferedWriter.write(fastaEntry.getHeaderLine() + "\n");
//					bufferedWriter.write(fastaEntry.getSequence() + "\n");
//				}
//			}
//			bufferedWriter.close();
//			bufferedFullCsvWriter.close();
//			fastaReader.close();
//			fullCsvReader.close();
//			bufferedReader.close();
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/ewout/Documents/phylogeo/EUResist_POL/RaxML/arevir.pure.csv"));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/ewout/Documents/phylogeo/EUResist_POL/RaxML/arevir.pure.subtypeToColor.csv"));
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				bufferedWriter.write(line.split(";")[0] + "," + line.split(";")[11] + "\n");
			}
			bufferedWriter.close();
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
}
