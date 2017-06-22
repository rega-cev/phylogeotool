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
		super("application/pdf", fileName, parent);
		this.graphicFormat = graphicFormat;
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	
	@Override
	public void handleRequest(WebRequest request, WebResponse response) {
		if (graphicFormat == null) {
			// NEXUS
			response.setContentType("application/nexus");
			this.suggestFileName(getFileName() + ".nxs");
		} else {
			if (graphicFormat.equals(GraphicFormat.PDF)) {
				response.setContentType("application/pdf");
				this.suggestFileName(getFileName() + ".pdf");
			} else if (graphicFormat.equals(GraphicFormat.PNG)) {
				response.setContentType("image/png");
				this.suggestFileName(getFileName() + ".png");
			} else if (graphicFormat.equals(GraphicFormat.SVG)) {
				response.setContentType("image/svg+xml");
				this.suggestFileName(getFileName() + ".svg");
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