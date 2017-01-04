package be.kuleuven.rega.phylogeotool.pplacer;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.matchers.KeyMatcher;

public class JobScheduler {

	private Scheduler sched;
	private String jobKey = "pplaceJob";
	private int i = 0;
	
//	public static void main(String[] args) {
//		JobScheduler jobScheduler = new JobScheduler();
//		jobScheduler.addPPlacerJob("/Users/ewout/Documents/pplacer_newTry", "/Users/ewout/Documents/phylogeo/portugal/pplacer/phylo.tree","/Users/ewout/Documents/phylogeo/portugal/pplacer/alignment.fasta",
//				"/Users/ewout/Documents/pplacer/HIV_1_B_one_seq.fasta", "/Users/ewout/Documents/phylogeo/portugal/pplacer/logfile.log", "ewout.vandeneynden@kuleuven.be");
//		jobScheduler.addPPlacerJob("/Users/ewout/Documents/pplacer_newTry", "/Users/ewout/Documents/phylogeo/portugal/pplacer/phylo.tree","/Users/ewout/Documents/phylogeo/portugal/pplacer/alignment.fasta",
//				"/Users/ewout/Documents/pplacer/HIV_1_C_one_seq.fasta", "/Users/ewout/Documents/phylogeo/portugal/pplacer/logfile.log", "ewout.vandeneynden@kuleuven.be");
//		jobScheduler.addPPlacerJob("/Users/ewout/Documents/pplacer_newTry", "/Users/ewout/Documents/phylogeo/portugal/pplacer/phylo.tree","/Users/ewout/Documents/phylogeo/portugal/pplacer/alignment.fasta",
//				"/Users/ewout/Documents/pplacer/HIV_1_G_one_seq.fasta", "/Users/ewout/Documents/phylogeo/portugal/pplacer/logfile.log", "ewout.vandeneynden@kuleuven.be");
////		jobScheduler.addPPlacerJob();
//	}
	
	public JobScheduler() {
		Properties properties = new Properties();
		properties.setProperty("org.quartz.scheduler.instanceName", "PPlacerScheduler");
		properties.setProperty("org.quartz.threadPool.threadCount", "1");
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(properties);
			sched = schedFact.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
//	public void addPPlacerJob() {
//		//TODO: Remove the parameters from here
//		String pplacer_scripts_location = "/Users/ewout/Documents/pplacer_newTry";
//		String phylo_tree_location = "/Users/ewout/Documents/phylogeo/portugal/pplacer/phylo.tree";
//		String alignment_fasta_location = "/Users/ewout/Documents/phylogeo/portugal/pplacer/alignment.fasta";
//		String new_sequences_location = "/Users/ewout/Documents/phylogeo/portugal/pplacer/sequences.fasta";
//		String logfile_location = "/Users/ewout/Documents/phylogeo/portugal/pplacer/logfile.log";
//		
//		JobDetail job = newJob(PPlacingJob.class).withIdentity(new String(jobKey + ++i)).build();
//		job.getJobDataMap().put(PPlacingJob.PPLACER_SCRIPTS_LOCATION, pplacer_scripts_location);
//		job.getJobDataMap().put(PPlacingJob.PHYLO_TREE_LOCATION, phylo_tree_location);
//		job.getJobDataMap().put(PPlacingJob.ALIGNMENT_FASTA_LOCATION, alignment_fasta_location);
//		job.getJobDataMap().put(PPlacingJob.NEW_SEQUENCES_LOCATION, new_sequences_location);
//		job.getJobDataMap().put(PPlacingJob.LOGFILE_LOCATION, logfile_location);
//		Trigger trigger = newTrigger().startNow().build(); 
//		try {
//			sched.scheduleJob(job, trigger);
//			sched.getListenerManager().addJobListener(new PPlacerListener("PPlacerListener" + i), KeyMatcher.keyEquals(new JobKey(new String(jobKey + i))));
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void addPPlacerJob(String pplacer_tmp_folder, String pplacer_scripts_location, String phylo_tree_location, String alignment_fasta_location, String new_sequences_location, String logfile_location, String email) {
		
		JobDetail job = newJob(PPlacingJob.class).withIdentity(new String(jobKey + ++i)).build();
		job.getJobDataMap().put(PPlacingJob.TEMPDIR_LOCATION, pplacer_tmp_folder);
		job.getJobDataMap().put(PPlacingJob.PPLACER_SCRIPTS_LOCATION, pplacer_scripts_location);
		job.getJobDataMap().put(PPlacingJob.PHYLO_TREE_LOCATION, phylo_tree_location);
		job.getJobDataMap().put(PPlacingJob.ALIGNMENT_FASTA_LOCATION, alignment_fasta_location);
		job.getJobDataMap().put(PPlacingJob.NEW_SEQUENCES_LOCATION, new_sequences_location);
		job.getJobDataMap().put(PPlacingJob.LOGFILE_LOCATION, logfile_location);
		job.getJobDataMap().put(PPlacerListener.EMAIL_ADDRESS, email);
		Trigger trigger = newTrigger().startNow().build(); 
		try {
			sched.scheduleJob(job, trigger);
			sched.getListenerManager().addJobListener(new PPlacerListener("PPlacerListener" + i), KeyMatcher.keyEquals(new JobKey(new String(jobKey + i))));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
