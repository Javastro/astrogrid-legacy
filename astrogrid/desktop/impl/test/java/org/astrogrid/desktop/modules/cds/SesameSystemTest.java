/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.custommonkey.xmlunit.XMLAssert;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** test the connection to sesame name resolver.
 * also acts as a barrier test - detects any changes made to this service
 * - important as used elsewhere in the UI.
 *  - hence we're comparing against local copies of the published schema.
 * @author Noel Winstanley
 * @since Jun 9, 20066:01:46 PM
 */
public class SesameSystemTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);	
		sesame = (Sesame)reg.getService(Sesame.class);
		dtdURL = SesameDynamicImpl.class.getResource("sesame_1.dtd");
		assertNotNull(dtdURL);
	
	}
	
	protected Sesame sesame;
	protected URL dtdURL;

	protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.SesameDynamicImpl.sesame(String, String)'
	 */
	public void testSesame() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesame("m32","x");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		DomHelper.newDocument(result); // checks is well-formed xml
		// 2 attributes used by us.
		XMLAssert.assertXpathExists("/Sesame/Resolver/jradeg",result);
		XMLAssert.assertXpathExists("/Sesame/Resolver/jdedeg",result);		

	}
	// expect this to fail.
	public void testSesameResponseIsSchemaCorrect() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesame("m32","x");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		

	}
	public void testSesameAllIdentifiers() throws ServiceException, ParserConfigurationException, SAXException, IOException {
		String result = sesame.sesame("m32","xi");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml


	}	
	
	public void testSesameUnknown() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesame("notKnownObject","x");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml
		XMLAssert.assertXpathNotExists("/Sesame/Resolver/jradeg",result);
		XMLAssert.assertXpathExists("/Sesame/Resolver",result);

	}	
	

	
	public void testSesameEmpty() throws ServiceException, TransformerException, ParserConfigurationException, IOException, SAXException {
		String result = sesame.sesame("","x");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml
		XMLAssert.assertXpathNotExists("/Sesame/Resolver",result);
		XMLAssert.assertXpathExists("/Sesame",result);
	}
	
	public void testSesameInvalidResultType() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesame("m32","q");
		assertNotNull(result);
		//falls back to odd internal CDS format. 
		try {
		DomHelper.newDocument(result); // not XML this time.
		// expected to fail
		} catch (SAXParseException e) {
			// ok
		}			
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.SesameDynamicImpl.sesameChooseService(String, String, boolean, String)'
	 */
	public void testSesameChooseService() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesameChooseService("m32","x",true,"N");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		DomHelper.newDocument(result); // checks is well-formed xml
		// 2 attributes used by us.
		XMLAssert.assertXpathExists("/Sesame/Resolver/jradeg",result);
		XMLAssert.assertXpathExists("/Sesame/Resolver/jdedeg",result);		
		// if service name is unspecified - falls back to Simbad.
		XMLAssert.assertXpathEvaluatesTo("Ned","/Sesame/Resolver/@name",result);

	}
	
	public void testSesameChooseServiceNone() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
		String result = sesame.sesameChooseService("m32","x",true,"");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		DomHelper.newDocument(result); // checks is well-formed xml
		// 2 attributes used by us.
		XMLAssert.assertXpathExists("/Sesame/Resolver/jradeg",result);
		XMLAssert.assertXpathExists("/Sesame/Resolver/jdedeg",result);
		// if service name is unspecified - falls back to Simbad.
		XMLAssert.assertXpathEvaluatesTo("Simbad","/Sesame/Resolver/@name",result);
		//System.out.println(result);
	}
	
	
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(SesameSystemTest.class));
    }

}
