/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.resource.SsapCapability.Query;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20082:59:36 PM
 */
public class SsapCapabilityTest extends CapabilityTest {

    private SsapCapability sc;

    protected void setUp() throws Exception {
        super.setUp();
        sc = new SsapCapability();
        cap = sc;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testComplianceLevel() throws Exception {
        final String l = "a level";
        sc.setComplianceLevel(l);
        assertEquals(l,sc.getComplianceLevel());
    }
    
    public void testCreationTypes() throws Exception {
        final String[] ctypes = new String[] {
        };
        sc.setCreationTypes(ctypes);
        assertSame(ctypes,sc.getCreationTypes());
       
    }
    
    public void testDataSources() throws Exception {
        final String[] ds = new String[] {
        };
        sc.setDataSources(ds);
        assertSame(ds,sc.getDataSources());
    }
    
    public void testDefaultMaxRecords() throws Exception {
        sc.setDefaultMaxRecords(42);
        assertEquals(42,sc.getDefaultMaxRecords());
    }
    
    public void testMaxApeture() throws Exception {
        sc.setMaxAperture(42.0);
        assertEquals(42.0,sc.getMaxAperture());
        
    }
    
    public void testFileSize() throws Exception {
        sc.setMaxFileSize(42);
        assertEquals(42,sc.getMaxFileSize());
    }
    
    public void testMaxRecords() throws Exception {
        sc.setMaxRecords(42);
        assertEquals(42,sc.getMaxRecords());
    }
    
    public void testMaxSearchReadius() throws Exception {
        sc.setMaxSearchRadius(42.0);
        assertEquals(42.0,sc.getMaxSearchRadius());
    }
    
    public void testSupportedFrames() throws Exception {
        final String[] fr = new String[] {
        };
        sc.setSupportedFrames(fr);
        assertSame(fr,sc.getSupportedFrames());
    }
    
    public void testTestQuery() throws Exception {
        final Query q= new SsapCapability.Query();
        sc.setTestQuery(q);
        assertEquals(q,sc.getTestQuery());
    }
    
    
    
    
}
