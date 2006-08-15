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
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ivoa.resource");
		//$JUnit-BEGIN$
		suite.addTestSuite(ResourceStreamParserUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
