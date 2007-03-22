/**
 * 
 */
package org.astrogrid.desktop.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.hivemind.ServiceInterceptorFactory;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** tests that all components of builtin module are present.
 * @author Noel Winstanley
 * @since Jan 8, 20073:57:55 PM
 */
public class BuiltinModuleIntegrationTest extends InARTestCase implements ShutdownListener {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testACR() throws Exception {
		ACRInternal dep = (ACRInternal)assertComponentExists(ACRInternal.class, "builtin.acr");
		assertNotNull(dep.getDescriptors());
	}
	
	public void testSessionManager() throws Exception {
		SessionManagerInternal dep = (SessionManagerInternal)assertComponentExists(SessionManagerInternal.class, "builtin.sessionManager");
		assertNotNull(dep.getDefaultSessionId());
	}
	
	public void testShutdown() throws Exception {
		Shutdown dep = (Shutdown)assertComponentExists(Shutdown.class, "builtin.shutdown");
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
