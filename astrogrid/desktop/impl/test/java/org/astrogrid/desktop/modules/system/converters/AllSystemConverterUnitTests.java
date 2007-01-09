/**
 * 
 */
package org.astrogrid.desktop.modules.system.converters;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 7, 200612:31:45 PM
 */
public class AllSystemConverterUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Test for System convertors");
		//$JUnit-BEGIN$
		suite.addTestSuite(CollectionConvertorUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
