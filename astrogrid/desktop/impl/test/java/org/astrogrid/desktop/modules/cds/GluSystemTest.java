/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test for interface to glu service. never works for me..
 * @author Noel Winstanley
 * @since Jun 9, 20065:49:58 PM
 */
public class GluSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);	
		glu = (Glu)reg.getService(Glu.class);
	}
	
	protected Glu glu;
	protected void tearDown() throws Exception {
		super.tearDown();
		glu = null;
	}
	
	/** @todo fails at the moment.
	 * Test method for 'org.astrogrid.desktop.modules.cds.GluImpl.getURLfromTag(String)'
	 */
	public void testGetURLfromTag() throws ServiceException, MalformedURLException {
		String r = glu.getURLfromTag("VizieR.MetaCat");
		assertNotNull(r);
		URL u = new URL(r);
		
	}
	
	public void testGetURLfromTagNull() {
		try {
			glu.getURLfromTag(null);
			fail("expected to fail");
		} catch (ServiceException e) {
			// ok
		}
	}
	
	public void testGetURLfromTagEmpty() {
		try {
			glu.getURLfromTag("");
			fail("expected to fail");
		} catch (ServiceException e) {
			// ok
		}
	}
	
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(GluSystemTest.class));
    }

}
