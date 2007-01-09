/**
 * 
 */
package org.astrogrid.desktop.modules.background;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20073:50:27 PM
 */
public class AllBackgroundIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration Tests for Background.");
		suite.addTest(BackgroundModuleIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
