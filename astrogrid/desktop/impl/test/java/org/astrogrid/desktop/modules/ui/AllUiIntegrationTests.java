/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 20085:05:35 PM
 */
public class AllUiIntegrationTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.ui");
        //$JUnit-BEGIN$
        suite.addTest(UIModuleIntegrationTest.suite());
        //$JUnit-END$
        return suite;
    }

}
