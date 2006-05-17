/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Dimension2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;

import javax.swing.JFormattedTextField;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;

/** UI field that allows input of a dimension.
 * 
 * Input may either be 1-D, or 2-D.
 * In case of 1-D, the dimension object returned has same value for width and height.
 * 
 * Register this class as a listener on a DecSexToggle for it to automatically adjust <b>input & display</b> 
 * units between decimal degrees and sexagesimal. The internal model always maintains the value as decimal degrees.
 * 
 * @author Noel Winstanley
 * @since May 16, 20068:41:44 AM
 */
public class DimensionTextField extends JFormattedTextField implements DecSexListener {
	
	/** construct a dimension field, whose value is initialized to 0.1,0.1 */ 
	public DimensionTextField() {
		this(new DoubleDimension(0.1,Double.NaN));
	}
	
	/** construct a dimension field, providing a starting value */
	public DimensionTextField(DoubleDimension dim) {
		super();
		setFormatterFactory(new AbstractFormatterFactory() {
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return isDecimal ? decimal : sexa;
			}
		});
		setValue(dim);
	}
	
	private boolean isDecimal = true;
	protected final AbstractFormatter decimal = new DecimalDimensionFormatter();
	protected final AbstractFormatter sexa = new SexagesimalDimensionFormatter();

	protected class DecimalDimensionFormatter extends AbstractFormatter {

		private NumberFormat nfd = NumberFormat.getNumberInstance();
		{
			nfd.setGroupingUsed(false);
			nfd.setMinimumFractionDigits(3);
			nfd.setMaximumFractionDigits(6);
		}
		public Object stringToValue(String arg0) throws ParseException {
			String[] nums = StringUtils.split(arg0,',');
			return new DoubleDimension(
					nfd.parse(nums[0]).doubleValue()
					,nums.length > 1 ? nfd.parse(nums[1]).doubleValue() : Double.NaN 
					);
		}

		public String valueToString(Object arg0) throws ParseException {
			DoubleDimension dim = (DoubleDimension)arg0;
			if (dim == null) {
				return null;
			}
			if (Double.isNaN(dim.getHeight())) {
				return nfd.format( dim.getWidth());
			} else {
				return nfd.format(dim.getWidth()) + "," + nfd.format(dim.getHeight());
			}
		}
	}
	
	protected class SexagesimalDimensionFormatter extends AbstractFormatter {

		public Object stringToValue(String arg0) throws ParseException {
			String[] nums = StringUtils.split(arg0,',');
			return new DoubleDimension (
					PositionUtils.sexagesimalRaToDecimal(nums[0])
					,nums.length > 1 ? PositionUtils.sexagesimalDecToDecimal(nums[1]) : Double.NaN
							);
		}

		public String valueToString(Object arg0) throws ParseException {
			DoubleDimension dim = (DoubleDimension)arg0;
			if (dim == null) {
				return null;
			}
			if (Double.isNaN(dim.getHeight())) {
				return PositionUtils.decimalDecToSexagesimal(dim.getWidth());
			} else {
				return PositionUtils.decimalToSexagesimal(dim.getWidth(),dim.getHeight());
				
			}
		}
	}
	
	
	/** return a copy of the dimension this field represents
	 * 
	 * @return a dimension in decimal degrees.
	 * width - ra size
	 * height - dec size.
	 */
	public Dimension2D getDimension() {
		DoubleDimension d = (DoubleDimension)((DoubleDimension)getValue()).clone();
		if (Double.isNaN(d.getHeight())) {
			d.setSize(d.getWidth(),d.getWidth());
		}
		return d;
	}
	/** set the internal model (decimal degrees) - same value for width and height */
	public void setDimension(double single) {
		setDimension(single,Double.NaN);
	}
	
	/** set the internal model (decimal degrees) */
	public void setDimension(double x,double y) {
		setValue(new DoubleDimension(x,y));
	}
	
	/** set the internal model (decimal degrees) */
	public void setDimension(Dimension2D dim) {
		setValue(new DoubleDimension(dim.getWidth(),dim.getHeight()));
	}
	
	/** set the internal model (decimal degrees)
	 * 
	 * @param s string in format width,height  - where both width and height are decimal degrees.
	 * @throws ParseException if string cannot be parsed
	 */
	public void setDimension(String s) throws ParseException {
		setValue(getFormatter().stringToValue(s));
	}
	/** listener interface to a DecSexToggle 
	 * 
	 * call to set display to degrees
	 * @param e ignored
	 * */
	public void degreesSelected(EventObject e) {
		isDecimal= true;
		setValue(getValue()); //forces display to update
	}

	/** part of the listener interface to a DecSexToggle 
	 * 
	 * call to set display to sexagesimal
	 * @param e ignored
	 * */
	public void sexaSelected(EventObject e) {
		isDecimal= false;
		setValue(getValue()); // forces display to update.
	}

}
