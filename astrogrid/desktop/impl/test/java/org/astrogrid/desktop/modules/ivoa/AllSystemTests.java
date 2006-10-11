/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System Tests for ivoa module.");
		suite.addTestSuite(SiapSystemTest.class);
		suite.addTestSuite(SiapRmiSystemTest.class);
		suite.addTestSuite(SiapRpcSystemTest.class);
		suite.addTestSuite(SsapSystemTest.class);
		suite.addTestSuite(ConeSystemTest.class);
		suite.addTestSuite(ConeRmiSystemTest.class);
		suite.addTestSuite(ConeRpcSystemTest.class);

		suite.addTestSuite(SkyNodeSystemTest.class);

		suite.addTestSuite(Adql074SystemTest.class);
		suite.addTestSuite(ExternalRegistryBasicsSystemTest.class);
		suite.addTestSuite(ExternalRegistryKeywordSystemTest.class);
		suite.addTestSuite(ExternalRegistryADQLSystemTest.class);
		suite.addTestSuite(ExternalRegistryXQuerySystemTest.class);		
		return new ACRTestSetup(suite,true);
	}

}
