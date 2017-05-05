package be.kuleuven.rega.phylogeotool.pplacer;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class PPlacerListener implements JobListener {

    private String name;
    public static final String EMAIL_ADDRESS= "EMAIL_ADDRESS";
	private final Mail mail = new Mail();

    public PPlacerListener(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        // do something with the event
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
    	System.err.println("Job has finished");
    	System.err.println("Location: " + context.getJobDetail().getJobDataMap().getString(PPlacingJob.TEMPDIR_LOCATION));
    	mail.sendEmail(context.getJobDetail().getJobDataMap().getString(EMAIL_ADDRESS), context.getJobDetail().getJobDataMap().getString(PPlacingJob.TEMPDIR_LOCATION).split("pplacer")[1],context.getJobDetail().getJobDataMap().getString(PPlacingJob.RESULT));
    	System.err.println("An email was sent to: " + context.getJobDetail().getJobDataMap().getString(EMAIL_ADDRESS));
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
    	// do something with the event
    }
}