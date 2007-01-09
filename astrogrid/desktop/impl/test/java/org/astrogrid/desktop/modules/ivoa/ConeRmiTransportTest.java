/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 10, 200610:30:21 AM
 */
public class ConeRmiTransportTest extends ConeSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ConeRmiTransportTest.class));
    }

}
