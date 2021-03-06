/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 7, 200610:04:37 AM
 */
public class AllSystemContributionUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Test for System Contributions");
		suite.addTestSuite(ServletsContributionUnitTest.class);
		suite.addTestSuite(ConverterContributionUnitTest.class);
		suite.addTestSuite(ServletContextContributionUnitTest.class);
		suite.addTestSuite(StylesheetsContributionUnitTest.class);
		suite.addTestSuite(HelpItemContributionUnitTest.class);
		return suite;
	}

}
