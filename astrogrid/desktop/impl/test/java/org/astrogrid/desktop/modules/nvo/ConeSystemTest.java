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
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.desktop.ACRTestSetup;

/** System test for the cone search interface.
 * @author Noel Winstanley
 * @since Jun 10, 200610:18:51 AM
 * @todo implement
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
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.getRegistryQuery()'
	 */
	public void testGetRegistryQuery() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = cone.getRegistryQuery();
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
	
	private void checkConeInformation(ConeInformation ci) {
		//@todo any further data validation to do here??
		assertNotNull(ci.getAccessURL());
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

}
