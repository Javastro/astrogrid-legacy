/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Manifest;

import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;

import junit.framework.TestCase;

/** tests of components used to build astroscope.
 * @author Noel Winstanley
 * @since May 16, 20064:25:07 PM
 */
public class ScopeUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testManifest() throws IOException {
		InputStream is = this.getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
		assertNotNull(is);
		Manifest mf = new Manifest(is);
		Map m = mf.getEntries();
		for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry)i.next();
			System.out.println(e.getKey() + ":" + e.getValue());
		}
	}
	
	public void testWibble() {
		System.getProperties().list(System.out);
	}
	
	/** test my assumptions of how a buffer works */
	public void testBuffer() {
		BoundedFifoBuffer b = new BoundedFifoBuffer(5);
		b.add("a");
		b.add("b");
		b.add("c");
		b.add("d");
		assertFalse(b.isFull());
		b.add("e");
		assertTrue(b.isFull());
		try {
			b.add("fail");
			fail("expected to overflow");
		} catch (BufferOverflowException e) {
		}
		//Iteratres in oldest-first order.
		Iterator i = b.iterator();
		assertEquals("a",i.next());
		assertEquals("b",i.next());
		assertEquals("c",i.next());
		assertEquals("d",i.next());
		assertEquals("e",i.next());
		assertFalse(i.hasNext());
		
	}

}
