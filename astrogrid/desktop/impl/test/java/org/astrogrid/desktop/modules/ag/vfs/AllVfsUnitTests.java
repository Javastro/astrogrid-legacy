/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 200712:50:21 PM
 * @TEST extend to cover more of ag.vfs and ag.vfs.myspace
 */
public class AllVfsUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for VFS Providers.");
		//$JUnit-BEGIN$
		suite.addTestSuite(MyspaceNameParserUnitTest.class);
		suite.addTestSuite(MyspaceFileNameUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
