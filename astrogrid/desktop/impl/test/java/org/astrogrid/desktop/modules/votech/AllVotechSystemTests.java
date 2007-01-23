/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jan 9, 20071:38:19 AM
 */
public class AllVotechSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System tests for Votech module");
		suite.addTest(VoMonSystemTest.suite());
		return new ARTestSetup(suite);
	}

}
