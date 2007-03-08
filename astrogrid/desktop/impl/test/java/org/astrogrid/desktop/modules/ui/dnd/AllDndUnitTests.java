/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20073:23:41 PM
 */
public class AllDndUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for drag n drop components.");
		//$JUnit-BEGIN$
		suite.addTestSuite(ResourceTransferableUnitTest.class);
		suite.addTestSuite(ResourceListTransferableUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
