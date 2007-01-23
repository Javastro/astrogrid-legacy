/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20065:39:54 PM
 */
public class AllCdsIntegrationTests {


	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Integration Tests for CDS Module");
		suite.addTestSuite(CdsModuleIntegrationTest.class);
		return new ARTestSetup(suite);
	}
	
	
}
