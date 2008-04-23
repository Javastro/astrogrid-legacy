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
public class AllIvoaUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for IVOA module");
		//$JUnit-BEGIN$
		suite.addTestSuite(DalUnitTest.class);
		suite.addTestSuite(SiapUnitTest.class);
		suite.addTestSuite(SsapUnitTest.class);
		suite.addTestSuite(ConeUnitTest.class);
		suite.addTestSuite(AdqlUnitTest.class);
		suite.addTestSuite(CacheFactoryUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
