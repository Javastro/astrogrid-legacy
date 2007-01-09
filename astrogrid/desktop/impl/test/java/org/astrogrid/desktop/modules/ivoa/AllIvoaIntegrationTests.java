/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jan 7, 20071:10:23 AM
 */
public class AllIvoaIntegrationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration tests for IVOA module.");
		suite.addTestSuite(IvoaModuleIntegrationTest.class);
		return new ARTestSetup(suite,true);
	}
}
