/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

/** System tests for ADQL querying part of External Registry Component.
 * @implement
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryADQLSystemTest extends InARTestCase {



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
	

	protected void tearDown() throws Exception {
		super.tearDown();
		ex = null;
		endpoint = null;
	}
	 public static final String ADQLS_QUERY_STRING = "select * from Registry where vr:identifier='ivo://uk.ac.le.star/filemanager'";
	  
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlsSearch(java.net.URI, java.lang.String)}.
	 */
	public void testAdqlsSearch() throws Exception{
		Resource[] r = ex.adqlsSearch(endpoint,ADQLS_QUERY_STRING );
		assertNotNull(r);
		assertEquals(1,r.length);
		assertEquals("ivo://uk.ac.le.star/filemanager",r[0].getId().toString());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlsSearchXML(java.net.URI, java.lang.String, boolean)}.
	 */
	public void testAdqlsSearchXML() throws Exception{
		Document d = ex.adqlsSearchXML(endpoint,ADQLS_QUERY_STRING,false);
		assertNotNull(d);
		
		XMLAssert.assertXpathEvaluatesTo("VOResources","local-name(/*)",d); // root is a VOResources
		XMLAssert.assertXpathEvaluatesTo("Resource","local-name(/*/*)",d); // it contains a resource
		XMLAssert.assertXpathNotExists("/*/*[local-name() != 'Resource']",d); // only contains resources

		
		// feed it through the parser.
		Resource[] r = ex.buildResources(d);
		assertNotNull(r);
		assertEquals(1,r.length);
		assertEquals("ivo://uk.ac.le.star/filemanager",r[0].getId().toString());	
	}
	
	public void testAdqlsSearchXMLIdentifiersOnly() throws Exception{
		Document d = ex.adqlsSearchXML(endpoint,ADQLS_QUERY_STRING,true);
		assertNotNull(d);

	}

	
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlxSearch(java.net.URI, org.w3c.dom.Document)}.
	 */
//	public void testAdqlxSearch() throws Exception{
//		fail("Not yet implemented");
//	}
//	@FIXME implement these.
//	public void testAdqlxSearchEmpty() throws Exception{
//		fail("Not yet implemented");
//	}
//	
//	public void testAdqlxSearchInvalid() throws Exception{
//		fail("Not yet implemented");
//	}
//		
//	/**
//	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlxSearchXML(java.net.URI, org.w3c.dom.Document, boolean)}.
//	 */
//	public void testAdqlxSearchXML()throws Exception {
//		fail("Not yet implemented");
//		// can't work out how to construct a query.
//	}
//
//
//	public void testAdqlxSearchXMLEmpty()throws Exception {
//		fail("Not yet implemented");
//	}
//	
//	public void testAdqlxSearchXMLInvalid()throws Exception {
//		fail("Not yet implemented");
//	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ExternalRegistryADQLSystemTest.class));
    }
}
