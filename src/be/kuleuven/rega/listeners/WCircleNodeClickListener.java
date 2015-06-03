package be.kuleuven.rega.listeners;

import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.GraphWebApplication;
import eu.webtoolkit.jwt.Signal;

public class WCircleNodeClickListener implements Signal.Listener {

	private WCircleNode wCircleNode = null;
	private GraphWebApplication graphWebApplication = null;
	
	public WCircleNodeClickListener(WCircleNode wCircleNode, GraphWebApplication graphWebApplication) {
		this.wCircleNode = wCircleNode;
		this.graphWebApplication = graphWebApplication;
	}
	
	@Override
	public void trigger() {
		graphWebApplication.clicked(wCircleNode.getNode());
	}

}
