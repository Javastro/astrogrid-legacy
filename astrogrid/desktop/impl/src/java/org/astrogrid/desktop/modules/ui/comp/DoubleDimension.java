/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Dimension2D;

/**
 * Implementation of the standard Dimension2D that uses doubles as data fields.
 * (Standard implementation only uses ints ).
 * Used within implementation of {@link DimensionTextField}
 * @author Noel Winstanley
 * @since May 17, 20065:54:11 PM
 */
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