/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.net.URI;

import org.astrogrid.acr.ivoa.resource.CapabilityTest;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:50:25 AM
 */
public class CeaServerCapabilityTest extends CapabilityTest {

    private CeaServerCapability cc;

    protected void setUp() throws Exception {
        super.setUp();
        cc = new CeaServerCapability();
        cap = cc;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
 
    public void testManagedApplications() throws Exception {
        final URI[] arr = new URI[] {
        };
        cc.setManagedApplications(arr);
        assertSame(arr,cc.getManagedApplications());
    }

}
