/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:11:58 PM
 */
public class AccessURLTest extends TestCase {

    private AccessURL a;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = new AccessURL();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        a = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(a.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(a,a);
    }
    
    public void testUse() throws Exception {
        final String u = "a use";
        a.setUse(u);
        assertEquals(u,a.getUse());
    }
    
    public void testValue() throws Exception {
        final URI v = new URI("test://foo.bar");
        a.setValueURI(v);
        assertEquals(v,a.getValueURI());
    }
    

}
