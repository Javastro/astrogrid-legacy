/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import junit.framework.TestCase;

import org.freixas.jcalendar.JCalendarCombo;

/** interactive tests of the JCalendar Combo.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 23, 20071:23:58 PM
 */
public class JCalendarComboUIExercise extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testShowCombo() throws Exception {
        JCalendarCombo cal = new JCalendarCombo();
        JOptionPane.showInputDialog(cal);
        Calendar calendar = cal.getCalendar();
        Date date = cal.getDate();
        System.out.println(calendar);
        System.out.println(date);
        assertEquals(calendar.getTime(),date); // ok. so the date we get from JCalendar combo is the same as calendar.getTime()
        // sheesh, isn't java date handling wonderful.
        
    }

}
