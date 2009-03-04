/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.ag.StapSystemTest;

/** System tests just relevant to integration of vfs into DAL
 * Not part of the main suite of system tests - as the tests it assembles
 * are all incuded there anyhow.
 * 
 * First: get all these tests working.
 * Nothing is exercising executeAndSave..
 * or save datasets either.

 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class DalVFSIntegrationSystemTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"System Tests DAl VFS Integration");
		suite.addTestSuite(SiapSystemTest.class);
		suite.addTestSuite(SsapSystemTest.class);
		suite.addTestSuite(ConeSystemTest.class);
		suite.addTestSuite(StapSystemTest.class);
	
		return new ARTestSetup(suite,true);
	}

}
