/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Dimension2D;

public class DoubleDimension extends Dimension2D {

	private double height;
	private double width;
	
	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public DoubleDimension(double width, double height) {
		super();
		this.height = height;
		this.width = width;
	}
}