/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20086:37:16 PM
 */
public class AllUIUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.ui");
        //$JUnit-BEGIN$
        suite.addTestSuite(MonitoringOutputStreamUnitTest.class);
        suite.addTestSuite(MonitoringInputStreamUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
