/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import org.astrogrid.desktop.modules.system.pref.PreferenceManagerImplUnitTest;
import org.astrogrid.desktop.modules.system.pref.PreferenceUnitTest;
import org.astrogrid.desktop.modules.system.pref.PreferencesArrangerImplUnitTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 5, 20074:50:41 PM
 */
public class AllPreferenceUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for System Preferences Module");
		//$JUnit-BEGIN$
		suite.addTestSuite(PreferenceManagerImplUnitTest.class);
		suite.addTestSuite(PreferenceUnitTest.class);
		suite.addTestSuite(PreferencesArrangerImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
