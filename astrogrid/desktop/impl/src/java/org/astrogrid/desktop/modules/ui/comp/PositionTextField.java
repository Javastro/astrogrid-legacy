package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.JFormattedTextField;

/**
 * UI field that allows input of a point position
 * 
 * 
 * Register this class as a listener on a DecSexToggle for it to automatically adjust <b>input & display</b> 
 * units between decimal degrees and sexagesimal. The internal model always maintains the value as decimal degrees.
 * 
 */ 
public class PositionTextField extends JFormattedTextField implements DecSexToggle.DecSexListener{


    
    protected class DecimalPositionFormatter extends AbstractFormatter {

        // Use fixed number formats.  Default-locale-sensitive ones are 
        // dangerous here - we don't want one which uses a comma for a 
        // decimal point since we are using comma as a delimiter.
        private final NumberFormat rNfd;
        private final NumberFormat dNfd;
        {
            DecimalFormatSymbols syms = new DecimalFormatSymbols(Locale.UK);
            rNfd = new DecimalFormat("0.000000", syms);
            dNfd = new DecimalFormat("+0.000000;-0.000000", syms);
        }

		public Object stringToValue(String arg0) throws ParseException {
			String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}
            String sx = nums[0].trim();
            String sy = nums[1].trim();

            // Double.parseDouble is more lenient than using the NumberFormat
            // parser; for instance it allows optional leading plus signs.
            double x;
            try {
                x = Double.parseDouble(sx);
            }
            catch (NumberFormatException e) {
                throw new ParseException(sx + " is not numeric", 0);
            }
            double y;
            try {
                y = Double.parseDouble(sy);
            }
            catch (NumberFormatException e) {
                throw new ParseException(sy + " is not numeric", 0);
            }
            return new Point2D.Double(x,y);
		}

		public String valueToString(Object arg0) throws ParseException {
			Point2D dim = (Point2D)arg0;
			if (dim == null) {
				return null;
			}
            return rNfd.format(dim.getX()) + "," + dNfd.format(dim.getY());
		}
    }
    
    protected class SexagesimalPositionFormatter extends AbstractFormatter {

    	public Object stringToValue(String arg0) throws ParseException {
			String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}
            String sx = nums[0].trim();
            String sy = nums[1].trim();
            try {
                return new Point2D.Double(PositionUtils.sexagesimalRaToDecimal(nums[0].trim()),
                                          PositionUtils.sexagesimalDecToDecimal(nums[1].trim()));
            }
            catch (NumberFormatException e) {
                throw new ParseException(e.getMessage(), 0);
            }
		}

		public String valueToString(Object arg0) throws ParseException {
			Point2D dim = (Point2D)arg0;
			if (dim == null) {
				return null;
			}
            return PositionUtils.decimalToSexagesimal(dim.getX(),dim.getY());
		}
    }
    
    /**
	Construct a new position text field, intiialized to 0.0,0.0
     */
    public PositionTextField() {
    	this(new Point2D.Double(0,0));
    }
    
    /** construct a new position text field
     * 
     * @param p value to initialize to (decimal degrees)
     */
    public PositionTextField(Point2D p) {
        super();
        degreesSelected(null);
        setFormatterFactory(new AbstractFormatterFactory() {
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				return isDecimal ? decimal : sexa;
			}
        });
        setValue(p);
        setToolTipText("Position (187.27,+2.05 or 12:29:06.00,+02:03:08.60)");
        
    }
    
    
    private boolean isDecimal = true;
    protected AbstractFormatter decimal = new DecimalPositionFormatter();
    protected AbstractFormatter sexa = new SexagesimalPositionFormatter();
    

    

    /** return a copy of the position this field currently points at
     * @return position in decimal degrees 
     * <ul><li>x - ra value
     * <li>y - dec value
     * </ul> */
    public Point2D getPosition() {
    	return (Point2D)((Point2D)getValue()).clone();
    }
    
    /** set the internal model (decimal degrees)
     * 
     * @param ra right ascension
     * @param dec declination
     */
    public void setPosition(double ra,double dec) {
    	setValue(new Point2D.Double(ra,dec));
    }
    
    /** set the internal model (decimal degrees) */
    public void setPosition(Point2D pos) {
    	setValue(pos);
    }
    /** set the internal model (decimal degrees)
     * 
     * @param s string in format width,height - where both width and heigt are in decimal degrees
     * @throws ParseException if string cannot be parsed
     */
    public void setPosition(String s) throws ParseException {
    	setValue(getFormatter().stringToValue(s));
    }
   
    /** listener interface to a DecSexToggle 
	 * 
	 * call to set display to degrees
	 * @param e ignored
	 * */
	public void degreesSelected(EventObject e) {
		isDecimal = true;
		setValue(getValue()); // forces display to update.
	}
	/** part of the listener interface to a DecSexToggle 
	 * 
	 * call to set display to sexagesimal
	 * @param e ignored
	 * */
	public void sexaSelected(EventObject e) {
		isDecimal = false;
		setValue(getValue()); // forces display to update
	}    
}
