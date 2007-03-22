/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.astrogrid.desktop.alternatives.SingleSessionManager;
import org.astrogrid.desktop.alternatives.SingleSessionManagerUnitTest;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.easymock.MockControl;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:03:54 PM
 */
public class SessionManagerImplUnitTest extends SingleSessionManagerUnitTest {

	protected MockControl commControl;
	protected CommunityInternal comm;
	protected MockControl schedControl;
	protected SchedulerInternal sched;

	protected void setUp() throws Exception {
		this.commControl = MockControl.createNiceControl(CommunityInternal.class);
		comm = (CommunityInternal)commControl.getMock();
		
		this.schedControl = MockControl.createNiceControl(SchedulerInternal.class);
		sched = (SchedulerInternal)schedControl.getMock();
		
		
		schedControl.replay();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected SessionManagerInternal createSessionManager() {
		return new SessionManagerImpl(ss,ws,comm,sched);
	}
	
	// overridden, as this session manager does allow new sessions.
	public void testNewSessionsAllowed() throws Exception {
		String id = sm.createNewSession(10);
		assertNotNull(id);
		assertTrue(sm.exists(id));
		assertNotNull(sm.findSessionForKey(id));
		sm.dispose(id);
		assertFalse(sm.exists(id));
		assertNull(sm.findSessionForKey(id));
		
	}
}
