/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ivoa");
		//$JUnit-BEGIN$
		suite.addTestSuite(Adql074SystemTest.class);
		//$JUnit-END$
		return suite;
	}

}
