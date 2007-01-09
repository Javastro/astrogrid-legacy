/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.HelioScope;
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
public class HelioscopeUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        scope = (HelioScope)reg.getService(HelioScope.class);
        assertNotNull(scope);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	scope = null;
    }
    protected HelioScope scope;
	public void testShow() {
		scope.show();
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(HelioscopeUISystemTest.class));
    }


}
