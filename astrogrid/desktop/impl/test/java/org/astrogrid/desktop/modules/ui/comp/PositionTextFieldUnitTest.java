package org.astrogrid.desktop.modules.ui.comp;

import java.text.ParseException;
import javax.swing.JFormattedTextField.AbstractFormatter;

import junit.framework.TestCase;

/**
 * @author   Mark Taylor
 * @since 24 Jul 2007
 */
public class PositionTextFieldUnitTest extends TestCase {

    private final PositionTextField ptf = new PositionTextField();
    private final AbstractFormatter dec = ptf.decimal;
    private final AbstractFormatter sex = ptf.sexa;

    public void testConversions() throws Exception {
        assertEquals(dec.stringToValue("90,45"),
                     sex.stringToValue("06:00:00,45:00:00"));
        assertEquals(dec.stringToValue(" 90, 45 "),
                     sex.stringToValue("06:00:00, 45:00:00 "));
        assertEquals(dec.stringToValue("90,+45"),
                     sex.stringToValue("06:00:00,+45:00:00"));
        assertEquals(dec.stringToValue("90.0,+45.000"),
                     sex.stringToValue("06:00:00.00,+45:00:00.00"));
    }

    public void testRoundTrip() throws Exception {

        // These tests will fail if the numeric formatting is changed,
        // e.g. a different number of trailing zeroes is used for the
        // decimal or sexagesimal formatters in PositionTextField.
        // If that happens, modify or delete the tests here.

        assertRoundTrip(dec, "90.000000,+45.000000");
        assertRoundTrip(dec, "90.000000,-45.000000");

        assertRoundTrip(sex, "06:00:00.00,+45:00:00.00");
        assertRoundTrip(sex, "06:00:00.00,-45:00:00.00");
    }

    public void testBadFormat() throws Exception {
        assertParseError(dec, "0, forty-five");
        assertParseError(dec, "ninety,-1.0");
        assertParseError(dec, "1,2,3");
        assertParseError(dec, "06:00:00.00,+45:00:00.00");

        assertParseError(sex, "06:00:00.00,forty-five");
        assertParseError(sex, "six:00:00,+45:00:00");
        assertParseError(sex, "1,2,3");
        assertParseError(sex, "6.0,+45.0");
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

    private void assertRoundTrip(AbstractFormatter fmt, String xy) throws Exception {
        assertEquals(xy, fmt.valueToString(fmt.stringToValue(xy)));

        boolean isDec;
        if (fmt == dec) {
            isDec = true;
        }
        else if (fmt == sex) {
            isDec = false;
        }
        else {
            fail( "What is it then?" );
            return;
        }

        setFormat(ptf, isDec);
        ptf.setValue(fmt.stringToValue(xy));
        assertEquals(xy, ptf.getText());
        setFormat(ptf, ! isDec);
        assertTrue(! xy.equals(ptf.getText()));
        setFormat(ptf, isDec);
        assertEquals(xy, ptf.getText());
    }

    private static void setFormat(DecSexToggle.DecSexListener l, boolean isDecimal) {
        if (isDecimal) {
            l.degreesSelected(null);
        }
        else {
            l.sexaSelected(null);
        }
    }
}
