/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 25, 200712:46:37 PM
 */
public class AllFileDialogUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for File Dialog");
		suite.addTestSuite(MyspaceNodeUnitTest.class);
		suite.addTestSuite(MyspaceBranchUnitTest.class);
		suite.addTestSuite(MyspaceRootNodeUnitTest.class);
		suite.addTestSuite(MyspaceLeafUnitTest.class);
		return suite;
	}

}
