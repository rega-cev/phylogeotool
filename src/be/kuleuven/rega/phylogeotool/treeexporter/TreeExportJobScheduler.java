package be.kuleuven.rega.phylogeotool.treeexporter;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.matchers.KeyMatcher;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.widgets.WConfirmationDialog;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WApplication.UpdateLock;
import figtree.application.GraphicFormat;

public class TreeExportJobScheduler {
	
	private Scheduler sched;
	
	public TreeExportJobScheduler() {
		Properties properties = new Properties();
		properties.setProperty("org.quartz.scheduler.instanceName", "TreeExportScheduler");
		properties.setProperty("org.quartz.threadPool.threadCount", "1");
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(properties);
			sched = schedFact.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void addExportTreeJob(GraphWebApplication graphWebApplication, WConfirmationDialog wConfirmationDialog, WObject parent, String fileName, Cluster cluster, GraphicFormat graphicFormat, ByteArrayOutputStream byteArrayOutputStream, int minClusterSize, boolean colorTree, boolean showTips) {
		SecureRandom random = new SecureRandom();
		JobKey jobKey = new JobKey(new BigInteger(130, random).toString(32));
		JobDetail job = newJob(TreeExportJob.class).withIdentity(jobKey).build();
		job.getJobDataMap().put(TreeExportJob.CLUSTER, cluster);
		job.getJobDataMap().put(TreeExportJob.GRAPHIC_FORMAT, graphicFormat);
//		job.getJobDataMap().put(TreeExportJob.WEB_RESPONSE, webResponse);
		job.getJobDataMap().put(TreeExportJob.OUTPUT_STREAM, byteArrayOutputStream);
		job.getJobDataMap().put(TreeExportJob.MIN_CLUSTER_SIZE, minClusterSize);
		job.getJobDataMap().put(TreeExportJob.COLOR_TREE, colorTree);
		job.getJobDataMap().put(TreeExportJob.SHOW_TIPS, showTips);
//		job.getJobDataMap().put(TreeExportJob.THREAD, thread);
		Trigger trigger = newTrigger().startNow().build(); 
		try {
			sched.scheduleJob(job, trigger);
			sched.getListenerManager().addJobListener(new ExportTreeListener("ExportTreeListener" + jobKey, graphWebApplication, wConfirmationDialog, parent, byteArrayOutputStream, fileName, graphicFormat), KeyMatcher.keyEquals(jobKey));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
