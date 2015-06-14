package be.kuleuven.rega.listeners;

import be.kuleuven.rega.phylogeotool.tree.WCircleNode;
import be.kuleuven.rega.webapp.GraphWebApplication;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WMouseEvent;

public class WCircleMouseWentOutListener implements Signal1.Listener<WMouseEvent> {

	private WCircleNode wCircleNode = null;
	private GraphWebApplication graphWebApplication = null;
	
	public WCircleMouseWentOutListener(WCircleNode wCircleNode, GraphWebApplication graphWebApplication) {
		this.wCircleNode = wCircleNode;
		this.graphWebApplication = graphWebApplication;
	}
	
	@Override
	public void trigger(WMouseEvent wMouseEvent) {
		graphWebApplication.mouseWentOut(wMouseEvent, wCircleNode);
	}

}