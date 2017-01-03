package be.kuleuven.rega.form;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WButtonGroup;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WRadioButton;

public class MyButtonGroupColorTree extends WButtonGroup {

	private WRadioButton radioYes;
	private WRadioButton radioNo;
	private boolean colorTree = true;
	
	public MyButtonGroupColorTree(WDialog wDialog) {
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
					setColorTree(true);
				} else {
					setColorTree(false);
				}
//				wDownloadResource.dataChanged().trigger();
			}
		});
	    
	}
	
	private void setColorTree(boolean colorTree) {
		this.colorTree = colorTree;
	}
	
	public boolean isTreeColored() {
		return this.colorTree;
	}
	
	public WRadioButton getRadioYes() {
		return this.radioYes;
	}
	
	public WRadioButton getRadioNo() {
		return this.radioNo;
	}
	
}
