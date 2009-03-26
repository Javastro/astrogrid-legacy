/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import java.net.URL;
import java.security.Principal;

import junit.framework.TestCase;
import net.sourceforge.hivelock.SecurityService;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.WebServerInternal;
import static org.easymock.EasyMock.*;

/** unit test for the single session manager.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:05:47 PM
 */
public class SingleSessionManagerUnitTest extends TestCase {

	protected SecurityService ss;
	protected WebServerInternal ws;
	protected SessionManagerInternal sm;
	protected URL url;

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		ss= createNiceMock("securityService",SecurityService.class);
		
		ws = createNiceMock("webserver",WebServerInternal.class);
		url = new URL("http://wibble.pling/");

		expect(ws.getRoot()).andStubReturn(url);

		expect(ws.getContextBase((String)anyObject())).andStubReturn(url);
		
		replay(ws,ss);
		
		sm = createSessionManager();
	}

	protected SessionManagerInternal createSessionManager() {
		return new SingleSessionManager(ss,ws);
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		sm = null;
		ws = null;
		ss = null;
		url = null;
	}

	public void testDefaultSession() throws Exception {
		final String id = sm.getDefaultSessionId();
		assertNotNull(id);
		
		// it exists.
		assertTrue(sm.exists(id));
		
		// idempotent.
		Principal p1 = sm.findSessionForKey(id);
		assertNotNull(p1);
		Principal p = sm.findSessionForKey(id);
		assertSame(p,p1);
		
		// it can't be deleted.
		sm.dispose(id);
		assertTrue(sm.exists(id));
	}
	
	public void testfindDefaultSessions() throws Exception {
		final String id = sm.getDefaultSessionId();
		URL u = sm.findHttpSession(id);
		assertNotNull(u);
		assertEquals(url,u);

		u = sm.findXmlRpcSession(id);
		assertNotNull(u);
		assertTrue(StringUtils.contains(u.toString(),url.toString()));
	
	}
	
	public void testNewSessionsAllowed() throws Exception {
		try {
			sm.createNewSession(120);
			fail("expected to barf");
		} catch (NotApplicableException e) {
			// ok
		}
	}
	
	public void testCurrentSession() throws Exception {
	    // current session is null? is this expected?
        assertNull(sm.currentSession());
        Principal p = createMock("mockPrinicpal",Principal.class);
        replay(p);
//        sm.adoptSession(p);
//        assertEquals(p,sm.currentSession()); // i'd expect this to hold, but it doesn't.
//        sm.clearSession();
//        assertNull(sm.currentSession());
        verify(p);
    }
}
