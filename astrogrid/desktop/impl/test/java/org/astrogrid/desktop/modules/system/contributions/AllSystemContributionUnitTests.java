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
		//$JUnit-BEGIN$
		suite.addTestSuite(ServletsContributionUnitTest.class);
		suite.addTestSuite(UITabContributionUnitTest.class);
		suite.addTestSuite(HelpsetContributionUnitTest.class);
		suite.addTestSuite(ConverterContributionUnitTest.class);
		suite.addTestSuite(UIMenuContributionUnitTest.class);
		suite.addTestSuite(ServletContextContributionUnitTest.class);
		suite.addTestSuite(UIActionContributionUnitTest.class);
		suite.addTestSuite(StylesheetsContributionUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
