/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.ivoa.Adql;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import junit.framework.TestCase;

/** unit test for the ar's adql parser.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:58:17 PM
 */
public class AdqlUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		adql = new AdqlImpl();
	}
	Adql adql;
	protected void tearDown() throws Exception {
		super.tearDown();
		adql = null;
	}

	public void testEmptyS2x() {
		
	}
	
	public void testNullS2x() {
	}
	
	public void testSimpleS2x() throws Exception {
		Document d= adql.s2x("select * from x as a");
		assertNotNull(d);
		String string = DomHelper.DocumentToString(d);
		assertTrue(string.indexOf("Select") != -1);
	}

}
