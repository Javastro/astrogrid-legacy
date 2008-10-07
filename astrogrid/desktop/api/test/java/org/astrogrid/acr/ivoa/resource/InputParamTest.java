/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:16:54 PM
 */
public class InputParamTest extends BaseParamTest {

    private InputParam ip;
    protected void setUp() throws Exception {
        super.setUp();
        ip = new InputParam();
        bp = ip;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        ip = null;
    }
   
    public void testDataType() throws Exception {
        final SimpleDataType sd = new SimpleDataType();
        ip.setDataType(sd);
        assertEquals(sd,ip.getDataType());
    }
    
    public void testUse() throws Exception {
        final String u = "a use";
        ip.setUse(u);
        assertEquals(u,ip.getUse());
    }
    
    public void testStandard() throws Exception {
        assertFalse(ip.isStandard());
        ip.setStandard(true);
        assertTrue(ip.isStandard());
    }

}
