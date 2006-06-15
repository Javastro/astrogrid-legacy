/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpRmiIntegrationTest;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20067:33:25 PM
 */
public class CoordinateRmiSystemTest extends CoordinateSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(CoordinateRmiSystemTest.class));
    }
}
