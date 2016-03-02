package be.kuleuven.rega.form;

import be.kuleuven.rega.webapp.widgets.WDownloadResource;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WButtonGroup;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WRadioButton;

public class MyButtonGroupColorTree extends WButtonGroup {

	private WRadioButton radioYes;
	private WRadioButton radioNo;
	
	public MyButtonGroupColorTree(WDialog wDialog, final WDownloadResource wDownloadResource) {
		super(wDialog.getContents());
		
		radioYes = new WRadioButton("Yes");
	    this.addButton(radioYes);
	    
	    radioNo = new WRadioButton("No");
	    this.addButton(radioNo);
	    
	    this.setSelectedButtonIndex(0);
	    
	    checkedChanged().addListener(wDialog, new Signal.Listener() {
			@Override
			public void trigger() {
				if(getCheckedButton().getText().equals("Yes")) {
					wDownloadResource.setColorTree(true);
				} else {
					wDownloadResource.setColorTree(false);
				}
				wDownloadResource.dataChanged().trigger();
			}
		});
	    
	}
	
	public WRadioButton getRadioYes() {
		return this.radioYes;
	}
	
	public WRadioButton getRadioNo() {
		return this.radioNo;
	}
	
}
