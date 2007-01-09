/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20064:55:30 PM
 */
public class AllHivemindUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Unit Test for hivemind bits.");
		//$JUnit-BEGIN$
		suite.addTestSuite(HIvemindOrdererUnitTest.class);
		suite.addTestSuite(ServiceBeanUnitTest.class);
		suite.addTestSuite(HivemindMutableACRUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
