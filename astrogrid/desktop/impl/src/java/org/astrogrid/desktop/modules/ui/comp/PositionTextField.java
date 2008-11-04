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
 * UI field that allows input of a point position.
 * 
 * 
 * Register this class as a listener on a DecSexToggle for it to automatically adjust <b>input & display</b> 
 * units between decimal degrees and sexagesimal. The internal model always maintains the value as decimal degrees.
 * 
 */ 
public class PositionTextField extends JFormattedTextField implements DecSexToggle.DecSexListener{

    /**
     * name of the property to monitor to be notified when the value of this field changes.
     */
    public static final String VALUE_PROPERTY = "value";

    
    protected class DecimalPositionFormatter extends AbstractFormatter {

        // Use fixed number formats.  Default-locale-sensitive ones are 
        // dangerous here - we don't want one which uses a comma for a 
        // decimal point since we are using comma as a delimiter.
        private final NumberFormat rNfd;
        private final NumberFormat dNfd;
        {
            final DecimalFormatSymbols syms = new DecimalFormatSymbols(Locale.UK);
            rNfd = new DecimalFormat("0.000000", syms);
            dNfd = new DecimalFormat("+0.000000;-0.000000", syms);
        }

		public Object stringToValue(final String arg0) throws ParseException {
			final String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}
            final String sx = nums[0].trim();
            final String sy = nums[1].trim();

            // Double.parseDouble is more lenient than using the NumberFormat
            // parser; for instance it allows optional leading plus signs.
            double x;
            try {
                x = Double.parseDouble(sx);
            }
            catch (final NumberFormatException e) {
                throw new ParseException(sx + " is not numeric", 0);
            }
            double y;
            try {
                y = Double.parseDouble(sy);
            }
            catch (final NumberFormatException e) {
                throw new ParseException(sy + " is not numeric", 0);
            }
            return new Point2D.Double(x,y);
		}

		public String valueToString(final Object arg0) throws ParseException {
			final Point2D dim = (Point2D)arg0;
			if (dim == null) {
				return null;
			}
            return rNfd.format(dim.getX()) + "," + dNfd.format(dim.getY());
		}
    }
    
    protected class SexagesimalPositionFormatter extends AbstractFormatter {

    	public Object stringToValue(final String arg0) throws ParseException {
			final String[] nums = arg0.split(",");
			if (nums.length != 2) {
				throw new ParseException("Expected 2 numbers",0);
			}
        //unused    String sx = nums[0].trim();
        //    String sy = nums[1].trim();
            try {
                return new Point2D.Double(PositionUtils.sexagesimalRaToDecimal(nums[0].trim()),
                                          PositionUtils.sexagesimalDecToDecimal(nums[1].trim()));
            }
            catch (final NumberFormatException e) {
                throw new ParseException(e.getMessage(), 0);
            }
		}

		public String valueToString(final Object arg0) throws ParseException {
			final Point2D dim = (Point2D)arg0;
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
    public PositionTextField(final Point2D p) {
        super();
        degreesSelected(null);
        setFormatterFactory(new AbstractFormatterFactory() {
			public AbstractFormatter getFormatter(final JFormattedTextField tf) {
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
    public void setPosition(final double ra,final double dec) {
    	setValue(new Point2D.Double(ra,dec));
    }
    
    /** set the internal model (decimal degrees) */
    public void setPosition(final Point2D pos) {
    	setValue(pos);
    }
    /** set the internal model (decimal degrees)
     * 
     * @param s string in format width,height - where both width and heigt are in decimal degrees
     * @throws ParseException if string cannot be parsed
     */
    public void setPosition(final String s) throws ParseException {
    	setValue(getFormatter().stringToValue(s));
    }
   
    /** listener interface to a DecSexToggle 
	 * 
	 * call to set display to degrees
	 * @param e ignored
	 * */
	public void degreesSelected(final EventObject e) {
		isDecimal = true;
		setValue(getValue()); // forces display to update.
	}
	/** part of the listener interface to a DecSexToggle 
	 * 
	 * call to set display to sexagesimal
	 * @param e ignored
	 * */
	public void sexaSelected(final EventObject e) {
		isDecimal = false;
		setValue(getValue()); // forces display to update
	}    
}
