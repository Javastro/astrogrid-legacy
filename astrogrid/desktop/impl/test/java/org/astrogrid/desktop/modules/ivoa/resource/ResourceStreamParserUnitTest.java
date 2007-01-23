package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;

/** unit test for the resource stream parser. */
public class ResourceStreamParserUnitTest extends TestCase {

	XMLInputFactory fac ;
	
	/** test we can parse a single entry in a file */
	public void testSingleResource() throws Exception{
		InputStream is = this.getClass().getResourceAsStream("pegase.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		assertTrue(p.hasNext());
		Object o = p.next();
		assertNotNull(o);
		assertTrue(o instanceof Resource);
		Resource r = (Resource)o;
		validateResource(r);
		assertFalse(p.hasNext());
	}
	/** expect remove to not be implemented */
	public void testRemove() throws Exception{
		InputStream is = this.getClass().getResourceAsStream("pegase.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		assertTrue(p.hasNext());
		Object o = p.next();
		assertNotNull(o);
		try {
			p.remove();
			fail("expected to chuck");
		} catch (UnsupportedOperationException e) {
			// ok
		}
	}
	
	/** expect next to throw when it's empty */
	public void testNextEmpty() throws Exception{
		InputStream is = this.getClass().getResourceAsStream("pegase.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		assertTrue(p.hasNext());
		Object o = p.next();
		assertNotNull(o);
		assertFalse(p.hasNext());
		try {
			p.next();
			fail("expected to chuck");
		} catch (NoSuchElementException e) {
			// ok
		}
	}
/** test parsing of a resource nested within other xml */
	public void testNestedResource() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("pegase-nested.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		assertTrue(p.hasNext());
		Object o = p.next();
		assertNotNull(o);
		assertTrue(o instanceof Resource);
		Resource r = (Resource)o;
		validateResource(r);
		assertFalse(p.hasNext());		
	}
	/** test parsing of a document containig not resources (but other xml) */
	public void testEmpty() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("noresource.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		assertFalse(p.hasNext());
		assertFalse(p.hasNext());		
	}
	/** test parsing of a document copntainig multiple resources - nested within all sorts of other xml */
	public void testMultipleResource() throws Exception{
		InputStream is = this.getClass().getResourceAsStream("multiple.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		int count = 0;
		while (p.hasNext()) {
			count ++;
			Object o = p.next();
			assertNotNull(o);
			assertTrue(o instanceof Resource);
			Resource r = (Resource)o;
			validateResource(r);
		} 
		assertEquals(3,count);
		assertFalse(p.hasNext());		
	}

	/** tests the implementation of equals,  toString(), etc on resource */
	public void testObjectMethods() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("multiple.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);
//sanity check
		assertNotNull(arr);
		assertEquals(3,arr.length);
		assertTrue(Proxy.isProxyClass(arr[0].getClass()));
//equals;
		assertEquals(arr[0],arr[0]);
		assertEquals(arr[1],arr[1]);
		assertEquals(arr[2],arr[2]);
		assertTrue(! arr[0].equals(arr[1]));
		assertTrue(! arr[1].equals(arr[2]));
		assertTrue(! arr[2].equals(arr[0]));
		
// hashcode.
		assertTrue(arr[0].hashCode() != arr[1].hashCode());
		assertTrue(arr[1].hashCode() != arr[2].hashCode());
		assertTrue(arr[2].hashCode() != arr[0].hashCode());
// toString
		assertNotNull(arr[0].toString());
		assertNotNull(arr[1].toString());
		assertNotNull(arr[2].toString());		

	} 
	
	public void testService() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("multiple.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		assertTrue(arr[0] instanceof Service);
		Service s = (Service)arr[0];
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNull(c.getDescription());
		assertNotNull(c.getType());
		assertEquals("vs:WebService",c.getType());
		assertEquals(1,c.getInterfaces().length);
		assertNotNull(c.getInterfaces()[0].getAccessUrls()[0].getValue());
		
		assertFalse(arr[1] instanceof Service);
		
		assertTrue(arr[2] instanceof Service);
		
		
	}
	
	public void testCeaService() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("multiple.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		assertTrue(arr[0] instanceof Service);
		Service s = (Service)arr[0];
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNotNull(c);
		assertTrue(arr[0] instanceof CeaService);
		assertTrue(c instanceof CeaServerCapability);
		assertSame(c,((CeaService)arr[0]).findCeaServerCapability());
	}
	

	public void testCeaApplication() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("pegase.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
	
		assertTrue(arr[0] instanceof CeaApplication) ;
		CeaApplication a = (CeaApplication)arr[0];
		assertNotNull(a.getInterfaces());
		assertEquals(1,a.getInterfaces().length);
		assertEquals("simple",a.getInterfaces()[0].getName());
		
		assertNotNull(a.getInterfaces()[0].getInputs());
		assertEquals(18,a.getInterfaces()[0].getInputs().length);		
		assertNotNull(a.getInterfaces()[0].getOutputs());
		assertEquals(2,a.getInterfaces()[0].getOutputs().length);
		
		assertNotNull(a.getParameters());
		assertEquals(20,a.getParameters().length);
		
	}
	
	public void testCatalog() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("catalog.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
	
		assertTrue(arr[0] instanceof DataCollection) ;
		DataCollection a = (DataCollection)arr[0];
		assertNotNull(a.getCatalog());
		Catalog c = a.getCatalog();
		TableBean[] tables = c.getTables();
		assertNotNull(tables);
		assertEquals(17,tables.length);
		assertEquals("yohkoh_flare_list",tables[16].getName());
		assertEquals(15,tables[16].getColumns().length);
		
	}
	
	public void testConeService() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("cone.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		assertTrue(arr[0] instanceof Service);
		Service s = (Service)arr[0];
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNotNull(c);
		assertTrue(arr[0] instanceof ConeService);
		assertTrue(c instanceof ConeCapability);
		assertSame(c,((ConeService)arr[0]).findConeCapability());
	}
	
	
	public void testSiapService() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("siap.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		assertTrue(arr[0] instanceof Service);
		Service s = (Service)arr[0];
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNotNull(c);
		assertTrue(arr[0] instanceof SiapService);
		assertTrue(c instanceof SiapCapability);
		assertSame(c,((SiapService)arr[0]).findSiapCapability());
	}
	
// @todo test multiple subclsses once we get to new schema.

	/**
	 * @param r
	 */
	private void validateResource(Resource r) {
		assertNotNull(r.getStatus());
		assertNotNull(r.getId());
		System.out.println(r.getId());
		assertNotNull(r.getTitle());
		System.out.println(r.getTitle());
		Content c = r.getContent();
		assertNotNull(c);
		assertNotNull(c.getDescription());
		System.out.println(c.getDescription());
		assertNotNull(c.getReferenceURL());
		Curation cu = r.getCuration();
		assertNotNull(cu);
		ResourceName pub = cu.getPublisher();
		assertNotNull(pub);
		assertNotNull(pub.getValue());
		System.out.println(pub.getValue());
		Contact con = cu.getContacts()[0];
		assertNotNull(con);
		assertNotNull(con.getName());
		System.out.println(con.getName().getValue());
		
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		fac = XMLInputFactory.newInstance();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		fac = null;
	}
	
	// @todo add tests for malformed docs, and docs that express difference between v1.0 and v0.10
	
}
