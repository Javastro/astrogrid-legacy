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
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** @implement some test queries.
 * @author Noel Winstanley
 * @since Jun 13, 20062:24:01 PM
 */
public class SsapSystemTest extends InARTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		ssap = (Ssap)reg.getService(Ssap.class);
		assertNotNull(ssap);		
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		ssap = null;
	}
	protected Ssap ssap;

	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SsapSystemTest.class));
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
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	
}
