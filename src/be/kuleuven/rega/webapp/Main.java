package be.kuleuven.rega.webapp;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7955893733032239397L;
	private String pplacer;
	
	public static GraphWebApplication getApp() {
		return (GraphWebApplication)WApplication.getInstance();
	}
	
	@Override
	public WApplication createApplication(WEnvironment env) {
		WApplication wApplication = new GraphWebApplication(env, pplacer);
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
}
