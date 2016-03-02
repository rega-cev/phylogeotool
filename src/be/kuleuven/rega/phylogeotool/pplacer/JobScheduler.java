package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.File;
import java.io.IOException;

public class JobScheduler {
	/**
	 * We suppose that the program is installed on a UNIX machine as PPlacer cannot work on another system
	 */
	private String scriptFolder = "/Users/ewout/Documents/pplacer_newTry";
	private String binariesFolder = "/Users/ewout/Downloads/pplacer-Darwin-v1.1.alpha17-6-g5cecf99";
	
	/**
	 * @return the path to the temporarily workdir
	 */
	public String createTempDirs() {
		String args[] = {scriptFolder + File.separator + "init.sh"};
		
		StreamGobbler streamGobbler = runProcess(args);
		System.out.println(streamGobbler.getLastLine());
		return streamGobbler.getLastLine();
	}
	
	/**
	 * @return the path to the tree with the pplaced sequences
	 */
	public String doPPlacing(String tmpDir, String treeFile, String alignmentFile, String sequenceFile, String logFile) {
		String args[] = {scriptFolder + File.separator + "place.sh", tmpDir, treeFile, alignmentFile, sequenceFile, logFile};
		
		StreamGobbler streamGobbler = runProcess(args);
		System.out.println(streamGobbler.getLastLine());
		return tmpDir + File.separator + "sequences.tog.tre";
	}
	
	private StreamGobbler runProcess(String args[]) {
		Runtime rt = Runtime.getRuntime();
		try {
			Process process = rt.exec(args);
			// any error message?
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal = process.waitFor();
            System.out.println("Exit value: " + exitVal);

            return outputGobbler;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		JobScheduler jobScheduler = new JobScheduler();
		String tempDir = jobScheduler.createTempDirs();
		String pplacedTree = jobScheduler.doPPlacing(tempDir, "/Users/ewout/Documents/phylogeo/portugal/pplacer/phylo.tree", 
				"/Users/ewout/Documents/phylogeo/portugal/pplacer/alignment.fasta", "/Users/ewout/Documents/phylogeo/portugal/pplacer/sequences.fasta", 
				"/Users/ewout/Documents/phylogeo/portugal/pplacer/logfile.log");
	}

}
