/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Tests the SSAP specific bits of DalImpl
 * Basic votable retrieval, parsing, etc is common, and exercised by ConeSystemTest.
 * Savedatasets code is also common, and exercised by SiapSystemTest
 * @author Noel Winstanley
 * @since Jun 13, 20062:24:01 PM
 */
public class SsapSystemTest extends InARTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		ssap = (Ssap)assertServiceExists(Ssap.class,"ivoa.ssap");
		reg = (Registry)assertServiceExists(Registry.class,"ivoa.registry");
		ses = (Sesame)assertServiceExists(Sesame.class,"cds.sesame");
        info = (Info) assertServiceExists(Info.class,"file.info");
        manager = (Manager) assertServiceExists(Manager.class,"file.manager");      
        pos = ses.resolve("crab");  		
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		ssap = null;
		reg = null;
		ses = null;		
        info = null;
        manager = null;     
        pos = null;		
	}
	protected Ssap ssap;
	protected Registry reg;
	protected Sesame ses;	
    public static final String SSAP_TEST_SERVICE = "ivo://stecf.euro-vo/SSA/HST/FOS";
    protected Info info;
    protected Manager manager;  
    protected SesamePositionBean pos;    

	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SsapSystemTest.class));
	    }    

	    /*
	    Run a test 'execute', just to verify query params and service is ok, and that some rows are returned.
	     */	    
		public void testQuery() throws Exception {
			final Resource r = reg.getResource(new URI(SSAP_TEST_SERVICE));
			final URL u = ssap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.01);
			final Map[] rows = ssap.execute(u);
			assertNotNull(rows);
			assertTrue(rows.length > 0);
			for (int i = 0; i < rows.length; i++) {
				assertNotNull(rows[i].get("DATA_LINK"));
			}
		}
	    

	    public void testSRQLQuery() throws Exception {
	        final String q = ssap.getRegistryQuery();
	        assertNotNull(q);
	        final Resource[] arr = reg.search(q);
	        assertNotNull(arr);
	        assertTrue(arr.length > 0);
	        for (int i = 0; i < arr.length; i++) {
	            checkSsapResource(arr[i]);
	        }
	    }
	
	public void testXQuery() throws Exception {
		final String xq = ssap.getRegistryXQuery();
		assertNotNull(xq);
		final Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			checkSsapResource(arr[i]);
		}		
		
	}
	
	
	private void checkSsapResource(final Resource r) {
		assertTrue(r instanceof Service);
		assertTrue("not an instanceof of ssap service",r instanceof SsapService);
		assertNotNull("ssap capability is null",((SsapService)r).findSsapCapability());
	}	
}
