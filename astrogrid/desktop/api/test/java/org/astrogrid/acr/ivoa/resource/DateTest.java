/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:15:28 PM
 */
public class DateTest extends TestCase {

    private Date d;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        d = new Date();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        d = null;        
    }
    
    public void testToString() throws Exception {
        assertNotNull(d.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(d,d);
    }
    
    public void testDate() throws Exception {
        final String v=  "a date value";
        d.setValue(v);
        assertEquals(v,d.getValue());
    }
    
    public void testRole() throws Exception {
        final String r = "a role";
        d.setRole(r);
        assertEquals(r,d.getRole());
    }
    
    
    
    

}
