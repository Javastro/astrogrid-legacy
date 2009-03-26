/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import static org.custommonkey.xmlunit.XMLAssert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** test the connection to sesame name resolver.
 * also acts as a barrier test - detects any changes made to this service
 * - important as used elsewhere in the UI.
 *  - hence we're comparing against local copies of the published schema.
 *  
 *  NB: first three tests refer to the cache - ensure cache is cleaned before runing tests.
 * @author Noel Winstanley
 * @since Jun 9, 20066:01:46 PM
 */
public class SesameSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		sesame = assertServiceExists(Sesame.class,"cds.sesame");
	
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		sesame = null;
	}
	protected Sesame sesame;


	public void testResolve() throws Exception {
		SesamePositionBean pos = sesame.resolve("crab");
		assertNotNull(pos);
		assertEquals("crab",pos.getTarget());
		assertNotNull(pos.getService());
		assertNotNull(pos.getPosStr());
		assertTrue( pos.getRa() != 0.0);
		assertTrue(pos.getDec() != 0.0);
		String[] aliases = pos.getAliases();
		assertNotNull(aliases);
	//	assertTrue(aliases.length > 0);
		
	}
	

    public void testResolveM32() throws Exception {
        SesamePositionBean pos = sesame.resolve("m32");
        assertNotNull(pos);
        assertEquals("m32",pos.getTarget());
        assertNotNull(pos.getService());
        assertNotNull(pos.getPosStr());
        assertTrue( pos.getRa() != 0.0);
        assertTrue(pos.getDec() != 0.0);
        String[] aliases = pos.getAliases();
        assertNotNull(aliases);
      //  assertTrue(aliases.length > 0);
    }
	
	   public void testResolveWithSpace() throws Exception {
	        SesamePositionBean pos = sesame.resolve("NGC 4321");
	        assertNotNull(pos);    
	        assertEquals("NGC 4321",pos.getTarget());
	        assertNotNull(pos.getService());
	        assertNotNull(pos.getPosStr());
	        assertTrue( pos.getRa() != 0.0);
	        assertTrue(pos.getDec() != 0.0);
	        String[] aliases = pos.getAliases();
	        assertNotNull(aliases);
	        assertTrue(aliases.length > 0);
	    }
	    
	
	public void testResolveUnknown() throws ServiceException {
		try {
			
			SesamePositionBean pos = sesame.resolve("unknown_object");
			fail("expected not to be found");
		} catch (NotFoundException e) {
			// ok.
		}
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.SesameDynamicImpl.sesame(String, String)'
	 */
	public void testSesame() throws  Exception {
		String result = sesame.sesame("m32","x");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		DomHelper.newDocument(result); // checks is well-formed xml
		// 2 attributes used by us.
		assertXpathExists("/Sesame/Resolver/jradeg",result);
		assertXpathExists("/Sesame/Resolver/jdedeg",result);		

	}

	public void testSesameAllIdentifiers() throws ServiceException, ParserConfigurationException, SAXException, IOException {
		String result = sesame.sesame("m32","xi");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml


	}	
	
	public void testSesameUnknown() throws Exception {
		String result = sesame.sesame("notKnownObject","x");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml
		assertXpathNotExists("/Sesame/Resolver/jradeg",result);
		assertXpathExists("/Sesame/Resolver",result);

	}	
	

	
	public void testSesameEmpty() throws  Exception {
		String result = sesame.sesame("","x");
		assertNotNull(result);
		DomHelper.newDocument(result); // checks is well-formed xml
		assertXpathNotExists("/Sesame/Resolver",result);
		assertXpathExists("/Sesame",result);
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
// NED connection seems to be permenently broken.
//	public void testSesameChooseService() throws ServiceException, ParserConfigurationException, SAXException, IOException, TransformerException {
//		String result = sesame.sesameChooseService("m32","x",true,"N");
//		assertNotNull(result);
//		System.out.println(result);
//		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
//		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
//		DomHelper.newDocument(result); // checks is well-formed xml
//		// 2 attributes used by us.
//		XMLAssert.assertXpathExists("/Sesame/Resolver/jradeg",result);
//		XMLAssert.assertXpathExists("/Sesame/Resolver/jdedeg",result);		
//		// if service name is unspecified - falls back to Simbad.
//		XMLAssert.assertXpathEvaluatesTo("Ned","/Sesame/Resolver/@name",result);
//
//	}
	
	public void testSesameChooseServiceNone() throws  Exception {
		String result = sesame.sesameChooseService("m32","x",true,"");
		assertNotNull(result);
		// doesn't validate against published DTD - as has xsi:schemalocation, etc in.
		///	AstrogridAssert.assertDTDValid(result,"Sesame",dtdURL);
		DomHelper.newDocument(result); // checks is well-formed xml
		// 2 attributes used by us.
		assertXpathExists("/Sesame/Resolver/jradeg",result);
		assertXpathExists("/Sesame/Resolver/jdedeg",result);
		// if service name is unspecified - falls back to Simbad.
		assertXpathEvaluatesTo("Simbad","/Sesame/Resolver/@name",result);
		//System.out.println(result);
	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(SesameSystemTest.class));
    }

}
