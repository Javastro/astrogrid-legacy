/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:07:44 PM
 */
public class AllVoExplorerFoldersUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ui.voexplorer.folders");
		//$JUnit-BEGIN$
		suite.addTestSuite(DumbResourceFolderUnitTest.class);
		suite.addTestSuite(QueryResourceFolderUnitTest.class);
		suite.addTestSuite(FilterResourceFolderUnitTest.class);
		suite.addTestSuite(FolderUnitTest.class);
		suite.addTestSuite(StorageFolderUnitTest.class);
		suite.addTestSuite(AbstractFoldersProviderUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}