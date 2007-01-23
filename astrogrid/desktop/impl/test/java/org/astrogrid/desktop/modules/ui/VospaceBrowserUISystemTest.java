/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class VospaceBrowserUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        browser = (MyspaceBrowser)reg.getService(MyspaceBrowser.class);
        assertNotNull(browser);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	browser = null;
    }
    protected MyspaceBrowser browser;
	public void testShow() {
		browser.show();
	}
	
	public void testHide() {
		browser.hide();
	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(VospaceBrowserUISystemTest.class),true);
    }

}
