/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 24, 20091:41:30 PM
 */
public class AllMessagingUnitTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.system.messaging");
        //$JUnit-BEGIN$
        suite.addTestSuite(PlasticApplicationDescriptionUnitTest.class);
        suite.addTestSuite(MessageTypeUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
