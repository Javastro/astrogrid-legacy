/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.Adql;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** unit test for the ar's adql parser.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:58:17 PM
 */
public class AdqlUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		adql = new AdqlImpl();
	}
	AdqlInternal adql;
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
	
	public void testS2xs() throws Exception {
		String string= adql.s2xs("Select Top 100 * From iras_asc as a ");
		assertNotNull(string);
		assertTrue(string.indexOf("Select") != -1);
	}

}
