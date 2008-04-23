/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.astrogrid.desktop.alternatives.SingleSessionManagerUnitTest;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import static org.easymock.EasyMock.*;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:03:54 PM
 */
public class SessionManagerImplUnitTest extends SingleSessionManagerUnitTest {

	protected CommunityInternal comm;
	protected SchedulerInternal sched;

	protected void setUp() throws Exception {
		comm = createNiceMock(CommunityInternal.class);
		
		sched = createNiceMock(SchedulerInternal.class);
		replay(sched);
		
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
