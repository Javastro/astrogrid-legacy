/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.util.Comparator;

import junit.framework.TestCase;
/** unit test for version comparators
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 19, 20087:10:09 PM
 */
public class VersionComparatorUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        c = new UpdateChecker.VersionComparator();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        c = null;
        super.tearDown();
    }
    
    Comparator c;

    public void testBasics() throws Exception {
        assertEquals(0,c.compare("2008.1","2008.1"));
        
        assertTrue(c.compare("2008.1","2008.2") < 0 ); // first is smaller than second.
        assertTrue(c.compare("2008.2","2008.1") > 0);
    }
    
    public void testPointRelease() throws Exception {
      assertTrue(c.compare("2008.1","2008.1.0") < 0);  
      assertTrue("point release should be larger",
              c.compare("2008.1.0","2008.1.1") < 0);
    }

    public void testReleaseCandidateDowngrade() throws Exception {
        assertTrue("rc should be larger than previous release",
                c.compare("2008.1.rc1","2008.0") > 0); //rc is more modern than previous release (and so users of rc won't get pestered to 'up' grade 
        assertTrue("rc should be smaller than next release",
                c.compare("2008.1.rc1","2008.2") < 0); //rc is more modern than previous release (and so users of rc won't get pestered to 'up' grade 
 
    }
    
    public void testReleaseCandidateUpgrade() throws Exception {
        assertTrue("later rc should be larger than earlier rc",
                c.compare("2008.1.rc1","2008.1.rc3") < 0);        
        assertTrue("later rc should be larger than rc f earlier version",
                c.compare("2008.1.rc1","2008.0.rc3") > 0);  
    }
    public void testReleaseCandidateToNewVersion() {
        assertTrue("poiint release should be larger than earlier rc",
                c.compare("2008.1.rc1","2008.1.1") < 0);
        // important - users who are running a rc need to be notified of later version.
        assertTrue("release shiould be largeet than earlier rc",
                c.compare("2008.1.rc1","2008.1") < 0); // sneaky - 2008.1 is more modern than 2008.1.rc1  
      }
    
    

    
    
}
