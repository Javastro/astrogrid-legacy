/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Jobs;
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

	protected void setUp() throws Exception {
		super.setUp();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// forces login - need to fix this first.
	public void testMyspace() throws Exception {
		Myspace m = (Myspace) assertServiceExists(Myspace.class,"astrogrid.myspace");
		m.listStores();
	}
	
	public void testCommunity() throws Exception {
		Community c = (Community) assertServiceExists(Community.class,"astrogrid.community");
		c.isLoggedIn();
	}
	
	public void testApplications() throws Exception {
		Applications a = (Applications)assertServiceExists(Applications.class,"astrogrid.applications");
		a.getRegistryAdqlQuery();
	}
	//forces login - fix first.
	public void testJobs() throws Exception {
		Jobs j = (Jobs)assertServiceExists(Jobs.class,"astrogrid.jobs");
		j.list();
	}
	
	
	//
	public void testRemoteProcessManager() throws Exception {
		RemoteProcessManager rpm  = (RemoteProcessManager)assertServiceExists(RemoteProcessManager.class, "astrogrid.processManager");
		rpm.list();
	}
	
	
	public void testStap() throws Exception {
		Stap s = (Stap)assertServiceExists(Stap.class, "astrogrid.stap");
		s.getRegistryAdqlQuery();
	}
	
	// supporting non-ar components.
	/* private - can't be accessed
	public void testCastorTransformer() throws Exception {
		CastorTransformer c = (CastorTransformer) assertComponentExists(CastorTransformer.class, "astrogrid.castorTransformer");
		c.transform(null);
	}*/
	public void testMessaging() throws Exception {
		MessagingInternal m = (MessagingInternal)assertComponentExists(MessagingInternal.class, "astrogrid.messaging");
		m.removeEventProcessor(null);
		
	}
	// requires login
	public void testMessageRecorder() throws Exception {
		MessageRecorderInternal rec = (MessageRecorderInternal)assertComponentExists(MessageRecorderInternal.class, "astrogrid.recorder");
		rec.removeRecorderListener(null);
	}
	
	public void testStore() throws Exception {
		StoreInternal st = (StoreInternal)assertComponentExists(StoreInternal.class, "astrogrid.store");
		assertNotNull(st.getManager());
	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(AstrogridModuleIntegrationTest.class),true);
    }
	

}
