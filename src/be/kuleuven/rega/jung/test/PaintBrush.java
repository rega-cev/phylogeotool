package be.kuleuven.rega.jung.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.webtoolkit.jwt.WBrush;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

class PaintBrush extends WPaintedWidget {
	private static Logger logger = LoggerFactory.getLogger(PaintBrush.class);

	public PaintBrush(WContainerWidget parent) {
		super(parent);
		this.end_ = 100;
		this.resize(new WLength(200), new WLength(60));
	}

	public PaintBrush() {
		this((WContainerWidget) null);
	}

	public void setEnd(int end) {
		this.end_ = end;
		this.update();
	}

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		painter.setBrush(new WBrush(WColor.blue).clone());
		painter.drawRect(0, 0, this.end_, 50);
	}

	private int end_;
}