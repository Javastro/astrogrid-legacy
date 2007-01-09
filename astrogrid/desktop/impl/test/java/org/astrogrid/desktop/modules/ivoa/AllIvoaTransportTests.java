/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllIvoaTransportTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Transport Tests for ivoa module.");
		suite.addTestSuite(SiapRmiTransportTest.class);
		suite.addTestSuite(SiapRpcTransportTest.class);
		suite.addTestSuite(ConeRmiTransportTest.class);
		suite.addTestSuite(ConeRpcTransportTest.class);
	
		return new ARTestSetup(suite,true);
	}

}
