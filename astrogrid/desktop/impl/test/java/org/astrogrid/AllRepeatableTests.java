/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

/** All tests that are 'repeatable' - i.e. depend on no exteernal services. in practice, this is all unit and integration tests 
 * @author Noel Winstanley
 * @since Jan 8, 20078:46:51 AM
 */
public class AllRepeatableTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Repeatable Tests");
		suite.addTest(AbsolutelyAllUnitTests.suite());
		suite.addTest(AbsolutelyAllIntegrationTests.suite());
		// runs in a new ar instance.
		suite.addTest(BestSystemTests.suite());
		return suite;
	}

}
