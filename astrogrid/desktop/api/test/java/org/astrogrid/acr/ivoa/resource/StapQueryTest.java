/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;
import org.astrogrid.acr.ivoa.resource.StapCapability.Query;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 7, 200810:53:05 AM
 */
public class StapQueryTest extends TestCase {

    private Query q;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        q = new StapCapability.Query();
    }

    @Override
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
    
    public void testHascode() throws Exception {
        q.hashCode();
    }
    
    public void testEnd() throws Exception {
        final String e= "the end";
        q.setEnd(e);
        assertEquals(e,q.getEnd());
    }
    
    public void testPos() throws Exception {
        final SkyPos p = new SkyPos();
        q.setPos(p);
        assertEquals(p,q.getPos());
    }
    
    public void testSize() throws Exception {
        final SkySize sz = new SkySize();
        q.setSize(sz);
        assertEquals(sz,q.getSize());
    }

    public void testStart() throws Exception {
        final String s = "a start";
        q.setStart(s);
        assertEquals(s,q.getStart());
    }

}
