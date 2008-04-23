/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20077:07:44 PM
 */
public class AllUiFoldersUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ui.folders");
		//$JUnit-BEGIN$
		suite.addTestSuite(StaticListUnitTest.class);
		suite.addTestSuite(XQueryListUnitTest.class);
		suite.addTestSuite(SmartListUnitTest.class);
		suite.addTestSuite(FolderUnitTest.class);
		suite.addTestSuite(StorageFolderUnitTest.class);
		suite.addTestSuite(AbstractListProviderUnitTest.class);
		suite.addTestSuite(StorageFoldersProviderUnitTest.class);
        suite.addTestSuite(BranchBeanUnitTest.class);
        suite.addTestSuite(PersistentTreeProviderUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
