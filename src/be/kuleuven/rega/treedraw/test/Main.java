package be.kuleuven.rega.treedraw.test;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet {

	@Override
	public WApplication createApplication(WEnvironment env) {
		return new Application(env);
	}
	
}
