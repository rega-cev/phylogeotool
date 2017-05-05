package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.File;
import java.io.IOException;
import java.lang.Character.Subset;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAReader;

import com.google.common.io.Files;

import be.kuleuven.rega.blast.AlignmentImpl;
import be.kuleuven.rega.blast.AlignmentSequenceType;
import be.kuleuven.rega.blast.BlastAnalysis;
import be.kuleuven.rega.blast.BlastSequenceImpl;
import be.kuleuven.rega.blast.BlastAnalysis.Result;
import be.kuleuven.rega.fastatools.SubSample;

@DisallowConcurrentExecution
public class PPlacingJob implements Job {

	public static final String PPLACER_SCRIPTS_LOCATION = "PPLACER_SCRIPTS_LOCATION";
	public static final String PHYLO_TREE_LOCATION= "PHYLO_TREE_LOCATION";
	public static final String ALIGNMENT_FASTA_LOCATION= "ALIGNMENT_FASTA_LOCATION";
	public static final String NEW_SEQUENCES_LOCATION= "NEW_SEQUENCES_LOCATION";
	public static final String LOGFILE_LOCATION= "LOGFILE_LOCATION";
	public static final String TEMPDIR_LOCATION= "TEMPDIR_LOCATION";
	public static final String RESULT = "RESULT";
	
//	private String binariesFolder = "/Users/ewout/Downloads/pplacer-Darwin-v1.1.alpha17-6-g5cecf99";
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.err.println("PPlacer job is running!"); 
		
//		String tempDir = createTempDirs(jobExecutionContext.getJobDetail().getJobDataMap().getString(PPLACER_SCRIPTS_LOCATION));
//		jobExecutionContext.getJobDetail().getJobDataMap().put(TEMPDIR_LOCATION, tempDir.split("pplacer.")[1]);
		String pplacedTree = doPPlacing(jobExecutionContext, jobExecutionContext.getJobDetail().getJobDataMap().getString(PPLACER_SCRIPTS_LOCATION), jobExecutionContext.getJobDetail().getJobDataMap().getString(TEMPDIR_LOCATION), jobExecutionContext.getJobDetail().getJobDataMap().getString(PHYLO_TREE_LOCATION), 
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
	public String doPPlacing(JobExecutionContext jobExecutionContext, String pplacer_scripts_location, String tmpDir, String treeFile, String alignmentFile, String sequenceFile, String logFile) {
		String args[] = {pplacer_scripts_location + File.separator + "place.sh", tmpDir, treeFile, alignmentFile, sequenceFile, logFile};
		
		// Check if user given sequence is sufficient to start an analysis on
		if(blastCheck(alignmentFile, sequenceFile)) {
		
			// Create subset from original alignment
			int nrSequencesSubset = 200;
			SubSample.subSample(alignmentFile, tmpDir + File.separator + "alignment.short.fasta", nrSequencesSubset);
			
			StreamGobbler streamGobbler = StreamGobbler.runProcess(args);
			System.out.println(streamGobbler.getLastLine());
			jobExecutionContext.getJobDetail().getJobDataMap().put(RESULT, streamGobbler.getLastLine());
			return tmpDir + File.separator + "sequences.tog.tre";
		} else {
			jobExecutionContext.getJobDetail().getJobDataMap().put(RESULT, "Uploaded sequence didn't pass the BLAST test.");
			return "Uploaded sequence didn't pass the BLAST test.";
		}
	}
	
	private boolean blastCheck(String alignmentLocation, String sequenceLocation) {
		File alignmentFile = new File(alignmentLocation);
		File sequenceFile = new File(sequenceLocation);
		
		FASTAReader fastaReader = null;
		FASTAEntry fastaEntry = null;
		try {
			fastaReader = FASTAReader.getInstance(sequenceFile);
			fastaEntry = fastaReader.readNext();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		AlignmentImpl alignmentImpl = new AlignmentImpl(AlignmentSequenceType.NT, alignmentFile);
		BlastSequenceImpl blastSequenceImpl = new BlastSequenceImpl(fastaEntry.getSequence().length(), sequenceFile);
		
		double cutoff = 50.0;
		String blastOptions = "-q -1";
		
		try {
			Result result = new BlastAnalysis(alignmentImpl, cutoff, blastOptions).run(alignmentImpl, blastSequenceImpl, Files.createTempDir());
			return result.haveSupport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
