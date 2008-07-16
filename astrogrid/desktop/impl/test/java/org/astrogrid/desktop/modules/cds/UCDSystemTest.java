/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** rudimentary connections tests for UCD service
 * @author Noel Winstanley
 * @since Jun 9, 20066:57:04 PM
 */
public class UCDSystemTest extends InARTestCase {

	/**
	 * 
	 */
	public static final String SAMPLE_UCD = "PHOT_JHN_V";
	/**
	 * 
	 */
	public static final String SAMPLE_UCD_1_PLUS = "ivoa:phot.mag;em.opt.B";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ucd = (UCD)assertServiceExists(UCD.class,"cds.ucd");
	
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		ucd = null;
	}
	protected UCD ucd;


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
		assertEquals("Johnson magnitude V (JHN)",s);

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
		assertEquals("phot.mag;em.opt.V",s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.upgrade(String)'
	 */
	public void testUpgrade() throws ServiceException {
		String s = ucd.upgrade("pos.gal.lat");
		assertNonEmptyResponse(s);
		assertEquals("pos.galactic.lat",s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.validate(String)'
	 */
	public void testValidate() throws ServiceException {
		String s = ucd.validate(SAMPLE_UCD_1_PLUS);
		assertNonEmptyResponse(s);
		assertEquals("0",s);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.UCDImpl.explain(String)'
	 */
	public void testExplain() throws ServiceException {
		String s = ucd.explain(SAMPLE_UCD_1_PLUS);
		assertNonEmptyResponse(s);
		assertEquals("Photometric magnitude / Optical band between 400 and 500 nm",s);
	}

	public void testAssign() throws Exception {
	    String s= ucd.assign("V magnitude");
	    assertNonEmptyResponse(s);
	    assertEquals("phot.mag;em.opt.V",s);
	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(UCDSystemTest.class));
    }

}
