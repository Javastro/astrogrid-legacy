/**
 * 
 */
package org.astrogrid.desktop.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** tests that all components of builtin module are present.
 * @author Noel Winstanley
 * @since Jan 8, 20073:57:55 PM
 */
public class BuiltinModuleIntegrationTest extends InARTestCase implements ShutdownListener {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testACR() throws Exception {
		ACRInternal dep = assertComponentExists(ACRInternal.class, "builtin.acr");
		assertNotNull(dep.getDescriptors());
	}
	
	public void testSessionManager() throws Exception {
		SessionManagerInternal dep = assertComponentExists(SessionManagerInternal.class, "builtin.sessionManager");
		assertNotNull(dep.getDefaultSessionId());
	}
	
	public void testShutdown() throws Exception {
		Shutdown dep = assertComponentExists(Shutdown.class, "builtin.shutdown");
		dep.addShutdownListener(this);
	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(BuiltinModuleIntegrationTest.class));
    }

	public void halting() {
	}

	public String lastChance() {
		return null;
	}
	

}
