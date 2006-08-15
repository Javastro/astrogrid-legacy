/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ACRTestSetup;

/** @todo implement some test queries.
 * @author Noel Winstanley
 * @since Jun 13, 20062:22:58 PM
 */
public class SiapSystemTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		siap = (Siap)reg.getService(Siap.class);
		assertNotNull(siap);		
	}
	protected Siap siap;
	   protected ACR getACR() throws Exception{
	        return (ACR)ACRTestSetup.acrFactory.getACR();
	    }    
	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(SiapSystemTest.class));
	    }    

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() {
		fail("implement me");
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryF(URI, double, double, double, String)'
	 */
	public void testConstructQueryF() {
		fail("implement me");
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryS(URI, double, double, double, double)'
	 */
	public void testConstructQueryS() {
		fail("implement me");
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuerySF(URI, double, double, double, double, String)'
	 */
	public void testConstructQuerySF() {
		fail("implement me");
	}


	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.getRegistryQuery()'
	 */
	public void testGetAdqlRegistryQueryOldReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = siap.getRegistryAdqlQuery();
		assertNotNull(q);
		Registry reg = (Registry)getACR().getService(Registry.class);
		ResourceInformation[] arr = reg.adqlSearchRI(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			assertTrue(arr[i] instanceof SiapInformation);
			checkSiapInformation((SiapInformation)arr[i]);
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
	private void checkSiapInformation(SiapInformation ci) {
		assertNotNull(ci.getAccessURL());
	}
	
	private void checkSiapResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	

}
