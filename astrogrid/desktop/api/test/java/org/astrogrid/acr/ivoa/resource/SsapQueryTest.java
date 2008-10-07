/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SsapCapability.PosParam;
import org.astrogrid.acr.ivoa.resource.SsapCapability.Query;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20084:39:44 PM
 */
public class SsapQueryTest extends TestCase {

    private Query q;

    protected void setUp() throws Exception {
        super.setUp();
        q = new SsapCapability.Query();
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
        final PosParam p = new SsapCapability.PosParam();
        q.setPos(p);
        assertEquals(p,q.getPos());
    }
    
    public void testCmd() throws Exception {
        final String c= "a command";
        q.setQueryDataCmd(c);
        assertEquals(c,q.getQueryDataCmd());
    }
    
    public void testSize() throws Exception {
        q.setSize(42.0);
        assertEquals(42.0,q.getSize());
    }
    
    


}
