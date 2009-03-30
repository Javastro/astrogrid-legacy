/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:59:08 PM
 */
public class SiapSkySizeTest extends TestCase {

    private SkySize is;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        is = new SiapCapability.SkySize();
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
        is.setLat(42.0f);
        assertEquals(42.0f,is.getLat());
    }
    
    public void testLong() throws Exception {
        is.setLong(42.0f);
        assertEquals(42.0f,is.getLong());
    }

}
