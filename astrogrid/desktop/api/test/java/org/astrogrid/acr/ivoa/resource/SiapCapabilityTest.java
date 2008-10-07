/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.Query;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:20:47 PM
 */
public class SiapCapabilityTest extends CapabilityTest {

    private SiapCapability sc;

    protected void setUp() throws Exception {
        super.setUp();
        sc = new SiapCapability();
        cap = sc;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        sc = null;
    }
    

    public void testImageServiceType() throws Exception {
        final String t = "a ttpe";
        sc.setImageServiceType(t);
        assertEquals(t,sc.getImageServiceType());
    }
    
    public void testMaxFileSize() throws Exception {
        sc.setMaxFileSize(42);
        assertEquals(42,sc.getMaxFileSize());
    }
    
    public void testMaxImageExtent() throws Exception {
        final SkySize ss = new SkySize();
        sc.setMaxImageExtent(ss);
        assertEquals(ss,sc.getMaxImageExtent());
    }
    public void testMaxImageSize() throws Exception {
        final ImageSize is = new ImageSize();
        sc.setMaxImageSize(is);
        assertEquals(is,sc.getMaxImageSize());
    }
    
    
    public void testMaxQueryRegioint() throws Exception {
        final SkySize ss = new SkySize();
        sc.setMaxQueryRegionSize(ss);
        assertEquals(ss,sc.getMaxQueryRegionSize());
    }
    
    public void testMaxRecords() throws Exception {
        sc.setMaxRecords(42);
        assertEquals(42,sc.getMaxRecords());
    }
    
    public void testTestQuery() throws Exception {
        final Query q = new Query();
        sc.setTestQuery(q);
        assertEquals(q,sc.getTestQuery());
    }

}
