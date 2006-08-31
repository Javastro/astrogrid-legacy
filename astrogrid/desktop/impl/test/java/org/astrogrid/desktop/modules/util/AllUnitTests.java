/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Aug 25, 20061:50:17 AM
 */
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(TablesImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
