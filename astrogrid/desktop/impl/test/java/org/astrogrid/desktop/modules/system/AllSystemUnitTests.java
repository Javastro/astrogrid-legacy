/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 22, 20089:17:26 PM
 */
public class AllSystemUnitTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.system");
        //$JUnit-BEGIN$
        suite.addTestSuite(HelpServerImplUnitTest.class);
        suite.addTestSuite(ClockDaemonSchedulerUnitTest.class);
        suite.addTestSuite(VersionComparatorUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
