/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.TestingFinder;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20067:35:10 PM
 */
public class UCDRmiTransportTest extends UCDSystemTest {
    @Override
    protected ACR getACR() throws Exception{
        return (new TestingFinder()).find();
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(UCDRmiTransportTest.class));
    }
}
