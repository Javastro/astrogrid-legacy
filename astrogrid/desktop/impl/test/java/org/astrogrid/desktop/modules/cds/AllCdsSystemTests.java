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
public class AllCdsSystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"System Tests for CDS Module");
		suite.addTestSuite(CoordinateSystemTest.class);
		suite.addTestSuite(SesameSystemTest.class);
		suite.addTestSuite(UCDSystemTest.class);
		//FIXME tooo slow. client needs to be downgraded to use HTTP access.
		// then a test like this would be realistic.
		suite.addTestSuite(VizierSystemTest.class); 
		
		
		return new ARTestSetup(suite);
	}

}
