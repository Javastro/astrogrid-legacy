package org.astrogrid.desktop.modules.plastic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUnitTests {
/** tests the features of the plastic hub */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for PLASTIC");
		//$JUnit-BEGIN$
		suite.addTestSuite(ApplicationStoreUnitTest.class);
		suite.addTestSuite(RMIListenerUnitTest.class);
		suite.addTestSuite(SysTrayUnitTest.class);
		suite.addTestSuite(RegisterNoCallbackUnitTest.class);
		suite.addTestSuite(PollingUnitTest.class);
		suite.addTestSuite(AdvancedUnitTest.class);
		suite.addTestSuite(PlasticClientProxyUnitTest.class);
		suite.addTestSuite(BasicUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
