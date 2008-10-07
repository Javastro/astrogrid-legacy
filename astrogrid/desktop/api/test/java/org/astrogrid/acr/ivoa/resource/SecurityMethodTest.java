/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:20:20 PM
 */
public class SecurityMethodTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        sm = new SecurityMethod();
    }
    SecurityMethod sm;
    protected void tearDown() throws Exception {
        super.tearDown();
        sm = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(sm.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(sm,sm);
    }
    
    public void testStandardID() throws Exception {
        final URI u = new URI("test://foo");
        sm.setStandardID(u);
        assertEquals(u,sm.getStandardID());
    }

}
