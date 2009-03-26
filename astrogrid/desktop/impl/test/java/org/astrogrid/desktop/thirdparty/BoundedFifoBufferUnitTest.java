/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;

/** tests of some libraries used to build astroscope.
 * @author Noel Winstanley
 * @since May 16, 20064:25:07 PM
 */
public class BoundedFifoBufferUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
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
