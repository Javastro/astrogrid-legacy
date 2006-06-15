/**
 * 
 */
package org.astrogrid.desktop.modules.nvo;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:42:30 PM
 */
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for NVO module");
		//$JUnit-BEGIN$
		suite.addTestSuite(ConeUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
