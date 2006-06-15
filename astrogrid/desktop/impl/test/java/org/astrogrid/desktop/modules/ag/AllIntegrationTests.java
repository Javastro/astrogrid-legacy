/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 12, 20061:45:13 PM
 */
public class AllIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration tests for astrogrid module");
	      suite.addTestSuite(StoreIntegrationTest.class);
		return suite;
	}

}
