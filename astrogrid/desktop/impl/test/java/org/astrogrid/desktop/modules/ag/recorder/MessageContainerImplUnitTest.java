/**
 * 
 */
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.IOException;

import junit.framework.TestCase;

import org.astrogrid.WorkbenchAssert;
import org.astrogrid.acr.astrogrid.ExecutionMessage;

/** unit test for message container
 * @author Noel Winstanley
 * @since Jun 13, 20064:06:15 PM
 */
public class MessageContainerImplUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		summary = "suimmary";
		msg = new ExecutionMessage(null,null,null,null,null);
		mci = new MessageContainerImpl(summary,msg);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		summary = null;
		msg = null;
		mci = null;
	}
	String summary;
	ExecutionMessage msg;
	MessageContainerImpl mci;
	
	public void testSerializable() throws IOException, ClassNotFoundException {
		WorkbenchAssert.assertSerializable(mci);
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl.setUnread(boolean)'
	 */
	public void testSetUnread() {
		assertTrue(mci.isUnread());
		mci.setUnread(false);
		assertFalse(mci.isUnread());
		mci.setUnread(true);
		assertTrue(mci.isUnread());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl.getSummary()'
	 */
	public void testGetSummary() {
		assertEquals(summary,mci.getSummary());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl.getMessage()'
	 */
	public void testGetMessage() {
		assertEquals(msg,mci.getMessage());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl.setKey(Long)'
	 */
	public void testSetKey() {
		assertNull(mci.getKey());
		Long l = new Long(42);
		mci.setKey(l);
		assertEquals(l,mci.getKey());
	}
	
	public void testNullKey() {
		try {
			mci.setKey(null);
			fail("expected to fail");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
	}
	
	public void testRepeatedSetKey() {
		testSetKey(); // sets key once.
		Long l = new Long(54);
		try {
			mci.setKey(l);
			fail("expected to fail");
		} catch (IllegalStateException e) {
			// ok.
		}
	}

}
