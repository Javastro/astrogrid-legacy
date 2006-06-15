/**
 * 
 */
package org.astrogrid.desktop.framework;

import org.apache.hivemind.ShutdownCoordinator;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** unit test of shutdown.
 * disables the confirmation dialog - so not tested completely.
 * @author Noel Winstanley
 * @since Jun 14, 20061:12:40 PM
 */
public class ShutdownImplUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		coordControl = MockControl.createControl(ShutdownCoordinator.class);
		coord = (ShutdownCoordinator)coordControl.getMock();
		shutdown = new ShutdownImpl(coord,false);
		shutdown.setConfirmIfObjections(false);
		listenerControl = MockControl.createControl(ShutdownListener.class);
		listener = (ShutdownListener)listenerControl.getMock();
	}

	protected MockControl coordControl;
	protected ShutdownCoordinator coord;
	protected MockControl listenerControl;
	protected ShutdownListener listener;
	protected ShutdownImpl shutdown;
	
	/* when halt is called, each listener should be notified once.
	 * no objects - shutdown called.
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.halt()'
	 */
	public void testHalt() {
		listener.lastChance();
		listenerControl.setReturnValue(null);
		listenerControl.replay();
		coord.shutdown();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		
		listenerControl.verify();
		coordControl.verify();		

	}
	
	public void testHaltObjection() {
		listener.lastChance();
		listenerControl.setReturnValue("I object!!");
		listenerControl.replay();
		coord.shutdown();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		
		listenerControl.verify();
		coordControl.verify();		

	}
	/** any listener that throws - shutdown isn't interrupted */
	public void testHaltException() {
		listener.lastChance();
		listenerControl.setThrowable(new RuntimeException("Blerghh"));
		listenerControl.replay();
		coord.shutdown();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		
		listenerControl.verify();
		coordControl.verify();		

	}

	/*
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.reallyHalt()'
	 */
	public void testReallyHalt() {
		listener.halting();
		listenerControl.replay();
		coord.shutdown();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		
		listenerControl.verify();
		coordControl.verify();		
	}
	
	public void testReallyHaltException() {
		listener.halting();
		listenerControl.setThrowable(new RuntimeException("Blerghh"));		
		listenerControl.replay();
		coord.shutdown();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		
		listenerControl.verify();
		coordControl.verify();			
	}


	public void testAddRemoveListener() {
		listenerControl.replay();
		coordControl.replay();
		shutdown.addShutdownListener(listener);
		shutdown.removeShutdownListener(listener);
		
		shutdown.addShutdownListener(null);
		shutdown.removeShutdownListener(null);
		
		listenerControl.verify();
		coordControl.verify();
	}

}
