/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**Integration test that verifies all components in astrogrid module are at least instantiable
 *  - by fetching each in turn, and callinig trivial methods on them
 *  
 *  this test verifes that the hivemind config for this module is working - not
 *  overly concerned with funciotnlaity of the components themselves.
 * @author Noel Winstanley
 * @since Jan 8, 200712:17:06 AM
 */
public class AstrogridModuleIntegrationTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();		
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMyspace() throws Exception {
		final Myspace m = assertServiceExists(Myspace.class,"astrogrid.myspace");
//	// forces login - need to fix this first.
//		m.listStores();
	}
	
	public void testCommunity() throws Exception {
		final Community c = assertServiceExists(Community.class,"astrogrid.community");
		c.isLoggedIn();
	}
	
	public void testApplications() throws Exception {
		final Applications a = assertServiceExists(Applications.class,"astrogrid.applications");
		a.getRegistryXQuery();
	}

	
	//
	public void testRemoteProcessManager() throws Exception {
		final RemoteProcessManager rpm  = assertServiceExists(RemoteProcessManager.class, "astrogrid.processManager");
		rpm.list();
	}
	
	
	public void testStap() throws Exception {
		final Stap s = assertServiceExists(Stap.class, "astrogrid.stap");
		s.getRegistryXQuery();
	}
	

	
	
	// astrogrid also provides the s
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(AstrogridModuleIntegrationTest.class));
    }
	

}
