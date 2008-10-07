/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SiapCapability.Query;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:58:10 PM
 */
public class SiapQueryTest extends TestCase {

    private Query q;

    protected void setUp() throws Exception {
        super.setUp();
        q = new SiapCapability.Query();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        q = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(q.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(q,q);
    }
    
    
    public void testPos() throws Exception {
        final SkyPos p = new SkyPos();
        q.setPos(p);
        assertEquals(p,q.getPos());
    }

    public void testSize() throws Exception {
        final SkySize s= new SkySize();
        q.setSize(s);
        assertEquals(s,q.getSize());
    }
    public void testVerb() throws Exception {
        q.setVerb(2);
        assertEquals(2,q.getVerb());
    }
    
    public void testExtras() throws Exception {
        final String s= "extras";
        q.setExtras(s);
        assertEquals(s,q.getExtras());
    }
    
}
