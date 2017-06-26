package be.kuleuven.rega.form;

import be.kuleuven.rega.enums.SequenceExportFormat;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;

public class MyComboBoxSequencesExportFormat extends WComboBox {

	private String[] exportFormats = {"FASTA"};
	private SequenceExportFormat sequenceExportFormat = SequenceExportFormat.FASTA;
	
	public MyComboBoxSequencesExportFormat(WDialog dialog) {
		super(dialog.getContents());
		
		for(int i = 0; i < exportFormats.length; i++) {
			this.addItem(exportFormats[i]);
		}

		this.changed().addListener(dialog, new Signal.Listener() {
			@Override
			public void trigger() {
				if(getCurrentText().equals("FASTA")) {
					sequenceExportFormat = SequenceExportFormat.FASTA;
				} else {
					sequenceExportFormat = null;
				}
				
				setSequenceExportFormat(sequenceExportFormat);
			}
		});
	}
	
	private void setSequenceExportFormat(SequenceExportFormat sequenceExportFormat) {
		this.sequenceExportFormat = sequenceExportFormat;
	}
	
	public SequenceExportFormat getSequenceExportFormat() {
		return this.sequenceExportFormat;
	}
}
