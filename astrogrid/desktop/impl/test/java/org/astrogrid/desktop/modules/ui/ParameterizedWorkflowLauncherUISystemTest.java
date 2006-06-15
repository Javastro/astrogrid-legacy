/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class ParameterizedWorkflowLauncherUISystemTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        launcher = (ParameterizedWorkflowLauncher)reg.getService(ParameterizedWorkflowLauncher.class);
        assertNotNull(launcher);
    } 
    protected ACR getACR() throws Exception{
        return ACRTestSetup.acrFactory.getACR();
    }
    protected ParameterizedWorkflowLauncher launcher;
    /** blocks the rest of the applicaiton, and doesn't test uch */
	public void doNotTestShow() {
		launcher.run();
	}
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ParameterizedWorkflowLauncherUISystemTest.class));
    }
}
