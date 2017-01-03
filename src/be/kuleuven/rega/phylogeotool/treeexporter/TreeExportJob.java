package be.kuleuven.rega.phylogeotool.treeexporter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tools.ColorClusters;
import figtree.application.GraphicFormat;

@DisallowConcurrentExecution
public class TreeExportJob implements Job {
	
	public static final String CLUSTER = "CLUSTER";
	public static final String GRAPHIC_FORMAT = "GRAPHIC_FORMAT";
//	public static final String WEB_RESPONSE = "WEB_RESPONSE";
	public static final String OUTPUT_STREAM = "OUTPUT_STREAM";
	public static final String MIN_CLUSTER_SIZE = "MIN_CLUSTER_SIZE";
	public static final String COLOR_TREE = "COLOR_TREE";
	public static final String SHOW_TIPS = "SHOW_TIPS";
//	public static final String THREAD = "THREAD";
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//		try {
			OutputStream outputStream = (OutputStream)jobExecutionContext.getJobDetail().getJobDataMap().get(OUTPUT_STREAM);
			ColorClusters.prepareFullTreeView(null, Settings.getInstance().getPhyloTree(), 
				(Cluster)jobExecutionContext.getJobDetail().getJobDataMap().get(CLUSTER), 
				(GraphicFormat)jobExecutionContext.getJobDetail().getJobDataMap().get(GRAPHIC_FORMAT),
				outputStream,
				jobExecutionContext.getJobDetail().getJobDataMap().getInt(MIN_CLUSTER_SIZE),
				jobExecutionContext.getJobDetail().getJobDataMap().getBoolean(COLOR_TREE),
				jobExecutionContext.getJobDetail().getJobDataMap().getBoolean(SHOW_TIPS));
//			Thread thread = ((Thread)jobExecutionContext.getJobDetail().getJobDataMap().get(THREAD));
//			synchronized (thread) {
//				thread.notifyAll();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
