package be.kuleuven.rega.form;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WButtonGroup;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WRadioButton;

public class MyButtonGroupNodeTips extends WButtonGroup {

	private WRadioButton radioYes;
	private WRadioButton radioNo;
	private boolean showTips = false;
	
	public MyButtonGroupNodeTips(WDialog wDialog) {
		super(wDialog.getContents());
		
		radioYes = new WRadioButton("Yes");
	    this.addButton(radioYes);
	    
	    radioNo = new WRadioButton("No");
	    this.addButton(radioNo);
	    
	    this.setSelectedButtonIndex(1);
	    
	    checkedChanged().addListener(wDialog, new Signal.Listener() {
			@Override
			public void trigger() {
				if(getCheckedButton().getText().equals("Yes")) {
					setShowTips(true);
				} else {
					setShowTips(false);
				}
//				wDownloadResource.dataChanged().trigger();
			}
		});
	    
	}
	
	private void setShowTips(boolean showTips) {
		this.showTips = showTips;
	}
	
	public boolean getShowTips() {
		return this.showTips;
	}
	
	public WRadioButton getRadioYes() {
		return this.radioYes;
	}
	
	public WRadioButton getRadioNo() {
		return this.radioNo;
	}
	
}
