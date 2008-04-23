/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20089:33:40 PM
 */
public class AllFileExplorerUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.ui.fileexplorer");
        //$JUnit-BEGIN$
        suite.addTestSuite(HistoryUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
