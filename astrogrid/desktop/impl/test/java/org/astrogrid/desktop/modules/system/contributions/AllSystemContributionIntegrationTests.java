/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jan 10, 200712:00:42 PM
 */
public class AllSystemContributionIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration Tests for System Contributions");
		suite.addTest(UIContributionIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
