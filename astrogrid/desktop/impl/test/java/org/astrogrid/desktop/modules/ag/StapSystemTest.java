/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** system test to call some stap services.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 23, 20071:40:27 PM
 */
public class StapSystemTest extends InARTestCase {

    private Registry reg;
    private Stap stap;


    protected void setUp() throws Exception {
        super.setUp();
        reg = (Registry)assertServiceExists(Registry.class,"ivoa.registry");
        stap = (Stap)assertServiceExists(Stap.class,"astrogrid.stap");
    }
    
 

    protected void tearDown() throws Exception {
        super.tearDown();
        reg = null;
        stap = null;
    }
    


 public static final String STAP_TEST_SERVICE = "ivo://mssl.ucl.ac.uk/stap/cdaw/cluster";
/*
 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
 */
public void testQuery() throws Exception {
    Resource r = reg.getResource(new URI(STAP_TEST_SERVICE));
    assertNotNull("stap service not known in registry",r);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date start = sdf.parse("2000-01-01");
    Date end = sdf.parse("2000-02-01");
    URL u = stap.constructQuery(r.getId(),start,end);
    Map[] rows = stap.execute(u);
    assertNotNull(rows);
    assertEquals(1,rows.length); // this service should just return a singleresults
  
}
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(StapSystemTest.class));
    }    
    
    
    public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
        String q = stap.getRegistryAdqlQuery();
        assertNotNull(q);
            Resource[] arr = reg.adqlsSearch(q);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        // just services for now..
        for (int i = 0; i < arr.length; i++) {
            checkStapResource(arr[i]);
        }
    }
    
    public void testGetXQueryRegistryQuery() throws Exception {
        String xq = stap.getRegistryXQuery();
        assertNotNull(xq);
        Resource[] arr = reg.xquerySearch(xq);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        // just services for now..
        for (int i = 0; i < arr.length; i++) {
            checkStapResource(arr[i]);
        }           
    }
    
    private void checkStapResource(Resource r) {
        //@todo refine this later..
        assertTrue(r instanceof Service);
    }   

}
