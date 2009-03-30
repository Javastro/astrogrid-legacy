/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:19:59 PM
 */
public class SearchCapabilityTest extends CapabilityTest {

    private SearchCapability sc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sc = new SearchCapability();
        cap = sc;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testExtensionSupport() throws Exception {
        final String x = "extension support";
        sc.setExtensionSearchSupport(x);
        assertEquals(x,sc.getExtensionSearchSupport());
    }
    public void testMaxRecords() throws Exception {
        sc.setMaxRecords(42);
        assertEquals(42,sc.getMaxRecords());
    }
    public void testOptionalProtocol() throws Exception {
        final String[] op = new String[] {
        };
        sc.setOptionalProtocol(op);
        assertSame(op,sc.getOptionalProtocol());
    }
    
    

}
