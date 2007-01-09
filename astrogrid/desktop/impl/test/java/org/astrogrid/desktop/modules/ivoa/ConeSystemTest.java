/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.votech.VoMon;
import org.votech.VoMonBean;

/** System test for the cone search interface.
 * @author Noel Winstanley
 * @since Jun 10, 200610:18:51 AM
 */
public class ConeSystemTest extends InARTestCase {

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
		vomon = (VoMon)acr.getService(VoMon.class);
		assertNotNull(cone);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		cone =null;
		reg = null;
		ses = null;
		vomon = null;
	}
	protected Cone cone;
	protected Registry reg;
	protected Sesame ses;
	protected VoMon vomon;
 
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ConeSystemTest.class));
    }    

	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testQuery() throws Exception{
		String xq = cone.getRegistryXQuery();
		Resource[] res = reg.xquerySearch(xq);
		assertNotNull(res);
		assertTrue(res.length > 0);
		List l = Arrays.asList(res);
		Collections.shuffle(l);
		Resource r = null;
		// find a service that is up.
		for (Iterator i = l.iterator(); i.hasNext();) {
			Resource x = (Resource)i.next();
			URI id = x.getId();
			VoMonBean bean = vomon.checkAvailability(id);
			if (bean != null && bean.getStatus().equals("up")) {
				r = x;
				break;
			}
		}
		assertNotNull("no available service found",r);
		System.out.println(r.getId());
		SesamePositionBean pos = ses.resolve("crab");
		assertNotNull(pos);
		System.out.println(pos);
		URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),1.0);
		Map[] rows = cone.execute(u);
		assertNotNull(rows);
		assertTrue("no results returned",rows.length > 0);

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
