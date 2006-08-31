/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Aug 25, 200612:29:16 AM
 */
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.dialogs.registry");
		//$JUnit-BEGIN$
		suite.addTestSuite(QueryVisitorUnitTest.class);
		suite.addTestSuite(QueryParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
