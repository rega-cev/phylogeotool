package be.kuleuven.rega.listeners;

import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.GraphWebApplication;
import eu.webtoolkit.jwt.Signal;

public class WCircleNodeDoubleClickListener implements Signal.Listener {

	private WCircleNode wCircleNode = null;
	private GraphWebApplication graphWebApplication = null;
	
	public WCircleNodeDoubleClickListener(WCircleNode wCircleNode, GraphWebApplication graphWebApplication) {
		this.wCircleNode = wCircleNode;
		this.graphWebApplication = graphWebApplication;
	}
	
	@Override
	public void trigger() {
		graphWebApplication.doubleClicked(wCircleNode.getNode());
	}

}