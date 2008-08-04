/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.ag.vfs.AllVfsSystemTests;
import org.astrogrid.desktop.modules.votech.AllVotechSystemTests;

/** System tests that have been cleaned up, run in a sensible amount of time and
 * are fairly deterministic. Useful to run occasionally during development.
 * 
 * don't forget to run the python xml-rpc tests too.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 20071:36:41 PM
 */
public class SystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Best System Tests");
		suite.addTest(org.astrogrid.desktop.modules.cds.AllCdsSystemTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.AllIvoaSystemTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridSystemTests.suite());
		suite.addTest(AllVotechSystemTests.suite());
		suite.addTest(AllVfsSystemTests.suite());
		return new ARTestSetup(suite,true);
	}

}
