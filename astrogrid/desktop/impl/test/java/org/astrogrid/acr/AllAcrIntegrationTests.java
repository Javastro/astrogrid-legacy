/**
 * 
 */
package org.astrogrid.acr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 12, 20061:37:56 PM
 */
public class AllAcrIntegrationTests {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AllAcrIntegrationTests.class);

	public static Test suite() {
		TestSuite suite = new TestSuite("Integration Tests for ACR");
		suite.addTest(RemoteFinderIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}
