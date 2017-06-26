package be.kuleuven.rega.webapp.widgets;

import be.kuleuven.rega.form.MyComboBoxSequencesExportFormat;
import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.TextFormat;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WWidget;

public class WExportSequencesForm {
	
	private WTable table = null;
	private MyComboBoxSequencesExportFormat wMyComboBoxSequencesExportFormat;
	
	public WExportSequencesForm(WDialog dialog, WPushButton button, WPushButton cancel) {
		dialog.setWidth(new WLength(340));
		table = new WTable(dialog.getContents());
	    table.setHeaderCount(1);
	    table.setWidth(new WLength("100%"));
	    table.setStyleClass("tableDialog");
//	    table.setMargin(new WLength(-15), EnumSet.of(Side.Left, Side.Top));
	    
	    /*
	     * Sequence Export Format
	     */
	    wMyComboBoxSequencesExportFormat = new MyComboBoxSequencesExportFormat(dialog);
	    wMyComboBoxSequencesExportFormat.setWidth(new WLength(130));
	    WLabel wLabelDesiredOutputForm = new WLabel("<b>Sequence Export Format</b>");
	    wLabelDesiredOutputForm.setTextFormat(TextFormat.XHTMLText);
//	    wLabelDesiredOutputForm.setStyleClass("bold");
	    table.getElementAt(1, 1).addWidget(wLabelDesiredOutputForm);
//	    table.getElementAt(3, 3).setContentAlignment(AlignmentFlag.AlignRight);
	    table.getElementAt(1, 2).addWidget(wMyComboBoxSequencesExportFormat);
	    table.getElementAt(1, 2).setColumnSpan(2);

	    table.getElementAt(2, 3).setContentAlignment(AlignmentFlag.AlignRight);
	    table.getElementAt(2, 3).addWidget(button);
	    table.getElementAt(2, 3).addWidget(cancel);
//	    table.getElementAt(4, 2).setColumnSpan(2);
	    
	}
	
	public MyComboBoxSequencesExportFormat getwMyComboBoxSequenceExportFormat() {
		return wMyComboBoxSequencesExportFormat;
	}

	public WWidget getWidget() {
		return table;
	}

}
