/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 9, 20071:29:17 AM
 */
public class AllVotechUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for VoTech module");
		//$JUnit-BEGIN$
		suite.addTestSuite(VoMonImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
