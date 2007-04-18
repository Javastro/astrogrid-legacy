/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 20071:10:32 AM
 */
public class AllSystemUiUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for System UI componentsi");
		//$JUnit-BEGIN$
		suite.addTestSuite(UIContextImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
