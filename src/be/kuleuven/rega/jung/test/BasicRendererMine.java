package be.kuleuven.rega.jung.test;

import edu.uci.ics.jung.visualization.renderers.BasicRenderer;

public class BasicRendererMine<V,E> extends BasicRenderer<V, E>{

	public BasicRendererMine() {
		super();
		super.setVertexLabelRenderer(new BasicVertexLabelRendererMine<V,E>());
	}
	
}
