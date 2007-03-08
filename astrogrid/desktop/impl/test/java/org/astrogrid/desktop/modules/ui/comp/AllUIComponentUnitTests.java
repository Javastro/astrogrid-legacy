/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20071:50:41 PM
 */
public class AllUIComponentUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ui.comp");
		suite.addTestSuite(UIComponentBodyguardUnitTest.class);
		return suite;
	}

}
