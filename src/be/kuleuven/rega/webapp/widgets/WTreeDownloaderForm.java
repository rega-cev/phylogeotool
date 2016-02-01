package be.kuleuven.rega.webapp.widgets;

import be.kuleuven.rega.form.MyButtonGroupColorTree;
import be.kuleuven.rega.form.MyButtonGroupNodeTips;
import be.kuleuven.rega.form.MyComboBoxExportFormat;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WWidget;

public class WTreeDownloaderForm {

	private WTable table = null;
	
	public WTreeDownloaderForm(WDialog dialog, WDownloadResource wDownloadResource, WPushButton button, WPushButton cancel) {
		table = new WTable(dialog.getContents());
	    table.setHeaderCount(1);
	    table.setWidth(new WLength("100%"));
	    table.getElementAt(0, 1).addWidget(new WText("Export Tree Options"));
	    
	    /*
	     * Color Tree
	     */
	    
	    table.getElementAt(1, 1).addWidget(new WText("Color Tree: "));
	    MyButtonGroupColorTree colorTree = new MyButtonGroupColorTree(dialog, wDownloadResource);
	    table.getElementAt(1, 2).addWidget(colorTree.getRadioYes());
	    table.getElementAt(1, 3).addWidget(colorTree.getRadioNo());
	    
	    /*
	     * Show node tips
	     */
	    
	    table.getElementAt(2, 1).addWidget(new WText("Show node tips: "));
	    MyButtonGroupNodeTips showLabels = new MyButtonGroupNodeTips(dialog, wDownloadResource);
	    table.getElementAt(2, 2).addWidget(showLabels.getRadioYes());
	    table.getElementAt(2, 3).addWidget(showLabels.getRadioNo());
	    
	    /*
	     * Export format selector
	     */
	    
	    table.getElementAt(3, 1).addWidget(new WText("Desired output format: "));
	    WComboBox exportFormat = new MyComboBoxExportFormat(dialog, wDownloadResource);

	    // TODO: Must span 2 columns
	    table.getElementAt(3, 2).addWidget(exportFormat);
//	    table.getElementAt(3, 3).addWidget(exportFormat);
	    
	    table.getElementAt(4, 1).addWidget(button);
	    table.getElementAt(4, 3).addWidget(cancel);
	}
	
	public WWidget getWidget() {
		return table;
	}
	
}
