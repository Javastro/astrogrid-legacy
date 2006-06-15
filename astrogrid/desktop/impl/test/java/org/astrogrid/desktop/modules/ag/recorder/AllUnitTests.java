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
public class AllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit tests for r org.astrogrid.desktop.modules.ag.recorder");
		//$JUnit-BEGIN$
		suite.addTestSuite(MessageContainerImplTest.class);
		suite.addTestSuite(MessagesUnitTest.class);
		suite.addTestSuite(FoldersUnitTest.class);
		suite.addTestSuite(FolderImplUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
