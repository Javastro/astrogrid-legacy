/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import javax.xml.transform.TransformerException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20067:13:58 PM
 */
public class VizierSystemTest extends InARTestCase{

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		ACR reg = getACR();
		assertNotNull(reg);	
		viz = (VizieR)reg.getService(VizieR.class);
	
	}
	
	protected VizieR viz;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		viz = null;
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.VizieRImpl.cataloguesMetaData(String, double, String, String)'
	 */
	public void testCataloguesMetaData() throws Exception {
		Document doc = viz.cataloguesMetaData("m32",0.1,"arcsec","");
		assertIsRoughlyVotable(doc);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.VizieRImpl.cataloguesMetaDataWavelength(String, double, String, String, String)'
	 */
	public void testCataloguesMetaDataWavelength() throws Exception {
		Document doc = viz.cataloguesMetaDataWavelength("m32",0.1,"arcsec","","optical");
		assertIsRoughlyVotable(doc);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.VizieRImpl.cataloguesData(String, double, String, String)'
	 */
	public void testCataloguesData() throws Exception {
		Document doc = viz.cataloguesData("m32",0.1,"arcsec","");
		assertIsRoughlyVotable(doc);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.VizieRImpl.cataloguesDataWavelength(String, double, String, String, String)'
	 */
	public void testCataloguesDataWavelength() throws Exception {
		Document doc = viz.cataloguesDataWavelength("m32",0.1,"arcsec","","optical");
		assertIsRoughlyVotable(doc);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.VizieRImpl.metaAll()'
	 */
	public void testMetaAll() throws Exception {
		Document doc  = viz.metaAll();
		// not a schema valid votable. 
		assertIsRoughlyVotable(doc);

	}

	/** assert a value is roughly a votable.
	 * the votables returs from cds do not validate against dtd.
	 * @param doc
	 * @throws TransformerException
	 */
	private void assertIsRoughlyVotable(Document doc) throws Exception {
		assertNotNull(doc);
		assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(VizierSystemTest.class));
    }


}
