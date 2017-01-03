package be.kuleuven.rega.webapp.widgets;

import be.kuleuven.rega.webapp.Main;
import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;

public class WConfirmationDialog extends WDialog {
	
	private WPushButton ok;
	private WText content;
	private WVBoxLayout wContent;
	private WImage wImage;
	
	public WConfirmationDialog(String title, String content, WWidget... extraWidgets) {
		super(title);
		this.setWidth(new WLength(500));

		wImage = new WImage(new WLink(Main.getApp().getServletContext().getContextPath().concat("/images/info.png")));
		wImage.setMaximumSize(new WLength(50), new WLength(50));
		wImage.setWidth(new WLength(50));
		wImage.setHeight(new WLength(50));
		
		wContent = new WVBoxLayout();
		this.content = new WText(content);
		this.content.setMargin(8, Side.Top);
		this.content.setMargin(10, Side.Left);
		
		if(extraWidgets.length == 0) {
			wContent.addWidget(this.content);
		} else {
			wContent.addWidget(this.content);
			extraWidgets[0].setMargin(10, Side.Left);
			wContent.addWidget(extraWidgets[0]);
		}
			
		this.ok = new WPushButton("Cancel");
		this.ok.setMaximumSize(new WLength(50), new WLength(30));
		// TODO: Improve this part to put the Exit button in the middle by not using margin
		this.ok.setMargin(200, Side.Left);
		
		WHBoxLayout whBoxLayout = new WHBoxLayout();
		whBoxLayout.addWidget(wImage);
		whBoxLayout.addLayout(wContent);
		
		WVBoxLayout wvBoxLayout = new WVBoxLayout();
		wvBoxLayout.addItem(whBoxLayout);
		wvBoxLayout.addWidget(this.ok);
		this.ok.hide();
		
		this.getContents().setLayout(wvBoxLayout);
	}
	
	public void addWidget(WWidget wWidget) {
		wImage.setOffsets(new WLength(wImage.getMargin(Side.Top).getValue() + 20));
		wWidget.setMargin(10, Side.Left);
		wContent.addWidget(wWidget);
	}
	
	public WPushButton getOkButton() {
		return this.ok;
	}
}
