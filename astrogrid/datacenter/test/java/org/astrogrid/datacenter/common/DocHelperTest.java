package org.astrogrid.datacenter.common;

import junit.framework.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * JUnit test case for DocHelperTest2
 */

public class DocHelperTest extends TestCase {
	//declare reusable objects to be used across multiple tests
	public DocHelperTest(String name) {
		super(name);
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(DocHelperTest.class);
	}
	public static Test suite() {
		return new TestSuite(DocHelperTest.class);
	}
	protected void setUp() {
	//define reusable objects to be used across multiple tests
	}
	protected void tearDown() {
	//clean up after testing (if necessary)
	}
	public void testWrap() throws Exception{
	

		String var1 = "<dummy />";
		Document var2 = null;

			var2 = DocHelper.wrap(var1);
        assertNotNull(var2);
        assertEquals("dummy",var2.getDocumentElement().getLocalName());


		//insert code testing exception triggering
		try {
			//insert code triggering SAXException 
			var2 = DocHelper.wrap("faulty xml");

			fail("Should raise SAXException");
		} catch (SAXException e) {
		} catch (Throwable e) {
			fail("Failed with:" + e);
		}
	}
}