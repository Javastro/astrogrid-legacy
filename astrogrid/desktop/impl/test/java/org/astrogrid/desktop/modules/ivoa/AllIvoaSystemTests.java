/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllIvoaSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System Tests for ivoa module.");
		suite.addTestSuite(SiapSystemTest.class);
		suite.addTestSuite(SsapSystemTest.class);
		suite.addTestSuite(ConeSystemTest.class);

		suite.addTestSuite(SkyNodeSystemTest.class);

		suite.addTestSuite(ExternalRegistryBasicsSystemTest.class);
		suite.addTestSuite(ExternalRegistryKeywordSystemTest.class);
		suite.addTestSuite(ExternalRegistryADQLSystemTest.class);
		//@todo debug - seems to be too expensive at the moment.
		//suite.addTestSuite(ExternalRegistryXQuerySystemTest.class);		
		return new ARTestSetup(suite,true);
	}

}
