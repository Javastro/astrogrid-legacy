package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.ivoa.resource.Resource;

/** unit test for the resource stream parser.  - tests the behaviour of the 
 * parser. Other test suites in this package test the results produced by the parser
 * in more detail.*/
public class ResourceStreamParserUnitTest extends AbstractTestForParser{



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
		ResourceParserUnitTest.checkResource(r, "ivo://org.astrogrid/Pegase", "Pegase", "Pegase App", "CeaApplicationType");
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
			assertNotNull(r);
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
		

	// @todo add tests for malformed docs, and docs that express difference between v1.0 and v0.10
	
}
