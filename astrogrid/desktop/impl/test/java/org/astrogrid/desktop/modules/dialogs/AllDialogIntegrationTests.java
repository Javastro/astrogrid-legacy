/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 20085:02:12 PM
 */
public class AllDialogIntegrationTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.dialogs");
        //$JUnit-BEGIN$
        suite.addTest(DialogModuleIntegrationTest.suite());
        //$JUnit-END$
        return suite;
    }

}
