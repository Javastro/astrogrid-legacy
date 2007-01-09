package org.astrogrid.desktop.modules.plastic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlasticUnitTests {
    /** tests the features of the plastic hub */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for PLASTIC");
        //$JUnit-BEGIN$
        suite.addTestSuite(RMIListenerUnitTest.class);
        suite.addTestSuite(SysTrayUnitTest.class);
        suite.addTestSuite(RegisterNoCallbackUnitTest.class);
        suite.addTestSuite(ApplicationStoreUnitTest.class);
        suite.addTestSuite(PlasticClientProxyUnitTest.class);
        suite.addTestSuite(BasicUnitTest.class);
        suite.addTestSuite(PollingUnitTest.class);
        suite.addTestSuite(AdvancedUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
