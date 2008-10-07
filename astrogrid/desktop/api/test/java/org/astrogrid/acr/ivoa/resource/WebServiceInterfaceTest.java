/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:23:09 PM
 */
public class WebServiceInterfaceTest extends InterfaceTest {

    private WebServiceInterface wi;

    protected void setUp() throws Exception {
        super.setUp();
        wi = new WebServiceInterface();
        i = wi;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWsdl() throws Exception {
        final URI[] arr = new URI[] {
        };
        wi.setWsdlURLs(arr);
        assertSame(arr,wi.getWsdlURLs());
    }
}
