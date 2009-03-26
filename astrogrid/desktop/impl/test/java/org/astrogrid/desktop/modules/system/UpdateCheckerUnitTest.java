/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.pref.Preference;
/** Tests the version comparator as applied within Update checker.
 * Necessary, because before I was testing the version comparator fine, but the logic in update checker was wrong.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20093:00:53 PM
 */
public class UpdateCheckerUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = new Preference();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        p = null;
    }
    
    UpdateChecker uc = null;
    Preference p = new Preference();

    private UpdateChecker create(final String version) throws Exception{
        return new UpdateChecker(null,null,version,"http://www.astrogrid.org",p);
    }
    
    public void testMajor() throws Exception {
        assertTrue(create("2008.1").isNewer("2008.2"));
        assertFalse(create("2008.1").isNewer("2008.1"));
        assertFalse(create("2008.2").isNewer("2008.1"));        
    }

    public void testMinor() throws Exception {
      //  assertFalse(create("2008.1").isNewer("2008.1.0")); - .0 is more recent. ohdear
        assertTrue(create("2008.1").isNewer("2008.1.1"));
        assertFalse(create("2008.1.1").isNewer("2008.1.1"));        
        assertFalse(create("2008.1.1").isNewer("2008.1"));           
        assertFalse(create("2008.1.1").isNewer("2008.1.0")); 
    }

    public void testOneTwo() throws Exception {
        assertTrue(create("1.2.0").isNewer("1.2.1"));
        assertFalse(create("1.2.0").isNewer("1.2.0"));
        assertFalse(create("1.2.1").isNewer("1.2.1"));
        assertFalse(create("1.2.1").isNewer("1.2.0"));        

    
        assertTrue(create("1.2.1").isNewer("1.2.2"));
        assertFalse(create("1.2.2").isNewer("1.2.2"));
        assertFalse(create("1.2.1").isNewer("1.2.1"));        
        assertFalse(create("1.2.2").isNewer("1.2.1"));  
        
        // bug in implementation of 1.2.0 and 1.2.1
        // probably in 1.2.2 too.
            // gary's suggestion is to use 1.2.2a - this worksd.
        // harrumph. don't care about this - just want to make sure that 
        // _this_ new release works correctly.
        // so, beta's and alphas won't be announced on the version.xml
        // but they still need to detect when there's something for them to upgrade to
        
        assertTrue(create("1.2.3.beta1").isNewer("1.2.3"));
        assertFalse(create("1.2.3.beta1").isNewer("1.2.2"));
    }
    
}
