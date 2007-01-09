/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Aug 2, 20061:34:17 AM
 */
public class AllResourceUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit test for ivoa.resource");
		//$JUnit-BEGIN$
		suite.addTestSuite(ResourceStreamParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
