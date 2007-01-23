/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20067:34:13 PM
 */
public class GluRmiTransportTest extends GluSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(GluRmiTransportTest.class));
    }
}
