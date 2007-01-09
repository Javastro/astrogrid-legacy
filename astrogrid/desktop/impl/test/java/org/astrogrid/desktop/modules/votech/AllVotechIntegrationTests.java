/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20079:31:48 PM
 */
public class AllVotechIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration Tests for VoMon");
		suite.addTest(VotechModuleIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
