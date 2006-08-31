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
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.SsapInformation;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ACRTestSetup;

/** @implement some test queries.
 * @author Noel Winstanley
 * @since Jun 13, 20062:24:01 PM
 */
public class SsapSystemTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		ssap = (Ssap)reg.getService(Ssap.class);
		assertNotNull(ssap);		
	}
	protected Ssap ssap;
	   protected ACR getACR() throws Exception{
	        return (ACR)ACRTestSetup.acrFactory.getACR();
	    }    
	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(SsapSystemTest.class));
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
		String q = ssap.getRegistryAdqlQuery();
		assertNotNull(q);
		Registry reg = (Registry)getACR().getService(Registry.class);
		ResourceInformation[] arr = reg.adqlSearchRI(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			assertTrue(arr[i] instanceof SsapInformation);
			checkSsapInformation((SsapInformation)arr[i]);
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
	private void checkSsapInformation(SsapInformation ci) {
		assertNotNull(ci.getAccessURL());
	}
	
	private void checkSsapResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	
}
