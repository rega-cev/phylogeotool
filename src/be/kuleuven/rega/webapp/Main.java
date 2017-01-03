package be.kuleuven.rega.webapp;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.rega.phylogeotool.io.read.ReadTree;
import be.kuleuven.rega.phylogeotool.pplacer.JobScheduler;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.treeexporter.TreeExportJobScheduler;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet implements javax.servlet.ServletContextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7955893733032239397L;
	private String pplacer;
	private static Settings settings;
	private static JobScheduler jobScheduler;
	private static TreeExportJobScheduler treeExportJobScheduler;
	
	public static GraphWebApplication getApp() {
		return (GraphWebApplication)WApplication.getInstance();
	}
	
	@Override
	public WApplication createApplication(WEnvironment env) {
		WApplication wApplication = new GraphWebApplication(env, getServletContext(), pplacer);
		// We do this to clear our the variable in case a user refreshes the webpage
		pplacer = null;
		return wApplication;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("pplacer") != null) {
			if(pplacer == null) {
				pplacer = req.getParameter("pplacer");
			}
		}
		super.doGet(req, resp);
	}
	
//	@Override
//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.err.println("Phylogeotool stop");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.err.println("Phylogeotool started");
		ReadTree.setJeblTree(Settings.getInstance().getPhyloTree());
//		System.out.println(ReadTree.getJeblTree().getTaxa().size());
		ReadTree.setTreeDrawTree(ReadTree.getJeblTree());
		settings = Settings.getInstance();
		jobScheduler = new JobScheduler();
		treeExportJobScheduler = new TreeExportJobScheduler();
	}
	
	public static JobScheduler getJobScheduler() {
		return jobScheduler;
	}
	
	public static TreeExportJobScheduler getTreeExportJobScheduler() {
		return treeExportJobScheduler;
	}
}
