/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.util.DomHelper;
import static org.custommonkey.xmlunit.XMLAssert.*;
import org.w3c.dom.Document;

/** System tests for XQuery part of  External Registry Component.
 * @todo find out from kevin what the document root is called - should be more efficient than //vr:Resource
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryXQuerySystemTest extends InARTestCase {

	/**
	 *  simple xpath that returns a scalar value - no xml
	 */
	private static final String SCALAR_XPATH_XQUERY = "count(//vor:Resource)";
	/**
	 * returns a scalar value in a templagte.
	 */
	private static final String SCALAR_XPATH_TEMPLATE_XQUERY = "<result>{count(//vor:Resource) != 0}</result>";
	/**
	 * id of resource searched for in SINGLE resourtce query.
	 */
	public static final String SINGLE_RESOURCE_XQUERY_ID = "ivo://nasa.heasarc/ASD";
	/**
	 * query that returns a single resource
	 */
	
	private static final String SINGLE_RESOURCE_XQUERY = "//vor:Resource[identifier='" + SINGLE_RESOURCE_XQUERY_ID + "']";
  //  private static final String SINGLE_RESOURCE_XQUERY = "RootResource/vor:Resource[identifier='" + SINGLE_RESOURCE_XQUERY_ID + "']";

	//KMB 	private static final String SINGLE_RESOURCE_XQUERY = "//RootResource[vr:identifier='" + SINGLE_RESOURCE_XQUERY_ID + "']";
	
	/**
	 * query that returns multiople resources.
	 */
	private static final String MULTIPLE_RESOURCE_XQUERY = "//vor:Resource[@xsi:type &= '*:Authority']";
//KMB private static final String MULTIPLE_RESOURCE_XQUERY = "//RootResource[@xsi:type &= '*:Authority']";

	/** an xpath query containing an unknown function */
	private static final String INVALID_RESOURCE_XQUERY = "//vor:Resource[fred() != 32]";
