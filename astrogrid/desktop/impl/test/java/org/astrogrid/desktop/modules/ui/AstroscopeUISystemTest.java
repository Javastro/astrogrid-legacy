/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class AstroscopeUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        scope = (AstroScope)reg.getService(AstroScope.class);
        assertNotNull(scope);
    } 
    protected void tearDown() throws Exception {
    	super.tearDown();
    	scope = null;
    }

    protected AstroScope scope;
	public void testShow() {
		scope.show();
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(AstroscopeUISystemTest.class));
    }

}
