/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpRmiIntegrationTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class AstroscopeUISystemTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        scope = (AstroScope)reg.getService(AstroScope.class);
        assertNotNull(scope);
    } 
    protected ACR getACR() throws Exception{
        return ACRTestSetup.acrFactory.getACR();
    }
    protected AstroScope scope;
	public void testShow() {
		scope.show();
	}
	
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(AstroscopeUISystemTest.class));
    }

}
