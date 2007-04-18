/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 200711:39:34 AM
 */
public class AllVfsSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.desktop.modules.ag.vfs");
		//$JUnit-BEGIN$
		suite.addTestSuite(MyspaceVFSSystemTest.class);
		//$JUnit-END$
		return new ARTestSetup(suite,true);
	}

}
