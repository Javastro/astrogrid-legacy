/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:13:23 PM
 */
public class ConeCapabilityTest extends CapabilityTest {

    private ConeCapability cc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cc = new ConeCapability();
        cap = cc;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cc = null;
    }
    public void testMaxRecords() throws Exception {
        cc.setMaxRecords(42);
        assertEquals(42,cc.getMaxRecords());
    }

    public void testMaxSR() throws Exception {
        cc.setMaxSR(42.0f);
        assertEquals(42.0f,cc.getMaxSR());
    }
    
    public void testVerbosidy() throws Exception {
        assertFalse(cc.isVerbosity());
        cc.setVerbosity(true);
        assertTrue(cc.isVerbosity());
    }
    
    public void testTestQuery() throws Exception {
        final Query q = new Query();
        cc.setTestQuery(q);
        assertEquals(q,cc.getTestQuery());
    }
    

}
