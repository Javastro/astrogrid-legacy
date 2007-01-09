/**
 * 
 */
package org.astrogrid.desktop.modules.ag.recorder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 20063:58:27 PM
 */
public class AllRecorderUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit test for recorderr");
		//$JUnit-BEGIN$
		suite.addTestSuite(MessageContainerImplUnitTest.class);
		suite.addTestSuite(MessagesUnitTest.class);
		suite.addTestSuite(FoldersUnitTest.class);
		suite.addTestSuite(FolderImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
