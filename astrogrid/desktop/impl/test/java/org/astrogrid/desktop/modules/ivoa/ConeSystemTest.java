/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ACRTestSetup;

/** System test for the cone search interface.
 * @author Noel Winstanley
 * @since Jun 10, 200610:18:51 AM
 */
public class ConeSystemTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR acr = getACR();
		assertNotNull(acr);
		cone = (Cone)acr.getService(Cone.class);
		reg = (Registry)acr.getService(Registry.class);
		ses = (Sesame)acr.getService(Sesame.class);		
		assertNotNull(cone);
	}
	
	protected Cone cone;
	protected Registry reg;
	protected Sesame ses;	
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();
    }    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ConeSystemTest.class));
    }    

	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testQuery() throws Exception{
		String xq = cone.getRegistryXQuery();
		Resource[] res = reg.xquerySearch(xq);
		assertNotNull(res);
		assertTrue(res.length > 0);
		Resource r = res[0];
		SesamePositionBean pos = ses.resolve("crab");
		assertNotNull(pos);
		URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),1.0);
		Map[] rows = cone.execute(u);
		assertNotNull(rows);
		assertTrue(rows.length > 0);

	}
	



	public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = cone.getRegistryAdqlQuery();
		assertNotNull(q);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.adqlsSearch(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkConeResource(arr[i]);
		}
	}
	
	public void testGetXQueryRegistryQuery() throws Exception {
		String xq = cone.getRegistryXQuery();
		assertNotNull(xq);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkConeResource(arr[i]);
		}		
		
	}
	

	
	private void checkConeResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	
	

}
