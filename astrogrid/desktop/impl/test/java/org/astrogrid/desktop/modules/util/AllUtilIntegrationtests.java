/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20077:42:03 PM
 */
public class AllUtilIntegrationtests {

	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"Integration tests for Utils module");
		suite.addTest(UtilModuleIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
