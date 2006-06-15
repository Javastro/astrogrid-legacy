/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.desktop.ACRTestSetup;

/** rudimentary connections tests for UCD service
 * @author Noel Winstanley
 * @since Jun 9, 20066:57:04 PM
 */
public class UCDSystemTest extends TestCase {

	/**
	 * 
	 */
	public static final String SAMPLE_UCD = "PHOT_JHN_V";
	/**
	 * 
	 */
	public static final String SAMPLE_UCD_1_PLUS = "phot.mag;em.opt.B";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);	
		ucd = (UCD)reg.getService(UCD.class);
	
	}
	
	protected UCD ucd;

	protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.UCDList()'
	 */
	public void testUCDList() throws ServiceException {
		String s= ucd.UCDList();
		assertNonEmptyResponse(s);

	}

	/**
	 * @param s
	 */
	private void assertNonEmptyResponse(String s) {
		assertNotNull(s);
		assertTrue(s.trim().length() > 0);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.resolveUCD(String)'
	 */
	public void testResolveUCD() throws ServiceException {
		String s = ucd.resolveUCD(SAMPLE_UCD);
		assertNonEmptyResponse(s);
		

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.UCDofCatalog(String)'
	 */
	public void testUCDofCatalog() throws ServiceException {
		String s = ucd.UCDofCatalog("I/239");
		assertNonEmptyResponse(s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.translate(String)'
	 */
	public void testTranslate() throws ServiceException {
		String s = ucd.translate(SAMPLE_UCD);
		assertNonEmptyResponse(s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.upgrade(String)'
	 */
	public void testUpgrade() throws ServiceException {
		String s = ucd.upgrade(SAMPLE_UCD);
		assertNonEmptyResponse(s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.validate(String)'
	 */
	public void testValidate() throws ServiceException {
		String s = ucd.validate(SAMPLE_UCD_1_PLUS);
		assertNonEmptyResponse(s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.explain(String)'
	 */
	public void testExplain() throws ServiceException {
		String s = ucd.explain(SAMPLE_UCD_1_PLUS);
		assertNonEmptyResponse(s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.assign(String)'
	 */
	public void testAssign() throws ServiceException {
		String s = ucd.explain(SAMPLE_UCD_1_PLUS);
		assertNonEmptyResponse(s);
		String u = ucd.assign(s);
		assertEquals(SAMPLE_UCD_1_PLUS,u.trim());
	}
	
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(UCDSystemTest.class));
    }

}
