/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class ApplicationLauncherUISystemTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        launcher = (ApplicationLauncher)reg.getService(ApplicationLauncher.class);
        assertNotNull(launcher);
    } 
    protected ACR getACR() throws Exception{
        return ACRTestSetup.acrFactory.getACR();
    }
    protected ApplicationLauncher launcher;
	public void testShow() {
		launcher.show();
	}
	
	public void testHide() {
		launcher.hide();
	}
	

}
