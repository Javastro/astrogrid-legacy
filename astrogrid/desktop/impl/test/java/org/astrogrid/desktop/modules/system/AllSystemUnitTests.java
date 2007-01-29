/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 5, 20074:50:41 PM
 */
public class AllSystemUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for System Module");
		//$JUnit-BEGIN$
		suite.addTestSuite(PreferenceManagerImplUnitTest.class);
		suite.addTestSuite(PreferenceUnitTest.class);
		suite.addTestSuite(PreferencesArrangerImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
