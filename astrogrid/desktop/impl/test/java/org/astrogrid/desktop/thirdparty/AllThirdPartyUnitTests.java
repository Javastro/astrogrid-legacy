/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 21, 20089:21:49 AM
 */
public class AllThirdPartyUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for third party libraries");
        //$JUnit-BEGIN$
        suite.addTestSuite(VfsUnitTest.class);
        suite.addTestSuite(VfsVirtualUnitTest.class);
        suite.addTestSuite(VfsHttpResolvingUnitTest.class);
        suite.addTestSuite(CastorUnitTest.class);
        suite.addTestSuite(BoundedFifoBufferUnitTest.class);
        suite.addTestSuite(UnicodeUnitTest.class);
        suite.addTestSuite(JodaTimeUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
