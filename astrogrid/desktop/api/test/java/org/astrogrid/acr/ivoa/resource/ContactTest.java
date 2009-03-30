/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:13:39 PM
 */
public class ContactTest extends TestCase {

    private Contact c;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Contact();
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
    
    public void testAddress() throws Exception {
        final String a = "an address";
        c.setAddress(a);
        assertEquals(a,c.getAddress());
    }
    
    public void testEmail() throws Exception {
        final String ema = "an email";
        c.setEmail(ema);
        assertEquals(ema,c.getEmail());
    }
    
    public void testName() throws Exception {
        final ResourceName rn = new ResourceName();
        c.setName(rn);
        assertEquals(rn,c.getName());
    }
    
    public void testTelephone() throws Exception {
        final String t = "4334634690";
        c.setTelephone(t);
        assertEquals(t,c.getTelephone());
    }
    

}
