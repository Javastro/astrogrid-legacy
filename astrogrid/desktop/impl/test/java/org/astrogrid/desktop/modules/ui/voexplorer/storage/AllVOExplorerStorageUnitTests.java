/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.storage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20078:10:10 PM
 */
public class AllVOExplorerStorageUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ui.voexplorer.storage");
		//$JUnit-BEGIN$
		suite.addTestSuite(HistoryUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
