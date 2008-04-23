/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Aug 25, 200612:29:16 AM
 */
public class AllSRQLUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for SRQL Parser");
		suite.addTestSuite(SRQLVisitorUnitTest.class);
		suite.addTestSuite(SRQLParserUnitTest.class);
		suite.addTestSuite(VisitorsUnitTest.class);
		return suite;
	}

}
