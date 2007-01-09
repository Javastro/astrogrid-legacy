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
public class AllRegistryDialogUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for Registry Dialog");
		//$JUnit-BEGIN$
		suite.addTestSuite(QueryVisitorUnitTest.class);
		suite.addTestSuite(QueryParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
