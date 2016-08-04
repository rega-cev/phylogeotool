package be.kuleuven.rega.webapp.widgets;

/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.webtoolkit.jwt.PenCapStyle;
import eu.webtoolkit.jwt.PenJoinStyle;
import eu.webtoolkit.jwt.WBrush;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPen;
import eu.webtoolkit.jwt.chart.WChartPalette;
import eu.webtoolkit.jwt.chart.WStandardPalette;

public class WStandardPaletteMine implements WChartPalette {
	private static Logger logger = LoggerFactory.getLogger(WStandardPalette.class);
	private WColor secondColor = new WColor(255,255,255);
	/**
	 * Creates a standard palette of a particular flavour.
	 */
	public WStandardPaletteMine() {
		super();
	}

	public WBrush getBrush(int index) {
		return new WBrush(this.color(index));
	}

	public WPen getBorderPen(int index) {
		WPen p = new WPen(new WColor(0x44, 0x44, 0x44));
		p.setCapStyle(PenCapStyle.SquareCap);
		return p;
	}

	public WPen getStrokePen(int index) {
		WPen p = new WPen(this.color(index));
		p.setWidth(new WLength(2));
		p.setJoinStyle(PenJoinStyle.BevelJoin);
		p.setCapStyle(PenCapStyle.SquareCap);
		return p;
	}

	public WColor getFontColor(int index) {
		WColor c = this.color(index);
		if (c.getRed() + c.getGreen() + c.getBlue() > 3 * 128) {
			return WColor.black;
		} else {
			return WColor.white;
		}
	}

	/**
	 * Returns the color for the given index.
	 */
	public WColor color(int index) {
//		int v = 255 - index % 8 * 32;
//		return new WColor(v, v, v);
		switch(index) {
			case 0: return secondColor;
			case 1: if(!this.secondColor.equals(WColor.white)) return new WColor(255,255,255);
			default: return new WColor(0,0,0);
		}
	}

	public void setSecondColor(WColor color) {
		this.secondColor = color;
	}

//	private static int[][] standardColors = { { 0xC3D9FF, 0xEEEEEE, 0xFFFF88, 0xCDEB8B, 0x356AA0, 0x36393D, 0xF9F7ED, 0xFF7400 },
//			{ 0xFF1A00, 0x4096EE, 0xFF7400, 0x008C00, 0xFF0084, 0x006E2E, 0xF9F7ED, 0xCC0000 },
//			{ 0xB02B2C, 0x3F4C6B, 0xD15600, 0x356AA0, 0xC79810, 0x73880A, 0xD01F3C, 0x6BBA70 } };
}
