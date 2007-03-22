/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import java.net.URL;
import java.security.Principal;

import net.sourceforge.hivelock.SecurityService;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.WebServerInternal;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** unit test for the single session manager.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:05:47 PM
 */
public class SingleSessionManagerUnitTest extends TestCase {

	protected MockControl ssControl;
	protected SecurityService ss;
	protected MockControl wsControl;
	protected WebServerInternal ws;
	protected SessionManagerInternal sm;
	protected URL url;

	protected void setUp() throws Exception {
		super.setUp();
		this.ssControl = MockControl.createNiceControl(SecurityService.class);
		ss= (SecurityService)ssControl.getMock();
		
		this.wsControl = MockControl.createNiceControl(WebServerInternal.class);
		ws = (WebServerInternal)wsControl.getMock();
		url = new URL("http://wibble.pling/");

		ws.getRoot();
		wsControl.setDefaultReturnValue(url);

		ws.getContextBase(null); // for subclass test.
		wsControl.setDefaultReturnValue(url);
		
		wsControl.replay();
		ssControl.replay();
		
		sm = createSessionManager();
	}

	protected SessionManagerInternal createSessionManager() {
		return new SingleSessionManager(ss,ws);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		sm = null;
		ws = null;
		wsControl = null;
		ss = null;
		ssControl = null;
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
}
