/**
 * 
 */
package org.astrogrid;

import org.astrogrid.desktop.framework.AllIntegrationTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**Hook to run all integration tests in the project.
 * useful for running within eclipse. not used within maven build - which recognizes
 * individuals tests according to names
 * @author Noel Winstanley
 * @since Jun 12, 20062:27:48 PM
 */
public class AbsolutelyAllIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Integration Tests for workbench.");
		suite.addTest(AllIntegrationTests.suite());
		suite.addTest(org.astrogrid.acr.AllIntegrationTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.AllIntegrationTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.AllIntegrationTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.transformers.AllIntegrationTests.suite());
		return suite;
	}

}
