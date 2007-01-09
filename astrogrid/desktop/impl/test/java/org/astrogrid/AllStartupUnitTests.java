/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

/** Tests for this org.astrogrid module - that is, the various 'main' classes and the commandline parser.
 * @author Noel Winstanley
 * @since Jun 6, 20062:35:36 PM
 */
public class AllStartupUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Unit Test for org.astrogrid");
		//$JUnit-BEGIN$
		suite.addTestSuite(CmdLineParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
