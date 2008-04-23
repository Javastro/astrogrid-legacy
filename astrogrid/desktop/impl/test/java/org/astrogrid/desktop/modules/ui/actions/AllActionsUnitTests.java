/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 18, 200812:33:30 PM
 */
public class AllActionsUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for UI Actions");
        //$JUnit-BEGIN$
        suite.addTestSuite(CopyCommandUnitTest.class);
        suite.addTestSuite(BulkCopyWorkerUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
