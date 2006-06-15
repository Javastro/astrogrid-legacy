/**
 * 
 */
package org.astrogrid.desktop.modules.nvo;

import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 10, 200610:21:00 AM
 */
public class AllSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System Tests for NVO Module");
		suite.addTestSuite(ConeSystemTest.class);
		suite.addTestSuite(ConeRmiSystemTest.class);
		suite.addTestSuite(ConeRpcSystemTest.class);		
		return new ACRTestSetup(suite);
	}

}
