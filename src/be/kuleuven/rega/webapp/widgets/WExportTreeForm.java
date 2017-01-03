package be.kuleuven.rega.webapp.widgets;

import be.kuleuven.rega.form.MyButtonGroupColorTree;
import be.kuleuven.rega.form.MyButtonGroupNodeTips;
import be.kuleuven.rega.form.MyComboBoxExportFormat;
import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.TextFormat;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WWidget;

public class WExportTreeForm {
	
	private WTable table = null;
	private MyButtonGroupColorTree wMyButtonGroupColorTree;
	private MyButtonGroupNodeTips wMyButtonGroupNodeTips;
	private MyComboBoxExportFormat wMyComboBoxExportFormat;
	
	public WExportTreeForm(WDialog dialog, WPushButton button, WPushButton cancel) {
		dialog.setWidth(new WLength(340));
		table = new WTable(dialog.getContents());
	    table.setHeaderCount(1);
	    table.setWidth(new WLength("100%"));
	    table.setStyleClass("tableDialog");
//	    table.setMargin(new WLength(-15), EnumSet.of(Side.Left, Side.Top));
	    
	    /*
	     * Color Tree
	     */
	    WLabel labelColorTree = new WLabel("<b>Color Tree</b>");
	    labelColorTree.setTextFormat(TextFormat.XHTMLText);
	    table.getElementAt(1, 1).addWidget(labelColorTree);
	    wMyButtonGroupColorTree = new MyButtonGroupColorTree(dialog);
	    table.getElementAt(1, 2).addWidget(wMyButtonGroupColorTree.getRadioYes());
	    table.getElementAt(1, 3).addWidget(wMyButtonGroupColorTree.getRadioNo());
	    
	    /*
	     * Show node tips
	     */
	    
	    WLabel labelNodeTips = new WLabel("<b>Show node tips</b>");
	    labelNodeTips.setTextFormat(TextFormat.XHTMLText);
	    table.getElementAt(2, 1).addWidget(labelNodeTips);
	    wMyButtonGroupNodeTips = new MyButtonGroupNodeTips(dialog);
	    table.getElementAt(2, 2).addWidget(wMyButtonGroupNodeTips.getRadioYes());
	    table.getElementAt(2, 3).addWidget(wMyButtonGroupNodeTips.getRadioNo());
	    
	    button.setWidth(new WLength(60));
	    cancel.setWidth(new WLength(60));
	    
	    wMyComboBoxExportFormat = new MyComboBoxExportFormat(dialog);
	    wMyComboBoxExportFormat.setWidth(new WLength(130));
	    WLabel wLabelDesiredOutputForm = new WLabel("<b>Output format</b>");
	    wLabelDesiredOutputForm.setTextFormat(TextFormat.XHTMLText);
//	    wLabelDesiredOutputForm.setStyleClass("bold");
	    table.getElementAt(3, 1).addWidget(wLabelDesiredOutputForm);
//	    table.getElementAt(3, 3).setContentAlignment(AlignmentFlag.AlignRight);
	    table.getElementAt(3, 2).addWidget(wMyComboBoxExportFormat);
	    table.getElementAt(3, 2).setColumnSpan(2);

	    table.getElementAt(4, 3).setContentAlignment(AlignmentFlag.AlignRight);
	    table.getElementAt(4, 3).addWidget(button);
	    table.getElementAt(4, 3).addWidget(cancel);
//	    table.getElementAt(4, 2).setColumnSpan(2);
	    
	}
	
	public MyButtonGroupColorTree getwMyButtonGroupColorTree() {
		return wMyButtonGroupColorTree;
	}

	public MyButtonGroupNodeTips getwMyButtonGroupNodeTips() {
		return wMyButtonGroupNodeTips;
	}

	public MyComboBoxExportFormat getwMyComboBoxExportFormat() {
		return wMyComboBoxExportFormat;
	}

	public WWidget getWidget() {
		return table;
	}

}
