/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20065:39:54 PM
 */
public class AllSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"CDS Module System Tests");
		suite.addTestSuite(CoordinateSystemTest.class);
		suite.addTestSuite(GluSystemTest.class);
		suite.addTestSuite(SesameSystemTest.class);
		suite.addTestSuite(UCDSystemTest.class);
		suite.addTestSuite(VizierSystemTest.class); 
		
		suite.addTestSuite(CoordinateRmiSystemTest.class);
		suite.addTestSuite(GluRmiSystemTest.class);
		suite.addTestSuite(SesameRmiSystemTest.class);
		suite.addTestSuite(UCDRmiSystemTest.class);
		suite.addTestSuite(VizierRmiSystemTest.class);		
		
		suite.addTestSuite(CoordinateRpcSystemTest.class);
		suite.addTestSuite(GluRpcSystemTest.class);
		suite.addTestSuite(SesameRpcSystemTest.class);
		suite.addTestSuite(UCDRpcSystemTest.class);
		suite.addTestSuite(VizierRpcSystemTest.class);			
		
		return new ACRTestSetup(suite);
	}

}
