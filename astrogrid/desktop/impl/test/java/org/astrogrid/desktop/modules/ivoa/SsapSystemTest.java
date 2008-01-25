/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** @implement some test queries.
 * @author Noel Winstanley
 * @since Jun 13, 20062:24:01 PM
 */
public class SsapSystemTest extends InARTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		ssap = (Ssap)assertServiceExists(Ssap.class,"ivoa.ssap");
		reg = (Registry)assertServiceExists(Registry.class,"ivoa.registry");
		ses = (Sesame)assertServiceExists(Sesame.class,"cds.sesame");	
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		ssap = null;
		reg = null;
		ses = null;		
	}
	protected Ssap ssap;
	protected Registry reg;
	protected Sesame ses;	
    public static final String SSAP_TEST_SERVICE = "ivo://stecf.euro-vo/SSA/HST/FOS";

	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SsapSystemTest.class));
	    }    

		public void testQuery() throws Exception {
			Resource r = reg.getResource(new URI(SSAP_TEST_SERVICE));
			SesamePositionBean pos = ses.resolve("crab");
			assertNotNull(pos);
			URL u = ssap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.01);
			Map[] rows = ssap.execute(u);
			assertNotNull(rows);
			assertTrue(rows.length > 0);
			for (int i = 0; i < rows.length; i++) {
				assertNotNull(rows[i].get("DATA_LINK"));
			}
		}
	    
	
	public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = ssap.getRegistryAdqlQuery();
		assertNotNull(q);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.adqlsSearch(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSsapResource(arr[i]);
		}
	}
	
	public void testGetXQueryRegistryQuery() throws Exception {
		String xq = ssap.getRegistryXQuery();
		assertNotNull(xq);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSsapResource(arr[i]);
		}		
		
	}
	
	
	private void checkSsapResource(Resource r) {
		assertTrue(r instanceof Service);
		assertTrue("not an instanceof of ssap service",r instanceof SsapService);
		assertNotNull("ssap capability is null",((SsapService)r).findSsapCapability());
	}	
}
