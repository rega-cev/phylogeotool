package be.kuleuven.rega.webapp.widgets;

import be.kuleuven.rega.phylogeotool.settings.Settings;
import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WWidget;

public class WTreeViewerForm {

	private WTable table = null;
	
	public WTreeViewerForm(WDialog dialog, WPushButton downloadSequences, WPushButton downloadTree, WPushButton cancel) {
		table = new WTable(dialog.getContents());
	    table.setHeaderCount(1);
	    table.setWidth(new WLength("100%"));
//	    table.getElementAt(0, 1).addWidget(new WText("Export Tree Options"));
	    
	    /*
	     * Color Tree
	     */
	    
//	    table.getElementAt(1, 1).addWidget(new WText("Color Tree: "));
//	    MyButtonGroupColorTree colorTree = new MyButtonGroupColorTree(dialog, wDownloadResource);
//	    table.getElementAt(1, 2).addWidget(colorTree.getRadioYes());
//	    table.getElementAt(1, 3).addWidget(colorTree.getRadioNo());
	    
	    /*
	     * Show node tips
	     */
	    
//	    table.getElementAt(2, 1).addWidget(new WText("Show node tips: "));
//	    MyButtonGroupNodeTips showLabels = new MyButtonGroupNodeTips(dialog, wDownloadResource);
//	    table.getElementAt(2, 2).addWidget(showLabels.getRadioYes());
//	    table.getElementAt(2, 3).addWidget(showLabels.getRadioNo());
	    
	    /*
	     * Export format selector
	     */
	    
//	    table.getElementAt(3, 1).addWidget(new WText("Desired output format: "));
//	    WComboBox exportFormat = new MyComboBoxExportFormat(dialog, wDownloadResource);

	    // TODO: Must span 2 columns
//	    table.getElementAt(3, 2).addWidget(exportFormat);
//	    table.getElementAt(3, 3).addWidget(exportFormat);
	    
	    downloadSequences.setWidth(new WLength(140));
	    downloadTree.setWidth(new WLength(100));
	    cancel.setWidth(new WLength(60));
	    
	    table.getElementAt(1, 3).setContentAlignment(AlignmentFlag.AlignRight);
	    // Only give the option to download the sequences if it says so in global-conf file
	    if(Settings.getInstance().getSequenceDetails()) {
	    	table.getElementAt(1, 3).addWidget(downloadSequences);
	    }
	    table.getElementAt(1, 3).addWidget(downloadTree);
	    table.getElementAt(1, 3).addWidget(cancel);
	}
	
	public WWidget getWidget() {
		return table;
	}
	
}
