/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.HarvestCapability;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.ag.XPathHelper;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParserUnitTest;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import static org.custommonkey.xmlunit.XMLAssert.*;

/** System tests for Basic parts of the External Registry Component.
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryBasicsSystemTest extends InARTestCase {

	

	@Override
    protected void setUp() throws Exception {
		super.setUp() ;
		ACR reg = getACR();
		ex =(ExternalRegistry) reg.getService(ExternalRegistry.class);
		assertNotNull(ex);
		internal = (Registry)reg.getService(Registry.class);
        endpoint = internal.getSystemRegistryEndpoint();
		assertNotNull(endpoint);
	}
	

	protected ExternalRegistry ex;
	protected URI endpoint;
    private Registry internal;
	

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		ex = null;
		endpoint = null;
		internal = null;
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
		assertEquals(4,res.length);
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
	public void testGetIdentity() throws Exception{
	    RegistryService r = ex.getIdentity(endpoint);
		assertNotNull(r);
		assertNotNull(r.getManagedAuthorities());
		assertTrue(r.getManagedAuthorities().length > 0);
		//System.err.println("Registry identitiy is " + r.getId());
		SearchCapability searchCapability = r.findSearchCapability();
		assertNotNull("search capability not available",searchCapability);
	//	System.err.println(searchCapability.getExtensionSearchSupport());
	//	System.err.println(Arrays.asList(searchCapability.getOptionalProtocol()));
		assertTrue(searchCapability.getMaxRecords() > 0);
				
		HarvestCapability harvestCapability = r.findHarvestCapability();
	//	System.err.println(harvestCapability.getStandardID());
		assertNotNull("harvest capabiulity not available",harvestCapability);
		assertTrue(harvestCapability.getMaxRecords() > 0);
		
		//assertTrue("expected to be a full registry ",r.isFull());
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getIdentityXML(java.net.URI)}.
	 */
	public void testGetIdentityXML() throws Exception {
		Document d = ex.getIdentityXML(endpoint);
		assertNotNull(d);
		assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource
		Resource[] arr = ex.buildResources(d); // check we can parse it.
		assertEquals(1,arr.length);
	}


	public void testGetRegistryOfRegistriesEndpoint()  throws Exception{
		assertNull(ex.getRegistryOfRegistriesEndpoint()); 
	}

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#getResource(java.net.URI, java.net.URI)}.
	 */
	public void testGetResource()throws Exception {
		URI id = new URI("ivo://nasa.heasarc/ASD");
		Resource r= ex.getResource(endpoint,id);
		assertNotNull(r);
		assertEquals(id,r.getId());
		
		// check it's equal to itself.
		Resource r1= ex.getResource(endpoint,id);	
		assertNotSame(r,r1); // no caching or aliasing happening.
		assertEquals(r,r1);
	}
	
	/** specify registry to query by URI, rather than URL
	 * use the 'internal' registry as the one to query.
	 * @throws Exception
	 */
	public void testGetResourceExternalRegistryByURI() throws Exception {
	    // first find the id of the registry we're calling...
	    RegistryService identity = internal.getIdentity();
	    URI regID = identity.findSearchCapability().getInterfaces()[0].getAccessUrls()[0].getValueURI();
	    assertNotNull(regID);

	       URI id = new URI("ivo://nasa.heasarc/ASD");
	        Resource r= ex.getResource(regID,id);
	        assertNotNull(r);
	        assertEquals(id,r.getId());
	}
	
	public void testGetServiceRecord() throws Exception{
		URI id = new URI("ivo://nasa.heasarc/skyview/2mass");
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
        URI id = new URI("ivo://nasa.heasarc/ASD");
		Document d= ex.getResourceXML(endpoint,id);
		
		
		assertXpathEvaluatesTo("Resource","local-name(/*)",d); // root is a Resource
		Resource[] arr = ex.buildResources(d); // check we can parse it.
		assertEquals(1,arr.length);
		assertEquals(id,arr[0].getId());
	}
	
	// complex this.
	// according to the wsdl and schemas, we should be getting a Resource element in the vor: namespace
	// which has _type_ vr:Resource.
	public void testRegistryReturnsResourceInCorrectNamespaceXML() throws Exception {
        URI id = new URI("ivo://nasa.heasarc/ASD");
		Document d= ex.getResourceXML(endpoint,id);
		DomHelper.DocumentToStream(d,System.out);
		assertXpathEvaluatesTo(XPathHelper.VOR_NS,"namespace-uri(/*)",d);

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
        return new ARTestSetup(new TestSuite(ExternalRegistryBasicsSystemTest.class));
    }
}
