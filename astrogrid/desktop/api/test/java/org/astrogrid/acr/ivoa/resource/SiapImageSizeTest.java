/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:57:41 PM
 */
public class SiapImageSizeTest extends TestCase {

    private ImageSize is;

    protected void setUp() throws Exception {
        super.setUp();
        is = new SiapCapability.ImageSize();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        is = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(is.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(is,is);
    }
    
    public void testLat() throws Exception {
        is.setLat(42);
        assertEquals(42,is.getLat());
    }
    
    public void testLong() throws Exception {
        is.setLong(42);
        assertEquals(42,is.getLong());
    }
    

}
