package uk.ac.roe.astrogrid.tests.agatroe.infrastructure;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for uk.ac.roe.astrogrid.tests.agatroe.infrastructure");
        //$JUnit-BEGIN$
        suite.addTestSuite(MySpaceTest.class);
        suite.addTestSuite(CommunityTest.class);
        //$JUnit-END$
        return suite;
    }

}
