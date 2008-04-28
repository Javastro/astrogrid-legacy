/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20088:53:09 PM
 */
public class AllVoexplorerUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for VOExplorer");
        //$JUnit-BEGIN$
        suite.addTestSuite(QuerySizerImplUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
