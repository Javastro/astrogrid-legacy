/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

/** System tests for Keyword Search part of  External Registry Component.
 * @todo get registry oddities sorted.
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryKeywordSystemTest extends InARTestCase {



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
	
	
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#keywordSearch(java.net.URI, java.lang.String, boolean)}.
	 */
	public void testKeywordSearch() throws Exception{
		Resource[] res = ex.keywordSearch(endpoint,"6df",false);
		assertNotNull(res);
		assertTrue(res.length > 0);
		// wo-hoo! abell returns 301 resources. in no problem.
	}
	
	public void testKeywordSearchNone() throws Exception {
		Resource[] res = ex.keywordSearch(endpoint,"nonexistentkeyword",false);
		assertNotNull(res);
		assertEquals(0,res.length);
	}
	
	/** test of our registry implementation - test that the 'or values' flag actually does somethinig */
	public void testKeywordSearchOrValues() throws Exception {
		int count =  ex.keywordSearch(endpoint,"6DF",false).length;
		assertTrue(count > 0);
		int count1 =  ex.keywordSearch(endpoint,"sextractor",false).length;	
		assertTrue(count1 > 0);
		Resource[] res = ex.keywordSearch(endpoint,"6DF sextractor",true); //OR
		assertNotNull(res);
		assertEquals(count + count1,res.length);
		Resource[] res1 = ex.keywordSearch(endpoint,"6DF sextractor",false);//AND
		assertNotNull(res1);		
		assertTrue("OrValues makes no difference",res.length > res1.length);
	}
	/** test of our registry implementaiton - keyword search should be case insensitive, I beleive */
	public void testKeywordSearchCaseInsensitive() throws Exception {
		Resource[] res = ex.keywordSearch(endpoint,"6DF",false);
		assertNotNull(res);
		Resource[] res1 = ex.keywordSearch(endpoint,"6df",false);
		assertNotNull(res1);		
		assertEquals("Registry implementation of keyword search is not case insensitive",
					res.length,res1.length);
	}

	/*
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#keywordSearchXML(java.net.URI, java.lang.String, boolean, boolean)}.
	 */
	public void testKeywordSearchXML() throws Exception{
		Document doc = ex.keywordSearchXML(endpoint,"6df",false); 
		assertNotNull(doc);
		XMLAssert.assertXpathEvaluatesTo("VOResources","local-name(/*)",doc); // root is a VOResources
		XMLAssert.assertXpathEvaluatesTo("Resource","local-name(/*/*)",doc); // it contains a resource
		XMLAssert.assertXpathNotExists("/*/*[local-name() != 'Resource']",doc); // only contains resources
	}
	
	
	public void testKeywordSearchXMLNone() throws Exception {
		Document doc = ex.keywordSearchXML(endpoint,"unknownsearchelement",false); 
		assertNotNull(doc);
		XMLAssert.assertXpathEvaluatesTo("VOResources","local-name(/*)",doc); // root is a VOResources
		XMLAssert.assertXpathNotExists("/*/*",doc); // VOResouces is empty
	}



    public static Test suite() {
        return new ARTestSetup(new TestSuite(ExternalRegistryKeywordSystemTest.class));
    }
}
