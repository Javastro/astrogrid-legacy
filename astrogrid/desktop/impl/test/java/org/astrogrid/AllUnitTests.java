/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:35:36 PM
 */
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Unit Test for org.astrogrid");
		//$JUnit-BEGIN$
		suite.addTestSuite(CmdLineParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
