/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.resource.StapCapability.Query;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:21:36 PM
 */
public class StapCapabilityTest extends CapabilityTest {

    private StapCapability sc;

    protected void setUp() throws Exception {
        super.setUp();
        sc = new StapCapability();
        cap = sc;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testMaxRecords() throws Exception {
        sc.setMaxRecords(42);
        assertEquals(42,sc.getMaxRecords());
    }
    
    public void testSupportedFormats() throws Exception {
        final String[] forma = new String[] {
        };
        sc.setSupportedFormats(forma);
        assertSame(forma,sc.getSupportedFormats());
    }
    
    public void testTestQuery() throws Exception {
        final Query q = new Query();
        sc.setTestQuery(q);
        assertEquals(q,sc.getTestQuery());
    }
    
    public void testSupportPositioning() throws Exception {
        assertFalse(sc.isSupportPositioning());
        sc.setSupportPositioning(true);
        assertTrue(sc.isSupportPositioning());
    }


}
