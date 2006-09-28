package uk.ac.roe.astrogrid.tests.agatroe;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for uk.ac.roe.astrogrid.tests.agatroe");
        //$JUnit-BEGIN$
        suite.addTest(uk.ac.roe.astrogrid.tests.agatroe.infrastructure.AllTests.suite());
        //$JUnit-END$
        return suite;
    }

}
