/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:58:35 PM
 */
public class SiapSkyPosTest extends TestCase {

    private SkyPos is;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        is = new SiapCapability.SkyPos();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    
    public void testToString() throws Exception {
        assertNotNull(is.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(is,is);
    }
    
    public void testLat() throws Exception {
        is.setLat(42.0);
        assertEquals(42.0,is.getLat());
    }
    
    public void testLong() throws Exception {
        is.setLong(42.0);
        assertEquals(42.0,is.getLong());
    }
}
