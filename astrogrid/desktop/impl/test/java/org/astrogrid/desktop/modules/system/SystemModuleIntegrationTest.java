/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.hivemind.ServiceInterceptorFactory;
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

/**
 * @author Noel Winstanley
 * @since Jan 8, 20073:57:55 PM
 */
public class SystemModuleIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testApiHelp() throws Exception {
		ApiHelp a = (ApiHelp)assertServiceExists(ApiHelp.class, "system.apihelp");
		assertNotNull(a.listMethods());
	}
	
	public void testBrowser() throws Exception {
		BrowserControl b = (BrowserControl) assertServiceExists(BrowserControl.class, "system.browser");
		b.openRelative("");
	}
	//@todo why this not working??
	public void testConfiguration() throws Exception {
		Configuration c  = (Configuration)assertServiceExists(Configuration.class,"system.configuration");
		c.list();
	}
	
	public void testDeprecation() throws Exception {
		ServiceInterceptorFactory dep = (ServiceInterceptorFactory)assertComponentExists(ServiceInterceptorFactory.class, "system.deprecation");
		try {
		dep.createInterceptor(null, null, null);
		} catch (Throwable e) {
		}
	}
	
	public void testExecutor() throws Exception {
		BackgroundExecutor e = (BackgroundExecutor)assertComponentExists(BackgroundExecutor.class, "system.executor");
		e.execute(new Runnable() {

			public void run() {
			}
			
		});
	}
	// 
	public void testHelp() throws Exception {
		HelpServer hsi = (HelpServer) assertServiceExists(HelpServer.class, "system.help");
		hsi.showHelp();
	}
	public void testRmi() throws Exception {
		RmiServer s = (RmiServer)assertServiceExists(RmiServer.class, "system.rmi");
		s.getPort();
	}
	
	public void testScheduler() throws Exception {
		SchedulerInternal s = (SchedulerInternal)assertComponentExists(SchedulerInternal.class, "system.scheduler");
		try {
		s.executePeriodically(null);
		} catch (NullPointerException e) {
			// ok, expected
		}
	}
	
	public void testSnitch() throws Exception {
		SnitchInternal s = (SnitchInternal)assertComponentExists(SnitchInternal.class,"system.snitch");
		s.snitch("hi");
		 
	}
	
	public void testSnitchInterceptor() throws Exception {
		ServiceInterceptorFactory dep = (ServiceInterceptorFactory)assertComponentExists(ServiceInterceptorFactory.class, "system.snitchInterceptor");
		try {
		dep.createInterceptor(null, null, null);
		} catch (Throwable e) {
			// don't care
		}
	}

 public void testSystray() throws Exception {
	 SystemTray t = (SystemTray)assertComponentExists(SystemTray.class, "system.systray");
	 t.displayInfoMessage("hi", "testing");
}
	public void testThrobberInterceptor() throws Exception {
		ServiceInterceptorFactory dep = (ServiceInterceptorFactory)assertComponentExists(ServiceInterceptorFactory.class, "system.throbber");
		try {
		dep.createInterceptor(null, null, null);
		} catch (Throwable e) {
			//don't care
		}
	}
public void testTupperware() throws Exception {
	TupperwareInternal tupp = (TupperwareInternal)assertComponentExists(TupperwareInternal.class, "system.tupperware");
	tupp.getRegisteredApplicationsModel();
}	

public void testUI() throws Exception {
	UI ui = (UI)assertComponentExists(UI.class, "system.ui");
	ui.hide();
}

public void testWebserver() throws Exception {
	WebServer ws = (WebServer)assertServiceExists(WebServer.class, "system.webserver");
	assertNotNull(ws.getKey());
	
}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(SystemModuleIntegrationTest.class));
    }
	

}
