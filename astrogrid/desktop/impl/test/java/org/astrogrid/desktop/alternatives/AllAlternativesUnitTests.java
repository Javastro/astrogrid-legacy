/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:15:04 PM
 */
public class AllAlternativesUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for Alternative implementations");
		//$JUnit-BEGIN$
		suite.addTestSuite(HeadlessUIUnitTest.class);
		suite.addTestSuite(HeadlessLoginDialogueUnitTest.class);
		suite.addTestSuite(InThreadExecutorUnitTest.class);
		suite.addTestSuite(LoggingSystemTrayUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
