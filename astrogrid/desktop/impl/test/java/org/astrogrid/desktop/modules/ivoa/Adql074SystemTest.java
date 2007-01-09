/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.ApiHelpIntegrationTest;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:23:00 AM
 */
public class Adql074SystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		adql = (Adql074)reg.getService(Adql074.class);
	}
	
	protected Adql074 adql;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.Adql074DynamicImpl.s2x(String)'
	 */
	public void testRoundTrip() throws ServiceException {
		String query = "SELECT * FROM Tab t";
		Document doc = adql.s2x(query);
	    XMLUtils.PrettyDocumentToStream(doc,System.out);
	    String s= adql.x2s(doc);
	    System.out.println(s);
	    assertEquals(query,s);
		
	}
	
	public void testCaseInsensitiveKeywords() throws ServiceException {
		String query = "SELECT * FROM Tab t";
		Document doc = adql.s2x(query);
		String query1 = "select * from Tab t";
		Document doc1 = adql.s2x(query);		
		XMLAssert.assertXMLEqual(doc,doc1);
		
	}

    public static Test suite() {
        return new ARTestSetup(new TestSuite(Adql074SystemTest.class));
    }

	public static void main(String[] args) {
	    try {
	        /*
	        InputStream is= Adql074DynamicImpl.class.getResourceAsStream("module.xml");       
	        XPathFactory fac = new XPathFactoryImpl(); //@todo work out why this isn't being auto-located
	        XPath xp = fac.newXPath();
	        System.out.println(xp.evaluate("//description",new InputSource(is)));
	        */
	    Adql074DynamicImpl i = new Adql074DynamicImpl();
	    Document doc = i.s2x("Select * From Tab t"); // returns no result.
	    XMLUtils.PrettyDocumentToStream(doc,System.out);
	    String s= i.x2s(doc);
	    System.out.println(s);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
