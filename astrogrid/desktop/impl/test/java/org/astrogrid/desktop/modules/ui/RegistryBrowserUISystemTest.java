/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class RegistryBrowserUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        browser = (RegistryBrowser)reg.getService(RegistryBrowser.class);
        assertNotNull(browser);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	browser = null;
    }
    protected RegistryBrowser browser;
	public void testShow() {
		browser.show();
	}
	
	public void testHide() {
		browser.hide();
	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(RegistryBrowserUISystemTest.class));
    }
}
