/*
 * Created on Aug 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.applications.integration.http.helloworld;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author jdt
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.astrogrid.applications.integration.http.helloworld");
		//$JUnit-BEGIN$
		suite.addTestSuite(HttpDirectExecutionTest.class);
		suite.addTestSuite(HttpFileIndirectExecutionTest.class);
		suite.addTestSuite(HttpVOSpaceIndirectExecutionTest.class);
		//$JUnit-END$
		return suite;
	}
}
