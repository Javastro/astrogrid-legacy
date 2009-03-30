/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:22:41 PM
 */
public class ValidationTest extends TestCase {

    private Validation v;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        v = new Validation();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        v = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(v.toString());
    }

    
    public void testEqials() throws Exception {
        assertEquals(v,v);
    }
    
    public void testValidationLevel() throws Exception {
        assertEquals(0,v.getValidationLevel());
        v.setValidationLevel(4);
        assertEquals(4,v.getValidationLevel());
    }
    
    public void testValidatedBy() throws Exception {
        final URI u = new URI("test://foo");
        v.setValidatedBy(u);
        assertSame(u,v.getValidatedBy());
    }
}
