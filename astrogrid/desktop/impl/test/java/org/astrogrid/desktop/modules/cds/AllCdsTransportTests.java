/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 9, 20065:39:54 PM
 */
public class AllCdsTransportTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Transport Tests for CDS Module");

		
		suite.addTestSuite(CoordinateRmiTransportTest.class);
		suite.addTestSuite(SesameRmiTransportTest.class);
		suite.addTestSuite(UCDRmiTransportTest.class);
		suite.addTestSuite(VizierRmiTransportTest.class);		
		
		suite.addTestSuite(CoordinateRpcTransportTest.class);
		suite.addTestSuite(SesameRpcTransportTest.class);
		suite.addTestSuite(UCDRpcTransportTest.class);
		suite.addTestSuite(VizierRpcTransportTest.class);			
		
		return new ARTestSetup(suite);
	}

}
