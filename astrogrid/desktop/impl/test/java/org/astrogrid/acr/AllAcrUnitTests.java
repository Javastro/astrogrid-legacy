/**
 * 
 */
package org.astrogrid.acr;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 12, 20061:37:41 PM
 */
public class AllAcrUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Unit Test for org.astrogrid.acr");
		//$JUnit-BEGIN$
		suite.addTestSuite(ACRExceptionUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
