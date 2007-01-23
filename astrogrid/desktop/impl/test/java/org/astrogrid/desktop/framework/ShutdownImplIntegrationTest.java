/**
 * 
 */
package org.astrogrid.desktop.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test for shutdown implementation.
 * @author Noel Winstanley
 * @todo debug these.
 * @since Jun 6, 20062:34:02 PM
 */
public class ShutdownImplIntegrationTest extends InARTestCase implements ShutdownListener{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		shutdown = (Shutdown)getACR().getService(Shutdown.class);
		assertNotNull(shutdown);
	}
	
	protected Shutdown shutdown;

	protected void tearDown() throws Exception {
		super.tearDown();
		shutdown = null;
	}
	
	/* Hard to test - requires UI input
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.halt()'
	 */
	/* @todo fix this test
	protected void testHalt() {
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(null);
		shutdown.halt();
		assertEquals(1,lastChanceSeen);
		assertEquals(0,haltingSeen);
	}*/

	/* Doesn't do what I expect at the moment. - seems to require ui input.
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.reallyHalt()'
	 
	protected void testReallyHalt() {
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(null);
		shutdown.reallyHalt();
		assertEquals(0,lastChanceSeen);
		assertEquals(1,haltingSeen);
	}
	
	public void testReminder() {
		fail("Refactor shutdown impl to remove UI part");
	}
*/
	public void testReminder() {
		System.err.println("Implement tests for " + this.getClass().getName());
	}
	public void halting() {
		haltingSeen++;
	}
	private int haltingSeen;

	public String lastChance() {
		lastChanceSeen ++;
		return "under test - don't stop";
	}
	private int lastChanceSeen;
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ShutdownImplIntegrationTest.class));
    }
}
