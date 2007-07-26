package org.astrogrid.desktop.modules.ui.comp;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import junit.framework.TestCase;

/**
 * @author   Mark Taylor
 * @since    25 Jul 2007
 */
public class RadiusTextFieldUnitTest extends TestCase {

    private final RadiusTextField rtf = new RadiusTextField();
    private final AbstractFormatter degree = rtf.degree;
    private final AbstractFormatter arcsec = rtf.arcsec;
    private static final double ARC_SEC = 1.0 / 60.0 / 60.0;

    public void testConversions() throws Exception {
        assertEquals(new Double(0.01), degree.stringToValue("0.01"));
        assertEquals(new Double(0.01), degree.stringToValue(" 0.01 "));
        assertEquals(new Double(0.0001), degree.stringToValue("0.0001"));
        assertEquals(new Double(1.), degree.stringToValue("1"));

        assertEquals(new Double(4.5*ARC_SEC), arcsec.stringToValue("4.5"));
        assertEquals(new Double(4.0*ARC_SEC), arcsec.stringToValue(" 4 "));
        assertEquals(new Double(1.0), arcsec.stringToValue("3600.0"));
        assertEquals(new Double(1.0/3600), arcsec.stringToValue("01"));
    }

    public void testBadFormat() throws Exception {
        assertParseError(degree, "five");
        assertParseError(degree, "-1");
        assertParseError(degree, "1,3");

        assertParseError(arcsec, "five");
        assertParseError(arcsec, "-1");
        assertParseError(arcsec, "1,3");
    }

    public void testRoundTrip() throws Exception {
        assertRoundTrip(degree, "0.01");
        assertRoundTrip(arcsec, "0.01");
    }

    private void assertRoundTrip(AbstractFormatter fmt, String r) throws Exception {
        assertTrue(sameStringValue(r, fmt.valueToString(fmt.stringToValue(r))));

        boolean isDeg;
        if (fmt == degree) {
            isDeg = true;
        }
        else if (fmt == arcsec) {
            isDeg = false;
        }
        else {
            fail("what is it then?");
            return;
        }

        setFormat(rtf, isDeg);
        rtf.setValue(fmt.stringToValue(r));
        assertTrue(sameStringValue(r, rtf.getText()));
        setFormat(rtf, ! isDeg);
        assertTrue(! sameStringValue(r, rtf.getText()));
        setFormat(rtf, isDeg);
        assertTrue(sameStringValue(r, rtf.getText()));
    }

    private static boolean sameStringValue(String r1, String r2) {
        return Math.abs(Double.parseDouble(r1) / Double.parseDouble(r2) - 1) < 1e-14;
    }

    private void assertParseError(AbstractFormatter fmt, String xy) throws Exception {
        try {
            fmt.stringToValue(xy);
            fail("Expected " + xy + " to throw a ParseException");
        }
        catch (ParseException e) {
            // good
        }
    }

    private static void setFormat(DecSexToggle.DecSexListener l, boolean isDegree) {
        if (isDegree) {
            l.degreesSelected(null);
        }
        else {
            l.sexaSelected(null);
        }
    }
}
