/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20083:45:55 PM
 */
public class AllFileIntegrationTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.file");
        //$JUnit-BEGIN$
        suite.addTest(InfoImplIntegrationTest.suite());
        suite.addTest(ManagerImplIntegrationTest.suite());
        suite.addTest(FileModuleIntegrationTest.suite());
        suite.addTest(SystemsImplIntegrationTest.suite());
        suite.addTest(NameImplIntegrationTest.suite());
        //$JUnit-END$
        return new ARTestSetup(suite);
    }

}
