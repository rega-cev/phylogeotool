package be.kuleuven.rega.form;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import figtree.application.GraphicFormat;

public class MyComboBoxExportFormat extends WComboBox {

	private String[] exportFormats = {"PDF","PNG","NEXUS","SVG"};
	private GraphicFormat graphicFormat = GraphicFormat.PDF;
	
	public MyComboBoxExportFormat(WDialog dialog) {
		super(dialog.getContents());
		
		for(int i = 0; i < exportFormats.length; i++) {
			this.addItem(exportFormats[i]);
		}

		this.changed().addListener(dialog, new Signal.Listener() {
			@Override
			public void trigger() {
				if(getCurrentText().equals("PDF")) {
					graphicFormat = GraphicFormat.PDF;
				} else if(getCurrentText().equals("PNG")) {
					graphicFormat = GraphicFormat.PNG;
				} else if(getCurrentText().equals("SVG")) {
					graphicFormat = GraphicFormat.SVG;
				} else if(getCurrentText().equals("NEXUS")) {
					graphicFormat = null;
				}
				
				setGraphicFormat(graphicFormat);
//				wDownloadResource.dataChanged().trigger();
			}
		});
	}
	
	private void setGraphicFormat(GraphicFormat graphicFormat) {
		this.graphicFormat = graphicFormat;
	}
	
	public GraphicFormat getGraphicFormat() {
		return this.graphicFormat;
	}
}
