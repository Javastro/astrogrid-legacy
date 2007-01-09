/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpRmiTransportTest;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20067:34:43 PM
 */
public class SesameRmiTransportTest extends SesameSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(SesameRmiTransportTest.class));
    }
}
