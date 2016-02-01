package be.kuleuven.rega.form;

import be.kuleuven.rega.webapp.widgets.WDownloadResource;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import figtree.application.GraphicFormat;

public class MyComboBoxExportFormat extends WComboBox {

	private String[] exportFormats = {"PDF","PNG","NEXUS","SVG"};
	
	public MyComboBoxExportFormat(WDialog dialog, WDownloadResource wDownloadResource) {
		super(dialog.getContents());
		
		for(int i = 0; i < exportFormats.length; i++) {
			this.addItem(exportFormats[i]);
		}

		this.changed().addListener(dialog, new Signal.Listener() {
			@Override
			public void trigger() {
				GraphicFormat graphicFormat = null;
				if(getCurrentText().equals("PDF")) {
					graphicFormat = GraphicFormat.PDF;
				} else if(getCurrentText().equals("PNG")) {
					graphicFormat = GraphicFormat.PNG;
				} else if(getCurrentText().equals("SVG")) {
					graphicFormat = GraphicFormat.SVG;
				}
				
				wDownloadResource.setGraphicFormat(graphicFormat);
				wDownloadResource.dataChanged().trigger();
			}
		});
	}
	
	
}
