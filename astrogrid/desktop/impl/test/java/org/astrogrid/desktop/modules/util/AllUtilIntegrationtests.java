/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20077:42:03 PM
 */
public class AllUtilIntegrationtests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration tests for Utils module.l");
		suite.addTest(UtilModuleIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
