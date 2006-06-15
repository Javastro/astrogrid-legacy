/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class RegistryBrowserUISystemTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        browser = (RegistryBrowser)reg.getService(RegistryBrowser.class);
        assertNotNull(browser);
    } 
    protected ACR getACR() throws Exception{
        return ACRTestSetup.acrFactory.getACR();
    }
    protected RegistryBrowser browser;
	public void testShow() {
		browser.show();
	}
	
	public void testHide() {
		browser.hide();
	}
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RegistryBrowserUISystemTest.class));
    }
}
