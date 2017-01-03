package be.kuleuven.rega.webapp.widgets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;
import figtree.application.GraphicFormat;

public class WDownloadResource extends WFileResource {

	private GraphicFormat graphicFormat;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	public WDownloadResource(WObject parent, String fileName, GraphicFormat graphicFormat, ByteArrayOutputStream byteArrayOutputStream) {
		super("image/pdf", fileName, parent);
		this.graphicFormat = graphicFormat;
		this.byteArrayOutputStream = byteArrayOutputStream;
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
		try {
			response.getOutputStream().write(byteArrayOutputStream.toByteArray());	
			response.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void setGraphicFormat(GraphicFormat graphicFormat) {
//		this.graphicFormat = graphicFormat;
//	}
	
	@Override
	public Signal dataChanged() {
		return super.dataChanged();
	}
}