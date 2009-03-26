/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import junit.framework.TestCase;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** tests for the stylesheetcontribution bean.
 * @author Noel Winstanley
 * @since Jun 7, 20069:33:19 AM
 */
public class StylesheetsContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		sc = new StylesheetsContribution();
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		sc = null;
	}
	protected StylesheetsContribution sc;



	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.StylesheetsContribution.isApplicable(Document)'
	 */
	public void testIsApplicableDocument() throws ParserConfigurationException, SAXException, IOException {
		final Document doc = DomHelper.newDocument("<foo></foo>");
		assertFalse(sc.isApplicable(doc));
		sc.setRegexp("<foo"); // doesn't match, as not a prefix - ther's an xml header before this.
		assertFalse(sc.isApplicable(doc));
		sc.setRegexp(".*<foo"); // need to include a wildcard. 
		assertTrue(sc.isApplicable(doc));
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.StylesheetsContribution.isApplicable(CharSequence)'
	 */
	public void testIsApplicableCharSequence() {
		assertFalse(sc.isApplicable("test string"));
		sc.setRegexp("test");
		assertTrue(sc.isApplicable("test string"));
		assertFalse(sc.isApplicable("another test string")); // only matches prefixes.

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.StylesheetsContribution.createTransformer()'
	 */
	public void testCreateTransformerFromUninitialized() {
		try {
			sc.createTransformer();
			fail("expected to chuck");
		} catch (final TransformerConfigurationException x) {
			//expecteed
		}

	}
	
	public  void testCreateTransformerFromMissing() {
		try {
			sc.setSheet("not-present.xsl");
			fail("expected to fail");
		} catch (final TransformerConfigurationException x) {
			// ok
		}
	}


	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.StylesheetsContribution.toString()'
	 */
	public void testToString() {
		assertEquals("uninitialized",sc.toString());
		sc.setRegexp("wibble");
		assertEquals("wibble",sc.toString());
	}

}
