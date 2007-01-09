package org.astrogrid.desktop.modules.plastic;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlasticIntegrationTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Integration Tests for PLASTIC");
        //$JUnit-BEGIN$
        suite.addTestSuite(XMLRPCListenerIntegrationTest.class);
        suite.addTestSuite(DeafListenerIntegrationTest.class);
        suite.addTestSuite(PlaskitIntegrationTest.class);
        suite.addTestSuite(RMIListenerIntegrationTest.class);
    // caues too many dialogs to be thrown up - run it separately.   suite.addTestSuite(SlightlyStressfulIntegrationTest.class);
        //$JUnit-END$
        return suite;
    }

}
