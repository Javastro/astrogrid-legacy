package org.astrogrid.desktop.modules.ui.comp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.JFormattedTextField;

/** Allows input of a single value to be used as a search radius.
 * Register this class as a listener on a DecSexToggle for it to automatically
 * adjust <b>input and display</b> units between decimal degrees and
 * arc seconds.  The internal model always maintains the value as 
 * decimal degrees.
 *
 * @author   Mark Taylor
 * @since    25 Jul 2007
 */
public class RadiusTextField extends JFormattedTextField implements DecSexToggle.DecSexListener {

    private static final AbstractFormatter DEGREE_FORMATTER =
        new RadiusFormatter(1.0, new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.UK)));
    private static final AbstractFormatter ARCSEC_FORMATTER =
        new RadiusFormatter(60.0 * 60.0, new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.UK)));
    /**
     * name of the property to monitor to be notified when the value of this field changes.
     */
    public static final String VALUE_PROPERTY = "value";

    private final double radius;
    private boolean isDecimal = true;
    protected final AbstractFormatter degree = DEGREE_FORMATTER;
    protected final AbstractFormatter arcsec = ARCSEC_FORMATTER;

    /**
     * Construct a dimension field initialised to 0.01 degrees.
     */
    public RadiusTextField() {
        this(0.01);
    }

    /**
     * Construct a dimension field providing an initial value.
     *
     * @param  radius initial radius value in degrees
     */
    public RadiusTextField(final double radius) {
        this.radius = radius;
        setFormatterFactory(new AbstractFormatterFactory() {
            @Override
            public AbstractFormatter getFormatter(final JFormattedTextField tf) {
                return isDecimal ? degree : arcsec;
            }
        });
        setRadius(radius);
    }

    /**
     * Formatter for numeric values representing radii.
     */
    protected static class RadiusFormatter extends AbstractFormatter {
        private final double factor;
        private final NumberFormat nfd;

        /**
         * Constructor.
         *
         * @param   factor   multiplicative factor by which numeric values are
         *                   multiplied before being represented
         * @return  nfd      numeric formatter for represented values
         */
        RadiusFormatter(final double factor, final NumberFormat nfd) {
            this.factor = factor;
            this.nfd = nfd;
        }

        @Override
        public Object stringToValue(final String str) throws ParseException {
            double val;
            try {
                val = Double.parseDouble(str) / factor;
            }
            catch (final NumberFormatException e) {
                throw new ParseException(str + " is not a number", 0);
            }
            if (val < 0) {
                throw new ParseException(str + " < 0", 0);
            }
            return new Double(val);
        }

        @Override
        public String valueToString(final Object val) throws ParseException {
            if (val instanceof Number) {
                return nfd.format(((Number) val).doubleValue() * factor);
            }
            else {
                throw new ParseException(val + " is not a number", 0);
            }
        }
    }

    /**
     * Returns the current radius value.
     *
     * @return  non-negative radius value in degrees
     */
    public double getRadius() {
        final double radius = ((Number) getValue()).doubleValue();
        assert radius >= 0.0;
        return radius;
    }

    /**
     * Sets the current radius value.
     *
     * @param  r  radius value
     */
    public void setRadius(final double r) {
        setValue(new Double(r));
    }

    /**
     * Sets display to degrees.
     * Part of the listener interface to a DecSexToggle.
     *
     * @param  e  ignored
     */
    public void degreesSelected(final EventObject e) {
        isDecimal=true;
        setValue(getValue());  // forces display to update
    }

    /**
     * Sets display to arcseconds.
     * Part of the listener interface to DecSexToggle.
     *
     * @param  e  ignored
     */
    public void sexaSelected(final EventObject e) {
        isDecimal=false;
        setValue(getValue());  // forces display to update
    }

    /**
     * Utility method to format a radius value (degrees) as a value 
     * in arcseconds.
     *
     * @param  radius  value in degrees
     * @return   formatted string in arcseconds (no units shown)
     */
    public static String formatAsArcsec(final double radius) {
        try {
            return ARCSEC_FORMATTER.valueToString(new Double(radius));
        }
        catch (final ParseException e) {
            return "??";
        }
    }
}
