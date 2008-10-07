/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20085:24:51 PM
 */
public class ConeQueryTest extends TestCase {

    private Query q;

    protected void setUp() throws Exception {
        super.setUp();
        q = new ConeCapability.Query();
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
    
    public void testHashCode() throws Exception {
        q.hashCode();
    }
    
    public void testRa() throws Exception {
        q.setRa(42.0);
        assertEquals(42.0,q.getRa());
    }
    
    public void testDec() throws Exception {
        q.setDec(42.0);
        assertEquals(42.0,q.getDec());
    }
    
    public void testExtras() throws Exception {
        final String x = "extras";
        q.setExtras(x);
        assertEquals(x,q.getExtras());
    }
    
    public void testCatalog() throws Exception {
        final String c = "a catalog";
        q.setCatalog(c);
        assertEquals(c,q.getCatalog());
        
    }
    
    public void testSr() throws Exception {
        q.setSr(42.0);
        assertEquals(42.0,q.getSr());
    }
    
    public void testVerb() throws Exception {
       
        q.setVerb(42);
        assertEquals(42,q.getVerb());
    }
    
    

}
