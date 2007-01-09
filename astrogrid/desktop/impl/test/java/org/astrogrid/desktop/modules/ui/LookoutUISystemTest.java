/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class LookoutUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        lookout = (Lookout)reg.getService(Lookout.class);
        assertNotNull(lookout);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	lookout = null;
    }
    protected Lookout lookout;
	public void testShow() {
		lookout.show();
	}
	
	public void testHide() {
		lookout.hide();
	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(LookoutUISystemTest.class),true);
    }
}
