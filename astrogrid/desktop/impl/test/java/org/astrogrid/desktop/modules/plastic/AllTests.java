package org.astrogrid.desktop.modules.plastic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.plastic");
		//$JUnit-BEGIN$
		suite.addTestSuite(RegisterNoCallbackTest.class);
		suite.addTestSuite(BasicTest.class);
		//$JUnit-END$
		return suite;
	}

}
