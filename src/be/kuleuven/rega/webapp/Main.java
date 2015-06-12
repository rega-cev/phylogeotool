package be.kuleuven.rega.webapp;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7955893733032239397L;

	public static GraphWebApplication getApp() {
		return (GraphWebApplication)WApplication.getInstance();
	}
	
	@Override
	public WApplication createApplication(WEnvironment env) {
		return new GraphWebApplication(env);
	}

}
