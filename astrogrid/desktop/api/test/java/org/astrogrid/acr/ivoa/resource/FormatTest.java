/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:15:46 PM
 */
public class FormatTest extends TestCase {

    private Format f;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        f = new Format();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        f = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(f.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(f,f);
    }
    
    public void testValue() throws Exception {
        final String val = "val";
        f.setValue(val);
        assertEquals(val,f.getValue());
    }
    
    public void testMimeType() throws Exception {
        assertFalse(f.isMimeType());
        f.setMimeType(true);
        assertTrue(f.isMimeType());
    }

}
