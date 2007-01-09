/**
 * 
 */
package org.astrogrid;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.ui.AllUISystemTests;
import org.astrogrid.desktop.modules.votech.AllVotechSystemTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**All system tests - tests workbench's integration with external services.
 * Looong running and not very repeatable - these coud do with some tidying up.
 * @author Noel Winstanley
 * @since Jan 4, 20077:13:28 PM
 */
public class AbsolutelyAllSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All System tests");
		suite.addTest(org.astrogrid.desktop.modules.ivoa.AllIvoaSystemTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.cds.AllCdsSystemTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridSystemTests.suite());
		suite.addTest(AllVotechSystemTests.suite());
		//suite.addTest(AllUISystemTests.suite());
		return new ARTestSetup(suite);
	}

}
