/**
 * 
 */
package org.astrogrid.acr;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 12, 20061:37:56 PM
 */
public class AllIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Integration Tests for org.astrogrid.acr");
		//$JUnit-BEGIN$
		suite.addTestSuite(InProcessFinderIntegrationTest.class);
		suite.addTest(RemoteFinderIntegrationTest.suite());
		//$JUnit-END$
		return suite;
	}

}
