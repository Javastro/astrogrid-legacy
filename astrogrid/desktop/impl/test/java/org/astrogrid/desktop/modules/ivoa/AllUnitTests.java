/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:20 PM
 */
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ivoa");
		//$JUnit-BEGIN$
		suite.addTestSuite(DalUnitTest.class);
		suite.addTestSuite(SiapUnitTest.class);
		suite.addTestSuite(SsapUnitTest.class);
		suite.addTestSuite(ConeUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
