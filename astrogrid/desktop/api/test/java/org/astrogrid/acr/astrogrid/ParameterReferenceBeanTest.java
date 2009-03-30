/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:04:02 PM
 */
public class ParameterReferenceBeanTest extends TestCase {

    /**
     * 
     */
    private static final int MIN = 10;
    /**
     * 
     */
    private static final int MAX = 22;
    /**
     * 
     */
    private static final String REF = "goo";
    private ParameterReferenceBean prb;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        prb = new ParameterReferenceBean(REF,MAX,MIN);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testRef() throws Exception {
        assertEquals(REF,prb.getRef());
    }
    
    public void testMin() throws Exception {
        assertEquals(MIN,prb.getMin());
    }
    
    public void testMax() throws Exception {
        assertEquals(MAX,prb.getMax());
    }
    
    public void testToString() throws Exception {
        assertNotNull(prb.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(prb,prb);
    }
    
    public void testHashCode() throws Exception {
        prb.hashCode();
    }

}
