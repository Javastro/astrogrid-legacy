/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.security.Principal;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.vfs.FileSystemManager;
import org.apache.hivemind.ServiceInterceptorFactory;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.messaging.Messaging;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20073:57:55 PM
 */
public class SystemModuleIntegrationTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testApiHelp() throws Exception {
		final ApiHelp a = assertServiceExists(ApiHelp.class, "system.apihelp");
		assertNotNull(a.listMethods());
	}
	
	public void testBrowser() throws Exception {
		final BrowserControl b = assertServiceExists(BrowserControl.class, "system.browser");
		b.openRelative("");
	}
	public void testConfiguration() throws Exception {
		final Configuration c  = assertServiceExists(Configuration.class,"system.configuration");
		c.list();
	}
	
	public void testDeprecation() throws Exception {
		final ServiceInterceptorFactory dep = assertComponentExists(ServiceInterceptorFactory.class, "system.deprecation");
		try {
		dep.createInterceptor(null, null, null);
		} catch (final Throwable e) {
		}
	}
	
	public void testExecutor() throws Exception {
		final BackgroundExecutor e = assertComponentExists(BackgroundExecutor.class, "system.executor");
		e.execute(new Runnable() {

			public void run() {
			}
			
		});
	}
	// 
	public void testHelp() throws Exception {
		final HelpServerInternal hsi = assertComponentExists(HelpServerInternal.class, "system.help");
		hsi.createContextSensitiveHelpListener();
	}
	public void testRmi() throws Exception {
		final RmiServer s = assertServiceExists(RmiServer.class, "system.rmi");
		s.getPort();
	}
	
	public void testScheduler() throws Exception {
		final SchedulerInternal s = assertComponentExists(SchedulerInternal.class, "system.scheduler");
		s.schedule(new ScheduledTask() {

			public void execute(final WorkerProgressReporter worker) {
			}

			public long getPeriod() {
				return 1000 * 1000;
			}

			public Principal getPrincipal() {
				return null;
			}

            public String getName() {
                return "Test";
            }
		});
	}
	
	public void testSnitch() throws Exception {
		final SnitchInternal s = assertComponentExists(SnitchInternal.class,"system.snitch");
		s.snitch("hi");
		 
	}
	
	public void testVFS() throws Exception {
		final FileSystemManager vfs = assertComponentExists(FileSystemManager.class,"system.vfs");
		vfs.getSchemes();
	}
	
	public void testSnitchInterceptor() throws Exception {
		final ServiceInterceptorFactory dep = assertComponentExists(ServiceInterceptorFactory.class, "system.snitchInterceptor");
		try {
		dep.createInterceptor(null, null, null);
		} catch (final Throwable e) {
			// don't care
		}
	}

 public void testSystray() throws Exception {
	 final SystemTray t = assertComponentExists(SystemTray.class, "system.systray");
	 t.displayInfoMessage("hi", "testing");
}
	public void testThrobberInterceptor() throws Exception {
		final ServiceInterceptorFactory dep = assertComponentExists(ServiceInterceptorFactory.class, "system.throbber");
		try {
		dep.createInterceptor(null, null, null);
		} catch (final Throwable e) {
			//don't care
		}
	}


public void testMessaging() throws Exception {
    final Messaging m = assertComponentExists(Messaging.class,"system.messaging");
    assertNotNull(m.getTargetList());
}

public void testUI() throws Exception {
	final UI ui = assertComponentExists(UI.class, "system.ui");
	ui.hide();
}

public void testWebserver() throws Exception {
	final WebServer ws = assertServiceExists(WebServer.class, "system.webserver");
	assertNotNull(ws.getKey());
	
}

	

    public static Test suite() {
        return new ARTestSetup(new TestSuite(SystemModuleIntegrationTest.class));
    }
	

}
