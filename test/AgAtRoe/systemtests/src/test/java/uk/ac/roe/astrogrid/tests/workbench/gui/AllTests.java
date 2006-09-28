package uk.ac.roe.astrogrid.tests.workbench.gui;

import uk.ac.roe.astrogrid.tests.RuntimeRequiringTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for uk.ac.roe.astrogrid.tests.workbench.gui");
        RuntimeRequiringTestCase.setWorkbenchStyle(RuntimeRequiringTestCase.WORKBENCH_JNLP);
        //$JUnit-BEGIN$
        suite.addTestSuite(DialogsTest.class);
        //$JUnit-END$
        return suite;
    }

}
