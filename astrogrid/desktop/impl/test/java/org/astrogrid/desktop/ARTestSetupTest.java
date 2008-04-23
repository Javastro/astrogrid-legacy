/**
 * 
 */
package org.astrogrid.desktop;

import java.security.Principal;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.ag.AstrogridModuleIntegrationTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Test that the ARTestSetup fixture does what it's expected to do.
 * in particular that the login happens.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 8, 200712:17:16 PM
 */
public class ARTestSetupTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testFixturePresent() throws Exception {
		// test that the ARTestSetup has left us a fixture to work with.
		assertNotNull(ARTestSetup.fixture);
		assertNotNull(ARTestSetup.fixture.getACR());
	}
	
	public void testSessioning() throws Exception {
		// test this thread is associated with teh default session.
		SessionManagerInternal s = (SessionManagerInternal)ARTestSetup.fixture.getHivemindRegistry().getService(SessionManagerInternal.class);
		assertNotNull(s.getDefaultSessionId());
		Principal principal = s.findSessionForKey(s.getDefaultSessionId());
		assertNotNull(principal);
		assertNotNull(s.currentSession());
		assertEquals(principal,s.currentSession());
	}
	/** @TEST work out what's appropriate to test here - check for login, but remove dep on extenal services? */
	public void testLoggedIn() throws Exception {
		// expect us to be logged in at this point too..
		Community comm = (Community)ARTestSetup.fixture.getACR().getService(Community.class);
		assertTrue(comm.isLoggedIn());
		// now try to do a login-requiring action...
		Myspace m = (Myspace)ARTestSetup.fixture.getACR().getService(Myspace.class);
		m.listStores();
		
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ARTestSetupTest.class),true);
    }

}
