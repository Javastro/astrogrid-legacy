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
import static org.custommonkey.xmlunit.XMLAssert.*;
import org.w3c.dom.Document;

/** System tests for Keyword Search part of  External Registry Component.
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryKeywordSystemTest extends InARTestCase {



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
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#keywordSearch(java.net.URI, java.lang.String, boolean)}.
	 */
	public void testKeywordSearch() throws Exception{
		Resource[] res = ex.keywordSearch(endpoint,"heao",false);
		assertNotNull(res);
		assertTrue(res.length > 0);
	}
	
	public void testKeywordSearchNone() throws Exception {
		Resource[] res = ex.keywordSearch(endpoint,"nonexistentkeyword",false);
		assertNotNull(res);
		assertEquals(0,res.length);
	}
	
	/** fails - our implementation ignores the 'or' flag */
	public void testKeywordSearchOrValues() throws Exception {
		int regCount =  ex.keywordSearch(endpoint,"registry",false).length;
	//	System.err.println("Matches for 'registry' " + regCount );
		assertTrue(regCount > 0);
		int heasCount =  ex.keywordSearch(endpoint,"heasarc",false).length;
	//	System.err.println("Matches for 'heasarc' " + heasCount);
		assertTrue(heasCount > 0);
		Resource[] or = ex.keywordSearch(endpoint,"registry heasarc",true); //OR
		assertNotNull(or);
		Resource[] and = ex.keywordSearch(endpoint,"registry heasarc",false);//AND
		assertNotNull(and);		
		
		//System.err.println("Matches for OR 'registry heasarc' " + or.length);
		//System.err.println("Matches for AND 'registry heasarc' " + and.length);
		
		assertTrue(or.length >= regCount);
		assertTrue(or.length >= heasCount);
		assertTrue(and.length < or.length);
		assertTrue(and.length < regCount);
	}
	/** test of our registry implementaiton - keyword search should be case insensitive, I beleive */
	public void testKeywordSearchCaseInsensitive() throws Exception {
		Resource[] res = ex.keywordSearch(endpoint,"registry",false);
		assertNotNull(res);
		Resource[] res1 = ex.keywordSearch(endpoint,"REGISTRY",false);
		assertNotNull(res1);		
		assertEquals("Registry implementation of keyword search is not case insensitive",
					res.length,res1.length);
	}

	/*
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#keywordSearchXML(java.net.URI, java.lang.String, boolean, boolean)}.
	 */
	public void testKeywordSearchXML() throws Exception{
		Document doc = ex.keywordSearchXML(endpoint,"heao",false); 
		assertNotNull(doc);
		assertXpathEvaluatesTo("VOResources","local-name(/*)",doc); // root is a VOResources
		assertXpathEvaluatesTo("Resource","local-name(/*/*)",doc); // it contains a resource
		assertXpathNotExists("/*/*[local-name() != 'Resource']",doc); // only contains resources
	}
	
	
	public void testKeywordSearchXMLNone() throws Exception {
		Document doc = ex.keywordSearchXML(endpoint,"unknownsearchelement",false); 
		assertNotNull(doc);
		assertXpathEvaluatesTo("VOResources","local-name(/*)",doc); // root is a VOResources
		assertXpathNotExists("/*/*",doc); // VOResouces is empty
	}



    public static Test suite() {
        return new ARTestSetup(new TestSuite(ExternalRegistryKeywordSystemTest.class));
    }
}
