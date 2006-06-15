/**
 * 
 */
package org.astrogrid.desktop.framework;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpIntegrationTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** test for shutdown implementation.
 * @author Noel Winstanley
 * @todo debug these.
 * @since Jun 6, 20062:34:02 PM
 */
public class ShutdownImplIntegrationTest extends TestCase implements ShutdownListener{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = ACRTestSetup.acrFactory.getACR();
		assertNotNull(reg);
		shutdown = (Shutdown)reg.getService(Shutdown.class);
		assertNotNull(shutdown);
	}
	
	protected Shutdown shutdown;

	
	/* Hard to test - requires UI input
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.halt()'
	 */
	public void dontTestHalt() {
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(this);
		shutdown.addShutdownListener(null);
		shutdown.halt();
		assertEquals(1,lastChanceSeen);
		assertEquals(0,haltingSeen);
	}

	/* Doesn't do what I expect at the moment. - seems to require ui input.
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.reallyHalt()'
	 */
	public void dontTestReallyHalt() {
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
        return new ACRTestSetup(new TestSuite(ShutdownImplIntegrationTest.class));
    }
}
