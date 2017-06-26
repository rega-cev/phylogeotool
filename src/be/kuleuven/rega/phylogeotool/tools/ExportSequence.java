package be.kuleuven.rega.phylogeotool.tools;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAReader;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.settings.Settings;

public class ExportSequence {

	public static void export(Cluster cluster, OutputStream output) {
		List<String> leafIds = cluster.getTree().getLeavesAsString(cluster.getRoot());
		PrintWriter printWriter = new PrintWriter(output);
		try {
			FASTAReader fastaReader = FASTAReader.getInstance(Settings.getInstance().getAlignmentLocation());
			FASTAEntry entry = fastaReader.readNext();
	        while( entry != null ) {
	        	if(leafIds.contains(entry.getHeaderLine().substring(1))) {
//	        		System.out.println("Found: " + entry.getHeaderLine());
	        		printWriter.write(entry.getHeaderLine() + "\n" + entry.getSequence() + "\n");
	        	}
	        	// get the next entry in the FASTA file
	            entry = fastaReader.readNext();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