//KMB private static final String INVALID_RESOURCE_XQUERY = "//RootResource[fred() != 32]";
	
	/** an xpath query contining malformed xml */
	private static final String MALFORMED_RESOURCE_XQUERY = "<foo>{" + SINGLE_RESOURCE_XQUERY + "}<bar></foo>";
	/** causes a more subtle error - which causes problems elsewhere in the code */
	private static final String INVALID_XPATH_XQUERY = "<result>{count(//vor:Resource) &lt;  0}</result>";
	
	@Override
    protected void setUp() throws Exception {
		super.setUp() ;
		ACR reg = getACR();
		ex =(ExternalRegistry) reg.getService(ExternalRegistry.class);
		assertNotNull(ex);
		Registry internal = (Registry)reg.getService(Registry.class);
		endpoint = internal.getSystemRegistryEndpoint();
		assertNotNull(endpoint);
	}

	protected ExternalRegistry ex;
	protected URI endpoint;
	

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		ex = null;
		endpoint = null;
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#xquerySearch(java.net.URI, java.lang.String)}.
	 */
	public void testXquerySearchOne() throws Exception{
		Resource[] r = ex.xquerySearch(endpoint,SINGLE_RESOURCE_XQUERY);
		assertNotNull(r);
		assertEquals(1,r.length);
		assertEquals(SINGLE_RESOURCE_XQUERY_ID,r[0].getId().toString());		
	}
	
	public void testXquerySearchMany() throws Exception{
		Resource[] r = ex.xquerySearch(endpoint,MULTIPLE_RESOURCE_XQUERY);
		assertNotNull(r);
		assertTrue(r.length > 1);		
	}

	/** document with no resource elemenrts */
	public void testXquerySearchNone() throws Exception{
		Resource[] r = ex.xquerySearch(endpoint,SCALAR_XPATH_TEMPLATE_XQUERY);
		assertNotNull(r);
		assertEquals(0,r.length);
	}
	
	/**valid query, but doesn't produce a root element */
	public void testXquerySearchEmpty() throws Exception{
		try {			
		Resource[] r = ex.xquerySearch(endpoint,SCALAR_XPATH_XQUERY);
		fail("expected to chuck");
	} catch (ServiceException e) {
		// expected
	}
	}
	
	/** what happens on an xquery syntax error */
	public void testXQuerySearchInvalidXQuery() throws Exception{
		try {
		Resource[] r = ex.xquerySearch(endpoint,INVALID_RESOURCE_XQUERY);
		fail("expexted to chuck");
		} catch (ServiceException e) {
		//	e.printStackTrace(); // see what the error reporting is like.
		}
	}

	/** what happens on an malformed xml in xquery  error */
	public void testXQuerySearchMalformedXQuery() throws Exception{
		try {
			Resource[] r = ex.xquerySearch(endpoint,MALFORMED_RESOURCE_XQUERY);
			fail("expexted to chuck");
			} catch (ServiceException e) {
		//		e.printStackTrace(); // see what the error reporting is like.
			}
	}
	

	/** test xpath returning string embedded in a template */
	public void testScalarReturningTemplateXquerySearchXML() throws Exception{
		Document d = ex.xquerySearchXML(endpoint,SCALAR_XPATH_TEMPLATE_XQUERY);
		assertXpathEvaluatesTo("result","local-name(/*)",d); // root is a result tag
		assertXpathNotExists("/*/*",d); // no child tags
		assertXpathEvaluatesTo("true","/result",d);
	}
	/** try an xpath returning a single resouce document */
	public void testSingleResourceXquerySearchXML() throws Exception{
		Document d = ex.xquerySearchXML(endpoint,SINGLE_RESOURCE_XQUERY);
		assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource tag
		// feed throug document parser
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertEquals(1,res.length);
		assertEquals(SINGLE_RESOURCE_XQUERY_ID,res[0].getId().toString());
	}
	
	/** try an xpath returning a single resouce document within a template. */
	public void testSingleResourceTemplateXquerySearchXML() throws Exception{
		Document d = ex.xquerySearchXML(endpoint,"<result>{//vor:Resource[identifier='ivo://nasa.heasarc/ASD']}</result>");
		assertXpathEvaluatesTo("result","local-name(/*)",d); // root is a some arbitrary tag
		// feed throug document parser
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertEquals(1,res.length);
		assertEquals(SINGLE_RESOURCE_XQUERY_ID,res[0].getId().toString());
	}
	
	/** try an xpath returning multiple resouce document 
	 * 
	 * this does a lengthy query, but only the first one makes it into the document.
	 * */
	public void testMultipleResourceXquerySearchXML() throws Exception{
		Document d = ex.xquerySearchXML(endpoint,MULTIPLE_RESOURCE_XQUERY);
		assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource tag
		// feed throug document parser
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertEquals(1,res.length);
	}
	
	/** same xpath in a template - gets us back a set of results.*/
	public void testMultipleResourceTemplateXquerySearchXML() throws Exception{
		Document d = ex.xquerySearchXML(endpoint,"<result>{//vor:Resource[@xsi:type &= '*:Authority']}</result>");
		assertXpathEvaluatesTo("result","local-name(/*)",d); // root is a Resource tag
		// feed throug document parser
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertTrue(res.length > 1);
	}
	

	public void testXQuerySearchXMLInvalid() throws Exception{
		try {
			Document d = ex.xquerySearchXML(endpoint,INVALID_XPATH_XQUERY);
			fail("expexted to chuck");
			} catch (ServiceException e) {
			//	e.printStackTrace(); // see what the error reporting is like.
			}
	}
	
	public void testXQuerySearchXMLMalformed() throws Exception{
		try {
			Document d = ex.xquerySearchXML(endpoint,MALFORMED_RESOURCE_XQUERY);
			DomHelper.DocumentToStream(d,System.out);
			fail("expexted to chuck");
			} catch (ServiceException e) {
			///	e.printStackTrace(); // see what the error reporting is like.
			}
	}

    public static Test suite() {
        return new ARTestSetup(new TestSuite(ExternalRegistryXQuerySystemTest.class));
    }
}
