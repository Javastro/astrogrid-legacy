/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:37:36 PM
 */
public class AllIvoaTransportTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Transport Tests for ivoa module.");
		suite.addTestSuite(SiapRmiTransportTest.class);
		suite.addTestSuite(ConeRmiTransportTest.class);
	
		return new ARTestSetup(suite,true);
	}

}
