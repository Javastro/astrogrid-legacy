/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:16:33 PM
 */
public class HarvestCapabilityTest extends CapabilityTest {

    private HarvestCapability hc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        hc = new HarvestCapability();
        cap = hc;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testMaxRecords() throws Exception {
        hc.setMaxRecords(42);
        assertEquals(42,hc.getMaxRecords());
    }
    
 

}
