/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import org.astrogrid.desktop.modules.auth.HeadlessLoginDialogue;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 200612:53:52 PM
 */
public class HeadlessLoginDialogueUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		hld = new HeadlessLoginDialogue("TESTING");
	}
	
	protected HeadlessLoginDialogue hld;

	
	public void testCommunity() {
		assertNull(hld.getCommunity());
		hld.setCommunity("comm");
		assertEquals("comm",hld.getCommunity());
	}
	public void testPassword() {
		assertNull(hld.getPassword());
		hld.setPassword("comm");
		assertEquals("comm",hld.getPassword());
	}
	
	
	public void testUser() {
		assertNull(hld.getUser());
		hld.setUser("comm");
		assertEquals("comm",hld.getUser());
	}
	
	public void testShowDialog() {
		assertFalse(hld.showDialog());
	}


}
