/**
 * 
 */
package org.astrogrid.desktop.modules.nvo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.desktop.ACRTestSetup;

/** System test for the cone search interface.
 * @author Noel Winstanley
 * @since Jun 10, 200610:18:51 AM
 * @implement
 */
public class ConeSystemTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		cone = (Cone)reg.getService(Cone.class);
		assertNotNull(cone);
	}
	
	protected Cone cone;
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();
    }    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ConeSystemTest.class));
    }    

	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() {
		fail("implement me");

	}
	

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.getResults(URL)'
	 */
	public void testGetResults() {
		fail("implement me");
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.saveResults(URL, URI)'
	 */
	public void testSaveResults() {
		fail("implement me");
	}



	/*@todo remove - really slow.
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.getRegistryQuery()'
	 */
	public void testGetAdqlRegistryQueryOldReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = cone.getRegistryAdqlQuery();
		assertNotNull(q);
		Registry reg = (Registry)getACR().getService(Registry.class);
		ResourceInformation[] arr = reg.adqlSearchRI(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			assertTrue(arr[i] instanceof ConeInformation);
			checkConeInformation((ConeInformation)arr[i]);
		}
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
	
	private void checkConeInformation(ConeInformation ci) {
		assertNotNull(ci.getAccessURL());
	}
	
	private void checkConeResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	
	

}
