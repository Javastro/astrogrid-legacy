/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SsapCapability.PosParam;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:39:12 PM
 */
public class SsapPosParamTest extends TestCase {

    private PosParam p;

    protected void setUp() throws Exception {
        super.setUp();
        p = new SsapCapability.PosParam();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        p = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(p.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(p,p);
    }
    public void testLat() throws Exception {
        p.setLat(42.0);
        assertEquals(42.0,p.getLat());
    }
    
    public void testLong() throws Exception {
        p.setLong(42.0);
        assertEquals(42.0,p.getLong());
    }
    
    public void testRefFrame() throws Exception {
        final String f = "a frame";
        p.setRefframe(f);
        assertEquals(f,p.getRefframe());
    }

}
