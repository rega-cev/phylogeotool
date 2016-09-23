package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.File;
import java.io.IOException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class PPlacingJob implements Job {

	public static final String PPLACER_SCRIPTS_LOCATION = "PPLACER_SCRIPTS_LOCATION";
	public static final String PHYLO_TREE_LOCATION= "PHYLO_TREE_LOCATION";
	public static final String ALIGNMENT_FASTA_LOCATION= "ALIGNMENT_FASTA_LOCATION";
	public static final String NEW_SEQUENCES_LOCATION= "NEW_SEQUENCES_LOCATION";
	public static final String LOGFILE_LOCATION= "LOGFILE_LOCATION";
	public static final String TEMPDIR_LOCATION= "TEMPDIR_LOCATION";
	
//	private String binariesFolder = "/Users/ewout/Downloads/pplacer-Darwin-v1.1.alpha17-6-g5cecf99";
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.err.println("PPlacer job is running!"); 
		
		String tempDir = createTempDirs(jobExecutionContext.getJobDetail().getJobDataMap().getString(PPLACER_SCRIPTS_LOCATION));
		jobExecutionContext.getJobDetail().getJobDataMap().put(TEMPDIR_LOCATION, tempDir.split("pplacer.")[1]);
		String pplacedTree = doPPlacing(jobExecutionContext.getJobDetail().getJobDataMap().getString(PPLACER_SCRIPTS_LOCATION), tempDir, jobExecutionContext.getJobDetail().getJobDataMap().getString(PHYLO_TREE_LOCATION), 
				jobExecutionContext.getJobDetail().getJobDataMap().getString(ALIGNMENT_FASTA_LOCATION), 
				jobExecutionContext.getJobDetail().getJobDataMap().getString(NEW_SEQUENCES_LOCATION), 
				jobExecutionContext.getJobDetail().getJobDataMap().getString(LOGFILE_LOCATION));
		System.out.println(pplacedTree);
	}
	
	/**
	 * @return the path to the temporarily workdir
	 */
	public String createTempDirs(String pplacer_scripts_location) {
		String args[] = {pplacer_scripts_location + File.separator + "init.sh"};
		
		StreamGobbler streamGobbler = StreamGobbler.runProcess(args);
		System.out.println(streamGobbler.getLastLine());
		return streamGobbler.getLastLine();
	}
	
	/**
	 * @return the path to the tree with the pplaced sequences
	 */
	public String doPPlacing(String pplacer_scripts_location, String tmpDir, String treeFile, String alignmentFile, String sequenceFile, String logFile) {
		String args[] = {pplacer_scripts_location + File.separator + "place.sh", tmpDir, treeFile, alignmentFile, sequenceFile, logFile};
		
		StreamGobbler streamGobbler = StreamGobbler.runProcess(args);
		System.out.println(streamGobbler.getLastLine());
		return tmpDir + File.separator + "sequences.tog.tre";
	}
}
