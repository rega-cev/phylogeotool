package be.kuleuven.rega.webapp.widgets;

import java.io.BufferedOutputStream;
import java.io.IOException;

import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.phylogeotool.settings.Settings;
import be.kuleuven.rega.phylogeotool.tools.ColorClusters;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;
import figtree.application.GraphicFormat;

public class WDownloadResource extends WFileResource {

	private Cluster cluster;
	private GraphicFormat graphicFormat;
	private boolean colorTree;
	private boolean showTips;

	public WDownloadResource(WObject parent, String fileName, Cluster cluster, GraphicFormat graphicFormat) {
		super("image/pdf", fileName, parent);
		this.cluster = cluster;
		this.graphicFormat = graphicFormat;
		this.colorTree = true;
		this.showTips = false;
	}

	@Override
	public void handleRequest(WebRequest request, WebResponse response) {
		if (graphicFormat == null) {
			// NEXUS
			response.setContentType("text/plain");
		} else {
			if (graphicFormat.equals(GraphicFormat.PDF)) {
				response.setContentType("application/pdf");
			} else if (graphicFormat.equals(GraphicFormat.PNG)) {
				response.setContentType("image/png");
			} else if (graphicFormat.equals(GraphicFormat.SVG)) {
				response.setContentType("image/svg+xml");
			}
		}
		BufferedOutputStream output;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			ColorClusters.prepareFullTreeView(null, Settings.getInstance().getPhyloTree(), cluster, graphicFormat, output, 2, colorTree, showTips);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public void setGraphicFormat(GraphicFormat graphicFormat) {
		this.graphicFormat = graphicFormat;
	}
	
	public void setColorTree(boolean colorTree) {
		this.colorTree = colorTree;
	}
	
	public void setShowTips(boolean showTips) {
		this.showTips = showTips;
	}
	
	@Override
	public Signal dataChanged() {
		return super.dataChanged();
	}
}