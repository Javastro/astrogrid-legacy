/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class ParameterizedWorkflowLauncherUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        launcher = (ParameterizedWorkflowLauncher)reg.getService(ParameterizedWorkflowLauncher.class);
        assertNotNull(launcher);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	launcher = null;
    }
    protected ParameterizedWorkflowLauncher launcher;
    /** blocks the rest of the applicaiton, and doesn't test uch */
	public void doNotTestShow() {
		launcher.run();
	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ParameterizedWorkflowLauncherUISystemTest.class));
    }
}
