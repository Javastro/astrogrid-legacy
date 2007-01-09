/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 12, 20061:45:13 PM
 */
public class AllAstrogridIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration tests for astrogrid module");
			suite.addTest(AstrogridModuleIntegrationTest.suite());
	      suite.addTest(StoreIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
