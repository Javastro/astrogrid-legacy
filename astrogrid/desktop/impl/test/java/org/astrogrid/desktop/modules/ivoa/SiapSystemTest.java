/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;


/**
 * @author Noel Winstanley
 * @since Jun 13, 20062:22:58 PM
 */
public class SiapSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR acr = getACR();
		assertNotNull(acr);
		siap = (Siap)acr.getService(Siap.class);
		assertNotNull(siap);		
		reg = (Registry)acr.getService(Registry.class);
		ses = (Sesame)acr.getService(Sesame.class);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		siap = null;
		reg = null;
		ses = null;
	}
	protected Siap siap;
	protected Registry reg;
	protected Sesame ses;

	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SiapSystemTest.class));
	    }    

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
	 */
	public void testQuery() throws ServiceException, NotFoundException, InvalidArgumentException {
		String xq = siap.getRegistryXQuery();
		Resource[] res = reg.xquerySearch(xq);
		assertNotNull(res);
		assertTrue(res.length > 0);
		Resource r = res[0];
		SesamePositionBean pos = ses.resolve("crab");
		assertNotNull(pos);
		URL u = siap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),1.0);
		Map[] rows = siap.execute(u);
		assertNotNull(rows);
		assertTrue(rows.length > 0);
		for (int i = 0; i < rows.length; i++) {
			assertNotNull(rows[i].get("AccessReference"));
			System.out.println(rows[i]);
		}
	}


	
	public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = siap.getRegistryAdqlQuery();
		assertNotNull(q);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.adqlsSearch(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSiapResource(arr[i]);
		}
	}
	
	public void testGetXQueryRegistryQuery() throws Exception {
		String xq = siap.getRegistryXQuery();
		assertNotNull(xq);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSiapResource(arr[i]);
		}			
	}
	
	private void checkSiapResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	

}
