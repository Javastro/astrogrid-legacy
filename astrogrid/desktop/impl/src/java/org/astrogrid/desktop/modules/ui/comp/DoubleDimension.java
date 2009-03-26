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
	
	@Override
    public double getHeight() {
		return height;
	}
	
	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
    public double getWidth() {
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
    public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	// for serialization.
	public DoubleDimension() {
	}
	
	public DoubleDimension(double width, double height) {
		super();
		this.height = height;
		this.width = width;
	}

	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.height);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.width);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DoubleDimension other = (DoubleDimension) obj;
		if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height))
			return false;
		if (Double.doubleToLongBits(this.width) != Double.doubleToLongBits(other.width))
			return false;
		return true;
	}
	
	@Override
    public String toString() {
		return width + ", " + height;
	}
}