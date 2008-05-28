/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 20065:02:05 PM
 */
public class AllAstrogridUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for astrogrid module");
		suite.addTestSuite(CeaHelperUnitTest.class);
		suite.addTestSuite(RemoteProcessManagerUnitTest.class);
		suite.addTestSuite(MonitorMapUnitTest.class);
		suite.addTestSuite(StapUnitTest.class);
		suite.addTestSuite(DocumentToStructureConversionUnitTest.class);
		return suite;
	}

}
