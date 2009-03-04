/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Test the STAP sepcifci bits of DalIMpl.
 * execute, executeDSave, etc are tesed in ConeSystemTest.
 * save datasets code is common too, and tested in SiapSystemTest
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 23, 20071:40:27 PM
 */
public class StapSystemTest extends InARTestCase {

    private Registry reg;
    private Stap stap;
    protected Info info;
    protected Manager manager;  

    protected void setUp() throws Exception {
        super.setUp();
        reg = (Registry)assertServiceExists(Registry.class,"ivoa.registry");
        stap = (Stap)assertServiceExists(Stap.class,"astrogrid.stap");
        info = (Info) assertServiceExists(Info.class,"file.info");
        manager = (Manager) assertServiceExists(Manager.class,"file.manager");      
   
    }
    
 

    protected void tearDown() throws Exception {
        super.tearDown();
        reg = null;
        stap = null;
        info = null;
        manager = null;         
    }
    


 public static final String STAP_TEST_SERVICE = "ivo://mssl.ucl.ac.uk/stap/cdaw/cluster";
/*
 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
 */
public void testQuery() throws Exception {
    final Resource r = reg.getResource(new URI(STAP_TEST_SERVICE));
    assertNotNull("stap service not known in registry",r);
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    final Date start = sdf.parse("2000-01-01");
    final Date end = sdf.parse("2000-02-01");
    final URL u = stap.constructQuery(r.getId(),start,end);
    final Map[] rows = stap.execute(u);
    assertNotNull(rows);
    assertTrue(rows.length > 0);
    for (final Map map : rows) {
        assertNotNull(map.get("VOX:AccessReference"));     
    }

  
}
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(StapSystemTest.class));
    }    
    
    public void testSRQLQuery() throws Exception {
        final String q = stap.getRegistryQuery();
        assertNotNull(q);
        final Resource[] arr = reg.search(q);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        for (int i = 0; i < arr.length; i++) {
            checkStapResource(arr[i]);
        }
    }
    

    public void testXQuery() throws Exception {
        final String xq = stap.getRegistryXQuery();
        assertNotNull(xq);
        final Resource[] arr = reg.xquerySearch(xq);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        // just services for now..
        for (int i = 0; i < arr.length; i++) {
            checkStapResource(arr[i]);
        }           
    }
    
    private void checkStapResource(final Resource r) {
        assertTrue(r instanceof Service);
        assertTrue(r instanceof StapService);
        assertNotNull(((StapService)r).findStapCapability());
    }   

}
