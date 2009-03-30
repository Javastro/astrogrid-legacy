/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:14:29 PM
 */
public class CreatorTest extends TestCase {

    private Creator c;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Creator();
    }

    @Override
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
    
    public void testLogoURI() throws Exception {
        final URI logo = new URI("http://www.foo.com");
        c.setLogoURI(logo);
        assertEquals(logo,c.getLogoURI());
    }
    
    public void testName() throws Exception {
        final ResourceName rn = new ResourceName();
        c.setName(rn);
        assertEquals(rn,c.getName());
    }
    

}
