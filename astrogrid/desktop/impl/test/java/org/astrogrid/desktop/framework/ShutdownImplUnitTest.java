/**
 * 
 */
package org.astrogrid.desktop.framework;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hivemind.ShutdownCoordinator;
import org.astrogrid.acr.builtin.ShutdownListener;
import static org.easymock.EasyMock.*;
/** unit test of shutdown.
 * disables the confirmation dialog - so not tested completely.
 * @author Noel Winstanley
 * @since Jun 14, 20061:12:40 PM
 */
public class ShutdownImplUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		coord = createMock("coordinator",ShutdownCoordinator.class);
		shutdown = new ShutdownImpl(coord,false);		
		shutdown.setConfirmIfObjections(false);
		listener = createMock("listener",ShutdownListener.class);
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		coord = null;
		listener = null;
		shutdown = null;
	}
	
	protected ShutdownCoordinator coord;
	protected ShutdownListener listener;
	protected ShutdownImpl shutdown;
	
	/* when halt is called, each listener should be notified once.
	 * no objects - shutdown called.
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.halt()'
	 */
	public void testHalt() {
		expect(listener.lastChance()).andReturn(null);
		listener.halting();
		coord.shutdown();
		replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		verify(listener,coord);	

	}
	
	public void testHaltObjection() {
		expect(listener.lastChance()).andReturn("I object!!");
		listener.halting();	
		coord.shutdown();
		replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
		verify(listener,coord);

	}
	/** any listener that throws - shutdown isn't interrupted */
	public void testHaltException() {
		expect(listener.lastChance()).andThrow(new RuntimeException("Expected exception thrown during testing"));
		listener.halting();	
		coord.shutdown();
		replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.halt();
        verify(listener,coord);

	}

	/*
	 * Test method for 'org.astrogrid.desktop.framework.ShutdownImpl.reallyHalt()'
	 */
	public void testReallyHalt() {
		listener.halting();
		coord.shutdown();
		replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.reallyHalt();
		verify(listener,coord);
	}
	
	public void testReallyHaltException() {
		listener.halting();
		expectLastCall().andThrow(new RuntimeException("Expected exception thrown during testing"));		
		coord.shutdown();
        replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.addShutdownListener(listener);
		shutdown.reallyHalt();
        verify(listener,coord);		
	}


	public void testAddRemoveListener() {
	    coord.shutdown();
	    expectLastCall().asStub();
        replay(listener,coord);
		shutdown.addShutdownListener(listener);
		shutdown.removeShutdownListener(listener);
		
		shutdown.addShutdownListener(null);
		shutdown.removeShutdownListener(null);
        verify(listener,coord);
	}
	
	public void testFmtEmpty() throws Exception {
		List<Object> l = new ArrayList<Object>();
		String s= shutdown.fmt(l);
		assertNotNull(s);
		assertTrue(s.trim().length() > 0);
	}
	
	public void testFmt() throws Exception {
		List<Object> l = new ArrayList<Object>();
		l.add(new Object());
		String s= shutdown.fmt(l);
		assertNotNull(s);
		assertTrue(s.trim().length() > 0);		
	}
	
	

}
