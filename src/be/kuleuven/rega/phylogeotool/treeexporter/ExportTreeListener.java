package be.kuleuven.rega.phylogeotool.treeexporter;

import java.io.ByteArrayOutputStream;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import be.kuleuven.rega.webapp.GraphWebApplication;
import be.kuleuven.rega.webapp.widgets.WConfirmationDialog;
import eu.webtoolkit.jwt.WObject;
import figtree.application.GraphicFormat;

public class ExportTreeListener implements JobListener {

    private String name;
    private WObject parent;
    private ByteArrayOutputStream byteArrayOutputStream;
    private String fileName;
    private GraphicFormat graphicFormat;
    private GraphWebApplication graphWebApplication;
    private WConfirmationDialog wConfirmationDialog;

// TODO: MAKE THIS AN OBSERVER! You cannot give the application here
    public ExportTreeListener(String name, GraphWebApplication graphWebApplication, WConfirmationDialog wConfirmationDialog, WObject parent, ByteArrayOutputStream byteArrayOutputStream, String fileName, GraphicFormat graphicFormat) {
        this.name = name;
        this.parent = parent;
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.fileName = fileName;
        this.graphicFormat = graphicFormat;
        this.graphWebApplication = graphWebApplication;
        this.wConfirmationDialog = wConfirmationDialog;
    }
    
    public String getName() {
        return name;
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        // do something with the event
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
    	System.err.println("Job has finished");
    	graphWebApplication.treeExportFinished(parent, wConfirmationDialog, byteArrayOutputStream, fileName, graphicFormat);
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
    	// do something with the event
    }
}