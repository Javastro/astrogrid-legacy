/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/** system tests for registry
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllRegistrySystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System Tests for ivoa registry");

		suite.addTestSuite(ExternalRegistryBasicsSystemTest.class);
		suite.addTestSuite(ExternalRegistryKeywordSystemTest.class);
		suite.addTestSuite(ExternalRegistryADQLSystemTest.class);
		suite.addTestSuite(ExternalRegistryXQuerySystemTest.class);		
		return new ARTestSetup(suite,false);
	}

}
