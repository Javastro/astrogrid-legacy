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
public class AllUtilUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit test for utils module.");
		//$JUnit-BEGIN$
		suite.addTestSuite(TablesImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
