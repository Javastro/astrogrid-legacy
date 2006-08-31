/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.impl.SelectDocumentImpl;
import org.astrogrid.adql.v1_0.beans.impl.TableTypeImpl;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParserUnitTest;
import org.astrogrid.util.DomHelper;
import org.codehaus.xfire.util.STAXUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** System tests for Basic parts of the External Registry Component.
 * @implement implement RoR, and Identitiy.
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryBasicsSystemTest extends TestCase {

	

	protected void setUp() throws Exception {
		super.setUp() ;
		ACR reg = getACR();
		ex =(ExternalRegistry) reg.getService(ExternalRegistry.class);
		assertNotNull(ex);
		Registry internal = (Registry)reg.getService(Registry.class);
		endpoint = internal.getSystemRegistryEndpoint();
		assertNotNull(endpoint);
	}
	
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();
    }   
	protected ExternalRegistry ex;
	protected URI endpoint;
	

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * parser itself is more thoroughly tested by it's own suite.
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#buildResources(org.w3c.dom.Document)}.
	 */
	public void testBuildResources() throws Exception {
		InputStream is = ResourceStreamParserUnitTest.class.getResourceAsStream("multiple.xml");
		assertNotNull(is);
		Document d = DomHelper.newDocument(is);
		assertNotNull(d);
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertEquals(3,res.length);
	}
	
	public void testBuildResourcesEmpty() throws Exception {
		InputStream is = ResourceStreamParserUnitTest.class.getResourceAsStream("noresource.xml");
		assertNotNull(is);
		Document d = DomHelper.newDocument(is);
		assertNotNull(d);
		Resource[] res = ex.buildResources(d);
		assertNotNull(res);
		assertEquals(0,res.length);		
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getIdentity(java.net.URI)}.
	 */
	// fails at moment, as don't have a parser for RegustryService.
	public void testGetIdentity() throws Exception{
		Resource r = ex.getIdentity(endpoint);
		assertNotNull(r);
		assertEquals(endpoint,r.getId());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getIdentityXML(java.net.URI)}.
	 */
	public void testGetIdentityXML() throws Exception {
		Document d = ex.getIdentityXML(endpoint);
		assertNotNull(d);
		XMLAssert.assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource
		Resource[] arr = ex.buildResources(d); // check we can parse it.
		assertEquals(1,arr.length);
	}


	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getRegistryOfRegistriesEndpoint()}.
	 */
	public void testGetRegistryOfRegistriesEndpoint()  throws Exception{
		assertNotNull(ex.getRegistryOfRegistriesEndpoint()); 
		// test it's a queryable registry.
		Document d = ex.getIdentityXML(ex.getRegistryOfRegistriesEndpoint());
		assertNotNull(d);
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getResource(java.net.URI, java.net.URI)}.
	 */
	public void testGetResource()throws Exception {
		URI id = new URI("ivo://org.astrogrid/Pegase");
		Resource r= ex.getResource(endpoint,id);
		assertNotNull(r);
		assertEquals(id,r.getId());
		
		// check it's equal to itself.
		Resource r1= ex.getResource(endpoint,id);	
		assertNotSame(r,r1); // no caching or aliasing happening.
		assertEquals(r,r1);
	}
	
	public void testGetServiceRecord() throws Exception{
		URI id = new URI("ivo://uk.ac.le.star/filemanager");
		Resource r= ex.getResource(endpoint,id);
		assertNotNull(r);
		assertEquals(id,r.getId());		
		assertTrue(r instanceof Service);
	}

	public void testGetResourceUnknown() throws URISyntaxException, ServiceException  {
		URI id = new URI("ivo://org.astrogrid/NonExistentResource");
		
		try {
			Resource r= ex.getResource(endpoint,id);
			fail("expected to throw exception");
		} catch (NotFoundException x) {
			//ok
		} 
	}
	
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getResourceXML(java.net.URI, java.net.URI)}.
	 */
	public void testGetResourceXML() throws Exception {
		URI id = new URI("ivo://uk.ac.le.star/filemanager");
		Document d= ex.getResourceXML(endpoint,id);
		
		
		XMLAssert.assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource
		Resource[] arr = ex.buildResources(d); // check we can parse it.
		assertEquals(1,arr.length);
		assertEquals(id,arr[0].getId());
	}
	/** will fail until we connect to a 1.0 registry */
	public void testRegistryReturnsResourceInCorrectNamespaceXML() throws Exception {
		URI id = new URI("ivo://uk.ac.le.star/filemanager");
		Document d= ex.getResourceXML(endpoint,id);
		XMLAssert.assertXpathEvaluatesTo("http://www.ivoa.net/xml/VOResource/v1.0","namespace-uri(/*)",d);

	}
	
	public void testGetResourceXMLUnknown() throws Exception {
		URI id = new URI("ivo://uk.ac.le.star/NonExistentResource");
		try {
		Document d= ex.getResourceXML(endpoint,id);
		fail("expected to throw exception");
		} catch (NotFoundException x) {
			//ok
		} 
	}


    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ExternalRegistryBasicsSystemTest.class));
    }
}
