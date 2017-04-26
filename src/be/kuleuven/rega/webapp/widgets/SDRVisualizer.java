package be.kuleuven.rega.webapp.widgets;

import java.io.File;

import be.kuleuven.rega.phylogeotool.settings.Settings;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget.Overflow;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;

public class SDRVisualizer {
	
	public SDRVisualizer(final WDialog wDialog, String clusterId) {
		wDialog.setWidth(new WLength(504));
		wDialog.setHeight(new WLength(504));
		wDialog.getContents().setOverflow(Overflow.OverflowAuto);
		wDialog.setMaximumSize(new WLength(504), new WLength(504));
		wDialog.setClosable(true);
		
		WPushButton wPushButton = new WPushButton("OK");
		wPushButton.setWidth(new WLength(75));
		wPushButton.clicked().addListener(wDialog,
	            new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent e1) {
            	wDialog.reject();
            }
        });

		File sdr = new File(Settings.getInstance().getStatisticalImagesPath() + File.separator + clusterId + "_sdr.png");
		File sgolay = new File(Settings.getInstance().getStatisticalImagesPath() + File.separator + clusterId + "_sgolay.png");
		File firstDerivative = new File(Settings.getInstance().getStatisticalImagesPath() + File.separator + clusterId + "_firstderivative.png");
		File secondDerivative = new File(Settings.getInstance().getStatisticalImagesPath() + File.separator + clusterId + "_secondDerivative.png");
		
		if(sdr.exists() && !sdr.isDirectory()) {
			wDialog.getContents().addWidget(new WLabel("<b>SDR</b>"));
			wDialog.getContents().addWidget(new WImage(new WLink(new WFileResource("PNG", sdr.getAbsolutePath()))));
		}
		
		if(sgolay.exists() && !sgolay.isDirectory()) {
			wDialog.getContents().addWidget(new WLabel("<b>Sgolay</b>"));
			wDialog.getContents().addWidget(new WImage(new WLink(new WFileResource("PNG", sgolay.getAbsolutePath()))));
		}
		
		if(firstDerivative.exists() && !firstDerivative.isDirectory()) {
			wDialog.getContents().addWidget(new WLabel("<b>First Derivative</b>"));
			wDialog.getContents().addWidget(new WImage(new WLink(new WFileResource("PNG", firstDerivative.getAbsolutePath()))));
		}
		
		if(secondDerivative.exists() && !secondDerivative.isDirectory()) {
			wDialog.getContents().addWidget(new WLabel("<b>Second Derivative</b>"));
			wDialog.getContents().addWidget(new WImage(new WLink(new WFileResource("PNG", secondDerivative.getAbsolutePath()))));
		}
		
		wDialog.getContents().addWidget(wPushButton);
		wDialog.setPopup(true);
		wDialog.rejectWhenEscapePressed();
		wDialog.show();
	}

}
