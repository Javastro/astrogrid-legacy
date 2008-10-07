/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:19:30 PM
 */
public class ResourceNameTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        rn = new ResourceName();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    ResourceName rn;
    
    public void testID() throws Exception {
        final URI id = new URI("i://foo");
        rn.setId(id);
        assertEquals(id,rn.getId());
        
    }
    
    public void testValue() throws Exception {
        final String val = "val";
        rn.setValue(val);
        assertEquals(val,rn.getValue());
    }
    
    public void testEquals() throws Exception {
        assertEquals(rn,rn);
    }
    
    public void testToString() throws Exception {
        assertNotNull(rn.toString());
    }

}
