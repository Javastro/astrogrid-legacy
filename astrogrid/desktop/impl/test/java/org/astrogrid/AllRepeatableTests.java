/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

/** All tests that are 'repeatable'  - i.e. don't depend on a very unreliable external 
 * service, or are sensitive to timing, and don't take too long to run.
 * This is the main suite of tests to run during development to check for a green bar.
 *  (under a minute at present).
 *  * @author Noel Winstanley
 * @since Jan 8, 20078:46:51 AM
 */
public class AllRepeatableTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Repeatable Tests");
		suite.addTest(AbsolutelyAllUnitTests.suite());
		suite.addTest(AbsolutelyAllIntegrationTests.suite());
	// I find this a bit too unreliable - run separately.
	//	suite.addTest(BestSystemTests.suite());
		return suite;
	}

}
