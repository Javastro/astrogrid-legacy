/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.cds.AllCdsTransportTests;
import org.astrogrid.desktop.modules.ivoa.AllIvoaTransportTests;
import org.astrogrid.desktop.modules.system.AllSystemTransportTests;

/**All tests that exercise the rmi transport. xmlrpc is exercised separately from python tests.
 * most are based on system tests - so aren't easily repeatable, and slow.
 * @author Noel Winstanley
 * @since Jun 12, 20062:27:48 PM
 */
public class AbolutelyAllTransportTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All Transport Tests");
		suite.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridTransportTests.suite());
		suite.addTest(AllCdsTransportTests.suite());
		suite.addTest(AllIvoaTransportTests.suite());
		suite.addTest(AllSystemTransportTests.suite());
		return new ARTestSetup(suite);
	}
	
}
