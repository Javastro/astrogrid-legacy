/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:14:12 PM
 */
public class CoverageTest extends TestCase {

    private Coverage c;

    protected void setUp() throws Exception {
        super.setUp();
        c = new Coverage();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(c.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(c,c);
    }
    public void testFootprint() throws Exception {
        final ResourceName rn = new ResourceName();
        c.setFootprint(rn);
        assertSame(rn,c.getFootprint());
    }
    
    public void testStcResourceProfile() throws Exception {
        final StcResourceProfile stc = new StcResourceProfile();
        c.setStcResourceProfile(stc);
        assertSame(stc,c.getStcResourceProfile());
    }
    
    public void testWavebands() throws Exception {
        final String[] wb = new String[] {
        };
        c.setWavebands(wb);
        assertSame(wb,c.getWavebands());
    }
    

}
