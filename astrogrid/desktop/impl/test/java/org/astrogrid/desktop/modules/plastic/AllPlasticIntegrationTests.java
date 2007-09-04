package org.astrogrid.desktop.modules.plastic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlasticIntegrationTests {

    public static Test suite() {
        System.setProperty(NOTIFICATION_KEY,"false");
        TestSuite suite = new TestSuite("Integration Tests for PLASTIC");
        //$JUnit-BEGIN$
        suite.addTestSuite(XMLRPCListenerIntegrationTest.class);
        suite.addTestSuite(DeafListenerIntegrationTest.class);
        suite.addTestSuite(PlaskitIntegrationTest.class);
        suite.addTestSuite(RMIListenerIntegrationTest.class);
        //$JUnit-END$
        return suite;
    }

    /**
     * 
     */
    public static final String NOTIFICATION_KEY = "org.votech.plastic.notificationsenabled";

}
