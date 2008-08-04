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

		suite.addTest(AllRegistrySystemTests.suite());		
		return new ARTestSetup(suite,true);
	}

}
