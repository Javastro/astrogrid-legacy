/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import org.joda.time.Duration;

import junit.framework.TestCase;

/** Test for how I'm using joda time package.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 8, 20087:46:03 AM
 */
public class JodaTimeUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** discovered a bug here, based on boxing, unboxing of nunbers
     * Durtation has a constructor that takes a long, and a constructor that takes an object (and it then tries to do the right thing with that kindof object).
     * I accidentally passed a double instead of a long - but instead of a compiler error, this is autoboxed into a Double, but then the constructor fails at runtime.
     * so
     * @throws Exception
     */
    public void testDuration() throws Exception {
        try {
            Duration d= new Duration(new Double(21));
            fail("expected to fail");
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            Duration d = new Duration((double)21);
            fail("expected to fail");
        } catch (IllegalArgumentException e) {
            // ok
        }  
        Duration d = new Duration(21);        
        d = new Duration(((long)21) * 60 * 1000);              
        d = new Duration(21 * 60 * 1000);           
        d = new Duration((21) * 60L * 1000);         
    }
}
